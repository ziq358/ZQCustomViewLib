package com.zq.zqcustomviewlib

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.PopupWindow
import butterknife.BindView
import butterknife.OnClick
import com.ziq.base.dagger.component.AppComponent
import com.ziq.base.mvp.BaseActivity
import com.ziq.base.mvp.IBasePresenter

class PopupWindowActivity : BaseActivity<IBasePresenter>(){
    override fun initForInject(appComponent: AppComponent?) {
    }

    @BindView(R.id.button1)
    lateinit var button1:Button

    override fun initLayoutResourceId(): Int {
        return R.layout.activity_popupwindow
    }

    override fun initData(savedInstanceState: Bundle?) {

    }

    @OnClick(R.id.button1)
    fun onClick(view: View) {
        when (view.id) {
            R.id.button1 -> {

                val contentView:View = LayoutInflater.from(this).inflate(R.layout.layout_popupwindow, null,false)

                val popupwindow : PopupWindow = PopupWindow(contentView,100,100)
                popupwindow.isOutsideTouchable = true
                popupwindow.showAsDropDown(button1)
            }
            else -> {

            }
        }
    }

}