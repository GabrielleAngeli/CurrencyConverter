package br.edu.ifsp.scl.sdm.currencyconverter.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import br.edu.ifsp.scl.sdm.currencyconverter.R
import br.edu.ifsp.scl.sdm.currencyconverter.databinding.ActivityMainBinding
import br.edu.ifsp.scl.sdm.currencyconverter.model.livedata.CurrencyConverterLiveData
import br.edu.ifsp.scl.sdm.currencyconverter.service.CurrenciesService

class MainActivity : AppCompatActivity() {
    private val amb: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val currenciesServiceIntent by lazy {
       Intent(this, CurrenciesService::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(amb.root)
        setSupportActionBar(amb.mainTb.apply { title = getString(R.string.app_name) })

        var fromQuote = ""
        var toQuote  = ""
        val currenciesAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item,
            mutableListOf<String>()
        )
        with(amb) {
            fromQuoteMactv.apply {
                setAdapter(currenciesAdapter)
                setOnItemClickListener { _, _, _, _ ->
                    fromQuote = text.toString()
                }
            }
            toQuoteMactv.apply {
                setAdapter(currenciesAdapter)
                setOnItemClickListener { _, _, _, _ ->
                    toQuote = text.toString()
                }
            }
            convertBt.setOnClickListener{}
        }

        CurrencyConverterLiveData.CurrenciesLiveData.observe(this) { currencyList ->
            currenciesAdapter.clear()
            currenciesAdapter.addAll(currencyList.currencies.keys.sorted())
            currenciesAdapter.getItem(0)?.also {quote ->
                amb.fromQuoteMactv.setText(quote, false)
                fromQuote = quote
            }
            currenciesAdapter.getItem(currenciesAdapter.count - 1)?.also {quote ->
                amb.toQuoteMactv.setText(quote, false)
                toQuote = quote
            }
        }

        startService(currenciesServiceIntent)
    }

    override fun onDestroy() {
        super.onDestroy()
        stopService(currenciesServiceIntent)
    }
}