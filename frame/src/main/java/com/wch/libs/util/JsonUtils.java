package com.wch.libs.util;

import java.util.ArrayList;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

/**
 * json管理
 *
 * @author Administrator
 *
 */
public class JsonUtils {
    private static Gson gson = new Gson();

    public static <T> T object(String json, Class<T> classOfT) {
        return gson.fromJson(json, classOfT);
    }

    public static <T> String toJson(Class<T> param) {
        return gson.toJson(param);
    }

    public static <T> String toJson(T param) {
        return gson.toJson(param);
    }

    public static <T> JsonObject toJsonObject(T param) {
        return new JsonParser().parse(gson.toJson(param)).getAsJsonObject();
    }

    public static <T> JsonArray toJsonArray(T param) {
        return new JsonParser().parse(gson.toJson(param)).getAsJsonArray();
    }

    /**
     * 将json格式转换成map对象
     *
     * @param jsonStr
     * @return
     */
    public static Map<?, ?> jsonToMap(String jsonStr) {
        Map<?, ?> objMap = null;
        if (gson != null) {
            java.lang.reflect.Type type = new com.google.gson.reflect.TypeToken<Map<?, ?>>() {
            }.getType();
            objMap = gson.fromJson(jsonStr, type);
        }
        return objMap;
    }

    public static Object getJsonValue(String jsonStr, String key) {
        Object rulsObj = null;
        Map<?, ?> rulsMap = jsonToMap(jsonStr);
        if (rulsMap != null && rulsMap.size() > 0) {
            rulsObj = rulsMap.get(key);
        }
        return rulsObj;
    }

    public static String getCode(String json) {
        try {
            JSONObject jb = new JSONObject(json);
            return jb.getString("code");
        } catch (JSONException e) {
        } catch (NumberFormatException e) {
        } catch (JsonSyntaxException e) {
        }
        return "-1";
    }

    public static String getHeadMessage(String json) {
        try {
            JSONObject jb = new JSONObject(json);
            return jb.getString("msg");
        } catch (JSONException e) {
        } catch (NumberFormatException e) {
        } catch (JsonSyntaxException e) {
        }
        return "";
    }

    public static <T> T getBodyObject(String json, Class<T> classOfT) {
        try {
            JSONObject jb = new JSONObject(json);
            String result = jb.getString("body");
            return gson.fromJson(result, classOfT);
        } catch (JSONException e) {
        } catch (NumberFormatException e) {
        } catch (JsonSyntaxException e) {
        }
        return null;
    }

    public static <T> ArrayList<T> getBodyArray(String json, Class<T> classOfT) {
        try {
            JSONObject jb = new JSONObject(json);
            JSONArray result = jb.getJSONArray("body");

            ArrayList<T> classoftarray = new ArrayList<T>();
            for (int i = 0; i < result.length(); i++) {
                classoftarray.add(gson.fromJson(result.getJSONObject(i)
                        .toString(), classOfT));
            }
            return classoftarray;
        } catch (JSONException e) {
        } catch (NumberFormatException e) {
        } catch (JsonSyntaxException e) {
        }
        return null;
    }

    public static <T> ArrayList<T> getArray(String json, Class<T> classOfT) {
        try {
            JSONObject result = new JSONObject(json);
            JSONArray array = result.getJSONArray("saverOrderList");
            ArrayList<T> classoftarray = new ArrayList<T>();
            for (int i = 0; i < array.length(); i++) {
                classoftarray.add(gson.fromJson(array.getJSONObject(i)
                        .toString(), classOfT));
            }
            return classoftarray;
        } catch (JSONException e) {
        } catch (NumberFormatException e) {
        } catch (JsonSyntaxException e) {
        }
        return null;
    }

    public static int setNullInt(JSONObject jsonObject, String key) {
        try {
            return jsonObject.getInt(key);
        } catch (Exception e) {
            return -1;
        }
    }

    public static String setNullStr(JSONObject jsonObject, String key) {
        try {
            return jsonObject.getString(key);
        } catch (Exception e) {
            return "";
        }
    }
}
