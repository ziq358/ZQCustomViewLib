package com.zq.customviewlib;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.Observable;

public class OverLayoutManager extends RecyclerView.LayoutManager {

    private static int num = 4;
    private int operateCount = num + 1; // 需处理的数目 若num=3，则多处理一个尾 若num=4，则多处理一个头一个尾

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    private int originalSize;

    public void setOriginalCount(int size) {
        originalSize = size;
    }

    @Override
    public boolean canScrollVertically() {
        return true;
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        if(state.getItemCount() == 0){
            removeAndRecycleAllViews(recycler);
            return;
        }
        Log.e("ziq", String.format("onLayoutChildren"));
        //分离全部已有的view，放入临时缓存
        detachAndScrapAttachedViews(recycler);//没有回收的话会 重叠。子view一直增多
        fillHorizontalLeft(recycler, state, 0);
    }

    private void fillHorizontalLeft(RecyclerView.Recycler recycler, RecyclerView.State state, int dx) {
        int count = getItemCount() > operateCount ? operateCount : getItemCount();
        float ratio = (3 / 5.0f);
        if (originalSize >= num || count > originalSize) {
            // 当 originalSize >= 4 时，或者 当大于初始数据时 即插入数据
            for (int i = count - 1; i >= 0; i--) {
                View child = recycler.getViewForPosition(i);
                measureChildWithMargins(child, 0, 0);
                addView(child);
                int width = getDecoratedMeasuredWidth(child);
                int height = getDecoratedMeasuredHeight(child);
                if (i == 0) { // 第一条
                    layoutDecoratedWithMargins(child, 0, -height, width, 0);
                } else if (i == num) { // 第五条
                    layoutDecoratedWithMargins(child, 0, (int)(2.2 * (height)), width, (int)(2.2 * (height)) + height);
                } else { // 第二三四条
                    layoutDecoratedWithMargins(child, 0, (int) ((i - 1) * height * ratio), width, (int) ((i - 1) * height * ratio + height));
                }
            }
        } else {
            // 当 originalSize < 4 时，即 1，2，3
            for (int i = count - 1; i >= 0; i--) {
                View child = recycler.getViewForPosition(i);
                measureChildWithMargins(child, 0, 0);
                addView(child);
                int width = getDecoratedMeasuredWidth(child);
                int height = getDecoratedMeasuredHeight(child);
                layoutDecoratedWithMargins(child, 0, (int) (i * (height * ratio)), width, (int) (i * (height * ratio) + height));
            }
        }
    }


}
