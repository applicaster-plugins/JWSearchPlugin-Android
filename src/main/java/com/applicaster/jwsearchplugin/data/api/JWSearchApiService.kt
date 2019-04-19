package com.applicaster.jwsearchplugin.data.api

import com.applicaster.jwsearchplugin.data.model.SearchResult
import com.applicaster.jwsearchplugin.plugin.PluginConfiguration
import io.reactivex.Observable
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * To implement this class I used as reference this article
 * https://medium.com/@elye.project/kotlin-and-retrofit-2-tutorial-with-working-codes-333a4422a890
 */
interface JWSearchApiService {

    @GET("{playlist}")
    fun search(@Path("playlist") queryPath: String, @Query("search") searchTerm: String,
               @Query("page_limit") pageLimit: String, @Query("page_offset") pageOffset: String):
            Observable<SearchResult>

    companion object {
        fun create(): JWSearchApiService {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            val client: OkHttpClient = OkHttpClient.Builder().addInterceptor(interceptor).build()

            val retrofit = Retrofit.Builder()
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .baseUrl(PluginConfiguration.baseUrl + "/")
                    .build()

            return retrofit.create(JWSearchApiService::class.java)
        }
    }
}