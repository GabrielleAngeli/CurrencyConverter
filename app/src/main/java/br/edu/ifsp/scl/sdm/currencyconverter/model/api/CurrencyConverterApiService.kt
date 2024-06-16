package br.edu.ifsp.scl.sdm.currencyconverter.model.api

import br.edu.ifsp.scl.sdm.currencyconverter.model.domain.ConversionResult
import br.edu.ifsp.scl.sdm.currencyconverter.model.domain.CurrencyList
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface CurrencyConverterApiService {

    @Headers(
        "x-rapidapi-key: 8fd02c598dmsh3c1a0701acaa752p1488c0jsn4aecfdea2ad5",
        "x-rapidapi-host: currency-converter5.p.rapidapi.com",
    )
    @GET("list")
    fun getCurrencies(): Call<CurrencyList>

    @Headers(
        "x-rapidapi-key: 8fd02c598dmsh3c1a0701acaa752p1488c0jsn4aecfdea2ad5",
        "x-rapidapi-host: currency-converter5.p.rapidapi.com",
    )
    @GET("convert")
    fun convert(
        @Query("from") from: String,
        @Query("to") to: String,
        @Query("amount") amount: String
    ): Call<ConversionResult>
}