package com.ssafy.smartstore_jetpack.data.model.dto

import android.content.Context
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader


class JsonReader {
    fun readJsonFromAsset(context: Context, fileName: String?): String? {
        var json: String? = null
        try {
            val assetManager = context.assets
            val inputStream = assetManager.open(fileName!!)
            val reader = BufferedReader(InputStreamReader(inputStream))
            val stringBuilder = StringBuilder()
            var line: String?
            while ((reader.readLine().also { line = it }) != null) {
                stringBuilder.append(line)
            }
            inputStream.close()
            json = stringBuilder.toString()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return json
    }
}