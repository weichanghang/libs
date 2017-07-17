package com.wch.libs.net;

import android.os.Environment;

/**
 * URLs集合
 * 
 */
public class URLs
{
    // 环境
    public final static String URL = "http://192.168.22.35:8080/qiyitong-api/api/gateway.do";
    // 本机环境
    public final static String ADDRESS = Environment
            .getExternalStorageDirectory() + "/com.wch.libs/";
    // 本机图片缓存拍照图片
    public final static String ADDRESSIMAGE = URLs.ADDRESS + "photo/";

    public static final String GETUIBASE = "https://restapi.getui.com/v1/";

}
