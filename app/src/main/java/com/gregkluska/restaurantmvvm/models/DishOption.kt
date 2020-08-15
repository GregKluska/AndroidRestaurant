package com.gregkluska.restaurantmvvm.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class DishOption(

    @PrimaryKey
    @ColumnInfo(name = "option_id")
    val id: Int,

    @ColumnInfo(name = "dish_id")
    val dishId: Int,

    @ColumnInfo(name="name")
    val name: String,

    @ColumnInfo(name="price")
    val price: Float

)