package com.gregkluska.restaurantmvvm.models

import androidx.room.Embedded
import androidx.room.Relation

data class DishWithDishOption (
    @Embedded val dish: Dish,
    @Relation(
        parentColumn = "dish_id",
        entityColumn = "dish_id"
    )
    val options: List<DishOption>
)