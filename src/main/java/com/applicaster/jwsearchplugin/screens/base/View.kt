package com.applicaster.jwsearchplugin.screens.base

import com.applicaster.jwsearchplugin.data.model.SearchResult

interface View {
    fun showProgress()
    fun hideProgress()
    fun getSearchSuccess(searchResult: SearchResult)
    fun getSearchError(error: String?)
}