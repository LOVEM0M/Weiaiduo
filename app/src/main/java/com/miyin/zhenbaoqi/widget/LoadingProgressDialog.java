package com.miyin.zhenbaoqi.widget;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;

public class LoadingProgressDialog extends ProgressDialog {

    private static final int MIN_SHOW_TIME = 500;
    private static final int MIN_DELAY = 500;

    private long mStartTime = -1;
    private boolean mPostedHide = false;
    private boolean mPostedShow = false;
    private boolean mDismissed = false;

    private Handler mHandler = new Handler();

    private final Runnable mDelayedHide = () -> {
        mPostedHide = false;
        mStartTime = -1;
        dismiss();
    };

    private final Runnable mDelayedShow = () -> {
        mPostedShow = false;
        if (!mDismissed) {
            mStartTime = System.currentTimeMillis();
            show();
        }
    };

    public LoadingProgressDialog(Context context) {
        this(context, ProgressDialog.STYLE_HORIZONTAL);
    }

    public LoadingProgressDialog(Context context, int theme) {
        super(context, theme);
    }

    public void showDialog() {
        mStartTime = -1;
        mDismissed = false;
        mHandler.removeCallbacks(mDelayedHide);
        mPostedHide = false;
        if (!mPostedShow) {
            mHandler.postDelayed(mDelayedShow, MIN_DELAY);
            mPostedShow = true;
        }
    }

    public void hideDialog() {
        mDismissed = true;
        mHandler.removeCallbacks(mDelayedShow);
        mPostedShow = false;
        long diff = System.currentTimeMillis() - mStartTime;
        if (diff >= MIN_SHOW_TIME || mStartTime == -1) {
            dismiss();
        } else {
            if (!mPostedHide) {
                mHandler.postDelayed(mDelayedHide, MIN_SHOW_TIME - diff);
                mPostedHide = true;
            }
        }
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mHandler.removeCallbacks(mDelayedHide);
        mHandler.removeCallbacks(mDelayedShow);
    }

}
