package com.zq.zqcustomviewlib

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.ViewPager
import android.util.Log
import android.view.View
import android.widget.TextView
import butterknife.BindView
import com.ziq.base.mvp.BaseActivity
import com.ziq.base.mvp.BaseFragment
import com.ziq.base.mvp.IBasePresenter
import com.zq.customviewlib.HorizontalScrollViewTab
import java.util.*

/**
 * @author wuyanqiang
 * @date 2018/10/12
 */
class HorizontalScrollViewTabActivity: BaseActivity<IBasePresenter>() {

    @BindView(R.id.HorizontalScrollViewTab)
    lateinit var mHorizontalScrollViewTab: HorizontalScrollViewTab;
    @BindView(R.id.viewpager)
    lateinit var mViewpager: ViewPager;

    override fun initLayoutResourceId(): Int {
        return R.layout.activity_horizontal_scrollview_tab
    }

    override fun initData(savedInstanceState: Bundle?) {
        var data:ArrayList<String> = arrayListOf()
        data.add("推荐")
        data.add("王者荣耀")
        data.add("绝地求生")
        data.add("LOL")
        data.add("星秀")
        data.add("吃鸡手游")
        data.add("吃喝玩乐")
        data.add("主机")
        data.add("CF")
        data.add("颜值")
        data.add("二次元")
        data.add("DNF")
        data.add("暴雪")
        data.add("我的世界")


        mHorizontalScrollViewTab.addData(data)
        var adapter: MyViewPagerAdapter = MyViewPagerAdapter(data, supportFragmentManager);
        mViewpager.adapter = adapter
        mHorizontalScrollViewTab.setOnHorizontalNavigationSelectListener {
            mViewpager.setCurrentItem(it, false)
        }
        mViewpager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {

            var isScrolling: Boolean = false
            var tempPosition: Int = 0;
            var currentPostion: Int = 0;
            override fun onPageScrollStateChanged(state: Int) {
                isScrolling = state == ViewPager.SCROLL_STATE_DRAGGING
                Log.d("ziq", String.format("onPageScrollStateChanged %d  ", state) )
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                if(isScrolling){
                    tempPosition = position
                    currentPostion = mViewpager.currentItem
                }
                if(tempPosition == position){
                    if(currentPostion == 0){
                        mHorizontalScrollViewTab.setPositionChange(currentPostion, 1, positionOffset)
                        Log.e("ziq", String.format("111 from = %d to = %d  positionOffset = %f", currentPostion, 1, positionOffset) )
                    }
//                    else if(currentPostion == mViewpager.adapter!!.count - 1) {
//                        mHorizontalScrollViewTab.setPositionChange(currentPostion, 1, positionOffset)
//                    }
                    else if(currentPostion > position){
                        //左滑
                        mHorizontalScrollViewTab.setPositionChange(currentPostion, currentPostion - 1, 1 - positionOffset)
                        Log.e("ziq", String.format("222 from = %d to = %d  positionOffset = %f", currentPostion, currentPostion - 1, 1 - positionOffset) )
                    }else{
                        //右滑
                        mHorizontalScrollViewTab.setPositionChange(currentPostion, currentPostion + 1, positionOffset)
                        Log.e("ziq", String.format("333 from = %d to = %d  positionOffset = %f", currentPostion, currentPostion + 1, positionOffset) )
                    }
                }
            }

            override fun onPageSelected(position: Int) {
                mHorizontalScrollViewTab.setPosition(position)
            }
        })
    }

    class MyViewPagerAdapter(var data:ArrayList<String>, var fm: FragmentManager): FragmentStatePagerAdapter(fm){

        override fun getCount(): Int {
            return data.size
        }

        override fun getItem(position: Int): Fragment {
            return MyFragment.getInstance(data.get(position))
        }

    }

    class MyFragment: BaseFragment<IBasePresenter>(){

        companion object {// 包裹范围内 属于静态方法
            fun getInstance(label: String): MyFragment {
                var fragment: MyFragment = MyFragment()
                var bundle: Bundle = Bundle()
                bundle.putString("label", label)
                fragment.arguments = bundle
                return fragment
            }
        }

        @BindView(R.id.label)
        lateinit var label:TextView

        override fun initLayoutResourceId(): Int {
            return R.layout.fragment_horizontalscrollviewtab_viewpager
        }

        override fun initData(view: View, savedInstanceState: Bundle?) {
            label.text = arguments?.getString("label")
        }

    }

}