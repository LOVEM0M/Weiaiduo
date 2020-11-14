package com.miyin.zhenbaoqi.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.miyin.zhenbaoqi.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;

public class TranslateScrollView extends NestedScrollView {

    private boolean mIsTranslate = false;
    private View mTranslateView;
    private float mDistance = 0;
    private float mDownY;
    private OnTranslateListener mOnTranslateListener;
    private int mMaxTranslateDistance;
    private int mType = 0;
    private float mScaleRatio = 0.4f;
    private float mLastDistance = 0;

    public TranslateScrollView(@NonNull Context context) {
        this(context, null);
    }

    public TranslateScrollView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TranslateScrollView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mMaxTranslateDistance = context.getResources().getDimensionPixelSize(R.dimen.dp_60);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        // 不可过度滚动，否则上移后下拉会出现部分空白的情况
        setOverScrollMode(OVER_SCROLL_NEVER);
        // 获得默认第一个view
        if (getChildAt(0) != null && getChildAt(0) instanceof ViewGroup) {
            ViewGroup vg = (ViewGroup) getChildAt(0);
            if (vg.getChildCount() > 0) {
                ViewGroup innerViewGroup = (ViewGroup) vg.getChildAt(0);
                if (innerViewGroup != null && innerViewGroup.getChildCount() > 0) {
                    mTranslateView = innerViewGroup.getChildAt(1);
                }
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (mTranslateView == null || mType != 0) {
            return super.onTouchEvent(ev);
        }
        switch (ev.getAction()) {
            case MotionEvent.ACTION_MOVE:
                if (!mIsTranslate) {
                    if (getScrollY() == 0) {
                        mDownY = ev.getY();
                    } else {
                        break;
                    }
                }

                float move = ev.getY();
                mDistance = (move - mDownY) * mScaleRatio + mLastDistance;
                mIsTranslate = true;

                if (mDistance < 0) {
                    if (getScrollY() == 0) {
                        mDownY = move;
                        mLastDistance = 0;
                        mTranslateView.setTranslationY(0);
                    }
                    break;
                } else if (mDistance > mMaxTranslateDistance) {
                    mDownY = move - mMaxTranslateDistance / mScaleRatio + mLastDistance;
                    mTranslateView.setTranslationY(mMaxTranslateDistance);
                    return true;
                } else {
                    mTranslateView.setTranslationY(mDistance);
                    return true;
                }
            case MotionEvent.ACTION_UP:
                mIsTranslate = false;

//                if (mDistance < 0) {
//                    mDistance = 0;
//                } else if (mDistance > mMaxTranslateDistance) {
//                    mDistance = mMaxTranslateDistance;
//                }
//                mLastDistance = mDistance;

                mTranslateView.setTranslationY(0);

                if (mDistance >= mMaxTranslateDistance) {
                    if (null != mOnTranslateListener) {
                        mOnTranslateListener.onTranslate();
                    }
                }
                break;
        }
        return super.onTouchEvent(ev);
    }

    public void setOnTranslateListener(OnTranslateListener onTranslateListener) {
        mOnTranslateListener = onTranslateListener;
    }

    public void setType(int type) {
        mType = type;
    }

    /**
     * 滑动监听
     */
    public interface OnTranslateListener {
        void onTranslate();
    }

}
