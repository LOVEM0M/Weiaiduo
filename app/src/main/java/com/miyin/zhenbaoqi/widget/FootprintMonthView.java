package com.miyin.zhenbaoqi.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.MonthView;
import com.miyin.zhenbaoqi.R;

import androidx.core.content.ContextCompat;

public class FootprintMonthView extends MonthView {

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

    public FootprintMonthView(Context context) {
        super(context);
        mContext = context;

        mSchemePaint.setAntiAlias(true);
        mSchemePaint.setStyle(Paint.Style.FILL);
        mSchemePaint.setTextAlign(Paint.Align.CENTER);

        mCurrentDayPaint.setAntiAlias(true);
        mCurrentDayPaint.setStyle(Paint.Style.FILL);
        mCurrentDayPaint.setColor(0xFFeaeaea);

        mRadius = context.getResources().getDimensionPixelSize(R.dimen.dp_11);
        mPointRadius = context.getResources().getDimensionPixelSize(R.dimen.dp_2);
    }

    @Override
    protected boolean onDrawSelected(Canvas canvas, Calendar calendar, int x, int y, boolean hasScheme) {
        int cx = x + mItemWidth / 2;
        int cy = y + mItemHeight / 3;
        canvas.drawCircle(cx, cy, mRadius, mSelectedPaint);
        return true;
    }

    @Override
    protected void onDrawScheme(Canvas canvas, Calendar calendar, int x, int y) {
        canvas.drawCircle(x + mItemWidth / 2f, y + mItemHeight / 3f * 2, mPointRadius, mSchemePaint);
    }

    @Override
    protected void onDrawText(Canvas canvas, Calendar calendar, int x, int y, boolean hasScheme, boolean isSelected) {
        float cx = x + mItemWidth / 2f - mContext.getResources().getDimension(R.dimen.dp_1);
        float cy = y + mItemHeight / 3f;
        float top = y - mItemHeight / 6f - mContext.getResources().getDimension(R.dimen.dp_1);

        if (calendar.isCurrentDay() && !isSelected) {
            canvas.drawCircle(cx, cy, mRadius, mCurrentDayPaint);
        }

        if (isSelected) {
            canvas.drawText(String.valueOf(calendar.getDay()), cx, mTextBaseLine + top, mSelectTextPaint);
        } else {
            canvas.drawText(String.valueOf(calendar.getDay()), cx, mTextBaseLine + top, calendar.isCurrentDay() ? mCurDayTextPaint : calendar.isCurrentMonth() ? mCurMonthTextPaint : mOtherMonthTextPaint);
        }
    }

}
