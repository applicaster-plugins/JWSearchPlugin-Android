package com.applicaster.jwsearchplugin.screens.base

import com.applicaster.jwsearchplugin.data.api.JWSearchApiService
import io.reactivex.disposables.Disposable

open class Interactor {
    protected var disposable: Disposable? = null

    protected val jwSearchApiService by lazy {
        JWSearchApiService.create()
    }
}