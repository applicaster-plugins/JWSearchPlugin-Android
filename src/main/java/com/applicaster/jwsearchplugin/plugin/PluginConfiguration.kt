package com.applicaster.jwsearchplugin.plugin

object PluginConfiguration {
    lateinit var baseUrl: String
    lateinit var path: String
    lateinit var itemLimit: String

    fun setPluginConfigs(host: String, urlPath: String, limit: String) {
        baseUrl = host
        path = urlPath
        itemLimit = limit
    }
}