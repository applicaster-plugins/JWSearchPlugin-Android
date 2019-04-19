package com.applicaster.jwsearchplugin.screens

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.Toast
import com.applicaster.copaamerica.statsscreenplugin.R
import com.applicaster.jwsearchplugin.data.model.Playlist
import com.applicaster.jwsearchplugin.data.model.SearchResult
import com.applicaster.jwsearchplugin.screens.dummy.DummyContent.DummyItem
import kotlinx.android.synthetic.main.fragment_video_list.*
import android.nfc.tech.MifareUltralight.PAGE_SIZE
import android.support.v7.widget.LinearLayoutManager
import android.widget.LinearLayout
import com.applicaster.jwsearchplugin.plugin.PluginConfiguration


/**
 * A fragment representing a list of Items.
 * Activities containing this fragment MUST implement the
 * [SearchFragment.OnListFragmentInteractionListener] interface.
 */
class SearchFragment : Fragment(), com.applicaster.jwsearchplugin.screens.base.View {

    private lateinit var searchPresenter: SearchPresenter

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

        list.adapter = VideoRecyclerViewAdapter(videos, listener)
        videos = emptyList<Playlist>().toMutableList()

        search_edit_text.setOnEditorActionListener(object : TextView.OnEditorActionListener {
            override fun onEditorAction(textView: TextView?, actionId: Int, keyEvent: KeyEvent?): Boolean {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    isLastPage = false
                    searchPresenter.performSearch(textView?.text.toString())
                    return true
                }
                return false
            }
        })

        list.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

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

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    override fun getSearchSuccess(searchResult: SearchResult) {
        if (searchResult.playlist.size != PluginConfiguration.itemLimit.toInt()) {
            isLastPage = true
        }
        tag_textview.visibility = View.GONE
        videos.addAll(searchResult.playlist)
        list.adapter = VideoRecyclerViewAdapter(videos, listener)
    }

    override fun getSearchError(error: String?) {
        Toast.makeText(activity, error, Toast.LENGTH_LONG).show()
    }

    override fun showProgress() {
        isLoading = true
        progress_indicator.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        isLoading = false
        progress_indicator.visibility = View.GONE
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson
     * [Communicating with Other Fragments](http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onListFragmentInteraction(item: DummyItem?)
    }

    companion object {

        // TODO: Customize parameter argument names
        const val ARG_COLUMN_COUNT = "column-count"

        // TODO: Customize parameter initialization
        @JvmStatic
        fun newInstance() =
                SearchFragment()
    }
}
