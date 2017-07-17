package com.wch.libs.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.widget.Toast;

/**
 * File Utils
 * <ul>
 * Read or write file
 * <li>{@link #readFile(String)} read file</li>
 * <li>{@link #readFileToList(String)} read file to string list</li>
 * <li>{@link #writeFile(String, String, boolean)} write file</li>
 * <li>{@link #writeFile(String, InputStream)} write file</li>
 * </ul>
 * <ul>
 * Operate file
 * <li>{@link #getFileExtension(String)}</li>
 * <li>{@link #getFileName(String)}</li>
 * <li>{@link #getFileNameWithoutExtension(String)}</li>
 * <li>{@link #getFileSize(String)}</li>
 * <li>{@link #deleteFile(String)}</li>
 * <li>{@link #isFileExist(String)}</li>
 * <li>{@link #isFolderExist(String)}</li>
 * </ul>
 * 
 * @author Trinea 2012-5-12
 */
public class FileUtil
{
    
    public final static String FILE_EXTENSION_SEPARATOR = ".";
    
    /**
     * read file
     * 
     * @param filePath
     * @return if file not exist, return null, else return content of file
     * @throws IOException if an error occurs while operator BufferedReader
     */
    public static StringBuilder readFile(String filePath)
    {
        File file = new File(filePath);
        StringBuilder fileContent = new StringBuilder("");
        if (file == null || !file.isFile())
        {
            return null;
        }
        
        BufferedReader reader = null;
        try
        {
            reader = new BufferedReader(new FileReader(file));
            String line = null;
            while ((line = reader.readLine()) != null)
            {
                if (!fileContent.toString().equals(""))
                {
                    fileContent.append("\r\n");
                }
                fileContent.append(line);
            }
            reader.close();
            return fileContent;
        }
        catch (IOException e)
        {
            throw new RuntimeException("IOException occurred. ", e);
        }
        finally
        {
            if (reader != null)
            {
                try
                {
                    reader.close();
                }
                catch (IOException e)
                {
                    throw new RuntimeException("IOException occurred. ", e);
                }
            }
        }
    }
    
    /**
     * write file
     * 
     * @param filePath
     * @param content
     * @param append is append, if true, write to the end of file, else clear content of file and write into it
     * @return return true
     * @throws IOException if an error occurs while operator FileWriter
     */
    public static boolean writeFile(String filePath, String content, boolean append)
    {
        FileWriter fileWriter = null;
        try
        {
            fileWriter = new FileWriter(filePath, append);
            fileWriter.write(content);
            fileWriter.close();
            return true;
        }
        catch (IOException e)
        {
            throw new RuntimeException("IOException occurred. ", e);
        }
        finally
        {
            if (fileWriter != null)
            {
                try
                {
                    fileWriter.close();
                }
                catch (IOException e)
                {
                    throw new RuntimeException("IOException occurred. ", e);
                }
            }
        }
    }
    
    /**
     * write file
     * 
     * @param filePath
     * @param stream
     * @return return true
     * @throws IOException if an error occurs while operator FileWriter
     */
    public static boolean writeFile(String filePath, InputStream stream)
    {
        OutputStream o = null;
        try
        {
            o = new FileOutputStream(filePath);
            byte data[] = new byte[1024];
            int length = -1;
            while ((length = stream.read(data)) != -1)
            {
                o.write(data, 0, length);
            }
            o.flush();
            return true;
        }
        catch (FileNotFoundException e)
        {
            throw new RuntimeException("FileNotFoundException occurred. ", e);
        }
        catch (IOException e)
        {
            throw new RuntimeException("IOException occurred. ", e);
        }
        finally
        {
            if (o != null)
            {
                try
                {
                    o.close();
                    stream.close();
                }
                catch (IOException e)
                {
                    throw new RuntimeException("IOException occurred. ", e);
                }
            }
        }
    }
    
    /**
     * read file to string list, a element of list is a line
     * 
     * @param filePath
     * @return if file not exist, return null, else return content of file
     * @throws IOException if an error occurs while operator BufferedReader
     */
    public static List<String> readFileToList(String filePath)
    {
        File file = new File(filePath);
        List<String> fileContent = new ArrayList<String>();
        if (file == null || !file.isFile())
        {
            return null;
        }
        
        BufferedReader reader = null;
        try
        {
            reader = new BufferedReader(new FileReader(file));
            String line = null;
            while ((line = reader.readLine()) != null)
            {
                fileContent.add(line);
            }
            reader.close();
            return fileContent;
        }
        catch (IOException e)
        {
            throw new RuntimeException("IOException occurred. ", e);
        }
        finally
        {
            if (reader != null)
            {
                try
                {
                    reader.close();
                }
                catch (IOException e)
                {
                    throw new RuntimeException("IOException occurred. ", e);
                }
            }
        }
    }
    
