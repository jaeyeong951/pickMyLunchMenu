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
import com.example.pickmylunchmenu.ui.MainViewModel
import com.example.pickmylunchmenu.ui.RestaurantListAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainTwoFragment : BaseFragment<FragmentMainTwoBinding, MainTwoViewModel>() {

    override val viewModel: MainTwoViewModel by viewModels()

    val activityViewModel: MainActivityViewModel by activityViewModels()

    var height = 0

    override fun inflateBinder(inflater: LayoutInflater, container: ViewGroup?) {
        _binding = FragmentMainTwoBinding.inflate(inflater, container, false)
    }

    override fun initView() {
        height = resources.getDimensionPixelSize(resources.getIdentifier("status_bar_height", "dimen", "android"))
        val params = _binding!!.mainTwoToolbar.layoutParams as ConstraintLayout.LayoutParams
        params.topMargin = height + 48

        _binding!!.mainTwoRecyclerView.adapter = RestaurantListAdapter { v, p ->
            val bundle = bundleOf("position" to p)
            findNavController().navigate(R.id.action_mainTwoFragment_to_restaurantFragment, bundle)
        }.apply {
            restaurantList = activityViewModel.cachedRestaurantList
            _binding!!.mainTwoChipGroup.setOnCheckedChangeListener { group, checkedId ->
                _binding!!.mainTwoRecyclerView.animate()
                when(checkedId) {
                    R.id.chip_favorite -> {
                        viewModel.getFavorites(activityViewModel.cachedUser!!.uid)
                        viewModel.isGetFavoritesFinished.observe(viewLifecycleOwner, {
                            val list = viewModel.favorites.map {
                                it.restaurantDto
                            }
                            restaurantList = list
                        })
                    }
                    R.id.chip_distance -> {
                        val list = activityViewModel.cachedRestaurantList.sortedBy {
                            it.distance
                        }
                        restaurantList = list
                    }
                    R.id.chip_abc -> {
                        val list = activityViewModel.cachedRestaurantList.sortedBy {
                            it.name
                        }
                        restaurantList = list
                    }
                    R.id.chip_review -> {
                        val list = activityViewModel.cachedRestaurantList.sortedByDescending {
                            it.user_ratings_total
                        }
                        restaurantList = list
                    }
                    else -> {
                        restaurantList = activityViewModel.cachedRestaurantList
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