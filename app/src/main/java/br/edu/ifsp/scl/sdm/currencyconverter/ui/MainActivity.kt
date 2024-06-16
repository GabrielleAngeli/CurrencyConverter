package br.edu.ifsp.scl.sdm.currencyconverter.ui

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.widget.ArrayAdapter
import br.edu.ifsp.scl.sdm.currencyconverter.R
import br.edu.ifsp.scl.sdm.currencyconverter.databinding.ActivityMainBinding
import br.edu.ifsp.scl.sdm.currencyconverter.model.livedata.CurrencyConverterLiveData
import br.edu.ifsp.scl.sdm.currencyconverter.service.ConverterService
import br.edu.ifsp.scl.sdm.currencyconverter.service.CurrenciesService

class MainActivity : AppCompatActivity() {
    private val amb: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val currenciesServiceIntent by lazy {
       Intent(this, CurrenciesService::class.java)
    }

    private var convertService: ConverterService? = null
    private val convertServiceConnection = object: ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            convertService = (service as ConverterService.ConverterServiceBinder).getConvertService()
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            convertService = null
        }
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
            convertBt.setOnClickListener {
                convertService?.convert(fromQuote, toQuote, amountTiet.text.toString())
            }
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

        CurrencyConverterLiveData.conversionResultLiveData.observe(this) { conversionResult ->
            with(amb){
                conversionResult.rates.values.first().rateForAmount.also {
                    resultTiet.setText(it)
                }
            }
        }

        startService(currenciesServiceIntent)
    }

    override fun onStart() {
        super.onStart()
        Intent(this@MainActivity, ConverterService::class.java).also { intent ->
            bindService(intent, convertServiceConnection, BIND_AUTO_CREATE)
        }
    }

    override fun onStop() {
        super.onStop()
        unbindService(convertServiceConnection)
    }
    override fun onDestroy() {
        super.onDestroy()
        stopService(currenciesServiceIntent)
    }
}