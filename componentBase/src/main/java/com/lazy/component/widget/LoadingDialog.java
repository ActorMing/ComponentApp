package com.lazy.component.widget;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import com.lazy.component.commpoentbase.R;


/**
 * Created by zdxiang on 2018/3/24 0024.下午 4:21
 *
 * @author zdxiang
 * @description LoadingDialog
 */

public class LoadingDialog extends Dialog {

    @SuppressLint("StaticFieldLeak")
    private static LoadingDialog dialog;

    private ImageView ivProgress;

    public LoadingDialog(@NonNull Context context) {
        super(context, R.style.loading_dialog);
        setContentView(R.layout.dialog_loading);
        ivProgress = findViewById(R.id.iv_progress);
        startAnim();
    }

    private void startAnim() {
        ObjectAnimator rot = ObjectAnimator.ofFloat(ivProgress, "rotation", 0, -359);
        rot.setInterpolator(new LinearInterpolator());
        rot.setDuration(1500);
        rot.setRepeatCount(-1);
        rot.start();
    }

    public static LoadingDialog with(Context context) {
        if (dialog == null) {
            dialog = new LoadingDialog(context);
            dialog.setCancelable(true);
        }
        return dialog;
    }

    public static LoadingDialog with(Context context, boolean cancelable) {
        if (dialog == null) {
            dialog = new LoadingDialog(context);
            dialog.setCancelable(cancelable);
        }
        return dialog;
    }


    @Override
    public void dismiss() {
        super.dismiss();
        if (dialog != null) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            dialog = null;
        }
    }
}
