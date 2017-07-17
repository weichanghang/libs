package com.wch.libs.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

/**
 * UncaughtException处理�?当程序发生Uncaught异常的时�?由该类来接管程序,并记录发送错误报�?
 */
public class CrashHandler implements UncaughtExceptionHandler
{
    private static final String TAG = "CrashHandler";
    
    private UncaughtExceptionHandler mDefaultHandler;// 系统默认的UncaughtException处理�?
    
    private static CrashHandler INSTANCE = null;// CrashHandler实例
    
    private Context mContext;// 程序的Context对象
    
    private Map<String, String> info;// 用来存储设备信息和异常信�?
    
    // 用于格式化日�?作为日志文件名的�?���?
    @SuppressLint("SimpleDateFormat")
    private SimpleDateFormat format;
    
    /** 保证只有�?��CrashHandler实例 */
    private CrashHandler()
    {
        format = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        info = new HashMap<String, String>();
    }
    
    /** 获取CrashHandler实例 ,单例模式 */
    public static CrashHandler getInstance()
    {
        synchronized (CrashHandler.class)
        {
            if (null == INSTANCE)
            {
                INSTANCE = new CrashHandler();
            }
        }
        
        return INSTANCE;
    }
    
    /**
     * 初始�?
     * 
     * @param context
     */
    public void init(Context context)
    {
        mContext = context;
        
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();// 获取系统默认的UncaughtException处理�?
        Thread.setDefaultUncaughtExceptionHandler(this);// 设置该CrashHandler为程序的默认处理�?
    }
    
    /**
     * 当UncaughtException发生时会转入该重写的方法来处�?
     */
    public void uncaughtException(Thread thread, Throwable ex)
    {
        if (!handleException(ex) && mDefaultHandler != null)
        {
            Log.i(TAG, "system catch exception");
            // 如果自定义的没有处理则让系统默认的异常处理器来处�?
            mDefaultHandler.uncaughtException(thread, ex);
        }
        else
        {
            Log.i(TAG, "custom catch exception");
            try
            {
                Thread.sleep(3000);// 如果处理了，让程序继续运�?秒再�?��，保证文件保存并上传到服务器
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
            
        }
        // 先�?出程�?
        
        // ActivityManager.getInstance().popAllActivity();
        
        // 再杀进程
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
    }
    
    /**
     * 自定义错误处�?收集错误信息 发�?错误报告等操作均在此完成.
     * 
     * @param ex 异常信息
     * @return true 如果处理了该异常信息;否则返回false.
     */
    public boolean handleException(Throwable ex)
    {
        if (ex == null)
        {
            return false;
        }
        
        new Thread()
        {
            public void run()
            {
                Looper.prepare();
                Toast.makeText(mContext, "抱歉，程序停止运行", Toast.LENGTH_LONG).show();
                Looper.loop();
            }
        }.start();
        
        // 收集设备参数信息
        collectDeviceInfo(mContext);
        
        // 保存日志文件
        saveCrashInfo2File(ex);
        
        return true;
    }
    
    /**
     * 收集设备参数信息
     * 
     * @param context
     */
    public void collectDeviceInfo(Context context)
    {
        try
        {
            PackageManager pm = context.getPackageManager();// 获得包管理器
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_ACTIVITIES);// 得到该应用的信息，即主Activity
            if (pi != null)
            {
                String versionName = pi.versionName == null ? "null" : pi.versionName;
                String versionCode = pi.versionCode + "";
                info.put("versionName", versionName);
                info.put("versionCode", versionCode);
            }
        }
        catch (NameNotFoundException e)
        {
            e.printStackTrace();
        }
        
        Field[] fields = Build.class.getDeclaredFields();// 反射机制
        for (Field field : fields)
        {
            try
            {
                field.setAccessible(true);
                info.put(field.getName(), field.get("").toString());
                Log.d(TAG, field.getName() + ":" + field.get(""));
            }
            catch (IllegalArgumentException e)
            {
                e.printStackTrace();
            }
            catch (IllegalAccessException e)
            {
                e.printStackTrace();
            }
        }
    }
    
    private String saveCrashInfo2File(Throwable ex)
    {
        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, String> entry : info.entrySet())
        {
            String key = entry.getKey();
            String value = entry.getValue();
            sb.append(key + "=" + value + "\r\n");
        }
        Writer writer = new StringWriter();
        PrintWriter pw = new PrintWriter(writer);
        ex.printStackTrace(pw);
        Throwable cause = ex.getCause();
        while (cause != null)
        {
            cause.printStackTrace(pw);
            cause = cause.getCause();
        }
        pw.close();// 记得关闭
        String result = writer.toString();
        sb.append(result);
        // 保存文件
        long timetamp = System.currentTimeMillis();
        String time = format.format(new Date());
        Log.i(TAG, "time-->" + time);
        String fileName = "crash-" + time + "-" + timetamp + ".txt";
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
        {
            
            try
            {
                File dir =
                    new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator
                        + "com.daguo.wuzi" + File.separator + "crash");
                getFileCount(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator
                    + "com.daguo.wuzi" + File.separator + "crash");
                Log.i("CrashHandler", dir.toString());
                if (!dir.exists())
                    dir.mkdir();
                FileOutputStream fos = null;
                try
                {
                    fos = new FileOutputStream(new File(dir, fileName));
                    fos.write(sb.toString().getBytes());
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                finally
                {
                    fos.close();
                }
                
                return fileName;
            }
            catch (FileNotFoundException e)
            {
                e.printStackTrace();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            finally
            {
                
            }
        }
        return null;
    }
    
    private void getFileCount(String path)
    {
        Map<Long, String> map = new HashMap<Long, String>();
        File file = new File(path);
        if (file.exists() && file.isDirectory())
        {
            File[] file2 = file.listFiles();
            if (file2.length > 0)
            {
                for (int i = 0; i < file2.length; i++)
                {
                    Log.e("getFileCount", file2[i].lastModified() + "");
                    Log.e("getFileCount", file2[i].getName() + "");
                    map.put(file2[i].lastModified(), file2[i].getName());
                }
                Map<Long, String> resultMap = sortMapByKey(map);
                int i = 0;
                for (Map.Entry<Long, String> entry : resultMap.entrySet())
                {
                    i++;
                    Log.e("getFileCount", entry.getKey() + " " + entry.getValue());
                    if (i > 4)
                    {
                        File deleteFile = new File(path + "/" + entry.getValue());
                        if (deleteFile.exists())
                        {
                            deleteFile.delete();
                        }
                    }
                }
                return; // 按Key进行排序 ;
            }
        }
    }
    
    /**
     * 使用 Map按key进行排序
     * 
     * @param map
     * @return
     */
    public static Map<Long, String> sortMapByKey(Map<Long, String> map)
    {
        if (map == null || map.isEmpty())
        {
            return null;
        }
        Map<Long, String> sortMap = new TreeMap<Long, String>(new MapKeyComparator());
        sortMap.putAll(map);
        return sortMap;
    }
}

// 比较器类
class MapKeyComparator implements Comparator<Long>
{
    public int compare(Long str1, Long str2)
    {
        return str2.compareTo(str1);
    }
}