package com.zq.zqcustomviewlib

import android.os.Bundle
import android.view.View
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.ziq.base.mvp.BaseActivity
import com.ziq.base.mvp.IBasePresenter
import com.zq.customviewlib.TranslateLoadingView

/**
 * @author wuyanqiang
 * @date 2018/9/21
 */
class TranslateLoadingViewActivity: BaseActivity<IBasePresenter>() {
    override fun initLayoutResourceId(): Int {
        return R.layout.activity_translate_loading_view;
    }

    override fun initData(savedInstanceState: Bundle?) {
        mTranslateLoadingView?.setStatus(TranslateLoadingView.STATUS_IDLE)
    }

    @BindView(R.id.translate_loading_view)
    lateinit var mTranslateLoadingView: TranslateLoadingView

    private var status: IntArray = intArrayOf(TranslateLoadingView.STATUS_IDLE, TranslateLoadingView.STATUS_SEND, TranslateLoadingView.STATUS_RECEIVE)
    private var index: Int = 0

    @OnClick(R.id.next, R.id.last)
    fun onClick(view: View) {
        when (view.id) {
            R.id.next -> {
                index++
                if (index >= status.size) {
                    index = 0
                }
                mTranslateLoadingView?.setStatus(status[index])
            }
            R.id.last -> {
                index--
                if (index < 0) {
                    index = status.size - 1
                }
                mTranslateLoadingView?.setStatus(status[index])
            }
        }
    }
}