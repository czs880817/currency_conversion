package com.cz.currency_conversion.extension

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.text.TextUtils
import android.util.Pair
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import com.cz.currency_conversion.AppConfig
import com.cz.currency_conversion.R
import java.lang.StringBuilder
import java.util.ArrayList

fun Activity.showDialogOneButton(s: String) {
    AlertDialog.Builder(this)
        .setMessage(s)
        .setPositiveButton(android.R.string.ok, null)
        .create()
        .show()
}

fun Activity.showWait(): AlertDialog {
    val alertDialog = AlertDialog.Builder(this)
        .setCustomTitle(LayoutInflater.from(this).inflate(R.layout.dialog_wait, null))
        .create()
    alertDialog.show()
    return alertDialog
}

fun Context.setTimestamp(timestamp: Long) {
    getSharedPreferences(AppConfig.SP_NAME, Context.MODE_PRIVATE).edit().putLong(AppConfig.SP_KEY_TIMESTAMP, timestamp).apply()
}

fun Context.getTimestamp(): Long {
    return getSharedPreferences(AppConfig.SP_NAME, Context.MODE_PRIVATE).getLong(AppConfig.SP_KEY_TIMESTAMP, 0L)
}

// Store exchange rates data by SharePreferences
fun Context.setData(source: String, quotes: LinkedHashMap<String, Double>) {
    val stringBuilder = StringBuilder()
    stringBuilder
            .append(source)
            .append(':')
            .append(1.0)
            .append('\n')
    quotes.forEach {
        stringBuilder
                .append(it.key.substring(source.length))
                .append(':')
                .append(it.value)
                .append('\n')
    }
    getSharedPreferences(AppConfig.SP_NAME, Context.MODE_PRIVATE).edit().putString(AppConfig.SP_KEY_DATA, stringBuilder.substring(0, stringBuilder.length - 1).toString()).apply()
}

fun Context.getData(): ArrayList<Pair<String, Double>> {
    val arrayList = ArrayList<Pair<String, Double>>()
    val dataString = getSharedPreferences(AppConfig.SP_NAME, Context.MODE_PRIVATE).getString(AppConfig.SP_KEY_DATA, "")
    if (!TextUtils.isEmpty(dataString)) {
        val data = dataString?.split("\n")
        data?.forEach {
            val item = it.split(":")
            arrayList.add(Pair<String, Double>(item[0], item[1].toDouble()))
        }
    }

    return arrayList
}

// Check network status
fun Context.isNetworkConnected(): Boolean {
    val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val networkInfo = connectivityManager.activeNetworkInfo
    if (networkInfo != null) {
        return networkInfo.isAvailable
    }

    return false
}