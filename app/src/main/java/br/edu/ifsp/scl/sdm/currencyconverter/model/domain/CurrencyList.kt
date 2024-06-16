package br.edu.ifsp.scl.sdm.currencyconverter.model.domain

data class CurrencyList(
    val currencies: Map<String, String>,
    val status: String
)