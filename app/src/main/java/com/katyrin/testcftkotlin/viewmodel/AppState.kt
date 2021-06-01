package com.katyrin.testcftkotlin.viewmodel

import com.katyrin.testcftkotlin.model.Currency

sealed class AppState {
    data class SuccessRemoteRequest(val currencies: List<Currency>) : AppState()
    data class SuccessLocalRequest(val currencies: List<Currency>) : AppState()
    data class SuccessSaveData(val currencies: List<Currency>) : AppState()
    data class Error(val error: Throwable) : AppState()
    object Loading : AppState()
    object EmptyLocalList : AppState()
}
