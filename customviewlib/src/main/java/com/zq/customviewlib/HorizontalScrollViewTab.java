package com.zq.customviewlib;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

/**
 * @author wuyanqiang
 * @date 2018/10/12
 */
public class HorizontalScrollViewTab extends HorizontalScrollView {

    private LinearLayout mLlContent;
    private int mCurrentPosition = 0;
    private OnHorizontalNavigationSelectListener mOnHorizontalNavigationSelectListener;

    public HorizontalScrollViewTab(Context context) {
        this(context, null);
    }

    public HorizontalScrollViewTab(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HorizontalScrollViewTab(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        mLlContent = new LinearLayout(context);
        mLlContent.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mLlContent.setOrientation(LinearLayout.HORIZONTAL);
        addView(mLlContent);
    }

    public void addData(List<String> list){
        if(list != null){
            for (int i = 0; i < list.size(); i++) {
                TextView labelTv = new TextView(getContext());
                labelTv.setPadding(10, 0, 10, 0);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
                lp.leftMargin = 10;
                lp.rightMargin = 10;
                labelTv.setGravity(Gravity.CENTER);
                labelTv.setLayoutParams(lp);
                labelTv.setText(list.get(i));
                labelTv.setTextColor(Color.parseColor("#000000"));
                labelTv.setTextSize(14);
                mLlContent.addView(labelTv);
                final int index = i;
                labelTv.setOnClickListener(new OnClickListener() {
                    public void onClick(View view) {
                        TextView tv = (TextView) mLlContent.getChildAt(index);
                        tv.setTextColor(Color.parseColor("#ffa200"));
                        tv.setScaleX(1 + 0.2f );
                        tv.setScaleY(1 + 0.3f );
                        TextView childLast = (TextView) mLlContent.getChildAt(mCurrentPosition);
                        childLast.setTextColor(Color.parseColor("#000000"));
                        childLast.setScaleX(1);
                        childLast.setScaleY(1);
                        HorizontalScrollViewTab.this.setPosition(index);
                        if (mOnHorizontalNavigationSelectListener != null) {
                            mOnHorizontalNavigationSelectListener.select(index);
                        }
                    }
                });
            }
        }
        linearGradientUtil = new LinearGradientUtil(Color.parseColor("#000000"), Color.parseColor("#ffa200"));
    }
    LinearGradientUtil linearGradientUtil;
    public void setPositionChange(int from, int to, float positionOffset){
        if(from != to
                && from >= 0 && from <= (mLlContent.getChildCount() - 1)
                && to >= 0 && to <= (mLlContent.getChildCount() -1)){

            TextView fromTv = (TextView) mLlContent.getChildAt(from);
            fromTv.setTextColor(linearGradientUtil.getColor(1 - positionOffset));
            fromTv.setScaleX(1 + 0.2f * (1 - positionOffset));
            fromTv.setScaleY(1 + 0.3f * (1 - positionOffset));

            TextView toTv = (TextView) mLlContent.getChildAt(to);
            toTv.setTextColor(linearGradientUtil.getColor(positionOffset));
            toTv.setScaleX(1 + 0.2f * positionOffset);
            toTv.setScaleY(1 + 0.3f * positionOffset);
        }

    }

    public synchronized void setPosition(int index){

        int childCount = this.mLlContent.getChildCount();
        if (index > childCount - 1) {
            throw new RuntimeException("position more size");
        }
        if (index == this.mCurrentPosition) {
            return;
        }
        this.mCurrentPosition = index;
        if (mCurrentPosition == 0) {
            scrollTo(0, 0);
        } else {
            View child = this.mLlContent.getChildAt(index);
            int left = child.getLeft();
            int parentWithMid = getWidth() / 2;
            left = left - parentWithMid + child.getWidth() / 2;
            smoothScrollTo(left, 0);
        }
    }

    public void setOnHorizontalNavigationSelectListener(OnHorizontalNavigationSelectListener mOnHorizontalNavigationSelectListener) {
        this.mOnHorizontalNavigationSelectListener = mOnHorizontalNavigationSelectListener;
    }

    public interface OnHorizontalNavigationSelectListener {
        void select(int index);
    }


    public class LinearGradientUtil {
        private int mStartColor;
        private int mEndColor;

        public LinearGradientUtil(int startColor, int endColor) {
            this.mStartColor = startColor;
            this.mEndColor = endColor;
        }

        public void setStartColor(int startColor) {
            this.mStartColor = startColor;
        }

        public void setEndColor(int endColor) {
            this.mEndColor = endColor;
        }
        public int getColor(float radio) {
            int redStart = Color.red(mStartColor);
            int blueStart = Color.blue(mStartColor);
            int greenStart = Color.green(mStartColor);
            int redEnd = Color.red(mEndColor);
            int blueEnd = Color.blue(mEndColor);
            int greenEnd = Color.green(mEndColor);

            int red = (int) (redStart + ((redEnd - redStart) * radio + 0.5));
            int greed = (int) (greenStart + ((greenEnd - greenStart) * radio + 0.5));
            int blue = (int) (blueStart + ((blueEnd - blueStart) * radio + 0.5));
            return Color.argb(255,red, greed, blue);
        }
    }

}
