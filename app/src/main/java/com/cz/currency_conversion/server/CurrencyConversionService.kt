package com.cz.currency_conversion.server

import com.cz.currency_conversion.server.bean.ExchangeRatesBean
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface CurrencyConversionService {
    @GET("live")
    fun getExchangeRates(@Query("access_key") accessKey: String): Call<ExchangeRatesBean>
}