package com.zq.zqcustomviewlib

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import butterknife.BindView
import com.ziq.base.baserx.dagger.component.AppComponent
import com.ziq.base.mvp.IBasePresenter
import com.ziq.base.mvp.MvpBaseActivity
import com.ziq.base.mvp.MvpBaseFragment
import com.zq.customviewlib.HorizontalScrollViewTab
import java.util.*

/**
 * @author wuyanqiang
 * @date 2018/10/12
 */
class HorizontalScrollViewTabActivity: MvpBaseActivity<IBasePresenter>() {
    override fun initForInject(appComponent: AppComponent?) {

    }

    @BindView(R.id.HorizontalScrollViewTab)
    lateinit var mHorizontalScrollViewTab: HorizontalScrollViewTab;
    @BindView(R.id.viewpager)
    lateinit var mViewpager: ViewPager;

    override fun initLayoutResourceId(): Int {
        return R.layout.activity_horizontal_scrollview_tab
    }

    override fun initData(savedInstanceState: Bundle?) {
        var data:ArrayList<HorizontalScrollViewTab.ContentItem> = arrayListOf()
        data.add(HorizontalScrollViewTab.ContentItem { "推荐" })
        data.add(HorizontalScrollViewTab.ContentItem { "王者荣耀"})
        data.add(HorizontalScrollViewTab.ContentItem { "绝地求生"})
        data.add(HorizontalScrollViewTab.ContentItem { "LOL"})
        data.add(HorizontalScrollViewTab.ContentItem { "星秀"})
        data.add(HorizontalScrollViewTab.ContentItem { "吃鸡手游"})
        data.add(HorizontalScrollViewTab.ContentItem { "吃喝玩乐"})
        data.add(HorizontalScrollViewTab.ContentItem { "主机"})
        data.add(HorizontalScrollViewTab.ContentItem { "CF"})
        data.add(HorizontalScrollViewTab.ContentItem { "颜值"})
        data.add(HorizontalScrollViewTab.ContentItem { "二次元"})
        data.add(HorizontalScrollViewTab.ContentItem { "DNF"})
        data.add(HorizontalScrollViewTab.ContentItem { "暴雪"})
        data.add(HorizontalScrollViewTab.ContentItem { "我的世界"})


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

    class MyViewPagerAdapter(var data:ArrayList<HorizontalScrollViewTab.ContentItem>, var fm: FragmentManager): FragmentStatePagerAdapter(fm){

        override fun getCount(): Int {
            return data.size
        }

        override fun getItem(position: Int): Fragment {
            return MyFragment.getInstance(data.get(position).title)
        }

    }

    class MyFragment: MvpBaseFragment<IBasePresenter>(){
        override fun initForInject(appComponent: AppComponent?) {
        }

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