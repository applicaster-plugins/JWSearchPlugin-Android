package com.applicaster.jwsearchplugin.data.model

import com.google.gson.annotations.SerializedName

data class SearchResult(
        @SerializedName("description")
        val description: String,
        @SerializedName("feed_instance_id")
        val feedInstanceId: String,
        @SerializedName("feedid")
        val feedid: String,
        @SerializedName("kind")
        val kind: String,
        @SerializedName("links")
        val links: Links,
        @SerializedName("playlist")
        val playlist: List<Playlist>,
        @SerializedName("title")
        val title: String
)