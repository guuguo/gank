package com.guuguo.gank.app.viewmodel

import androidx.databinding.BaseObservable
import com.google.gson.reflect.TypeToken
import com.guuguo.android.lib.extension.safe
import com.guuguo.gank.constant.MEIZI_COUNT
import com.guuguo.gank.model.Ganks
import com.guuguo.gank.model.entity.GankModel
import com.guuguo.gank.net.ApiServer
import com.guuguo.gank.net.http.BaseCallback
import com.guuguo.gank.app.fragment.GankDailyFragment
import com.guuguo.gank.constant.LocalData
import com.guuguo.gank.constant.myGson
import io.reactivex.Single
import io.reactivex.disposables.Disposable
import io.reactivex.functions.BiFunction
import kotlinx.android.synthetic.main.view_refresh_recycler.*
import java.util.*


/**
 * mimi 创造于 2017-05-22.
 * 项目 pika
 */
class GankDailyViewModel(val fragment: GankDailyFragment) : BaseObservable() {
    val activity = fragment.activity
    var hideLoading: () -> Unit = {}

    fun getMeiziDataFromNet(page: Int) {
        Single.zip(ApiServer.getGankData(ApiServer.TYPE_FULI, MEIZI_COUNT, page), ApiServer.getGankData(ApiServer.TYPE_REST, MEIZI_COUNT, page),
                BiFunction<Ganks<ArrayList<GankModel>>, Ganks<ArrayList<GankModel>>, List<GankModel>> { t1, t2 ->
                    t1.results?.zip(t2.results!!, { a: GankModel, b: GankModel ->
                        a.desc = b.desc
                        a.who = b.who
                        a
                    }).safe()
                })
                .subscribe(object : BaseCallback<List<GankModel>>() {
                    override fun onSubscribe(d: Disposable) {
                        fragment.addApiCall(d)
                    }

                    override fun onApiLoadError(msg: String) {
                        hideLoading()
                        activity.dialogErrorShow(msg)
                    }

                    override fun onSuccess(t: List<GankModel>) {
                        super.onSuccess(t)
                        t.let {
                            if (page == 1)
                                LocalData.gankDaily = myGson.toJson(it)
                            fragment.setUpMeiziList(it)
                        }
                    }
                })
    }

    fun getMeiziData() {
        val tempStr = LocalData.gankDaily
        if (tempStr.isNotEmpty())
            fragment.setUpMeiziList(myGson.fromJson(tempStr, object : TypeToken<ArrayList<GankModel>>() {}.type))
    }
}

