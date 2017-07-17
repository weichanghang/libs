package com.wch.libs.util;

import android.util.Log;

/**
 * @author hanweipeng
 * @category日志工具类
 */
public class DebugLog
{
    
    private DebugLog()
    {
    }
    
    private static boolean LOG_ENABLED = true;
    
    public static void v(String tag, String msg)
    {
        if (LOG_ENABLED)
        {
            Log.v("ttv2 " + tag, msg);
        }
    }
    
    public static void d(String tag, String msg)
    {
        if (LOG_ENABLED)
        {
            Log.d("ttv2 " + tag, msg);
        }
    }
    
    public static void i(String tag, String msg)
    {
        if (LOG_ENABLED)
        {
            Log.i("ttv2 " + tag, msg);
        }
    }
    
    public static void w(String tag, String msg)
    {
        if (LOG_ENABLED)
        {
            Log.w("ttv2 " + tag, msg);
        }
    }
    
    public static void e(String tag, String msg)
    {
        if (LOG_ENABLED)
        {
            Log.e("ttv2 " + tag, msg);
        }
    }
    
    public static void v(String tag, String msg, Throwable tr)
    {
        if (LOG_ENABLED)
        {
            Log.v("ttv2 " + tag, msg, tr);
        }
    }
    
    public static void i(String tag, String msg, Throwable tr)
    {
        if (LOG_ENABLED)
        {
            Log.i("ttv2 " + tag, msg, tr);
        }
    }
    
    public static void d(String tag, String msg, Throwable tr)
    {
        if (LOG_ENABLED)
        {
            Log.d("ttv2 " + tag, msg, tr);
        }
    }
    
    public static void w(String tag, String msg, Throwable tr)
    {
        if (LOG_ENABLED)
        {
            Log.w("ttv2 " + tag, msg, tr);
        }
    }
    
    public static void e(String tag, String msg, Throwable tr)
    {
        if (LOG_ENABLED)
        {
            Log.e("ttv2 " + tag, msg, tr);
        }
    }
    
    public static void printException(Exception e, boolean sendBugsense)
    {
        e.printStackTrace();
        if (sendBugsense)
        {
            // BugSenseHandler.sendException(e);
        }
    }
    
    public static void printException(String tag, Exception e, boolean sendBugsense)
    {
        
        DebugLog.e(tag, "Exception cause: " + e.getCause() + " message: " + e.getMessage());
        e.printStackTrace();
        if (sendBugsense)
        {
            // BugSenseHandler.sendExceptionMessage("Exception", tag, e);
        }
    }
    
    public static void printException(String tag, String secondLevelMsg, Exception e, boolean sendBugsense)
    {
        if (LOG_ENABLED)
        {
            e(tag, secondLevelMsg + " :Exception cause: " + e.getCause() + " message: " + e.getMessage());
            e.printStackTrace();
            if (sendBugsense)
            {
                // BugSenseHandler.sendExceptionMessage("Exception", tag, e);
            }
        }
    }
    
    public static String exceptionMessageGenerator(Exception e)
    {
        return " :Exception cause: " + e.getCause() + " message: " + e.getMessage();
    }
    
}
