package com.cz.currency_conversion.server

import android.util.Log
import com.cz.currency_conversion.server.bean.ExchangeRatesBean
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ServerAPI {
    private const val TAG = "ServerAPI"

    private const val BASE_URL = "http://api.currencylayer.com/"

    private val mRetrofit: Retrofit by lazy {
        Retrofit
            .Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun getExchangeRates(callback: (exchangeRatesBean: ExchangeRatesBean?) -> Unit) {
        mRetrofit
            .create(CurrencyConversionService::class.java)
            .getExchangeRates(AccessKey.getAccessKey())
            .enqueue(object : Callback<ExchangeRatesBean> {
                override fun onResponse(call: Call<ExchangeRatesBean>, response: Response<ExchangeRatesBean>) {
                    callback(response.body())
                }

                override fun onFailure(call: Call<ExchangeRatesBean>, t: Throwable) {
                    Log.e(TAG, "#getExchangeRates error: $t")
                    val exchangeRatesBean = ExchangeRatesBean()
                    exchangeRatesBean.success = false
                    exchangeRatesBean.error.info = t.toString()
                    callback(exchangeRatesBean)
                }
            })
    }
}