package com.zq.customviewlib;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;

import androidx.annotation.IntDef;
import androidx.appcompat.widget.AppCompatImageView;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


/**
 * @author john.
 * @since 2018/5/3.
 * Des:
 */
public class TranslateLoadingView extends AppCompatImageView {

    public static final int STATUS_IDLE = 0;
    public static final int STATUS_SEND = 1;
    public static final int STATUS_RECEIVE = 2;

    @IntDef({STATUS_IDLE, STATUS_SEND,STATUS_RECEIVE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface TranslateStatus {}

    private int status = STATUS_IDLE;

    private UIHandler mHandler;

    private int pointHeight;
    private int minHeight;
    private int maxHeight;
    private int dy;
    private int oneHeight;
    private int twoHeight;
    private int threeHeight;
    private boolean oneGrow = false;
    private boolean twoGrow = false;
    private boolean threeGrow = false;

    private int oneAngle = 240;
    private int twoAngle = 0;
    private int threeAngle = 120;
    private int angle = 0;


    private String oneColor = "#10C569";
    private String twoColor = "#FF9C1B";
    private String threeColor = "#1B8DFF";

    public TranslateLoadingView(Context context) {
        this(context, null);
    }

    public TranslateLoadingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TranslateLoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        setWillNotDraw(false);
        mHandler = new UIHandler();
        pointHeight = (int) 14 * 3;
        minHeight = pointHeight / 5;
        maxHeight = pointHeight * 2;
        dy = (maxHeight - minHeight) / 24;
    }

    public void startAnimation() {
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
            mHandler.sendEmptyMessage(0);
        }
    }

    public void stopAnimation() {
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        stopAnimation();
        super.onDetachedFromWindow();
    }

    private class UIHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    break;
                case 1:
                    if (oneGrow) {
                        oneHeight = oneHeight + dy * 2;
                        if (oneHeight >= maxHeight) {
                            oneHeight = maxHeight;
                            oneGrow = false;
                        }
                    } else {
                        oneHeight = oneHeight - dy * 2;
                        if (oneHeight < minHeight) {
                            oneHeight = minHeight;
                            oneGrow = true;
                        }
                    }
                    if (twoGrow) {
                        twoHeight = twoHeight + dy * 2;
                        if (twoHeight >= maxHeight) {
                            twoHeight = maxHeight;
                            twoGrow = false;
                        }
                    } else {
                        twoHeight = twoHeight - dy * 2;
                        if (twoHeight < minHeight) {
                            twoHeight = minHeight;
                            twoGrow = true;
                        }
                    }
                    if (threeGrow) {
                        threeHeight = threeHeight + dy * 2;
                        if (threeHeight >= maxHeight) {
                            threeHeight = maxHeight;
                            threeGrow = false;
                        }
                    } else {
                        threeHeight = threeHeight - dy * 2;
                        if (threeHeight < minHeight) {
                            threeHeight = minHeight;
                            threeGrow = true;
                        }
                    }
                    invalidate();
                    mHandler.sendEmptyMessageDelayed(1, 50);
                    break;
                case 2:
                    angle = (angle + 8) % 360;
                    invalidate();
                    mHandler.sendEmptyMessageDelayed(2, 20);
                    break;

            }
        }
    }

    public void setStatus(@TranslateStatus int status) {
        mHandler.removeCallbacksAndMessages(null);
        this.status = status;
        if (status == STATUS_SEND) {
            oneHeight = maxHeight;
            twoHeight = minHeight + dy * 8;
            threeHeight = minHeight + dy * 18;

            oneGrow = true;
            twoGrow = true;
            threeGrow = false;
            mHandler.sendEmptyMessage(1);
        } else if (status == STATUS_RECEIVE) {
            angle = 0;
            mHandler.sendEmptyMessage(2);
        }
        invalidate();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int height = getHeight();
        int width = getWidth();

        Paint pointPaint = new Paint();
        pointPaint.setAntiAlias(true);
        //背景
        pointPaint.setColor(Color.parseColor("#F1F1F1"));
        canvas.drawCircle(width / 2, height / 2, width / 2, pointPaint);

        if (status == STATUS_IDLE) {
            pointPaint.setColor(Color.parseColor(oneColor));
            canvas.drawCircle(width / 2 - width / 4, height / 2, pointHeight / 2, pointPaint);

            pointPaint.setColor(Color.parseColor(twoColor));
            canvas.drawCircle(width / 2, height / 2, pointHeight / 2, pointPaint);

            pointPaint.setColor(Color.parseColor(threeColor));
            canvas.drawCircle(width / 2 + width / 4, height / 2, pointHeight / 2, pointPaint);
        } else if (status == STATUS_SEND) {
            pointPaint.setStrokeWidth(pointHeight);
            pointPaint.setStrokeCap(Paint.Cap.ROUND);
            pointPaint.setColor(Color.parseColor(oneColor));
            canvas.drawLine(width / 2 - width / 4, height / 2 - oneHeight, width / 2 - width / 4, height / 2 + oneHeight, pointPaint);

            pointPaint.setColor(Color.parseColor(twoColor));
            canvas.drawLine(width / 2, height / 2 - twoHeight, width / 2, height / 2 + twoHeight, pointPaint);

            pointPaint.setColor(Color.parseColor(threeColor));
            canvas.drawLine(width / 2 + width / 4, height / 2 - threeHeight, width / 2 + width / 4, height / 2 + threeHeight, pointPaint);

        } else if (status == STATUS_RECEIVE) {

            int r = (int) (width / 6);
            int center = width / 2;

            pointPaint.setColor(Color.parseColor(oneColor));
            float hudu1 = (float) ((2 * Math.PI / 360) * (oneAngle + angle));
            float x1 = (float) (center + Math.sin(hudu1) * r);
            float y1 = (float) (center - Math.cos(hudu1) * r);
            canvas.drawCircle(x1, y1, pointHeight / 2, pointPaint);

            pointPaint.setColor(Color.parseColor(twoColor));
            float hudu2 = (float) ((2 * Math.PI / 360) * (twoAngle + angle));
            float x2 = (float) (center + Math.sin(hudu2) * r);
            float y2 = (float) (center - Math.cos(hudu2) * r);
            canvas.drawCircle(x2, y2, pointHeight / 2, pointPaint);

            pointPaint.setColor(Color.parseColor(threeColor));
            float hudu3 = (float) ((2 * Math.PI / 360) * (threeAngle + angle));
            float x3 = (float) (center + Math.sin(hudu3) * r);
            float y3 = (float) (center - Math.cos(hudu3) * r);
            canvas.drawCircle(x3, y3, pointHeight / 2, pointPaint);

        }


    }
}