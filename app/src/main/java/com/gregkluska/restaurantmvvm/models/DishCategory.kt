package com.gregkluska.restaurantmvvm.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class DishCategory (

    @Expose
    @SerializedName("id")
    val id: Int,

    @Expose
    @SerializedName("name")
    val name: String,

    @Expose
    @SerializedName("image")
    val image: String?
) {
    override fun toString(): String {
        return "DishCategory(id=$id, name='$name', image=$image)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as DishCategory

        if (id != other.id) return false

        return true
    }

}