    public static boolean writeListToFile(String filePath, List<String> list)
    {
        if (list == null || list.size() == 0)
        {
            return false;
        }
        FileWriter fileWriter = null;
        try
        {
            StringBuilder builder = new StringBuilder();
            int size = list.size();
            for (int i = 0; i < size; i++)
            {
                builder.append(list.get(i));
                if (i + 1 < size)
                {
                    builder.append("\n");
                }
            }
            fileWriter = new FileWriter(filePath, false);
            fileWriter.write(builder.toString());
            fileWriter.close();
            return true;
        }
        catch (IOException e)
        {
            throw new RuntimeException("IOException occurred. ", e);
        }
        finally
        {
            if (fileWriter != null)
            {
                try
                {
                    fileWriter.close();
                }
                catch (IOException e)
                {
                    throw new RuntimeException("IOException occurred. ", e);
                }
            }
        }
    }
    
    /**
     * get file name from path, not include suffix
     * 
     * <pre>
     *      getFileNameWithoutExtension(null)               =   null
     *      getFileNameWithoutExtension("")                 =   ""
     *      getFileNameWithoutExtension("   ")              =   "   "
     *      getFileNameWithoutExtension("abc")              =   "abc"
     *      getFileNameWithoutExtension("a.mp3")            =   "a"
     *      getFileNameWithoutExtension("a.b.rmvb")         =   "a.b"
     *      getFileNameWithoutExtension("c:\\")              =   ""
     *      getFileNameWithoutExtension("c:\\a")             =   "a"
     *      getFileNameWithoutExtension("c:\\a.b")           =   "a"
     *      getFileNameWithoutExtension("c:a.txt\\a")        =   "a"
     *      getFileNameWithoutExtension("/home/admin")      =   "admin"
     *      getFileNameWithoutExtension("/home/admin/a.txt/b.mp3")  =   "b"
     * </pre>
     * 
     * @param filePath
     * @return file name from path, not include suffix
     * @see
     */
    public static String getFileNameWithoutExtension(String filePath)
    {
        if (StringUtils.isEmpty(filePath))
        {
            return filePath;
        }
        
        int extenPosi = filePath.lastIndexOf(FILE_EXTENSION_SEPARATOR);
        int filePosi = filePath.lastIndexOf(File.separator);
        if (filePosi == -1)
        {
            return (extenPosi == -1 ? filePath : filePath.substring(0, extenPosi));
        }
        if (extenPosi == -1)
        {
            return filePath.substring(filePosi + 1);
        }
        return (filePosi < extenPosi ? filePath.substring(filePosi + 1, extenPosi) : filePath.substring(filePosi + 1));
    }
    
    /**
     * get file name from path, include suffix
     * 
     * <pre>
     *      getFileName(null)               =   null
     *      getFileName("")                 =   ""
     *      getFileName("   ")              =   "   "
     *      getFileName("a.mp3")            =   "a.mp3"
     *      getFileName("a.b.rmvb")         =   "a.b.rmvb"
     *      getFileName("abc")              =   "abc"
     *      getFileName("c:\\")              =   ""
     *      getFileName("c:\\a")             =   "a"
     *      getFileName("c:\\a.b")           =   "a.b"
     *      getFileName("c:a.txt\\a")        =   "a"
     *      getFileName("/home/admin")      =   "admin"
     *      getFileName("/home/admin/a.txt/b.mp3")  =   "b.mp3"
     * </pre>
     * 
     * @param filePath
     * @return file name from path, include suffix
     */
    public static String getFileName(String filePath)
    {
        if (StringUtils.isEmpty(filePath))
        {
            return filePath;
        }
        
        int filePosi = filePath.lastIndexOf(File.separator);
        return (filePosi == -1) ? filePath : filePath.substring(filePosi + 1);
    }
    
    /**
     * get folder name from path
     * 
     * <pre>
     *      getFolderName(null)               =   null
     *      getFolderName("")                 =   ""
     *      getFolderName("   ")              =   ""
     *      getFolderName("a.mp3")            =   ""
     *      getFolderName("a.b.rmvb")         =   ""
     *      getFolderName("abc")              =   ""
     *      getFolderName("c:\\")              =   "c:"
     *      getFolderName("c:\\a")             =   "c:"
     *      getFolderName("c:\\a.b")           =   "c:"
     *      getFolderName("c:a.txt\\a")        =   "c:a.txt"
     *      getFolderName("c:a\\b\\c\\d.txt")    =   "c:a\\b\\c"
     *      getFolderName("/home/admin")      =   "/home"
     *      getFolderName("/home/admin/a.txt/b.mp3")  =   "/home/admin/a.txt"
     * </pre>
     * 
     * @param filePath
     * @return
     */
    public static String getFolderName(String filePath)
    {
        
        if (StringUtils.isEmpty(filePath))
        {
            return filePath;
        }
        
        int filePosi = filePath.lastIndexOf(File.separator);
        return (filePosi == -1) ? "" : filePath.substring(0, filePosi);
    }
    
