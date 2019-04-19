package com.applicaster.jwsearchplugin.screens

import android.content.Context
import android.graphics.Rect
import android.nfc.tech.MifareUltralight.PAGE_SIZE
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import com.applicaster.copaamerica.statsscreenplugin.R
import com.applicaster.jwsearchplugin.data.model.Playlist
import com.applicaster.jwsearchplugin.data.model.SearchResult
import com.applicaster.jwsearchplugin.plugin.PluginConfiguration
import com.applicaster.model.APVodItem
import com.applicaster.plugin_manager.playersmanager.PlayableConfiguration
import com.applicaster.plugin_manager.playersmanager.internal.PlayersManager
import kotlinx.android.synthetic.main.fragment_video_list.*


/**
 * A fragment representing a list of Items.
 * Activities containing this fragment MUST implement the
 * [SearchFragment.OnListFragmentInteractionListener] interface.
 */
class SearchFragment : Fragment(), com.applicaster.jwsearchplugin.screens.base.View, OnListFragmentInteractionListener {

    private lateinit var searchPresenter: SearchPresenter
    private lateinit var adapter: VideoRecyclerViewAdapter

    private var listener: OnListFragmentInteractionListener? = null
    private var videos = mutableListOf<Playlist>()
    private var isLoading = false
    private var isLastPage = false


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_video_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchPresenter = SearchPresenter(this, SearchInteractor())
        adapter = VideoRecyclerViewAdapter(context!!, videos, this)

        list.adapter = adapter
        list.addItemDecoration(MarginItemDecoration(45))
        videos = emptyList<Playlist>().toMutableList()

        search_edit_text.setOnEditorActionListener(object : TextView.OnEditorActionListener {
            override fun onEditorAction(textView: TextView?, actionId: Int, keyEvent: KeyEvent?): Boolean {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    clearSearch()
                    isLastPage = false
                    view.hideKeyboard()
                    searchPresenter.performSearch(textView?.text.toString())
                    return true
                }
                return false
            }
        })

        search_edit_text.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable) {
                if (s.count() >= 3) {
                    clearSearch()
                    isLastPage = false
                    searchPresenter.performSearch(s.toString())
                } else {
                    clearSearch()
                }
            }
        })

        list.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                view.hideKeyboard()
                val layoutManager = recyclerView?.layoutManager as LinearLayoutManager
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                if (!isLoading && !isLastPage) {
                    if (visibleItemCount + firstVisibleItemPosition >= totalItemCount
                            && firstVisibleItemPosition >= 0
                            && totalItemCount >= PAGE_SIZE) {
                        searchPresenter.loadNextPage()
                    }
                }
            }
        })
    }

    private fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }

    private fun clearSearch() {
        tag_textview.visibility = View.VISIBLE
        adapter.clearItems()
    }

    override fun playFullScreenVideo(video: Playlist) {
        var vod = APVodItem(video.mediaid, video.title, null)
        vod.description = video.description
        vod.stream_url = video.sources.first().file

        val playersManager = PlayersManager.getInstance()
        val playerContract = playersManager.createPlayer(vod, context)

        playerContract.playInFullscreen(PlayableConfiguration(), 0, context!!)
    }

    override fun getSearchSuccess(searchResult: SearchResult) {
        if (searchResult.playlist.size < PluginConfiguration.itemLimit.toInt()) {
            isLastPage = true
        }
        tag_textview.visibility = View.GONE
        adapter.updateItems(searchResult.playlist)
    }

    override fun getSearchError(error: String?) {
        Toast.makeText(activity, error, Toast.LENGTH_LONG).show()
        clearSearch()
    }

    override fun showProgress() {
        isLoading = true
        progress_indicator.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        isLoading = false
        progress_indicator.visibility = View.GONE
    }

    companion object {

        // TODO: Customize parameter argument names
        const val ARG_COLUMN_COUNT = "column-count"

        // TODO: Customize parameter initialization
        @JvmStatic
        fun newInstance() =
                SearchFragment()
    }

    class MarginItemDecoration(private val spaceHeight: Int) : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(outRect: Rect, view: View,
                                    parent: RecyclerView, state: RecyclerView.State) {
            with(outRect) {
                if (parent.getChildAdapterPosition(view) == 0) {
                    top = spaceHeight
                }
                right = spaceHeight
                left = spaceHeight
                bottom = spaceHeight
            }
        }
    }
}

interface OnListFragmentInteractionListener {
    fun playFullScreenVideo(item: Playlist)
}
