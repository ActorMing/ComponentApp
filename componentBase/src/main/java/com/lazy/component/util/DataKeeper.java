package com.lazy.component.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static android.R.attr.value;

/**
 * Created by SCPC05 on 2017/4/6.
 * 数据存储工具类
 * @must
 * <br> 1.将fileRootPath中的包名
 * <br> 2.在Application中调用init方法
 *
 */
public class DataKeeper {
    private static final String TAG = "DataKeeper";

    public static final String SAVE_SUCCEED = "保存成功";
    public static final String SAVE_FAILED = "保存失败";
    public static final String DELETE_SUCCEED = "删除成功";
    public static final String DELETE_FAILED = "删除失败";

    public static final String ROOT_SHARE_PREFS_ = "SC_SHARE_PREFS_";

    //文件缓存<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    public static final String fileRootPath = getSDPath() != null ? (getSDPath() + "/soldcrazy/") : null;
    public static final String accountPath = fileRootPath + "account/";
    public static final String audioPath = fileRootPath + "audio/";
    public static final String videoPath = fileRootPath + "video/";
    public static final String imagePath = fileRootPath + "image/";
    public static final String tempPath = fileRootPath + "temp/";
    public static final String cachePath = fileRootPath + "cache/";
    //文件缓存>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    //文件名字<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    public static final String photo = "PHOTO";
    //文件名字>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


    //存储文件的类型<<<<<<<<<<<<<<<<<<<<<<<<<
    public static final int TYPE_FILE_TEMP = 0;								//保存保存临时文件
    public static final int TYPE_FILE_IMAGE = 1;							//保存图片
    public static final int TYPE_FILE_VIDEO = 2;							//保存视频
    public static final int TYPE_FILE_AUDIO = 3;							//保存语音

    //存储文件的类型>>>>>>>>>>>>>>>>>>>>>>>>>

    //不能实例化
    private DataKeeper() {}

