package com.wch.libs.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 保存数据至preference
 * 
 * @author hanweipeng
 * @version V1.0 创建时间：2013-12-25 下午6:30:32
 */
public class PreferenceUtils
{
    /**
     * 存入文件中的工程名
     */
    public static final String PREFERENCES_NAME = "com.weich.frame.preferences";
    
    /**
     * 保存至preferences中
     * 
     * @param key
     * @return
     */
    public static void setString(Context context, String key, String value)
    {
        SharedPreferences sp = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, value);
        editor.commit();
    }
    
    /**
     * 从preferences中获取
     * 
     * @param key
     */
    public static String getString(Context context, String key)
    {
        SharedPreferences sp = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        return sp.getString(key, "");
    }
    
    /**
     * 保存至preferences中
     * 
     * @param key
     * @return
     */
    public static void setInt(Context context, String key, int value)
    {
        SharedPreferences sp = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(key, value);
        editor.commit();
    }
    
    /**
     * 从preferences中获取
     * 
     * @param key
     */
    public static int getInt(Context context, String key)
    {
        SharedPreferences sp = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        return sp.getInt(key, -1);
    }
    
    /**
     * 从preferences中获取
     * 
     * @param key
     */
    public static String getStringWithDefault(Context context, String key)
    {
        SharedPreferences sp = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        return sp.getString(key, "0");
    }
    
    /**
     * 删除preferences中的数据
     * 
     * @param key
     * @return
     */
    public static void delString(Context context, String key)
    {
        SharedPreferences sp = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(key);
        editor.commit();
    }
    
    /**
     * 保存至preferences中
     * 
     * @param key
     * @return
     */
    public static void setLong(Context context, String key, Long value)
    {
        SharedPreferences sp = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putLong(key, value);
        editor.commit();
    }
    
    /**
     * 从preferences中获取
     * 
     * @param key
     */
    public static Long getLong(Context context, String key, long def)
    {
        SharedPreferences sp = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        return sp.getLong(key, def);
    }
    
    public static void setBoolean(Context context, String key, boolean value)
    {
        SharedPreferences sp = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }
    
    public static boolean getBoolean(Context context, String key)
    {
        SharedPreferences sp = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        return sp.getBoolean(key, false);
    }
    public static boolean getBoolean(Context context, String key,boolean defvalue)
    {
        SharedPreferences sp = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        return sp.getBoolean(key, defvalue);
    }
}
