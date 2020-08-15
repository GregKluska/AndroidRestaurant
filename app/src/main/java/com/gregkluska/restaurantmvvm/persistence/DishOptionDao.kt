package com.gregkluska.restaurantmvvm.persistence

import androidx.room.*
import com.gregkluska.restaurantmvvm.models.DishOption

@Dao
interface DishOptionDao {

    @Delete
    fun deleteOption(dishOption: DishOption)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOption(dishOption: DishOption)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOptions(vararg dishOptions: DishOption)

    //TODO("Add update fun")

}
