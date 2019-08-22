package com.zq.zqcustomviewlib

import android.os.Bundle
import com.ziq.base.dagger.component.AppComponent
import com.ziq.base.mvp.BaseActivity
import com.ziq.base.mvp.IBasePresenter

class PasswordInputActivity : BaseActivity<IBasePresenter>() {


    override fun initForInject(appComponent: AppComponent?) {
    }

    override fun initLayoutResourceId(): Int {
        return R.layout.activity_password
    }

    override fun initData(savedInstanceState: Bundle?) {
    }
}