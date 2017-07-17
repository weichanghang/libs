package com.wch.libs.callback;

import android.preference.PreferenceActivity;
import android.support.annotation.Nullable;
import android.support.annotation.UiThread;

import com.google.gson.stream.JsonReader;
import com.lzy.okgo.adapter.Call;
import com.lzy.okgo.callback.AbsCallback;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.wch.libs.util.Convert;
import com.wch.libs.util.JsonUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;

import static com.lzy.okgo.utils.HttpUtils.runOnUiThread;


public abstract class JsonCallback<T> extends AbsCallback<T> {

    protected Type type;
    protected Class<T> clazz;

    public JsonCallback(Type type) {
        this.type = type;
    }

    public JsonCallback(Class<T> clazz) {
        this.clazz = clazz;
    }

    public JsonCallback() {}

    @Override
    public void onStart(Request<T, ? extends Request> request) {
        super.onStart(request);
        // 主要用于在所有请求之前添加公共的请求头或请求参数,例如登录授权的 token
        // 使用的设备信息,可以随意添加,也可以什么都不传,还可以在这里对所有的参数进行加密，均在这里实现
        //  request.headers("header1", "HeaderValue1").params("params1", "ParamsValue1");
    }

    /**
     * 该方法是子线程处理，不能做ui相关的工作
     * 主要作用是解析网络返回的 response 对象,生产onSuccess回调中需要的数据对象
     * 这里的解析工作不同的业务逻辑基本都不一样,所以需要自己实现,以下给出的时模板代码,实际使用根据需要修改
     */
    @Override
    public T convertResponse(okhttp3.Response response) throws Throwable {
        if (type == null) {
            if (clazz == null) {
                // 如果没有通过构造函数传进来，就自动解析父类泛型的真实类型（有局限性，继承后就无法解析到）
                Type genType = getClass().getGenericSuperclass();
                type = ((ParameterizedType) genType).getActualTypeArguments()[0];
            } else {
                return parseClass(response, clazz);
            }
        }

        //  if (type instanceof ParameterizedType) {
        // return parseParameterizedType(response, (ParameterizedType) type);
        //  } else
        if (type instanceof Class) {
            return parseClass(response, (Class<?>) type);
        } else {
            return parseType(response, type);
        }
    }

    private T parseClass(okhttp3.Response response, Class<?> rawType) throws Exception {
        if (rawType == null) return null;
        okhttp3.ResponseBody body = response.body();
        if (body == null) return null;
        JsonReader jsonReader = new JsonReader(body.charStream());

        if (rawType == String.class) {
            //noinspection unchecked
            return (T) GetResult(body.string());
        } else if (rawType == JSONObject.class) {
            //noinspection unchecked
            return (T) new JSONObject(GetResult(body.string()));
        } else if (rawType == JSONArray.class) {
            //noinspection unchecked
            return (T) new JSONArray(GetResult(body.string()));
        } else {
            T t = Convert.fromJson(jsonReader, rawType);
            GetResult(Convert.toJson(t));
            return t;
        }
    }

    private T parseType(okhttp3.Response response, Type type) throws Exception {
        if (type == null) return null;
        ResponseBody body = response.body();
        if (body == null) return null;
        JsonReader jsonReader = new JsonReader(body.charStream());

        // 泛型格式如下： new JsonCallback<任意JavaBean>(this)
        T t = Convert.fromJson(jsonReader, type);
        GetResult(body.string());
        return t;
    }

    private String GetResult(final String result){
        runOnUiThread(new Runnable(){
            @Override
            public void run() {
                StringCallBack(result);
            }
        });
        return result;
    }
    protected void StringCallBack(String result){}

//    private T parseParameterizedType(okhttp3.Response response, ParameterizedType type) throws Exception {
//        if (type == null) return null;
//        ResponseBody body = response.body();
//        if (body == null) return null;
//        JsonReader jsonReader = new JsonReader(body.charStream());
//
//        Type rawType = type.getRawType();                     // 泛型的实际类型
//        Type typeArgument = type.getActualTypeArguments()[0]; // 泛型的参数
//        if (rawType != LzyResponse.class) {
//            // 泛型格式如下： new JsonCallback<外层BaseBean<内层JavaBean>>(this)
//            T t = Convert.fromJson(jsonReader, type);
//            response.close();
//            return t;
//        } else {
//            if (typeArgument == Void.class) {
//                // 泛型格式如下： new JsonCallback<LzyResponse<Void>>(this)
//                SimpleResponse simpleResponse = Convert.fromJson(jsonReader, SimpleResponse.class);
//                response.close();
//                //noinspection unchecked
//                return (T) simpleResponse.toLzyResponse();
//            } else {
//                // 泛型格式如下： new JsonCallback<LzyResponse<内层JavaBean>>(this)
//                LzyResponse lzyResponse = Convert.fromJson(jsonReader, type);
//                response.close();
//                int code = lzyResponse.code;
//                //这里的0是以下意思
//                //一般来说服务器会和客户端约定一个数表示成功，其余的表示失败，这里根据实际情况修改
//                if (code == 0) {
//                    //noinspection unchecked
//                    return (T) lzyResponse;
//                } else if (code == 104) {
//                    throw new IllegalStateException("用户授权信息无效");
//                } else if (code == 105) {
//                    throw new IllegalStateException("用户收取信息已过期");
//                } else {
//                    //直接将服务端的错误信息抛出，onError中可以获取
//                    throw new IllegalStateException("错误代码：" + code + "，错误信息：" + lzyResponse.msg);
//                }
//            }
//        }
//    }
}
