package com.katyrin.testcftkotlin.model.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [CurrencyEntity::class], version = 1, exportSchema = false)
abstract class MainDataBase : RoomDatabase() {
    abstract fun currenciesDao(): CurrenciesDao
}