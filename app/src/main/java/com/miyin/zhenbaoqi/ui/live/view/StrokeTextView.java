package com.miyin.zhenbaoqi.ui.live.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.TextView;

import com.miyin.zhenbaoqi.R;

public class StrokeTextView extends androidx.appcompat.widget.AppCompatTextView {

        private TextView borderText = null;///用于描边的TextView

    public StrokeTextView(Context context) {
            super(context);
            borderText = new TextView(context);
            init();
        }

    public StrokeTextView(Context context, AttributeSet attrs) {
            super(context, attrs);
            borderText = new TextView(context,attrs);
            init();
        }

    public StrokeTextView(Context context, AttributeSet attrs,
                          int defStyle) {
            super(context, attrs, defStyle);
            borderText = new TextView(context,attrs,defStyle);
            init();
        }

        public void init(){
            TextPaint tp1 = borderText.getPaint();
            tp1.setStrokeWidth(4);                                  //设置描边宽度
            tp1.setStyle(Paint.Style.STROKE);                             //对文字只描边
            borderText.setTextColor(getResources().getColor(R.color.price_stroke));  //设置描边颜色
            borderText.setGravity(getGravity());
        }

        @Override
        public void setLayoutParams (ViewGroup.LayoutParams params){
            super.setLayoutParams(params);
            borderText.setLayoutParams(params);
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            CharSequence tt = borderText.getText();

            //两个TextView上的文字必须一致
            if(tt== null || !tt.equals(this.getText())){
                borderText.setText(getText());
                this.postInvalidate();
            }
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            borderText.measure(widthMeasureSpec, heightMeasureSpec);
        }

        @Override
        protected void onLayout (boolean changed, int left, int top, int right, int bottom){
            super.onLayout(changed, left, top, right, bottom);
            borderText.layout(left, top, right, bottom);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            borderText.draw(canvas);
            super.onDraw(canvas);
        }
}
