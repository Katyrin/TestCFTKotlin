package com.katyrin.testcftkotlin

import android.app.Application
import androidx.room.Room
import com.katyrin.testcftkotlin.model.room.CurrenciesDao
import com.katyrin.testcftkotlin.model.room.MainDataBase


class App : Application() {
    override fun onCreate() {
        super.onCreate()
        appInstance = this
    }

    companion object {
        private lateinit var appInstance: App
        private var db: MainDataBase? = null
        private const val DB_NAME = "MainDataBase.db"

        private fun getMainDB(): MainDataBase? {
            if (db == null) {
                synchronized(MainDataBase::class.java) {
                    if (db == null) {
                        db = Room.databaseBuilder(
                            appInstance.applicationContext,
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
}