package com.example.proyectomusica.data.network
import android.content.Context
import com.example.proyectomusica.data.network.service.ApiService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object InstanceRetrofit {
    private const val BASE_URL = "http://10.0.2.2:8080/"

    private fun getClient(context: Context): OkHttpClient {
        return OkHttpClient.Builder().apply {
            addInterceptor { chain ->
                val token = getToken(context)
                val request = chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer $token")
                    .build()
                chain.proceed(request)
            }
        }.build()
    }

    fun getInstance(context: Context): ApiService {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(getClient(context))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    private fun getToken(context: Context): String {
        val prefs = context.getSharedPreferences("APP_PREFS", Context.MODE_PRIVATE)
        return prefs.getString("TOKEN", "") ?: ""
    }
}
