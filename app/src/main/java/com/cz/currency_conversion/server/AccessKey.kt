package com.cz.currency_conversion.server

object AccessKey {
    init {
        System.loadLibrary("AccessKey")
    }

    // Hide the Access Key in so library for security
    external fun getAccessKey(): String
}