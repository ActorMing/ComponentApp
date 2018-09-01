package com.lazy.component.util;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author :lazyMing
 * email   :407555147@qq.com
 * date    :2018/8/29
 * desc    :md5Utils(包括key排序返回md5加密)
 * address :
 * update  :
 */
public class Md5Utils {
    private static final String TAG = "Md5Utils";

    /**
     * 方法用途: 对所有传入参数按照字段名的 ASCII 码从小到大排序（字典序），并且生成url参数串<br>
     * 实现步骤: <br>
     *
     * @param paraMap    要排序的Map对象
     * @param urlEncode  是否需要URLENCODE
     * @param keyToLower 是否需要将Key转换为全小写
     *                   true:key转化成小写，false:不转化
     * @return
     */
    private static String formatUrlMap(Map<String, ? extends Object> paraMap, boolean urlEncode, boolean keyToLower) {
        String buff = "";
        Map<String, Object> tmpMap = (Map<String, Object>) paraMap;
        try {
            List<Map.Entry<String, Object>> infoIds = new ArrayList<>(tmpMap.entrySet());
            // 对所有传入参数按照字段名的 ASCII 码从小到大排序（字典序）
            Collections.sort(infoIds, new Comparator<Map.Entry<String, Object>>() {

                @Override
                public int compare(Map.Entry<String, Object> o1, Map.Entry<String, Object> o2) {
                    return (o1.getKey()).toString().compareTo(o2.getKey());
                }
            });
            // 构造URL 键值对的格式
            StringBuilder buf = new StringBuilder();
            for (Map.Entry<String, Object> item : infoIds) {
                if (!TextUtils.isEmpty(item.getKey())) {
                    String key = item.getKey();
                    String val = item.getValue().toString();
                    if (urlEncode) {
                        val = URLEncoder.encode(val, "utf-8");
                    }
                    if (keyToLower) {
//                        buf.append(key.toLowerCase() + "=" + val);
                        buf.append(key.toLowerCase() + val);
                    } else {
//                        buf.append(key + "=" + val);
                        buf.append(key + val);
                    }
//                    buf.append("&");
                }

            }
            buff = buf.toString();
//            if (buff.isEmpty() == false) {
//                buff = buff.substring(0, buff.length() - 1);
//            }
        } catch (Exception e) {
            return null;
        }
        return buff;
    }


    public static String getMD5Str(String str) {
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(str.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            System.out.println("NoSuchAlgorithmException caught!");
            System.exit(-1);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        byte[] byteArray = messageDigest.digest();
        StringBuffer md5StrBuff = new StringBuffer();
        for (int i = 0; i < byteArray.length; i++) {
            if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)
                md5StrBuff.append("0").append(Integer.toHexString(0xFF & byteArray[i]));
            else
                md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
        }
        return md5StrBuff.toString();
    }

    /**
     * 返回加密md5
     *
     * @return
     */
    public static String getsignature(String timestamp, String nonce, String hmacKey, Map<String, ? extends Object> paramMap) {
        String StringParamMap = null;
        if (paramMap != null) {
            StringParamMap = formatUrlMap(paramMap, true, false);
        }
        String Assemble = timestamp + nonce + hmacKey + StringParamMap;
        return getMD5Str(Assemble);
    }

    /**
     * json string to hashMap
     *
     * @param json
     * @return
     */
    public static HashMap<String, Object> json2HashMap(String json) {
        if (TextUtils.isEmpty(json)) {
            return null;
        } else {
//            return new Gson().fromJson(json,new TypeToken<HashMap<String,Object>>())
//            return JSON.parseObject(json, new TypeReference<HashMap<String, Object>>() {
//            });
            return new Gson().fromJson(json, new TypeToken<HashMap<String, Object>>() {
            }.getType());
        }
//        return new JS.fromJson(json, new TypeToken<HashMap<String, String>>() {
//        }.getType());
    }

    /**
     * 将JSONObjec对象转换成Map-List集合
     *
     * @param json
     * @return
     */
    public static Map<String, Object> toMap(JsonObject json) {
        Map<String, Object> map = new HashMap<String, Object>();
        Set<Map.Entry<String, JsonElement>> entrySet = json.entrySet();
        for (Iterator<Map.Entry<String, JsonElement>> iter = entrySet.iterator(); iter.hasNext(); ) {
            Map.Entry<String, JsonElement> entry = iter.next();
            String key = entry.getKey();
            Object value = entry.getValue();
            if (value instanceof JsonArray)
                map.put((String) key, toList((JsonArray) value));
            else if (value instanceof JsonObject)
                map.put((String) key, toMap((JsonObject) value));
            else
                map.put((String) key, value);
        }
        return map;
    }

    /**
     * 将JSONArray对象转换成List集合
     *
     * @param json
     * @return
     */
    public static List<Object> toList(JsonArray json) {
        List<Object> list = new ArrayList<Object>();
        for (int i = 0; i < json.size(); i++) {
            Object value = json.get(i);
            if (value instanceof JsonArray) {
                list.add(toList((JsonArray) value));
            } else if (value instanceof JsonObject) {
                list.add(toMap((JsonObject) value));
            } else {
                list.add(value);
            }
        }
        return list;
    }


    /**
     * 获取当前时间
     *
     * @return
     */
    public static String currentTime() {

        return ((int) (System.currentTimeMillis() / 1000)) + "";
    }


    /**
     * 获取随机数
     */
    public static String randoNumber() {

        return String.valueOf(((int) ((Math.random() * 9 + 1) * 100000)));
    }
}
