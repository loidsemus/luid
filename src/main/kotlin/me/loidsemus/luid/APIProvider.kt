package me.loidsemus.luid

import com.google.gson.Gson
import okhttp3.OkHttpClient

class APIProvider {

    companion object {
        val gson = Gson().apply {
            serializeNulls()
        }
        val okHttpClient = OkHttpClient()
    }

}