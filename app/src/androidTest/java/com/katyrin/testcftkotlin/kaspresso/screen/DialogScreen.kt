package com.katyrin.testcftkotlin.kaspresso.screen

import com.agoda.kakao.edit.KEditText
import com.agoda.kakao.screen.Screen
import com.agoda.kakao.text.KButton
import com.agoda.kakao.text.KTextView
import com.katyrin.testcftkotlin.R

object DialogScreen : Screen<DialogScreen>() {

    val amountInRubles = KEditText { withId(R.id.amountInRubles) }
    val result = KTextView { withId(R.id.result) }
    val positiveButtonDialog = KButton { withId(R.id.positiveButtonDialog) }
}