    /**
     * get suffix of file from path
     * 
     * <pre>
     *      getFileExtension(null)               =   ""
     *      getFileExtension("")                 =   ""
     *      getFileExtension("   ")              =   "   "
     *      getFileExtension("a.mp3")            =   "mp3"
     *      getFileExtension("a.b.rmvb")         =   "rmvb"
     *      getFileExtension("abc")              =   ""
     *      getFileExtension("c:\\")              =   ""
     *      getFileExtension("c:\\a")             =   ""
     *      getFileExtension("c:\\a.b")           =   "b"
     *      getFileExtension("c:a.txt\\a")        =   ""
     *      getFileExtension("/home/admin")      =   ""
     *      getFileExtension("/home/admin/a.txt/b")  =   ""
     *      getFileExtension("/home/admin/a.txt/b.mp3")  =   "mp3"
     * </pre>
     * 
     * @param filePath
     * @return
     */
    public static String getFileExtension(String filePath)
    {
        if (StringUtils.isBlank(filePath))
        {
            return filePath;
        }
        
        int extenPosi = filePath.lastIndexOf(FILE_EXTENSION_SEPARATOR);
        int filePosi = filePath.lastIndexOf(File.separator);
        if (extenPosi == -1)
        {
            return "";
        }
        return (filePosi >= extenPosi) ? "" : filePath.substring(extenPosi + 1);
    }
    
    /**
     * Creates the directory named by the trailing filename of this file, including the complete directory path required
     * to create this directory. <br/>
     * <br/>
     * <ul>
     * <strong>Attentions：</strong>
     * <li>makeDirs("C:\\Users\\Trinea") can only create users folder</li>
     * <li>makeFolder("C:\\Users\\Trinea\\") can create Trinea folder</li>
     * </ul>
     * 
     * @param filePath
     * @return true if the necessary directories have been created or the target directory already exists, false one of
     *         the directories can not be created.
     *         <ul>
     *         <li>if {@link FileUtil#getFolderName(String)} return null, return false</li>
     *         <li>if target directory already exists, return true</li>
     *         </ul>
     */
    public static boolean createFolder(String filePath, boolean recreate)
    {
        String folderName = getFolderName(filePath);
        if (StringUtils.isEmpty(folderName))
        {
            return false;
        }
        
        File folder = new File(folderName);
        if (folder.exists())
        {
            if (recreate)
            {
                deleteFile(folderName);
                return folder.mkdirs();
            }
            else
            {
                return true;
            }
        }
        else
        {
            return folder.mkdirs();
        }
    }
    
    /**
     * @param filePath
     * @return
     */
    public static boolean createFolder(String filePath)
    {
        return createFolder(filePath, false);
    }
    
    /**
     * Indicates if this file represents a file on the underlying file system.
     * 
     * @param filePath
     * @return
     */
    public static boolean isFileExist(String filePath)
    {
        if (StringUtils.isBlank(filePath))
        {
            return false;
        }
        
        File file = new File(filePath);
        return (file.exists() && file.isFile());
    }
    
    /**
     * Indicates if this file represents a directory on the underlying file system.
     * 
     * @param directoryPath
     * @return
     */
    public static boolean isFolderExist(String directoryPath)
    {
        if (StringUtils.isBlank(directoryPath))
        {
            return false;
        }
        
        File dire = new File(directoryPath);
        return (dire.exists() && dire.isDirectory());
    }
    
    /**
     * delete file or directory
     * <ul>
     * <li>if path is null or empty, return true</li>
     * <li>if path not exist, return true</li>
     * <li>if path exist, delete recursion. return true</li>
     * <ul>
     * 
     * @param path
     * @return
     */
    public static boolean deleteFile(String path)
    {
        if (StringUtils.isBlank(path))
        {
            return true;
        }
        
        File file = new File(path);
        if (!file.exists())
        {
            return true;
        }
        if (file.isFile())
        {
            return file.delete();
        }
        if (!file.isDirectory())
        {
            return false;
        }
        for (File f : file.listFiles())
        {
            if (f.isFile())
            {
                f.delete();
            }
            else if (f.isDirectory())
            {
                deleteFile(f.getAbsolutePath());
            }
        }
        return file.delete();
    }
    
