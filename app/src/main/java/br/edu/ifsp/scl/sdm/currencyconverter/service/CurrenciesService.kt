package br.edu.ifsp.scl.sdm.currencyconverter.service

import android.app.Service
import android.content.Intent
import android.os.Handler
import android.os.HandlerThread
import android.os.IBinder
import android.os.Looper
import android.os.Message
import android.util.Log
import br.edu.ifsp.scl.sdm.currencyconverter.model.api.CurrencyConverterApiClient
import br.edu.ifsp.scl.sdm.currencyconverter.model.livedata.CurrencyConverterLiveData
import java.net.HttpURLConnection.HTTP_OK

class CurrenciesService: Service() {

    private lateinit var handler: CurrenciesServiceHandler

    private lateinit var serviceLogTag: String

    private inner class CurrenciesServiceHandler(looper: Looper): Handler(looper) {
        override fun handleMessage(msg: Message) {
            CurrencyConverterApiClient.service.getCurrencies().execute().also {  response ->
                if(response.code() == HTTP_OK) {
                    response.body()?.also { currencyList ->
                        CurrencyConverterLiveData.CurrenciesLiveData.postValue(currencyList)
                    }
                }
            }
            stopSelf(msg.arg1)
        }
    }

    override fun onCreate() {
        super.onCreate()
        HandlerThread(this.javaClass.name).apply {
            start()
            handler = CurrenciesServiceHandler(looper)
        }
    }

    override fun onBind(p0: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        serviceLogTag = "${javaClass.simpleName}/${startId}"
        Log.v(serviceLogTag, "Service started")

        handler.obtainMessage().also { msg ->
            msg.arg1 = startId
            handler.sendMessage(msg)
        }

        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.v(serviceLogTag, "Service done")

    }
}