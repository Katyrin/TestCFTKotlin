package com.katyrin.testcftkotlin.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.katyrin.testcftkotlin.App
import com.katyrin.testcftkotlin.model.*
import com.katyrin.testcftkotlin.repository.*
import com.katyrin.testcftkotlin.utils.convertCurrenciesDTOToModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class CurrenciesViewModel(private val state: SavedStateHandle) : ViewModel() {

    val liveData: MutableLiveData<AppState> = MutableLiveData()
    private val currencyRemoteRepository: CurrencyRepository =
        CurrencyRepositoryImpl(RemoteDataSource())
    private val currencyLocalRepository: LocalRepository =
        LocalRepositoryImpl(App.getCurrenciesDao())

    fun getSaveStateLiveData() {
        val currencies = state.get<List<Currency>>("currencies")
        liveData.value = AppState.Loading
        Thread {
            liveData.postValue(currencies?.let { AppState.SuccessSaveData(it) })
        }.start()
    }
    fun saveState(currencies: List<Currency>) {
        state.set("currencies", currencies)
    }

    fun getAllCurrencies() {
        liveData.value = AppState.Loading
        Thread {
            liveData.postValue(AppState.SuccessLocalQuery(currencyLocalRepository.getAllCurrencies()))
        }.start()
    }

    fun saveCurrencyToDB(currency: Currency) {
        Thread {
            currencyLocalRepository.saveEntity(currency)
        }.start()
    }

    fun getCurrenciesFromRemoteSource() {
        liveData.value = AppState.Loading
        currencyRemoteRepository.getCurrenciesFromServer(object : Callback<CurrenciesDTO> {
            override fun onResponse(call: Call<CurrenciesDTO>, response: Response<CurrenciesDTO>) {
                val serverResponse: CurrenciesDTO? = response.body()
                liveData.postValue(
                    if (response.isSuccessful && serverResponse != null) {
                        checkResponse(serverResponse)
                    } else {
                        AppState.Error(Throwable(SERVER_ERROR))
                    }
                )
            }

            override fun onFailure(call: Call<CurrenciesDTO>, t: Throwable) {
                liveData.postValue(AppState.Error(Throwable(t.message ?: REQUEST_ERROR)))
            }

            private fun checkResponse(serverResponse: CurrenciesDTO): AppState {
                val valute = serverResponse.valute
                return if (valute == null) {
                    AppState.Error(Throwable(CORRUPTED_DATA))
                } else {
                    AppState.SuccessRemoteQuery(convertCurrenciesDTOToModel(serverResponse))
                }
            }
        })
    }
}