package com.applicaster.jwsearchplugin.data.model

import com.google.gson.annotations.SerializedName

data class Playlist(
        @SerializedName("description")
        val description: String,
        @SerializedName("duration")
        val duration: Int,
        @SerializedName("feedid")
        val feedid: String,
        @SerializedName("image")
        val image: String,
        @SerializedName("link")
        val link: String,
        @SerializedName("mediaid")
        val mediaid: String,
        @SerializedName("pubdate")
        val pubdate: Int,
        @SerializedName("sources")
        val sources: List<Source>,
        @SerializedName("tags")
        val tags: String,
        @SerializedName("title")
        val title: String,
        @SerializedName("tracks")
        val tracks: List<Track>,
        @SerializedName("variations")
        val variations: Variations
)