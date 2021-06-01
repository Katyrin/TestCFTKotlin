package com.katyrin.testcftkotlin.model.room

import androidx.room.*
import io.reactivex.Single

@Dao
interface CurrenciesDao {
    @Query("SELECT * FROM CurrencyEntity")
    fun all(): Single<List<CurrencyEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(entity: CurrencyEntity)

    @Update
    fun update(entity: CurrencyEntity)

    @Delete
    fun delete(entity: CurrencyEntity)
}