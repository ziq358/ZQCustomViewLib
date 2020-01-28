package com.zq.customviewlib;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.Observable;

public class OverLayoutManager extends RecyclerView.LayoutManager {

    private int mFirstVisiblePosition;//屏幕可见的第一个View的Position
    private int mLastVisiblePosition;//屏幕可见的最后一个View的Position

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public boolean canScrollHorizontally() {
        return true;
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        if(state.getItemCount() == 0){
            removeAndRecycleAllViews(recycler);
            return;
        }
        Log.e("ziq", String.format("onLayoutChildren"));

        fillHorizontalLeft(recycler, state, 0);
    }

    private void fillHorizontalLeft(RecyclerView.Recycler recycler, RecyclerView.State state, int dx) {
        //分离全部已有的view，放入临时缓存
        detachAndScrapAttachedViews(recycler);//没有回收的话会 重叠。子view一直增多

        mFirstVisiblePosition = 0;
        mLastVisiblePosition = getItemCount() - 1;
        int startX = 0;
        int margin = 10;

        for (int i = mFirstVisiblePosition; i <= mLastVisiblePosition; i++) {
            View child = recycler.getViewForPosition(i);
            measureChildWithMargins(child, 0, 0);
            addView(child);
            int width = getDecoratedMeasuredWidth(child);
            int height = getDecoratedMeasuredHeight(child);
            int l, t, r, b;
            l = startX;
            t = getPaddingTop();
            r = startX + width;
            b = getPaddingTop() + height;
            layoutDecoratedWithMargins(child, l, t, r, b);
            startX += width + margin;
            //判断下一个view的布局位置是不是已经超出屏幕了，若超出，修正mLastVisiPos并跳出遍历
            if (startX + width + margin > getWidth() - getPaddingRight()) {
                mLastVisiblePosition = i;
                break;
            }
        }
        Log.e("ziq", "fillHorizontalLeft: "+ mLastVisiblePosition);

    }


}
