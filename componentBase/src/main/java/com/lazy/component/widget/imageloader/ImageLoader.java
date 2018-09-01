package com.lazy.component.widget.imageloader;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

/**
 * @author :lazyMing
 * email   :407555147@qq.com
 * date    :2018/8/29
 * desc    :图片加载
 * address :https://mp.weixin.qq.com/s?__biz=MzA4NTQwNDcyMA==&mid=2650661623&idx=1&sn=ab28ac6587e8a5ef1241be7870851355#rd
 * update  :
 */
public class ImageLoader {


    /**
     * 加载图片
     *
     * @param context   上下文对象
     * @param imageUrl  图片地址
     * @param imageView imageView对象
     */
    public static void with(Context context, String imageUrl, ImageView imageView) {
        if (!TextUtils.isDigitsOnly(imageUrl)) {
            Glide.with(context)
                    .load(imageUrl)
                    .into(imageView);
        } else {
            // TODO: 2018/8/29 replace your app default image resource
            Glide.with(context).load(imageUrl).into(imageView);
        }
    }

}
