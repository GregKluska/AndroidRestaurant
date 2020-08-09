package com.gregkluska.restaurantmvvm.models

data class DishWithDishOption (
    val dish: Dish,
    val options: List<DishOption>
)