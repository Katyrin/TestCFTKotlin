package com.katyrin.testcftkotlin.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.katyrin.testcftkotlin.utils.CORRUPTED_DATA
import com.katyrin.testcftkotlin.model.CurrenciesDTO
import com.katyrin.testcftkotlin.model.Currency
import com.katyrin.testcftkotlin.utils.SERVER_ERROR
import com.katyrin.testcftkotlin.repository.CurrencyRepository
import com.katyrin.testcftkotlin.repository.LocalRepository
import com.katyrin.testcftkotlin.utils.convertCurrenciesDTOToModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject


class CurrenciesViewModel @Inject constructor(
    private val currencyRemoteRepository: CurrencyRepository,
    private val currencyLocalRepository: LocalRepository
) : ViewModel() {

    private var disposable: CompositeDisposable? = CompositeDisposable()
    private var currenciesSave = listOf<Currency>()
    private val _liveData: MutableLiveData<AppState> = MutableLiveData<AppState>()
    val liveData: LiveData<AppState> = _liveData

    fun getSaveStateLiveData() {
        _liveData.value = currenciesSave.let { AppState.SuccessSaveData(it) }
    }

    fun saveState(currencies: List<Currency>) {
        currenciesSave = currencies
    }

    fun getAllCurrencies() {
        _liveData.value = AppState.Loading
        disposable?.add(
            currencyLocalRepository.getAllCurrencies()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(::setLocalRequestState)
        )
    }

    private fun setLocalRequestState(currencies: List<Currency>) {
        _liveData.value = if (currencies.isEmpty()) {
            AppState.EmptyLocalList
        } else {
            AppState.SuccessLocalRequest(currencies)
        }
    }

    fun saveCurrencyToDB(currency: Currency) {
        disposable?.add(
            currencyLocalRepository.insertEntity(currency)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { _liveData.value = AppState.SuccessInsert }
        )
    }

    fun getCurrenciesFromRemoteSource() {
        _liveData.value = AppState.Loading
        disposable?.add(
            currencyRemoteRepository.getCurrenciesFromServer()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(::setRemoteRequestState) { setErrorStateServer() }
        )
    }

    private fun setRemoteRequestState(serverResponse: CurrenciesDTO) {
        _liveData.value = if (serverResponse.valute == null) {
            AppState.Error(Throwable(CORRUPTED_DATA))
        } else {
            AppState.SuccessRemoteRequest(convertCurrenciesDTOToModel(serverResponse))
        }
    }

    private fun setErrorStateServer() {
        _liveData.value = AppState.Error(Throwable(SERVER_ERROR))
    }

    override fun onCleared() {
        if (disposable != null) {
            disposable?.clear()
            disposable = null
        }
        super.onCleared()
    }
}