package com.wch.libs.demo;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.wch.libs.R;
import com.wch.libs.activity.BaseActivity;
import com.wch.libs.callback.DialogCallback;
import com.wch.libs.callback.JsonCallback;
import com.wch.libs.model.GetuiModel;
import com.wch.libs.net.URLs;
import com.wch.libs.util.PreferenceUtils;
import com.wch.libs.util.Sha256;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;


public class GetuiDemo extends BaseActivity {

    String appid = "";
    String appkey = "";
    String mastersecret = "";
    String token = "";
    private TextView tv;
    private EditText title, content;

    @Override
    protected void onCreateActivity(Bundle savedInstanceState) {
        super.onCreateActivity(savedInstanceState);
        setContentView(R.layout.demo_bmob);
    }

    @Override
    protected void initViews() {
        super.initViews();
        initkey();


        findViewById(R.id.click1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                request(1,"notification");
            }
        });
        findViewById(R.id.click2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                request(1,"transmission");
            }
        });
        tv = (TextView) findViewById(R.id.tv);
        title = (EditText) findViewById(R.id.title);
        content = (EditText) findViewById(R.id.content);
        request(0,"");
    }

    private void initkey() {
        try {
            ApplicationInfo appInfo = getPackageManager()
                    .getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
            appid = appInfo.metaData.getString("PUSH_APPID");
            appkey = appInfo.metaData.getString("PUSH_APPKEY");
            mastersecret = appInfo.metaData.getString("PUSH_MASTERSECRET");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void request(int num,String type) {
        if (num == 0 || TextUtils.isEmpty(token)) {//鉴权
            String time = String.valueOf(System.currentTimeMillis());
            JsonObject json = new JsonObject();
            json.addProperty("sign", Sha256.SHA256Encrypt(appkey + time + mastersecret));
            json.addProperty("timestamp", time);
            json.addProperty("appkey", appkey);
            OkGo.<GetuiModel>post(URLs.GETUIBASE + appid + "/auth_sign").tag(0).upJson(json.toString()).execute(callback);
        } else if (num == 1) {//单推
            OkGo.<GetuiModel>post(URLs.GETUIBASE + appid + "/push_single").tag(1).headers("authtoken", token).upJson(single(type)).execute(callback);
        } else if (num == 2) {//群发
            OkGo.<GetuiModel>post(URLs.GETUIBASE + appid + "/save_list_body").tag(2).headers("authtoken", token).upJson(all(type)).execute(callback);
        } else if (num == 3) {//群发有条件
            OkGo.<GetuiModel>post(URLs.GETUIBASE + appid + "/push_app").tag(3).headers("authtoken", token).upJson(allApp(type)).execute(callback);
        }
    }

    private String single(String type) {
        //根据cid单推
        JsonObject json = new JsonObject();
        json.add("message", getMessage(type));
        json.add(type, getNotification(type));
        json.addProperty("cid", PreferenceUtils.getString(getBaseContext(), "clientid"));
        Random random = new Random();
        json.addProperty("requestid", String.valueOf(random.nextInt(1000000))
                + String.valueOf(random.nextInt(1000000)) + String.valueOf(random.nextInt(1000000))
                + String.valueOf(random.nextInt(1000000)));//requestid length should be 10~30
        return json.toString();
    }

    private String all(String type) {
        //根据cid群推,---没反应，坏的
        JsonObject json = new JsonObject();
        json.add("message", getMessage(type));
        json.add(type, getNotification(type));
        return json.toString();
    }

    private String allApp(String type) {
        //根据条件群推---通的
        JsonObject json = new JsonObject();
        json.add("message", getMessage(type));
        json.add(type, getNotification(type));
        json.add("condition", getCondition());
        Random random = new Random();
        json.addProperty("requestid", String.valueOf(random.nextInt(1000000))
                + String.valueOf(random.nextInt(1000000)) + String.valueOf(random.nextInt(1000000))
                + String.valueOf(random.nextInt(1000000)));//requestid length should be 10~30
        return json.toString();
    }

    private JsonObject getNotification(String type) {
        JsonObject notification = new JsonObject();
        if(!type.equals("transmission"))
            notification.add("style", getStyle());
        notification.addProperty("transmission_type", false);//true为启动app
        notification.addProperty("transmission_content", "透传内容");
        if (!TextUtils.isEmpty(content.getText().toString())) {
            notification.addProperty("transmission_content", content.getText().toString());
        }
        return notification;
    }
    private JsonArray getCondition() {
        JsonArray condition = new JsonArray();
        JsonObject json1 = new JsonObject();
        JsonObject json2 = new JsonObject();
        JsonObject json3 = new JsonObject();

        json1.addProperty("key", "phonetype");
        JsonArray values = new JsonArray();
        values.add("ANDROID");
        json1.add("values", values);
        json1.addProperty("opt_type", 0);

        json2.addProperty("key", "region");
        JsonArray values1 = new JsonArray();
        values1.add("11000000");
        values1.add("12000000");
        json2.add("values", values1);
        json2.addProperty("opt_type", 0);

        json3.addProperty("key", "tag");
        JsonArray values2 = new JsonArray();
        values2.add("usertag");
        json3.add("values", values2);
        json3.addProperty("opt_type", 0);
        condition.add(json1);
//        condition.add(json2);
//        condition.add(json3);
        return condition;
    }

    private JsonObject getMessage(String msgtype) {
        JsonObject message = new JsonObject();
        message.addProperty("appkey", appkey);
        message.addProperty("is_offline", true);
        message.addProperty("offline_expire_time", 10000000);
        message.addProperty("msgtype", msgtype);
        return message;
    }

    private JsonObject getStyle() {
        JsonObject style = new JsonObject();
        style.addProperty("type", 1);
        style.addProperty("text", "测试wch的Demo");
        style.addProperty("title", "这里是测试");
        if (!TextUtils.isEmpty(title.getText().toString())) {
            style.addProperty("title", title.getText().toString());
        }
        if (!TextUtils.isEmpty(content.getText().toString())) {
            style.addProperty("text", content.getText().toString());
        }
        style.addProperty("logourl", "https://dev.getui.com/images/ic_launcher.png");
        style.addProperty("is_ring", true);
        style.addProperty("is_vibrate", true);
        style.addProperty("is_clearable", true);
        return style;
    }

    DialogCallback callback = new DialogCallback<GetuiModel>(this) {
        @Override
        public void onSuccess(Response<GetuiModel> response) {
            if ((Integer) response.getRawResponse().request().tag() == 0) {
              token = response.body().auth_token;
              Toast(response.body().result);
            } else if ((Integer) response.getRawResponse().request().tag() == 1) {
              Toast(response.body().result);
            }
        }

        @Override
        protected void StringCallBack(String result) {
          tv.setText(result);
        }
    };
}
