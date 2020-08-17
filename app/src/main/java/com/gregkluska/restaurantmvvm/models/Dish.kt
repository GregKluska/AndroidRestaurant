package com.gregkluska.restaurantmvvm.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Dish(

    @PrimaryKey
    @ColumnInfo(name = "dish_id")
    val id: Int,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "description")
    val description: String?,

    @ColumnInfo(name = "image")
    val image: String?
) {
    override fun toString(): String {
        return "Dish(id=$id, name='$name', description=$description, image=$image)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Dish

        if (id != other.id) return false

        return true
    }



}