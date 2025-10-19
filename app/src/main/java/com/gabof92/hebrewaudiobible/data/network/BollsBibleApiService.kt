package com.gabof92.hebrewaudiobible.data.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path


const val BASE_URL_BOLLS_API =
    "https://bolls.life/"

//Creating Moshi object
private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

//Interceptor for logging
private val loggingInterceptor = HttpLoggingInterceptor().apply {
    level = HttpLoggingInterceptor.Level.BODY
}

//Creating Retrofit Object
private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL_BOLLS_API)
    .client(
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    )
    .build()

interface BollsBibleApiService {
    @GET("dictionary-definition/BDBT/{query}")
    suspend fun getWordDefinitions(
        @Path("query") query: String,
    ): List<RootWordResult>
}

object BollsBibleApi {
    val retrofitService: BollsBibleApiService by lazy {
        retrofit.create(BollsBibleApiService::class.java)
    }
}