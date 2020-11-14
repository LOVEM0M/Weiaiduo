package com.miyin.zhenbaoqi.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.WeekView;
import com.miyin.zhenbaoqi.R;

public class FootprintWeekView extends WeekView {

    private Context mContext;
    private int mRadius;

    /**
     * 今天的背景色
     */
    private Paint mCurrentDayPaint = new Paint();

    /**
     * 圆点半径
     */
    private float mPointRadius;

    public FootprintWeekView(Context context) {
        super(context);
        mContext = context;

        mSchemePaint.setAntiAlias(true);
        mSchemePaint.setStyle(Paint.Style.FILL);
        mSchemePaint.setTextAlign(Paint.Align.CENTER);

        mCurrentDayPaint.setAntiAlias(true);
        mCurrentDayPaint.setStyle(Paint.Style.FILL);
        mCurrentDayPaint.setColor(0xFFeaeaea);

        mPointRadius = mContext.getResources().getDimensionPixelSize(R.dimen.dp_2);
        mRadius = mContext.getResources().getDimensionPixelSize(R.dimen.dp_11);
    }

    @Override
    protected boolean onDrawSelected(Canvas canvas, Calendar calendar, int x, boolean hasScheme) {
        float cx = x + mItemWidth / 2f;
        float cy = mItemHeight / 3f;
        canvas.drawCircle(cx, cy, mRadius, mSelectedPaint);
        return true;
    }

    @Override
    protected void onDrawScheme(Canvas canvas, Calendar calendar, int x) {
        canvas.drawCircle(x + mItemWidth / 2f, mItemHeight / 3f * 2, mPointRadius, mSchemePaint);
    }

    @Override
    protected void onDrawText(Canvas canvas, Calendar calendar, int x, boolean hasScheme, boolean isSelected) {
        float cx = x + mItemWidth / 2f - mContext.getResources().getDimension(R.dimen.dp_1);
        float cy = mItemHeight / 3f;
        float top = -mItemHeight / 6f - mContext.getResources().getDimension(R.dimen.dp_1);

        if (calendar.isCurrentDay() && !isSelected) {
            canvas.drawCircle(cx, cy, mRadius, mCurrentDayPaint);
        }

        if (isSelected) {
            canvas.drawText(String.valueOf(calendar.getDay()), cx, mTextBaseLine + top, mSelectTextPaint);
        } else {
            canvas.drawText(String.valueOf(calendar.getDay()), cx, mTextBaseLine + top, calendar.isCurrentDay() ? mCurDayTextPaint : mCurMonthTextPaint);
        }
    }

}
