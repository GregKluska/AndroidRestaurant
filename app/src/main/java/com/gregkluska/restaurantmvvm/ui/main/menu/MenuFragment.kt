package com.gregkluska.restaurantmvvm.ui.main.menu

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.RequestManager
import com.gregkluska.restaurantmvvm.R
import com.gregkluska.restaurantmvvm.models.Dish
import com.gregkluska.restaurantmvvm.models.DishCategory
import com.gregkluska.restaurantmvvm.ui.main.home.MenuViewModel
import com.gregkluska.restaurantmvvm.ui.main.menu.MenuRecyclerAdapter.Companion.CATEGORY_TYPE
import com.gregkluska.restaurantmvvm.ui.main.menu.MenuRecyclerAdapter.Companion.DISH_TYPE
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_menu.*
import javax.inject.Inject

private const val TAG = "AppDebug"

@AndroidEntryPoint
class MenuFragment : Fragment(), MenuRecyclerAdapter.Interaction {

    @Inject
    lateinit var requestManager: RequestManager
    private lateinit var recyclerAdapter: MenuRecyclerAdapter
    private val menuViewModel: MenuViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_menu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()
        subscribeObservers()
        menuViewModel.executeCategories()

    }

    private fun subscribeObservers() {
        Log.d(TAG, "subscribeObservers: called")
        menuViewModel.categories.observe(viewLifecycleOwner, Observer {
            Log.d(TAG, "subscribeObservers: ${it.data}")
            it.data?.let{ categoryList ->
                recyclerAdapter.submitDishCategoryList(categoryList)
            }
        })

        menuViewModel.dishes.observe(viewLifecycleOwner, Observer {
            Log.d(TAG, "subscribeObservers: ${it.data}")
            it.data?.let{ dishList ->
                recyclerAdapter.submitDishList(dishList)
            }
        })


    }

    private fun initRecyclerView() {
        recycler_view.apply {
            val layoutManager = GridLayoutManager(this@MenuFragment.context, 2)

            recyclerAdapter = MenuRecyclerAdapter(
                requestManager,
                this@MenuFragment
            )

            layoutManager.spanSizeLookup = object: GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    Log.d(TAG, "getSpanSize: ${recyclerAdapter.getItemViewType(position)}")
                    when (recyclerAdapter.getItemViewType(position)) {
                        CATEGORY_TYPE -> return 1
                        DISH_TYPE -> return 2
                    }
                    return 1
                }
            }


            this.layoutManager = layoutManager
            adapter = recyclerAdapter
        }
    }

    private fun testData() {
//        recyclerAdapter.submitList(testList)
    }

    override fun onDishCategorySelected(position: Int, item: DishCategory) {
        menuViewModel.searchMenuItems(item.name)
    }

    override fun onDishSelected(position: Int, item: Dish) {
        Log.d(TAG, "onDishSelected: $item")
    }

}