package com.miyin.zhenbaoqi.widget;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.miyin.zhenbaoqi.R;

public class StateLayout extends FrameLayout {

    private static final int STATE_NORMAL = 0;
    private static final int STATE_LOADING = 1;
    private static final int STATE_ERROR = 2;
    private static final int STATE_EMPTY = 3;

    private View mLoadingView;
    private View mErrorView;
    private View mEmptyView;
    private int mCurrentState = STATE_NORMAL;
    private OnRetryClickListener mOnRetryClickListener;

    public StateLayout(@NonNull Context context) {
        this(context, null);
    }

    public StateLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StateLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mLoadingView = View.inflate(context, R.layout.view_loading, null);
        addView(mLoadingView);
        mLoadingView.setVisibility(View.GONE);

        mErrorView = View.inflate(context, R.layout.view_error, null);
        addView(mErrorView);
        mErrorView.setVisibility(View.GONE);
        mErrorView.setOnClickListener(v -> {
            if (null != mOnRetryClickListener) {
                mOnRetryClickListener.onErrorRetryClick();
            }
        });

        mEmptyView = View.inflate(context, R.layout.view_empty, null);
        addView(mEmptyView);
        mEmptyView.setVisibility(View.GONE);
        mEmptyView.setOnClickListener(v -> {
            if (null != mOnRetryClickListener) {
                mOnRetryClickListener.onEmptyRetryClick();
            }
        });
    }

    public void showNormal() {
        if (mCurrentState == STATE_NORMAL) {
            return;
        }

        hideCurrentView();
        mCurrentState = STATE_NORMAL;
    }

    public void showLoading() {
        if (mCurrentState == STATE_LOADING) {
            return;
        }

        hideCurrentView();
        mCurrentState = STATE_LOADING;
        mLoadingView.setVisibility(View.VISIBLE);
    }

    public void showError() {
        if (mCurrentState == STATE_ERROR) {
            return;
        }

        hideCurrentView();
        mCurrentState = STATE_ERROR;
        mErrorView.setVisibility(View.VISIBLE);
    }

    public void showEmpty() {
        if (mCurrentState == STATE_EMPTY) {
            return;
        }

        hideCurrentView();
        mCurrentState = STATE_EMPTY;
        mEmptyView.setVisibility(View.VISIBLE);
    }

    private void hideCurrentView() {
        switch (mCurrentState) {
            case STATE_LOADING:
                mLoadingView.setVisibility(View.GONE);
                break;
            case STATE_ERROR:
                mErrorView.setVisibility(View.GONE);
                break;
            case STATE_EMPTY:
                mEmptyView.setVisibility(View.GONE);
                break;
        }
    }

    public void setOnRetryClickListener(OnRetryClickListener onRetryClickListener) {
        mOnRetryClickListener = onRetryClickListener;
    }

    public interface OnRetryClickListener {
        void onErrorRetryClick();

        void onEmptyRetryClick();
    }

}
