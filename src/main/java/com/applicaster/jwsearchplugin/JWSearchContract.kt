package com.applicaster.jwsearchplugin

import android.content.Context
import android.support.v4.app.Fragment
import com.applicaster.jwsearchplugin.plugin.PluginConfiguration
import com.applicaster.jwsearchplugin.screens.SearchFragment
import com.applicaster.jwsearchplugin.utils.Constants
import com.applicaster.plugin_manager.screen.PluginScreen
import com.applicaster.util.StringUtil
import com.google.gson.internal.LinkedTreeMap
import java.io.Serializable
import java.net.URL
import java.util.*

class JWSearchContract : PluginScreen {

    override fun generateFragment(screenMap: HashMap<String, Any>?, dataSource: Serializable?): Fragment {
        return if (setPluginConfiguration(screenMap!!["general"] as LinkedTreeMap<*, *>)) SearchFragment.newInstance() else SearchFragment()
    }

    private fun setPluginConfiguration(params: LinkedTreeMap<*, *>): Boolean {
        // first check if any of the params are null
        if (StringUtil.isEmpty(params[Constants.PARAM_PLAYLIST_URL].toString()) ||
                StringUtil.isEmpty(params[Constants.PARAM_PAGE_LIMIT].toString()))
            return false

        val url = URL(params[Constants.PARAM_PLAYLIST_URL].toString())

        val baseURL = url.protocol + "://" + url.host

        PluginConfiguration.setPluginConfigs(baseURL, url.path, params[Constants.PARAM_PAGE_LIMIT].toString())

        return true
    }

    override fun present(context: Context?, screenMap: HashMap<String, Any>?, dataSource: Serializable?, isActivity: Boolean) {

    }
}