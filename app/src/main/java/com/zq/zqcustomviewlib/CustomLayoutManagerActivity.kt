package com.zq.zqcustomviewlib

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import com.ziq.base.baserx.dagger.component.AppComponent
import com.ziq.base.mvp.IBasePresenter
import com.ziq.base.mvp.MvpBaseActivity
import com.ziq.base.recycleview.BaseViewHolder
import com.zq.customviewlib.OverLayoutManager
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

class CustomLayoutManagerActivity : MvpBaseActivity<IBasePresenter>() {

    @BindView(R.id.recycleView)
    lateinit var recycleView : RecyclerView

    var disposable: Disposable? = null
    private var list : ArrayList<DataItem> = ArrayList()

    override fun initForInject(appComponent: AppComponent?) {
    }

    override fun initLayoutResourceId(): Int {
        return R.layout.activity_layout_manager
    }

    override fun initData(savedInstanceState: Bundle?) {

        for (i in 0..3){
            list.add(DataItem(i.toString()))
        }
        recycleView.layoutManager = OverLayoutManager()
        recycleView.adapter = Adapter(list)

        Observable.interval(2000,2000, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<Long> {
                    override fun onComplete() {
                    }

                    override fun onSubscribe(d: Disposable) {
                        disposable = d
                    }

                    override fun onNext(t: Long) {
                        Log.e("ziq", String.format("onNext %s", t))
                        list.add(0,DataItem(list.size.toString()))
                        recycleView.adapter!!.notifyItemInserted(0)
                    }

                    override fun onError(e: Throwable) {
                    }

                })

    }

    override fun onDestroy() {
        disposable?.dispose()
        super.onDestroy()
    }

    //数据类
    private data class DataItem(var name: String)


    private class Adapter(var data:ArrayList<DataItem>) : RecyclerView.Adapter<BaseViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, p1: Int): BaseViewHolder {
            Log.e("ziq", "onCreateViewHolder")
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_layout_manager, parent, false)
            return BaseViewHolder(view)
        }

        override fun getItemCount(): Int {
            return data.size
        }

        override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
            Log.e("ziq", "===onBindViewHolder")
            holder.getViewById<TextView>(R.id.tv_name).text = "======${data.size}"
        }
    }

    companion object{
        fun getRandColor():String{
            var R:String
            var G:String
            var B:String

            val random = Random()
            R = Integer.toHexString(random.nextInt(256)).toUpperCase();
            G = Integer.toHexString(random.nextInt(256)).toUpperCase();
            B = Integer.toHexString(random.nextInt(256)).toUpperCase();
            R =  if(R.length == 1) "0$R" else R;
            G =  if(G.length == 1) "0$G" else G;
            B =  if(B.length == 1) "0$B" else R;
            return "#$R$G$B"
        }
    }
}