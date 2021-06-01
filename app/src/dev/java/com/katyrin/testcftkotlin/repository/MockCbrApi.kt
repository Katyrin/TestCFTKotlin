package com.katyrin.testcftkotlin.repository

import com.katyrin.testcftkotlin.model.BASE_URL
import com.katyrin.testcftkotlin.model.CurrenciesDTO
import com.katyrin.testcftkotlin.model.ValuteDTO
import io.reactivex.rxjava3.core.Single
import retrofit2.mock.BehaviorDelegate
import java.util.*

class MockCbrApi(private val behaviorDelegate: BehaviorDelegate<CbrApi>) : CbrApi {

    private val initValute: MutableMap<String, ValuteDTO>

    override fun getCurrencies(): Single<CurrenciesDTO> {
        val response = CurrenciesDTO("", "", BASE_URL, "", initValute)
        return behaviorDelegate.returningResponse(response).getCurrencies()
    }

    init {
        initValute = LinkedHashMap<String, ValuteDTO>()

        for (i in 1..20) {
            initValute["$i"] =
                ValuteDTO("$i", i, "CHAR$i", i, "NAME$i", (i * i).toDouble(), (i * i).toDouble())
        }
    }
}