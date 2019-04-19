package com.applicaster.jwsearchplugin.data.model

import com.google.gson.annotations.SerializedName

data class Source(
        @SerializedName("file")
        val `file`: String,
        @SerializedName("height")
        val height: Int,
        @SerializedName("label")
        val label: String,
        @SerializedName("type")
        val type: String,
        @SerializedName("width")
        val width: Int
)