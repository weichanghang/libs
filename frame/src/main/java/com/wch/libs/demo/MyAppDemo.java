package com.wch.libs.demo;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.HttpHeaders;
import com.wch.libs.application.WchApp;

/**
 * 用的时候一定要继承wchapp，范例就以这个来写
 */
public class MyAppDemo extends WchApp {
    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void initPre() {
        super.initPre();
        BmobOpen = true;//想要哪个第三方的用哪个,默认true不用写
        GetuiOpen = true;
        OKGoOpen=true;
    }

    @Override
    public void initPost() {
        super.initPost();
        HttpHeaders headers = new HttpHeaders();
        headers.put("application/json", "charset=utf-8");
        OkGo.getInstance().addCommonHeaders(headers);
    }
}
