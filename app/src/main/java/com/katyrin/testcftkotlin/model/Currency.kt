package com.katyrin.testcftkotlin.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Currency (
    val id: String = "",
    val numCode: Int = 0,
    val charCode: String = "",
    val nominal: Int = 0,
    val name: String = "",
    var value: Double = 0.0,
    var previous: Double = 0.0
): Parcelable