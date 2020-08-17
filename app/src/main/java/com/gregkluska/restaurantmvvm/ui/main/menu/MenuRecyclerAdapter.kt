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
import kotlinx.android.synthetic.main.layout_dish_item.view.*

class MenuRecyclerAdapter
constructor(
    private val requestManager: RequestManager,
    private val interaction: Interaction? = null
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val DISH_DIFF_CALLBACK = object : DiffUtil.ItemCallback<Dish>() {

        override fun areItemsTheSame(oldItem: Dish, newItem: Dish): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Dish, newItem: Dish): Boolean {
            return oldItem == newItem
        }

    }
    private val dishDiffer = AsyncListDiffer(this, DISH_DIFF_CALLBACK)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return DishViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.layout_dish_item,
                parent,
                false
            ),
            interaction,
            requestManager
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is DishViewHolder -> {
                holder.bind(dishDiffer.currentList[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return dishDiffer.currentList.size
    }

    fun submitList(list: List<Dish>) {
        dishDiffer.submitList(list)
    }

    class DishViewHolder
    constructor(
        itemView: View,
        private val interaction: Interaction?,
        private val requestManager: RequestManager
    ) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: Dish) = with(itemView) {
            itemView.setOnClickListener {
                interaction?.onItemSelected(adapterPosition, item)
            }

            requestManager
                .load(item.image)
                .into(itemView.thumbnail)

            title.text = item.name
            description.text = item.description
        }
    }

    interface Interaction {
        fun onItemSelected(position: Int, item: Dish)
    }
}