package com.zq.customviewlib;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.util.List;

/**
 * Created by john
 */

public class AutoRollViewPager extends ViewPager {
    private static final int ROLL_ANIMATION_DURATION = 1500;
    private static final int ROLL_DURATION = 2000;

    private RollViewPagerAdapter mRollViewPagerAdapter;
    private AutoRollHandler mAutoRollHandler;
    private static final int ROLL_START = 1;
    private static final int ROLL_STOP = 2;
    public AutoRollViewPager(Context context) {
        this(context, null);
    }

    public AutoRollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init(){
        setViewPagerScrollSpeed();
        mAutoRollHandler = new AutoRollHandler(this);
        addOnPageChangeListener(new SimpleOnPageChangeListener(){
            @Override
            public void onPageScrollStateChanged(int state) {
                if(mRollViewPagerAdapter != null && state == ViewPager.SCROLL_STATE_IDLE){
                    int position = getCurrentItem();
                    if(mRollViewPagerAdapter.isRollEnableForData()){
                        int count = mRollViewPagerAdapter.getCount();
                        if(position == 0){
                            setCurrentItem(count - 2, false);
                        }else if(position == count - 1){
                            setCurrentItem(1, false);
                        }
                    }
                }
            }
        });
    }

    private void setViewPagerScrollSpeed() {
        try {
            Field scroller = ViewPager.class.getDeclaredField("mScroller");
            scroller.setAccessible(true);
            AutoScroller scroll = new AutoScroller(getContext());
            scroller.set(this, scroll);
        } catch (Exception ignored) {
        }
    }

    @Override
    public void setAdapter(PagerAdapter adapter) {
        super.setAdapter(adapter);
        if(adapter instanceof RollViewPagerAdapter){
            mRollViewPagerAdapter = (RollViewPagerAdapter) adapter;
            setCurrentItem(1, false);
        }else{
            throw new RuntimeException("need to set RollViewPagerAdapter");
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        requestLayout();
        startRoll();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopRoll();
    }

    public void startRoll(){
        mAutoRollHandler.sendEmptyMessageDelayed(ROLL_START, ROLL_DURATION);
    }

    public void stopRoll(){
        mAutoRollHandler.sendEmptyMessage(ROLL_STOP);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch(ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                stopRoll();
                break;
            case MotionEvent.ACTION_UP:
                startRoll();
                break;

        }
        return super.dispatchTouchEvent(ev);
    }

    private static class AutoRollHandler extends Handler {

        private WeakReference<AutoRollViewPager> mRollViewPagerWeakReference;

        public AutoRollHandler(AutoRollViewPager viewPager) {
            mRollViewPagerWeakReference = new WeakReference<AutoRollViewPager>(viewPager);
        }

        @Override
        public void handleMessage(Message msg) {
            AutoRollViewPager autoRollViewPager = mRollViewPagerWeakReference.get();
            if(autoRollViewPager != null){
                PagerAdapter adapter = autoRollViewPager.getAdapter();
                if(adapter != null && adapter instanceof RollViewPagerAdapter){
                    if(((RollViewPagerAdapter) adapter).isRollEnableForData()){
                        switch (msg.what) {
                            case 1:
                                autoRollViewPager.setCurrentItem(autoRollViewPager.getCurrentItem() + 1, true);
                                sendEmptyMessageDelayed(ROLL_START, ROLL_DURATION);
                                break;
                            case 2:
                                removeMessages(ROLL_START);
                                break;
                        }
                    }
                }
            }
        }
    }

    public class AutoScroller extends Scroller {
        public AutoScroller(Context context) {
            super(context);
        }


        @Override
        public void startScroll(int startX, int startY, int dx, int dy, int duration) {
            super.startScroll(startX, startY, dx, dy, ROLL_ANIMATION_DURATION);
        }
    }


    public static abstract class RollViewPagerAdapter<T> extends PagerAdapter {
        private List<T> mData;
        private boolean isRollEnableForData;

        public RollViewPagerAdapter() {
        }

        public RollViewPagerAdapter(List<T> data) {
            this.mData = data;
        }

        public void setData(List<T> data) {
            this.mData = data;
            notifyDataSetChanged();
        }

        public List<T> getData() {
            return mData;
        }

        public T getItem(int realPosition){
            return mData.get(realPosition);
        }

        public boolean isRollEnableForData() {
            return isRollEnableForData;
        }

        @Override
        public int getCount() {
            int count = 0;
            isRollEnableForData = false;
            if(mData != null){
                count = mData.size();
                if(count > 1){
                    count = count + 2;
                    isRollEnableForData = true;
                }
            }
            return count;
        }

        public int getRealCount() {
            return mData == null ? 0 : mData.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            if(object instanceof View) {
                container.removeView(((View) object));
            }
        }

        public abstract int getItemLayoutRes();

        public abstract void onBindItemView(ViewGroup rootView, int position, int realPosition);


        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ViewGroup rootView = null;
            if(mData != null) {
                LayoutInflater inflater = LayoutInflater.from(container.getContext());
                rootView = (ViewGroup) inflater.inflate(getItemLayoutRes(), container, false);
                int realPosition;
                if(isRollEnableForData){
                    if(position == 0){
                        realPosition = getRealCount() - 1;
                    }else if(position == getCount() - 1){
                        realPosition = 0;
                    }else{
                        realPosition = position - 1;
                    }
                }else{
                    realPosition = position;
                }
                onBindItemView(rootView, position, realPosition);
                container.addView(rootView);
            }
            return rootView;
        }
    }

}
