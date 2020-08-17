package com.gregkluska.restaurantmvvm.ui.main.menu

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.RequestManager
import com.gregkluska.restaurantmvvm.R
import com.gregkluska.restaurantmvvm.models.Dish
import com.gregkluska.restaurantmvvm.util.Constants.Companion.DISH_CATEGORIES
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_menu.*
import javax.inject.Inject
import kotlin.random.Random

private const val TAG = "AppDebug"

@AndroidEntryPoint
class MenuFragment : Fragment(), MenuRecyclerAdapter.Interaction {

    @Inject
    lateinit var requestManager: RequestManager
    private lateinit var recyclerAdapter: MenuRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_menu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()
        testData()
    }

    private fun initRecyclerView() {
        recycler_view.apply {
            layoutManager = GridLayoutManager(this@MenuFragment.context, 2)

            recyclerAdapter = MenuRecyclerAdapter(
                requestManager,
                this@MenuFragment
            )

            adapter = recyclerAdapter
        }
    }

    private fun testData() {
        val testList: ArrayList<Dish> = ArrayList<Dish>()
        for(category in DISH_CATEGORIES) {
            val dish: Dish = Dish(
                id = Random.nextInt(0,100),
                name = category,
                description = null,
                image = "https://loremflickr.com/320/240/${category.toLowerCase().replace("\\s".toRegex(), "")}"
            )
            testList.add(dish)
        }
        recyclerAdapter.submitList(testList)
    }

    override fun onItemSelected(position: Int, item: Dish) {
        Log.d(TAG, "onItemSelected: Item at position $position has been clicked")
    }

}