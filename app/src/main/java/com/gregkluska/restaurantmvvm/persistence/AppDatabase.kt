package com.gregkluska.restaurantmvvm.persistence

import androidx.room.Database
import androidx.room.RoomDatabase
import com.gregkluska.restaurantmvvm.models.Dish
import com.gregkluska.restaurantmvvm.models.DishOption

@Database(entities = [Dish::class, DishOption::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getDishDao(): DishDao

    abstract fun getDishOptionDao(): DishOptionDao

    companion object {
        val DATABASE_NAME: String = "app_db"
    }

}