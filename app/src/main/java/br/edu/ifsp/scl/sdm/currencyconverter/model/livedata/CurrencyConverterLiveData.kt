package br.edu.ifsp.scl.sdm.currencyconverter.model.livedata

import androidx.lifecycle.MutableLiveData
import br.edu.ifsp.scl.sdm.currencyconverter.model.domain.ConversionResult
import br.edu.ifsp.scl.sdm.currencyconverter.model.domain.CurrencyList

object CurrencyConverterLiveData {
    val CurrenciesLiveData = MutableLiveData<CurrencyList>()
    val conversionResultLiveData = MutableLiveData<ConversionResult>()
}