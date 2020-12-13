package com.cz.currency_conversion.adapter

import android.text.TextUtils
import android.util.Pair
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cz.currency_conversion.R
import java.util.ArrayList

class CurrencyAdapter : RecyclerView.Adapter<CurrencyAdapter.ItemViewHolder>() {
    private var mAmount = 0.0
    private lateinit var mCurrentCurrency: Pair<String, Double>
    private val mCurrencyData = ArrayList<Pair<String, Double>>()

    fun setData(amount: String, currency: Pair<String, Double>, currencyData: ArrayList<Pair<String, Double>>) {
        mAmount = if (TextUtils.isEmpty(amount)) 0.0 else amount.toDouble()
        mCurrentCurrency = currency
        mCurrencyData.clear()
        currencyData.forEach {
            if (currency.first != it.first) {
                mCurrencyData.add(it)
            }
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_currency, parent, false))
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val pair = mCurrencyData[position]
        holder.currencyName.text = pair.first
        holder.exchangeRate.text = String.format("%.4f", pair.second / mCurrentCurrency.second)
        holder.amountData.text = (mAmount * pair.second / mCurrentCurrency.second).toString()
    }

    override fun getItemCount(): Int {
        return mCurrencyData.size
    }

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val currencyName: TextView = itemView.findViewById(R.id.currency_name)
        val exchangeRate: TextView = itemView.findViewById(R.id.exchange_rate)
        val amountData: TextView = itemView.findViewById(R.id.amount_data)
    }
}