    private static Context context;
    //获取context，获取存档数据库引用
    public static void init(Context context_) {
        context = context_;

        Log.i(TAG, "init fileRootPath = " + fileRootPath);

        //判断SD卡存在
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            if(fileRootPath != null) {
                File file = new File(imagePath);
                if(!file.exists()) {
                    file.mkdirs();
                }
                file = new File(videoPath);
                if(!file.exists()) {
                    file.mkdir();
                }
                file = new File(audioPath);
                if(!file.exists()) {
                    file.mkdir();
                }
                file = new File(fileRootPath + accountPath);
                if(!file.exists()) {
                    file.mkdir();
                }
                file = new File(tempPath);
                if(!file.exists()) {
                    file.mkdir();
                }
                file = new File(cachePath);
                if(!file.exists()) {
                    file.mkdir();
                }
            }
        }
    }




    //**********外部存储缓存***************
    /**
     * 存储缓存文件 返回文件绝对路径
     * @param file
     * 		要存储的文件
     * @param type
     * 		文件的类型
     *		IMAGE = "imgae";							//图片
     *		VIDEO = "video";							//视频
     *		VOICE = "voice";							//语音
     *		 = "voice";							//语音
     * @return	存储文件的绝对路径名
     * 		若SDCard不存在返回null
     */
    public static String storeFile(File file, String type) {

        if(!hasSDCard()) {
            return null;
        }
        String suffix = file.getName().substring(file.getName().lastIndexOf(".") + 1);
        byte[] data = null;
        try {
            FileInputStream in = new FileInputStream(file);
            data = new byte[in.available()];
            in.read(data, 0, data.length);
            in.close();
        } catch (IOException e) {
            Log.e(TAG, "storeFile  try { FileInputStream in = new FileInputStream(file); ... >>" +
                    " } catch (IOException e) {\n" + e.getMessage());
        }
        return storeFile(data, suffix, type);
    }

    /** @return	存储文件的绝对路径名
    若SDCard不存在返回null */
    @SuppressLint("DefaultLocale")
    public static String storeFile(byte[] data, String suffix, String type) {

        if(!hasSDCard()) {
            return null;
        }
        String path = null;
        if(type.equals(TYPE_FILE_IMAGE)) {
            path = imagePath + "IMG_" + Long.toHexString(System.currentTimeMillis()).toUpperCase()
                    + "." + suffix;
        } else if(type.equals(TYPE_FILE_VIDEO)) {
            path = videoPath + "VIDEO_" + Long.toHexString(System.currentTimeMillis()).toUpperCase()
                    + "." + suffix;
        } else if(type.equals(TYPE_FILE_AUDIO)) {
            path = audioPath + "VOICE_" + Long.toHexString(System.currentTimeMillis()).toUpperCase()
                    + "." + suffix;
        }
        try {
            FileOutputStream out = new FileOutputStream(path);
            out.write(data, 0, data.length);
            out.close();
        } catch (FileNotFoundException e) {
            Log.e(TAG, "storeFile  try { FileInputStream in = new FileInputStream(file); ... >>" +
                    " } catch (FileNotFoundException e) {\n" + e.getMessage() + "\n\n >> path = null;");
            path = null;
        } catch (IOException e) {
            Log.e(TAG, "storeFile  try { FileInputStream in = new FileInputStream(file); ... >>" +
                    " } catch (IOException e) {\n" + e.getMessage() + "\n\n >> path = null;");
            path = null;
        }
        return path;
    }

    /**jpg
     * @return
     */
    public static String getImageFileCachePath() {
        return getFileCachePath(TYPE_FILE_IMAGE, "IMG_" + Long.toHexString(System.currentTimeMillis()).toUpperCase(), "jpg");
    }


    /**jpg
     * @param fileName
     * @return
     */
    public static String getImageFileCachePath(String fileName) {
        return getFileCachePath(TYPE_FILE_IMAGE, fileName, "jpg");
    }
    /**mp4
     * @param fileName
     * @return
     */
    public static String getVideoFileCachePath(String fileName) {
        return getFileCachePath(TYPE_FILE_VIDEO, fileName, "mp4");
    }
    /**mp3
     * @param fileName
     * @return
     */
    public static String getAudioFileCachePath(String fileName) {
        return getFileCachePath(TYPE_FILE_AUDIO, fileName, "mp3");
    }

    /**temp
     * @return
     */
    public static String getTempFileCachePath(String extension) {
        return getFileCachePath(TYPE_FILE_TEMP, "TEMP_" + Long.toHexString(System.currentTimeMillis()).toUpperCase(), extension);
    }





    /** 获取一个文件缓存的路径  */
    public static String getFileCachePath(int fileType, String fileName, String formSuffix) {

        switch (fileType) {
            case TYPE_FILE_IMAGE:
                return imagePath + fileName + "." + formSuffix;
            case TYPE_FILE_VIDEO:
                return videoPath + fileName + "." + formSuffix;
            case TYPE_FILE_AUDIO:
                return audioPath + fileName + "." + formSuffix;
            default:
                return tempPath + fileName + "." + formSuffix;
        }
    }

    /**若存在SD 则获取SD卡的路径 不存在则返回null*/
    public static String getSDPath(){
        File sdDir = null;
        String path = null;
        //判断sd卡是否存在
        boolean sdCardExist = hasSDCard();
        if (sdCardExist) {
            //获取跟目录
            sdDir = Environment.getExternalStorageDirectory();
            path = sdDir.toString();
        }
        return path;
    }

    /** 获取文件名
     *
     * @param filepath
     * @return
     */
    public static String getFileNameFromPath(String filepath) {
        if ((filepath != null) && (filepath.length() > 0)) {
            int sep = filepath.lastIndexOf('/');
            if ((sep > -1) && (sep < filepath.length() - 1)) {
                return filepath.substring(sep + 1);
            }
        }
        return filepath;
    }

    /** 获取不带扩展名的文件名
     *
     * @param filename
     * @return
     */
    public static String getFileNameNoEx(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot > -1) && (dot < (filename.length()))) {
                return filename.substring(0, dot);
            }
        }
        return filename;
    }

    /**
     * 获取文件扩展名
     * @param filename
     * @return
     */
    public static String getExtensionName(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot > -1) && (dot < (filename.length() - 1))) {
                return filename.substring(dot + 1);
            }
        }
        return "";
    }

    /**判断是否有SD卡*/
    public static boolean hasSDCard() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }


    //使用SharedPreferences保存 <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    public static SharedPreferences getRootSharedPreferences() {
        return context.getSharedPreferences(ROOT_SHARE_PREFS_, Context.MODE_PRIVATE);
    }
    //String
    /**使用SharedPreferences保存
     * @param path
     * @param key
     * @param value
     */
    public static void saveString(String path, String key, String value) {
        saveString(path, Context.MODE_PRIVATE, key, value);
    }
    /**使用SharedPreferences保存
     * @param path
     * @param mode
     * @param key
     * @param value
     */
    public static void saveString(String path, int mode, String key, String value) {
        saveString(context.getSharedPreferences(path, mode), key, value);
    }
    /**使用SharedPreferences保存
     * @param sdf
     * @param key
     * @param value
     */
    public static void saveString(SharedPreferences sdf, String key, String value) {
        if (sdf == null || TextUtils.isEmpty(key) || TextUtils.isEmpty(value)) {
            Log.e(TAG, "save sdf == null || \n key = " + key + ";\n value = " + value + "\n >> return;");
            return;
        }
        sdf.edit().remove(key).putString(key, value).commit();
    }

    public static String getString(String path, String key) {
        return  getString(path, Context.MODE_PRIVATE, key);
    }

    public static void saveInt(String path, String key, int value) {
        saveInt(path, Context.MODE_PRIVATE, key, value);
    }
    /**使用SharedPreferences保存
     * @param path
     * @param mode
     * @param key
     * @param value
     */
    public static void saveInt(String path, int mode, String key, int value) {
        saveInt(context.getSharedPreferences(path, mode), key, value);
    }
    /**使用SharedPreferences保存
     * @param sdf
     * @param key
     * @param value
     */
    public static void saveInt(SharedPreferences sdf, String key, int value) {
        if (sdf == null || TextUtils.isEmpty(key)) {
            Log.e(TAG, "save sdf == null || \n key = " + key + ";\n value = " + value + "\n >> return;");
            return;
        }
        sdf.edit().remove(key).putInt(key, value).commit();
    }

    public static int getInt(String path, String key) {
        return  getInt(path, Context.MODE_PRIVATE, key);
    }
    public static int getInt(String path, int mode, String key) {
        return getInt(context.getSharedPreferences(path, mode), key);
    }
    public static int getInt(SharedPreferences sdf, String key) {
        if (sdf == null || TextUtils.isEmpty(key) ) {
            Log.e(TAG, "save sdf == null || \n key = " + key + ";\n value = " + value + "\n >> return;");
            return 0;
        }
        return sdf.getInt(key,0);
    }


    public static String getString(String path, int mode, String key) {
        return getString(context.getSharedPreferences(path, mode), key);
    }

    public static String getString(SharedPreferences sdf, String key) {
        if (sdf == null || TextUtils.isEmpty(key) ) {
            Log.e(TAG, "save sdf == null || \n key = " + key + ";\n value = " + value + "\n >> return;");
            return "";
        }
        return sdf.getString(key,"");
    }


    //boolean
    /**使用SharedPreferences保存
     * @param path
     * @param key
     * @param value
     */
    public static void saveBoolean(String path, String key, boolean value) {
        saveBoolean(path, Context.MODE_PRIVATE, key, value);
    }
    /**使用SharedPreferences保存
     * @param path
     * @param mode
     * @param key
     * @param value
     */
    public static void saveBoolean(String path, int mode, String key, boolean value) {
        saveBoolean(context.getSharedPreferences(path, mode), key, value);
    }
    /**使用SharedPreferences保存
     * @param sdf
     * @param key
     * @param value
     */
    public static void saveBoolean(SharedPreferences sdf, String key, boolean value) {
        if (sdf == null || TextUtils.isEmpty(key)) {
            Log.e(TAG, "save sdf == null || \n key = " + key + ";\n value = " + value + "\n >> return;");
            return;
        }
        sdf.edit().remove(key).putBoolean(key, value).commit();
    }

    public static boolean getBoolean(String path, String key) {
        return  getBoolean(path, Context.MODE_PRIVATE, key);
    }

    public static boolean getBoolean(String path, int mode, String key) {
        return getBoolean(context.getSharedPreferences(path, mode), key);
    }


    public static boolean getBoolean(SharedPreferences sdf, String key) {
        if (sdf == null || TextUtils.isEmpty(key) ) {
            Log.e(TAG, "save sdf == null || \n key = " + key + ";\n value = " + value + "\n >> return;");
            return false;
        }
        return sdf.getBoolean(key,false);
    }

    public static boolean getFistInBoolean(String key) {

        return context.getSharedPreferences(ROOT_SHARE_PREFS_, Context.MODE_PRIVATE).getBoolean(key,true);
    }
    public static void saveFistInBoolean(String key, boolean value) {
        saveBoolean(context.getSharedPreferences(ROOT_SHARE_PREFS_, Context.MODE_PRIVATE), key,value);
    }

    public static void clear(String name){
        context.getSharedPreferences(name, Context.MODE_PRIVATE).edit().clear().commit();
    }


    //使用SharedPreferences保存 >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>



}
