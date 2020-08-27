package com.gregkluska.restaurantmvvm.ui.main.menu

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.RequestManager
import com.gregkluska.restaurantmvvm.R
import com.gregkluska.restaurantmvvm.models.Dish
import com.gregkluska.restaurantmvvm.models.DishCategory
import kotlinx.android.synthetic.main.layout_dish_item.view.*

class MenuRecyclerAdapter
constructor(
    private val requestManager: RequestManager,
    private val interaction: Interaction? = null
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val CATEGORY_TYPE = 1
        const val DISH_TYPE = 2
    }

    val DISH_DIFF_CALLBACK = object : DiffUtil.ItemCallback<Dish>() {
        override fun areItemsTheSame(oldItem: Dish, newItem: Dish): Boolean {
            return oldItem.id == newItem.id
        }
        override fun areContentsTheSame(oldItem: Dish, newItem: Dish): Boolean {
            return oldItem == newItem
        }
    }

    val DISH_CATEGORY_DIFF_CALLBACK = object : DiffUtil.ItemCallback<DishCategory>() {
        override fun areItemsTheSame(oldItem: DishCategory, newItem: DishCategory): Boolean {
            return oldItem.id == newItem.id
        }
        override fun areContentsTheSame(oldItem: DishCategory, newItem: DishCategory): Boolean {
            return oldItem == newItem
        }
    }


    private val dishDiffer = AsyncListDiffer(this, DISH_DIFF_CALLBACK)
    private val dishCategoryDiffer = AsyncListDiffer(this, DISH_CATEGORY_DIFF_CALLBACK)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val view : View = LayoutInflater.from(parent.context).inflate(
            R.layout.layout_dish_item,
            parent,
            false
        )

        when (viewType) {
            CATEGORY_TYPE -> {
                return DishCategoryViewHolder(
                    view,
                    interaction,
                    requestManager
                )
            }
            DISH_TYPE -> {
                return DishViewHolder(
                    view,
                    interaction,
                    requestManager
                )
            }

            else -> {
                return DishCategoryViewHolder(
                    view,
                    interaction,
                    requestManager
                )
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is DishCategoryViewHolder -> {
                holder.bind(dishCategoryDiffer.currentList[position])
            }
            is DishViewHolder -> {
                holder.bind(dishDiffer.currentList[position])
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (dishDiffer.currentList.size > 0) {
            DISH_TYPE
        } else {
            CATEGORY_TYPE
        }
    }

    override fun getItemCount(): Int {
        return dishCategoryDiffer.currentList.size + dishDiffer.currentList.size
    }

    fun submitDishCategoryList(list: List<DishCategory>) {
        dishDiffer.submitList(null)
        dishCategoryDiffer.submitList(list)
    }

    fun submitDishList(list: List<Dish>) {
        dishCategoryDiffer.submitList(null)
        dishDiffer.submitList(list)
    }

    class DishCategoryViewHolder
    constructor(
        itemView: View,
        private val interaction: Interaction?,
        private val requestManager: RequestManager
    ) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: DishCategory) = with(itemView) {
            itemView.setOnClickListener {
                interaction?.onDishCategorySelected(adapterPosition, item)
            }

            requestManager
                .load(item.image)
                .into(itemView.thumbnail)

            title.text = item.name
        }
    }

    class DishViewHolder
    constructor(
        itemView: View,
        private val interaction: Interaction?,
        private val requestManager: RequestManager
    ) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: Dish) = with(itemView) {
            itemView.setOnClickListener {
                interaction?.onDishSelected(adapterPosition, item)
            }

            requestManager
                .load(item.image)
                .into(itemView.thumbnail)

            title.text = item.name
            description.text = item.description
        }
    }

    interface Interaction {
        fun onDishCategorySelected(position: Int, item: DishCategory)
        fun onDishSelected(position: Int, item: Dish)
    }
}