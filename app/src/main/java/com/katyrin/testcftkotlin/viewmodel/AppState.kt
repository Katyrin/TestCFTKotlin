package com.katyrin.testcftkotlin.viewmodel

import com.katyrin.testcftkotlin.model.Currency

sealed class AppState {
    data class SuccessRemoteQuery(val currencies: List<Currency>): AppState()
    data class SuccessLocalQuery(val currencies: List<Currency>): AppState()
    data class SuccessSaveData(val currencies: List<Currency>): AppState()
    data class Error(val error: Throwable): AppState()
    object Loading: AppState()
}
