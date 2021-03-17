package com.katyrin.testcftkotlin.model

import com.google.gson.annotations.SerializedName

data class CurrenciesDTO(
    @SerializedName("Date")
    val date: String?,
    @SerializedName("PreviousDate")
    val previousDate: String?,
    @SerializedName("PreviousURL")
    val previousURL: String?,
    @SerializedName("Timestamp")
    val timestamp: String?,
    @SerializedName("Valute")
    val valute: Map<String, ValuteDTO>?
)

data class ValuteDTO(
    @SerializedName("ID")
    val id: String?,
    @SerializedName("NumCode")
    val numCode: Int?,
    @SerializedName("CharCode")
    val charCode: String?,
    @SerializedName("Nominal")
    val nominal: Int?,
    @SerializedName("Name")
    val name: String?,
    @SerializedName("Value")
    val value: Double?,
    @SerializedName("Previous")
    val previous: Double?
)