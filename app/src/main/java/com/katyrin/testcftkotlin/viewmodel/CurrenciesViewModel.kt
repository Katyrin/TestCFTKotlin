package com.katyrin.testcftkotlin.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.katyrin.testcftkotlin.model.*
import com.katyrin.testcftkotlin.repository.CurrencyRepository
import com.katyrin.testcftkotlin.repository.LocalRepository
import com.katyrin.testcftkotlin.utils.convertCurrenciesDTOToModel
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


class CurrenciesViewModel @Inject constructor(
    private val currencyRemoteRepository: CurrencyRepository,
    private val currencyLocalRepository: LocalRepository,
    private val uiScheduler: Scheduler
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
                .observeOn(uiScheduler)
                .subscribe { currencies ->
                    _liveData.value = getLocalRequestState(currencies)
                }
        )
    }

    private fun getLocalRequestState(currencies: List<Currency>): AppState =
        if (currencies.isEmpty()) {
            AppState.EmptyLocalList
        } else {
            AppState.SuccessLocalRequest(currencies)
        }

    fun saveCurrencyToDB(currency: Currency) {
        currencyLocalRepository.insertEntity(currency)
    }

    fun getCurrenciesFromRemoteSource() {
        _liveData.value = AppState.Loading
        disposable?.add(
            currencyRemoteRepository.getCurrenciesFromServer()
                .subscribeOn(Schedulers.io())
                .observeOn(uiScheduler)
                .subscribe({ serverResponse ->
                    _liveData.postValue(
                        if (serverResponse != null) {
                            getRemoteRequestState(serverResponse)
                        } else {
                            AppState.Error(Throwable(SERVER_ERROR))
                        }
                    )
                }, { t ->
                    _liveData.postValue(AppState.Error(Throwable(t.message ?: REQUEST_ERROR)))
                })
        )
    }

    private fun getRemoteRequestState(serverResponse: CurrenciesDTO): AppState =
        if (serverResponse.valute == null) {
            AppState.Error(Throwable(CORRUPTED_DATA))
        } else {
            AppState.SuccessRemoteRequest(convertCurrenciesDTOToModel(serverResponse))
        }

    override fun onCleared() {
        super.onCleared()
        if (disposable != null) {
            disposable?.clear()
            disposable = null
        }
    }
}