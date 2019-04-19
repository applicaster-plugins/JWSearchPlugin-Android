package com.applicaster.jwsearchplugin.data.model

import com.google.gson.annotations.SerializedName

data class Track(
        @SerializedName("file")
        val `file`: String,
        @SerializedName("hits")
        val hits: List<Int>,
        @SerializedName("kind")
        val kind: String,
        @SerializedName("label")
        val label: String
)