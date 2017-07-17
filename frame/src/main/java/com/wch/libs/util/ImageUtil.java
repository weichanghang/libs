package com.wch.libs.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.text.TextUtils;

/**
 * ImageUtils
 * <ul>
 * convert between Bitmap, byte array, Drawable
 * <li>{@link #bitmapToByte(Bitmap)}</li>
 * <li>{@link #bitmapToDrawable(Bitmap)}</li>
 * <li>{@link #byteToBitmap(byte[])}</li>
 * <li>{@link #byteToDrawable(byte[])}</li>
 * <li>{@link #drawableToBitmap(Drawable)}</li>
 * <li>{@link #drawableToByte(Drawable)}</li>
 * </ul>
 * <ul>
 * get image
 * <li>{@link #getInputStreamFromUrl(String, int)}</li>
 * <li>{@link #getBitmapFromUrl(String, int)}</li>
 * <li>{@link #getDrawableFromUrl(String, int)}</li>
 * </ul>
 * <ul>
 * scale image
 * <li>{@link #scaleImageTo(Bitmap, int, int)}</li>
 * <li>{@link #scaleImage(Bitmap, float, float)}</li>
 * </ul>
 * 
 * @author Trinea 2012-6-27
 */
public class ImageUtil
{
    
    /**
     * convert Bitmap to byte array
     * 
     * @param b
     * @return
     */
    public static byte[] bitmapToByte(Bitmap b)
    {
        if (b == null)
        {
            return null;
        }
        
        ByteArrayOutputStream o = new ByteArrayOutputStream();
        b.compress(CompressFormat.PNG, 100, o);
        return o.toByteArray();
    }
    
    /**
     * convert byte array to Bitmap
     * 
     * @param b
     * @return
     */
    public static Bitmap byteToBitmap(byte[] b)
    {
        return (b == null || b.length == 0) ? null : BitmapFactory.decodeByteArray(b, 0, b.length);
    }
    
    /**
     * convert Drawable to Bitmap
     * 
     * @param d
     * @return
     */
    public static Bitmap drawableToBitmap(Drawable d)
    {
        return d == null ? null : ((BitmapDrawable)d).getBitmap();
    }
    
    /**
     * convert Bitmap to Drawable
     * 
     * @param b
     * @return
     */
    public static Drawable bitmapToDrawable(Bitmap b)
    {
        return b == null ? null : new BitmapDrawable(b);
    }
    
    /**
     * convert Drawable to byte array
     * 
     * @param d
     * @return
     */
    public static byte[] drawableToByte(Drawable d)
    {
        return bitmapToByte(drawableToBitmap(d));
    }
    
    /**
     * convert byte array to Drawable
     * 
     * @param b
     * @return
     */
    public static Drawable byteToDrawable(byte[] b)
    {
        return bitmapToDrawable(byteToBitmap(b));
    }
    
    /**
     * get input stream from network by imageurl, you need to close inputStream yourself
     * 
     * @param imageUrl
     * @param readTimeOutMillis read time out, if less than 0, not set, in mills
     * @return
     * @throws MalformedURLException
     * @throws IOException
     */
    public static InputStream getInputStreamFromUrl(String imageUrl, int readTimeOutMillis)
    {
        InputStream stream = null;
        try
        {
            URL url = new URL(imageUrl);
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            if (readTimeOutMillis > 0)
            {
                con.setReadTimeout(readTimeOutMillis);
            }
            stream = con.getInputStream();
        }
        catch (MalformedURLException e)
        {
            closeInputStream(stream);
            throw new RuntimeException("MalformedURLException occurred. ", e);
        }
        catch (IOException e)
        {
            closeInputStream(stream);
            throw new RuntimeException("IOException occurred. ", e);
        }
        return stream;
    }
    
    /**
     * get drawable by imageUrl
     * 
     * @param imageUrl
     * @param readTimeOutMillis read time out, if less than 0, not set, in mills
     * @return
     */
    public static Drawable getDrawableFromUrl(String imageUrl, int readTimeOutMillis)
    {
        InputStream stream = getInputStreamFromUrl(imageUrl, readTimeOutMillis);
        Drawable d = Drawable.createFromStream(stream, "src");
        closeInputStream(stream);
        return d;
    }
    
    /**
     * get Bitmap by imageUrl
     * 
     * @param imageUrl
     * @return
     */
    public static Bitmap getBitmapFromUrl(String imageUrl, int readTimeOut)
    {
        InputStream stream = getInputStreamFromUrl(imageUrl, readTimeOut);
        Bitmap b = BitmapFactory.decodeStream(stream);
        closeInputStream(stream);
        return b;
    }
    
