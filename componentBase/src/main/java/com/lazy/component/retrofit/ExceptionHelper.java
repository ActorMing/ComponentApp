package com.lazy.component.retrofit;

import android.content.Context;
import android.net.ParseException;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.JsonParseException;
import com.vondear.rxtool.view.RxToast;

import org.json.JSONException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

/**
 * @author :zdxiang
 * email   :407555147@qq.com
 * date    :2018/8/29
 * desc    :异常抛出帮助类
 * address :
 * update  :
 */
public class ExceptionHelper {

    public static String handleException(Throwable e) {
        e.printStackTrace();
        String error;
        // 网络超时
        if (e instanceof SocketTimeoutException) {
            Log.e("TAG", "网络连接异常: " + e.getMessage());
            error = "网络连接异常";

        }
        // 均视为网络错误
        else if (e instanceof ConnectException) {
            Log.e("TAG", "网络连接异常: " + e.getMessage());

            error = "网络连接异常";
        }
        // 均视为解析错误
        else if (e instanceof JsonParseException
                || e instanceof JSONException
                || e instanceof ParseException) {
            Log.e("TAG", "数据解析异常: " + e.getMessage());
            error = "数据解析异常";
        }
        // 服务器返回的错误信息
        else if (e instanceof ApiException) {
            error = e.getCause().getMessage();
        } else if (e instanceof UnknownHostException) {
            Log.e("TAG", "网络连接异常: " + e.getMessage());
            error = "网络连接异常";
        } else if (e instanceof IllegalArgumentException) {
            Log.e("TAG", "下载文件已存在: " + e.getMessage());
            error = "下载文件已存在";
        }
        // 未知错误
        else {
            try {
                Log.e("TAG", "错误: " + e.getMessage());
            } catch (Exception e1) {
                Log.e("TAG", "未知错误Debug调试 ");
            }
            error = "错误";
        }
        return error;
    }

    public static void handExceptionToast(Context context, Throwable e) {
        RxToast.showToast(context, handleException(e), Toast.LENGTH_SHORT);
    }
}
