package com.annhienktuit.muzikplayer.utils

import com.google.gson.GsonBuilder

import com.google.gson.Gson

class GsonUtils {
    companion object{
        var gson: Gson? = null

        fun getGsonParser(): Gson? {
            if (gson == null) {
                val builder = GsonBuilder()
                gson = builder.create()
            }
            return gson
        }
    }


}