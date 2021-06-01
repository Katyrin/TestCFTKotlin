package com.katyrin.testcftkotlin.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.katyrin.testcftkotlin.model.*
import com.katyrin.testcftkotlin.repository.CurrencyRepository
import com.katyrin.testcftkotlin.repository.LocalRepository
import com.katyrin.testcftkotlin.utils.convertCurrenciesDTOToModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
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
        _liveData.value = AppState.Loading
        Thread {
            _liveData.postValue(currenciesSave.let { AppState.SuccessSaveData(it) })
        }.start()
    }

    fun saveState(currencies: List<Currency>) {
        currenciesSave = currencies
    }

    fun getAllCurrencies() {
        _liveData.value = AppState.Loading
        Thread {
            _liveData.postValue(AppState.SuccessLocalQuery(currencyLocalRepository.getAllCurrencies()))
        }.start()
    }

    fun saveCurrencyToDB(currency: Currency) {
        Thread {
            currencyLocalRepository.saveEntity(currency)
        }.start()
    }

    fun getCurrenciesFromRemoteSource() {
        _liveData.value = AppState.Loading
        disposable?.add(
            currencyRemoteRepository.getCurrenciesFromServer()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ serverResponse ->
                    _liveData.postValue(
                        if (serverResponse != null) {
                            checkResponse(serverResponse)
                        } else {
                            AppState.Error(Throwable(SERVER_ERROR))
                        }
                    )
                }, { t ->
                    _liveData.postValue(AppState.Error(Throwable(t?.message ?: REQUEST_ERROR)))
                })
        )
    }

    private fun checkResponse(serverResponse: CurrenciesDTO): AppState {
        val valute = serverResponse.valute
        return if (valute == null) {
            AppState.Error(Throwable(CORRUPTED_DATA))
        } else {
            AppState.SuccessRemoteQuery(convertCurrenciesDTOToModel(serverResponse))
        }
    }

    override fun onCleared() {
        super.onCleared()
        if (disposable != null) {
            disposable?.clear()
            disposable = null
        }
    }
}