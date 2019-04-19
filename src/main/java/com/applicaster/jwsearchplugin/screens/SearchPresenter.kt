package com.applicaster.jwsearchplugin.screens

import com.applicaster.jwsearchplugin.data.model.SearchResult
import com.applicaster.jwsearchplugin.screens.base.View

class SearchPresenter(private var view: View, private var searchInteractor: SearchInteractor) : SearchInteractor.OnFinishedListener {

    private var pageOffset = 1
    private var currentSearchTerm = ""

    fun performSearch(term: String) {
        currentSearchTerm = term
        view.showProgress()
        searchInteractor.search(this, currentSearchTerm, pageOffset.toString())
    }

    fun loadNextPage() {
        view.showProgress()
        pageOffset++
        searchInteractor.search(this, currentSearchTerm, pageOffset.toString())
    }

    override fun onResultSuccess(searchResult: SearchResult) {
        view.hideProgress()
        view.getSearchSuccess(searchResult)
    }

    override fun onResultFail(error: String?) {
        view.hideProgress()
        view.getSearchError(error)
    }
}