    /**
     * scale image
     * 
     * @param org
     * @param newWidth
     * @param newHeight
     * @return
     */
    public static Bitmap scaleImageTo(Bitmap org, int newWidth, int newHeight)
    {
        return scaleImage(org, (float)newWidth / org.getWidth(), (float)newHeight / org.getHeight());
    }
    
    /**
     * scale image
     * 
     * @param org
     * @param scaleWidth sacle of width
     * @param scaleHeight scale of height
     * @return
     */
    public static Bitmap scaleImage(Bitmap org, float scaleWidth, float scaleHeight)
    {
        if (org == null)
        {
            return null;
        }
        
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        return Bitmap.createBitmap(org, 0, 0, org.getWidth(), org.getHeight(), matrix, true);
    }
    
    /**
     * close inputStream
     * 
     * @param s
     */
    private static void closeInputStream(InputStream s)
    {
        if (s == null)
        {
            return;
        }
        
        try
        {
            s.close();
        }
        catch (IOException e)
        {
            throw new RuntimeException("IOException occurred. ", e);
        }
    }
    
    /**
     * 把bitmap保存到SD卡上
     * 
     * @param bitmap 源图片
     * @param savePath 保存路径
     * @param format 图片格式
     */
    public static boolean saveBitmap(Bitmap bitmap, String savePath, CompressFormat format)
    {
        if (bitmap == null || TextUtils.isEmpty(savePath))
        {
            return false;
        }
        FileOutputStream fos = null;
        try
        {
            fos = new FileOutputStream(savePath, false);
            bitmap.compress(format, 80, fos);
        }
        catch (FileNotFoundException e)
        {
            return false;
        }
        finally
        {
            if (fos != null)
            {
                try
                {
                    fos.close();
                }
                catch (IOException e)
                {
                    return false;
                }
            }
        }
        return true;
    }
    
    public final static String TRAVEL_DIR = "com.weconex.nanjingtravel";
    