    /**
     * get file size
     * <ul>
     * <li>if path is null or empty, return -1</li>
     * <li>if path exist and it is a file, return file size, else return -1</li>
     * <ul>
     * 
     * @param path
     * @return
     */
    public static long getFileSize(String path)
    {
        if (StringUtils.isBlank(path))
        {
            return -1;
        }
        
        File file = new File(path);
        return (file.exists() && file.isFile() ? file.length() : -1);
    }
    
    /**
     * 重命名文件/文件夹
     * 
     * @param path
     * @param newName
     */
    public static boolean rename(final String path, final String newName)
    {
        boolean result = false;
        if (TextUtils.isEmpty(path) || TextUtils.isEmpty(newName))
        {
            return result;
        }
        try
        {
            File file = new File(path);
            if (file.exists())
            {
                result = file.renameTo(new File(newName));
            }
        }
        catch (Exception e)
        {
        }
        
        return result;
    }
    
    /**
     * 读取文件
     * 
     * @param filePath
     * @return 输入流
     */
    public static InputStream readFileInputStream(String filePath)
    {
        if (filePath == null || filePath.length() == 0)
        {
            return null;
        }
        
        InputStream is = null;
        try
        {
            if (isFileExist(filePath))
            {
                File f = new File(filePath);
                is = new FileInputStream(f);
            }
            else
            {
                return null;
            }
        }
        catch (Exception ex)
        {
            return null;
        }
        return is;
    }
    
    /**
     * 创建一个空的文件
     * 
     * @param filePath
     * @param recreate 是否删除重建
     * @return
     */
    public static boolean createFile(String filePath, boolean recreate)
    {
        if (TextUtils.isEmpty(filePath))
        {
            return false;
        }
        try
        {
            File file = new File(filePath);
            if (file.exists())
            {
                if (recreate)
                {
                    file.delete();
                    file.createNewFile();
                }
            }
            else
            {
                // 如果路径不存在，先创建路径
                File parentFile = file.getParentFile();
                if (!parentFile.exists())
                {
                    parentFile.mkdirs();
                }
                file.createNewFile();
            }
        }
        catch (Exception e)
        {
            return false;
        }
        return true;
    }
    
    public static String getPath(Context context, Uri uri)
    {
        
        String filePath = null;
        if (Build.VERSION.SDK_INT >= 19)
        {
            filePath = getPathKitKat(context, uri);
            
        }
        else
        {
            if ("content".equalsIgnoreCase(uri.getScheme()))
            {
                String[] projection = {"_data"};
                Cursor cursor = null;
                try
                {
                    cursor = context.getContentResolver().query(uri, projection, null, null, null);
                    int column_index = cursor.getColumnIndexOrThrow("_data");
                    if (cursor.moveToFirst())
                    {
                        filePath = cursor.getString(column_index);
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
            else if ("file".equalsIgnoreCase(uri.getScheme()))
            {
                filePath = uri.getPath();
            }
        }
        if (filePath == null || filePath.equals(""))
        {
            Toast.makeText(context, "请尝试使用其他软件获取", Toast.LENGTH_SHORT).show();
            return null;
        }
        File file = new File(filePath);
        if (file == null || !file.exists())
        {
            Toast.makeText(context, "文件不存在",Toast.LENGTH_SHORT ).show();
            return null;
        }
        
        return filePath;
    }
    
    // 选图4.4bug
    public static String getPathKitKat(final Context context, final Uri uri)
    {
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        System.out.println("uri=" + uri);
        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri))
        {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri))
            {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                
                if ("primary".equalsIgnoreCase(type))
                {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
                
                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri))
            {
                
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri =
                    ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                
                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri))
            {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                
                Uri contentUri = null;
                if ("image".equals(type))
                {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                }
                else if ("video".equals(type))
                {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                }
                else if ("audio".equals(type))
                {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                
                final String selection = "_id=?";
                final String[] selectionArgs = new String[] {split[1]};
                
                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme()))
        {
            
            // Return the remote address
            if (isGooglePhotosUri(uri))
            {
                Toast.makeText(context, "暂不支持google相册", Toast.LENGTH_SHORT).show();
                return null;
            }
            
            // return uri.getLastPathSegment();
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme()))
        {
            return uri.getPath();
        }
        else
        {
            
        }
        
        return null;
    }
    
    /**
     * Get the value of the data column for this Uri. This is useful for MediaStore Uris, and other file-based
     * ContentProviders.
     * 
     * @param context The context.
     * @param uri The Uri to query.
     * @param selection (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs)
    {
        
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};
        
        try
        {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst())
            {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        }
        finally
        {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }
    
    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri)
    {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }
    
    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri)
    {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }
    
    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri)
    {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }
    
    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri)
    {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }
}
