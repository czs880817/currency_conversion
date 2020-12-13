package com.cz.currency_conversion.server.bean

import com.google.gson.annotations.SerializedName

class ExchangeRatesBean {
    @SerializedName("success")
    var success = false

    @SerializedName("source")
    var source = ""

    @SerializedName("quotes")
    var quotes = LinkedHashMap<String, Double>()

    @SerializedName("error")
    var error = Error()

    inner class Error {
        @SerializedName("code")
        var code = 0

        @SerializedName("type")
        var type = ""

        @SerializedName("info")
        var info = ""
    }
}