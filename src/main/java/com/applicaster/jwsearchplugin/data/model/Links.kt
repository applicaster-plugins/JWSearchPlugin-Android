package com.applicaster.jwsearchplugin.data.model

import com.google.gson.annotations.SerializedName

data class Links(
        @SerializedName("first")
        val first: String,
        @SerializedName("last")
        val last: String,
        @SerializedName("next")
        val next: String
)