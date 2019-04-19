package com.applicaster.jwsearchplugin.screens


import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.applicaster.copaamerica.statsscreenplugin.R
import com.applicaster.jwsearchplugin.data.model.Playlist
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.fragment_video.view.*


class VideoRecyclerViewAdapter(
        private val context: Context,
        private var mValues: MutableList<Playlist>,
        private val mListener: OnListFragmentInteractionListener?)
    : RecyclerView.Adapter<VideoRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_video, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mValues[position]
        holder.titleView.text = item.title

        Glide.with(context).load(item.image).apply(RequestOptions()
                .placeholder(R.drawable.ic_placeholder)).into(holder.imageView)

        holder.mView.setOnClickListener {
            mListener?.playFullScreenVideo(item)
        }
    }

    override fun getItemCount(): Int = mValues.size

    fun updateItems(videos: List<Playlist>) {
        mValues.addAll(videos)
        notifyDataSetChanged()
    }

    fun clearItems() {
        mValues = emptyList<Playlist>().toMutableList()
        notifyDataSetChanged()
    }

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val titleView: TextView = mView.item_primary_text
        val imageView: ImageView = mView.item_image
    }
}
