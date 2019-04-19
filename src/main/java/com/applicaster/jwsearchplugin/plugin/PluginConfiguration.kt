package com.applicaster.jwsearchplugin.plugin

object PluginConfiguration {
    lateinit var playlistURL: String
    lateinit var itemLimit: String

    fun setPluginConfigs(url: String, limit: String) {
        playlistURL = url
        itemLimit = limit
    }
}