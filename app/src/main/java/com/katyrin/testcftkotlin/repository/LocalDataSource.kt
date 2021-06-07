package com.katyrin.testcftkotlin.repository

import android.content.Context
import androidx.room.Room
import com.katyrin.testcftkotlin.model.room.CurrenciesDao
import com.katyrin.testcftkotlin.model.room.MainDataBase
import com.katyrin.testcftkotlin.utils.DB_NAME

abstract class LocalDataSource(private val context: Context) {
    private var db: MainDataBase? = null

    private fun getMainDB(): MainDataBase? {
        if (db == null) {
            synchronized(MainDataBase::class.java) {
                if (db == null) {
                    db = Room.databaseBuilder(
                        context,
                        MainDataBase::class.java, DB_NAME
                    ).build()
                }
            }
        }
        return db
    }

    fun getCurrenciesDao(): CurrenciesDao {
        return getMainDB()!!.currenciesDao()
    }
}