package com.cz.currency_conversion

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.Pair
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.widget.AppCompatSpinner
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cz.currency_conversion.adapter.CurrencyAdapter
import com.cz.currency_conversion.extension.*
import com.cz.currency_conversion.server.ServerAPI
import com.google.android.material.textfield.TextInputEditText

class MainActivity : AppCompatActivity(), TextWatcher, AdapterView.OnItemSelectedListener {
    companion object {
        private const val TAG = "MainActivity"
        private const val GRID_COLUMNS_COUNT = 3
        private const val SERVER_API_PERIOD = 30L * 60L * 1000L
    }

    private lateinit var mAmountInput: TextInputEditText
    private lateinit var mCurrencySelect: AppCompatSpinner
    private lateinit var mAdapter: CurrencyAdapter

    // pair first is currency, second is exchange rates
    private lateinit var mCurrencyData: ArrayList<Pair<String, Double>>
    private var mCurrentCurrencyIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView(savedInstanceState)
        refreshData()
    }

    override fun onStart() {
        super.onStart()
        // Get data from server
        getCurrencyData()
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        outState.putString("currency_input", mAmountInput.text.toString())
        outState.putInt("current_currency_index", mCurrentCurrencyIndex)
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

    }

    override fun afterTextChanged(p0: Editable?) {
        // Refresh the list when the user input amount
        mAdapter.setData(p0.toString(), mCurrencyData[mCurrentCurrencyIndex], mCurrencyData)
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        // Refresh the list when the user select currency
        mCurrentCurrencyIndex = p2
        mAdapter.setData(mAmountInput.text.toString(), mCurrencyData[mCurrentCurrencyIndex], mCurrencyData)
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {

    }

    private fun initView(savedInstanceState: Bundle?) {
        mAmountInput = findViewById(R.id.amount_input)
        mAmountInput.addTextChangedListener(this)

        mCurrencySelect = findViewById(R.id.currency_select)
        mCurrencySelect.onItemSelectedListener = this

        val currencyList: RecyclerView = findViewById(R.id.currency_list)
        currencyList.layoutManager = GridLayoutManager(this, GRID_COLUMNS_COUNT)
        mAdapter = CurrencyAdapter()
        currencyList.adapter = mAdapter

        if (savedInstanceState != null) {
            mCurrencyData = getData()
            mAmountInput.setText(savedInstanceState.getString("currency_input"))
            mCurrentCurrencyIndex = savedInstanceState.getInt("current_currency_index")
        }
    }

    // Refresh the data to UI
    private fun refreshData() {
        mCurrencyData = getData()
        if (mCurrencyData.isEmpty()) {
            return
        }

        val items = Array<String>(mCurrencyData.size) {
            mCurrencyData[it].first
        }
        mCurrencySelect.adapter = ArrayAdapter(this,
                android.R.layout.simple_spinner_item, items)
        mCurrencySelect.setSelection(mCurrentCurrencyIndex)

        mAdapter.setData(mAmountInput.text.toString(), mCurrencyData[mCurrentCurrencyIndex], mCurrencyData)
    }

    private fun getCurrencyData() {
        val timestamp = System.currentTimeMillis()
        // Rates should be persisted locally and refreshed no more frequently than every 30 minutes (to limit bandwidth usage)
        if (timestamp - getTimestamp() > SERVER_API_PERIOD) {
            if (!isNetworkConnected()) {
                showDialogOneButton(getString(R.string.network_error))
                return
            }

            Log.i(TAG, "#getCurrencyData: Start to call server API")
            val waitDialog = showWait()
            ServerAPI.getExchangeRates {
                runOnUiThread {
                    waitDialog.dismiss()
                    if (it != null) {
                        if (it.success) {
                            setTimestamp(timestamp)
                            setData(it.source, it.quotes)
                            mCurrentCurrencyIndex = 0
                            refreshData()
                        } else {
                            showDialogOneButton(it.error.info)
                        }
                    }
                }
            }
        }
    }
}