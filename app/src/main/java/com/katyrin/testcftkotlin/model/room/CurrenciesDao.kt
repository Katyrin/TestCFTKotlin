package com.katyrin.testcftkotlin.model.room

import androidx.room.*

@Dao
interface CurrenciesDao {
    @Query("SELECT * FROM CurrencyEntity")
    fun all(): List<CurrencyEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(entity: CurrencyEntity)

    @Update
    fun update(entity: CurrencyEntity)

    @Delete
    fun delete(entity: CurrencyEntity)
}