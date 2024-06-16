package br.edu.ifsp.scl.sdm.currencyconverter.model.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object CurrencyConverterApiClient {
    private const val BASE_URL = "https://currency-converter5.p.rapidapi.com/currency/"

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val service: CurrencyConverterApiService = retrofit.create(CurrencyConverterApiService::class.java)
}