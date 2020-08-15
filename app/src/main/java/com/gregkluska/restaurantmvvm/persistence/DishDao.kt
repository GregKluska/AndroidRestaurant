package com.gregkluska.restaurantmvvm.persistence

import androidx.room.*
import com.gregkluska.restaurantmvvm.models.Dish
import com.gregkluska.restaurantmvvm.models.DishWithDishOption

@Dao
interface DishDao {

    @Delete
    fun deleteDish(dish: Dish)

    @Transaction
    @Query("SELECT * FROM Dish WHERE dish_id = :id")
    fun getDishWithOptions(id: Int): DishWithDishOption

    @Query("SELECT * FROM Dish")
    fun getDishes(): List<Dish>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertDish(dish: Dish)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertDishes(dishes: List<Dish>)

    //TODO("Add update fun")
}