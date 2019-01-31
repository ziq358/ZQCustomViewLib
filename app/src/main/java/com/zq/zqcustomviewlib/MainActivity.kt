package com.zq.zqcustomviewlib

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import butterknife.BindView
import com.ziq.base.dagger.component.AppComponent
import com.ziq.base.mvp.BaseActivity
import com.ziq.base.mvp.IBasePresenter
import com.ziq.base.recycleView.BaseViewHolder
import com.ziq.base.recycleView.adapter.ListRecyclerAdapter

class MainActivity() : BaseActivity<IBasePresenter>() {
    override fun initForInject(appComponent: AppComponent?) {

    }

    //绑定
    @BindView(R.id.rv_list)
    lateinit var mRvList: RecyclerView

    override fun initLayoutResourceId(): Int {
        return R.layout.activity_main;
    }

    override fun initData(savedInstanceState: Bundle?) {
        mRvList.layoutManager = LinearLayoutManager(this)
        mRvList.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        mRvList.adapter = mListRecyclerAdapter
        initData()
    }

    private fun initData(): Unit {
        val dataList: ArrayList<DataItem> = ArrayList()
        dataList.add(DataItem("录音翻译状态控件", TranslateLoadingViewActivity::class.java))
        dataList.add(DataItem("轮播图控件", AutoRollViewPagerActivity::class.java))
        dataList.add(DataItem("水平导航栏控件", HorizontalScrollViewTabActivity::class.java))
        dataList.add(DataItem("popupWindow", PopupWindowActivity::class.java))
        mListRecyclerAdapter.setData(dataList)
    }

    //匿名内部类
    private var mListRecyclerAdapter = object : ListRecyclerAdapter<DataItem>(this) {
        override fun getItemLayoutRes(): Int {
            return R.layout.item_list
        }

        override fun bindDataViewHolder(holder: BaseViewHolder?, position: Int) {
            val name: TextView? = holder?.getViewById(R.id.tv_name)
            name?.text = getItem(position).name
            holder?.itemView?.setOnClickListener(object : View.OnClickListener {
                override fun onClick(v: View?) {
                    startActivity(Intent(this@MainActivity, getItem(position).cls))
                }
            })
        }

    }

    //数据类
    private data class DataItem(var name: String, var cls: Class<*>)

}
