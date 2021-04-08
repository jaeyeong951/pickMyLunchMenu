package com.example.pickmylunchmenu.ui.main_two

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.pickmylunchmenu.MainActivityViewModel
import com.example.pickmylunchmenu.R
import com.example.pickmylunchmenu.base.BaseFragment
import com.example.pickmylunchmenu.databinding.FragmentMainTwoBinding
import com.example.pickmylunchmenu.dto.NearByRestaurantItem
import com.example.pickmylunchmenu.ui.MainViewModel
import com.example.pickmylunchmenu.ui.RestaurantListAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainTwoFragment : BaseFragment<FragmentMainTwoBinding, MainTwoViewModel>() {

    override val viewModel: MainTwoViewModel by viewModels()

    val activityViewModel: MainActivityViewModel by activityViewModels()

    var restaurantListForSort: List<NearByRestaurantItem> = mutableListOf()

    var height = 0

    override fun inflateBinder(inflater: LayoutInflater, container: ViewGroup?) {
        _binding = FragmentMainTwoBinding.inflate(inflater, container, false)
    }

    override fun initView() {
        height = resources.getDimensionPixelSize(resources.getIdentifier("status_bar_height", "dimen", "android"))
        val params = _binding!!.mainTwoToolbar.layoutParams as ConstraintLayout.LayoutParams
        params.topMargin = height + 48

        restaurantListForSort = activityViewModel.cachedRestaurantList

        _binding!!.mainTwoRecyclerView.adapter = RestaurantListAdapter { v, p ->
            val bundle = bundleOf("restaurant" to restaurantListForSort[p])
            findNavController().navigate(R.id.action_mainTwoFragment_to_restaurantFragment, bundle)
        }.apply {
            restaurantList = restaurantListForSort
            _binding!!.mainTwoChipGroup.setOnCheckedChangeListener { group, checkedId ->
                _binding!!.mainTwoRecyclerView.animate()
                when(checkedId) {
                    R.id.chip_favorite -> {
                        viewModel.getFavorites(activityViewModel.cachedUser!!.uid)
                        viewModel.isGetFavoritesFinished.observe(viewLifecycleOwner, {
                            restaurantListForSort = viewModel.favorites.map {
                                it.restaurantDto
                            }
                            restaurantList = restaurantListForSort
                        })
                    }
                    R.id.chip_distance -> {
                        restaurantListForSort = activityViewModel.cachedRestaurantList.sortedBy {
                            it.distance
                        }
                        restaurantList = restaurantListForSort
                    }
                    R.id.chip_abc -> {
                        restaurantListForSort = activityViewModel.cachedRestaurantList.sortedBy {
                            it.name
                        }
                        restaurantList = restaurantListForSort
                    }
                    R.id.chip_review -> {
                        restaurantListForSort = activityViewModel.cachedRestaurantList.sortedByDescending {
                            it.user_ratings_total
                        }
                        restaurantList = restaurantListForSort
                    }
                    else -> {
                        restaurantListForSort = activityViewModel.cachedRestaurantList
                        restaurantList = restaurantListForSort
                    }
                }
            }
        }

        _binding!!.toolbarBasket.setOnClickListener{
            findNavController().navigate(R.id.action_mainTwoFragment_to_basketFragment)
        }

        _binding!!.mainTwoTitle.text = activityViewModel.cachedRestaurantList[0].category
    }
}