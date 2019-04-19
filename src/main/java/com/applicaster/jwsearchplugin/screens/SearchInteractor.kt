package com.applicaster.jwsearchplugin.screens

import com.applicaster.jwsearchplugin.data.model.SearchResult
import com.applicaster.jwsearchplugin.plugin.PluginConfiguration
import com.applicaster.jwsearchplugin.screens.base.Interactor
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class SearchInteractor : Interactor() {

    interface OnFinishedListener {
        fun onResultSuccess(searchResult: SearchResult)
        fun onResultFail(error: String?)
    }

    fun search(onFinishedListener: OnFinishedListener, searchTerm: String, pageOffset: String) {

        disposable = jwSearchApiService.search(PluginConfiguration.path, searchTerm, PluginConfiguration.itemLimit, pageOffset)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { result -> onFinishedListener.onResultSuccess(result) },
                        { error -> onFinishedListener.onResultFail(error.message) }
                )
    }

}