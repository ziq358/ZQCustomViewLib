package com.zq.zqcustomviewlib

import android.os.Bundle
import com.ziq.base.baserx.dagger.component.AppComponent
import com.ziq.base.mvp.IBasePresenter
import com.ziq.base.mvp.MvpBaseActivity

class PasswordInputActivity : MvpBaseActivity<IBasePresenter>() {


    override fun initForInject(appComponent: AppComponent?) {
    }

    override fun initLayoutResourceId(): Int {
        return R.layout.activity_password
    }

    override fun initData(savedInstanceState: Bundle?) {
    }
}