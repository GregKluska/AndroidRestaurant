package com.gregkluska.restaurantmvvm.api.main.responses

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.gregkluska.restaurantmvvm.models.Dish
import com.gregkluska.restaurantmvvm.models.DishOption
import com.gregkluska.restaurantmvvm.models.DishWithDishOption

class DishResponse (

    @Expose
    @SerializedName("id")
    val id: Int,

    @Expose
    @SerializedName("name")
    val name: String,

    @Expose
    @SerializedName("description")
    val description: String,

    @Expose
    @SerializedName("image")
    val image: String,

    @Expose
    @SerializedName("options")
    val options: List<DishOption>

) {

    fun toDishWithDishOptions(): DishWithDishOption {
        return DishWithDishOption(
            dish = Dish(
                id = id,
                name = name,
                description = description,
                image = image
            ),
            options = options
        )
    }

    override fun toString(): String {
        return "DishResponse(id=$id, name='$name', description='$description', image='$image', options=$options)"
    }


}