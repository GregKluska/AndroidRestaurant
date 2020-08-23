package com.gregkluska.restaurantmvvm.models

data class Dish(

    val id: Int,

    val name: String,

    val description: String?,

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