    // 根据文件名组成全路径前缀
    public static String getDirectory(String filename)
    {
        String extStorageDirectory = "/mnt/sdcard";
        String dirPath = extStorageDirectory + "/" + TRAVEL_DIR;
        File dirFile = new File(dirPath);
        dirFile.mkdirs();
        dirPath = dirPath + "/image_cache";
        dirFile = new File(dirPath);
        dirFile.mkdir();
        return dirPath;
    }
    
    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight)
    {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        
        if (height > reqHeight || width > reqWidth)
        {
            if (width > height)
            {
                inSampleSize = Math.round((float)height / (float)reqHeight);
            }
            else
            {
                inSampleSize = Math.round((float)width / (float)reqWidth);
            }
        }
        return inSampleSize;
    }
    
    // 根据路径获得图片并压缩，返回bitmap用于显示
    public static Bitmap getSmallBitmap(String filePath)
    {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, 480, 800);
        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(filePath, options);
    }
    
    public static boolean saveZipBmpToSd(Bitmap bm, String filename)
    {
        if (bm == null)
        {
            return false;
        }
        // String dir = getDirectory(filename);
        // File file = new File(dir + "/" + filename);
        // File file = new File(dir);
        File file = new File(filename);
        System.out.println("file全路径=" + file);
        try
        {
            file.createNewFile();
            file.setWritable(Boolean.TRUE);
            OutputStream outStream = new FileOutputStream(file);
            bm.compress(CompressFormat.JPEG, 80, outStream);
            outStream.flush();
            outStream.close();
            return true;
        }
        catch (Exception e)
        {
            // Log.w(MyApplication.TAG, "FileNotFoundException");
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw, true));
            System.out.println("保存图片报错:" + sw.toString());
        }
        catch (Error e)
        {
            System.out.println("SD卡已满");
        }
        return false;
    }
    
    private static File scaleImageWithFilter(File mImageFile, File fileTemp, int screenMin, boolean b, boolean c,
        boolean d)
    {
        Bitmap bitmap = getSmallBitmap(mImageFile.getAbsolutePath());
        if (bitmap != null)
        {
            System.out.println("有图" + bitmap.getWidth() + ":" + bitmap.getHeight());
        }
        else
        {
            System.out.println("没有图");
        }
        boolean flag = saveZipBmpToSd(bitmap, fileTemp.getAbsolutePath());
        System.out.println("保存图片flag=" + flag + " 文件大小=" + fileTemp.length());
        return fileTemp;
    }
    
    static BitmapFactory.Options options;
    
    public static final SimpleDateFormat imageFileSD = new SimpleDateFormat("yyyyMMdd_hhmmss_SSS");
    
    public static long save(InputStream paramInputStream, String paramString)
    {
        File f;
        if (!(f = new File(paramString)).getParentFile().exists())
            f.getParentFile().mkdirs();
        try
        {
            f.createNewFile();
            FileOutputStream localFileOutputStream = new FileOutputStream(paramString);
            byte[] arrayOfByte = new byte[1024];
            int i = 0;
            while ((i = paramInputStream.read(arrayOfByte)) != -1)
            {
                localFileOutputStream.write(arrayOfByte, 0, i);
            }
            paramInputStream.close();
            localFileOutputStream.flush();
            localFileOutputStream.close();
            return f.length();
        }
        catch (IOException localIOException)
        {
            if (f.exists())
                f.delete();
        }
        return -1L;
    }
    
    public static boolean saveBmpToSd(Bitmap bm, String url)
    {
        if (bm == null)
        {
            return false;
        }
        String filename = convertUrlToFileName(url);
        String dir = getDirectory(filename);
        File file = new File(dir + "/" + filename);
        // File file = new File(dir);
        // System.out.println("file全路径="+file);
        try
        {
            file.createNewFile();
            file.setWritable(Boolean.TRUE);
            OutputStream outStream = new FileOutputStream(file);
            bm.compress(CompressFormat.JPEG, 100, outStream);
            outStream.flush();
            outStream.close();
            return true;
        }
        catch (Exception e)
        {
            // Log.w(MyApplication.TAG, "FileNotFoundException");
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw, true));
            System.out.println("保存图片报错:" + sw.toString());
        }
        catch (Error e)
        {
            System.out.println("SD卡已满");
        }
        return false;
    }
    
    public static String convertUrlToFileName(String url)
    {
        String filename = url;
        filename = filename.replace("/", "_");
        return filename;
    }
    
    /**
     * 获取需要进行缩放的比例，即options.inSampleSize
     * 
     * @param options
     * @param minSideLength
     * @param maxNumOfPixels
     * @return
     */
    public static int computeSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels)
    {
        int initialSize = computeInitialSampleSize(options, minSideLength, maxNumOfPixels);
        
        int roundedSize;
        if (initialSize <= 8)
        {
            roundedSize = 1;
            while (roundedSize < initialSize)
            {
                roundedSize <<= 1;
            }
        }
        else
        {
            roundedSize = (initialSize + 7) / 8 * 8;
        }
        
        return roundedSize;
    }
    
    public static final int UNCONSTRAINED = -1;
    
    private static int computeInitialSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels)
    {
        double w = options.outWidth;
        double h = options.outHeight;
        
        int lowerBound = (maxNumOfPixels == UNCONSTRAINED) ? 1 : (int)Math.ceil(Math.sqrt(w * h / maxNumOfPixels));
        int upperBound =
            (minSideLength == UNCONSTRAINED) ? 128 : (int)Math.min(Math.floor(w / minSideLength),
                Math.floor(h / minSideLength));
        
        if (upperBound < lowerBound)
        {
            // return the larger one when there is no overlapping zone.
            return lowerBound;
        }
        
        if ((maxNumOfPixels == UNCONSTRAINED) && (minSideLength == UNCONSTRAINED))
        {
            return 1;
        }
        else if (minSideLength == UNCONSTRAINED)
        {
            return lowerBound;
        }
        else
        {
            return upperBound;
        }
    }
    
    /**
     * 
     * @param angle 旋转角度
     * @param bitmap 要旋转的图片Bitmap
     * @return
     */
    public static Bitmap rotaingImageView(int angle, Bitmap bitmap)
    {
        // 旋转图片 动作
        Matrix matrix = new Matrix();
        
        matrix.postRotate(angle);
        System.out.println("angle2=" + angle);
        // 创建新的图片
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return resizedBitmap;
    }
    
    /**
     * 读取图片属性：旋转的角度
     * 
     * @param path 图片绝对路径
     * @return degree旋转的角度
     */
    public static int readPictureDegree(String path)
    {
        int degree = 0;
        try
        {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation =
                exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation)
            {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return degree;
        }
        return degree;
    }
    
    public static Bitmap compressImage(Bitmap image)
    {
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        if (image == null)
            return null;
        image.compress(CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 100)
        { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();// 重置baos即清空baos
            image.compress(CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;// 每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
        return bitmap;
    }
    
    // 图片按比例大小压缩方法（根据路径获取图片并压缩）
    public static Bitmap getimage(String srcPath)
    {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        // 开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);// 此时返回bm为空
        
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        // 现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        float hh = 800f;// 这里设置高度为800f
        float ww = 480f;// 这里设置宽度为480f
        // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;// be=1表示不缩放
        if (w > h && w > ww)
        {// 如果宽度大的话根据宽度固定大小缩放
            be = (int)(newOpts.outWidth / ww);
        }
        else if (w < h && h > hh)
        {// 如果高度高的话根据宽度固定大小缩放
            be = (int)(newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;// 设置缩放比例
        // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        return compressImage(bitmap);// 压缩好比例大小后再进行质量压缩
    }
}
