package com.zq.zqcustomviewlib

import android.os.Bundle
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import butterknife.BindView
import com.squareup.picasso.Picasso
import com.ziq.base.baserx.dagger.component.AppComponent
import com.ziq.base.mvp.IBasePresenter
import com.ziq.base.mvp.MvpBaseActivity
import com.zq.customviewlib.AutoRollViewPager

/**
 * @author wuyanqiang
 * @date 2018/9/21
 */
class AutoRollViewPagerActivity: MvpBaseActivity<IBasePresenter>() {
    override fun initForInject(appComponent: AppComponent?) {

    }

    override fun initLayoutResourceId(): Int {
        return R.layout.activity_auto_roll_view_pager;
    }

    @BindView(R.id.auto_roll_view_pager) lateinit var mAutoRollViewPager: AutoRollViewPager

    override fun initData(savedInstanceState: Bundle?) {
        mAutoRollViewPager.adapter = mRollViewPagerAdapter;
        val dataList: ArrayList<PhotoModel> = ArrayList()
        dataList.add(PhotoModel("https://qa-media-api.xogrp.com/images/986509f2-0a88-4480-ab41-4e7ec7ceb243.webp", "1-photo"))
        dataList.add(PhotoModel("https://qa-media-api.xogrp.com/images/5e917e94-d6da-422d-be39-2667f143b598.webp", "2-photo"))
        dataList.add(PhotoModel("https://qa-media-api.xogrp.com/images/58fe2f3a-0ecc-4b50-91ab-f378214f49f4.webp", "3-photo"))
        dataList.add(PhotoModel("https://qa-media-api.xogrp.com/images/088ea6d6-74fd-49a1-a483-0f34e847714c.webp", "4-photo"))
        dataList.add(PhotoModel("https://qa-media-api.xogrp.com/images/81036808-509e-4254-aa2a-e531791b436e.webp", "5-photo"))
        mRollViewPagerAdapter.data = dataList
    }


    private val mRollViewPagerAdapter: AutoRollViewPager.RollViewPagerAdapter<PhotoModel> = object : AutoRollViewPager.RollViewPagerAdapter<PhotoModel>(){
        override fun getItemLayoutRes(): Int {
            return R.layout.auto_roll_view_pager_item
        }

        override fun onBindItemView(rootView: ViewGroup?, position: Int, realPosition: Int) {
            val model = getItem(realPosition)
            val imageView = rootView?.findViewById(R.id.img) as ImageView
            Picasso.with(rootView.context).load(model.url).into(imageView)
            val textView = rootView.findViewById(R.id.text) as TextView
            textView.text = model.text
        }

    }

    internal inner class PhotoModel(var url: String, var text: String)
}