package com.katyrin.testcftkotlin.repository

import android.content.Context
import javax.inject.Inject

class LocalDataSourceImpl @Inject constructor(context: Context) : LocalDataSource(context)