package com.example.pickmylunchmenu.ui.restaurant

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.pickmylunchmenu.MainActivityViewModel
import com.example.pickmylunchmenu.R
import com.example.pickmylunchmenu.base.BaseFragment
import com.example.pickmylunchmenu.databinding.FragmentRestaurantBinding
import com.example.pickmylunchmenu.dto.NearByRestaurantItem
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.MapFragment
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RestaurantFragment : BaseFragment<FragmentRestaurantBinding, RestaurantViewModel>() {
    override val viewModel: RestaurantViewModel by viewModels()

    val activityViewModel: MainActivityViewModel by activityViewModels()

    var height = 0

    lateinit var restaurant: NearByRestaurantItem

    private val args: RestaurantFragmentArgs by navArgs()

    override fun inflateBinder(inflater: LayoutInflater, container: ViewGroup?) {
        _binding = FragmentRestaurantBinding.inflate(inflater, container, false)
    }

    override fun initView() {
        restaurant = args.restaurant
        height = resources.getDimensionPixelSize(resources.getIdentifier("status_bar_height", "dimen", "android"))
        val params = _binding!!.restaurantToolbar.layoutParams as ConstraintLayout.LayoutParams
        params.topMargin = height + 48

        viewModel.getFavorites(restaurant.id)

        val mapFragment = childFragmentManager.findFragmentById(R.id.restaurant_map) as MapFragment?
            ?: MapFragment.newInstance().also {
                childFragmentManager.beginTransaction().add(R.id.restaurant_map, it).commit()
            }

        mapFragment.getMapAsync { naverMap ->
            naverMap.uiSettings.apply {
                isZoomControlEnabled = false
            }
            naverMap.moveCamera(CameraUpdate.toCameraPosition(CameraPosition(LatLng(restaurant.lng.toDouble(), restaurant.lat.toDouble()), 17.0)))
            val marker = Marker()
            marker.apply {
                position = LatLng(restaurant.lng.toDouble(), restaurant.lat.toDouble())
                captionText = restaurant.name
                isIconPerspectiveEnabled = true
                icon = OverlayImage.fromResource(R.drawable.gastronomy)
                width = 70
                height = 70
                map = naverMap
            }
        }

        _binding!!.apply {
            restaurantTitle.text = restaurant.name
            restaurantCategory.text = restaurant.category
            restaurantAddress.text = restaurant.doro
            restaurantMenuList.adapter = RestaurantMenuListAdapter(activityViewModel).apply {
                menuList = restaurant.menus
            }
            restaurantSubmitButton.setOnClickListener {
                Log.e("basket", activityViewModel.orderMenuList.toString())
                if(activityViewModel.orderMenuList.isEmpty()) {
                    Toast.makeText(requireContext(), "메뉴를 선택해주세요.", Toast.LENGTH_SHORT).show()
                }
                else viewModel.submitOrder(activityViewModel.cachedUser!!.uid, activityViewModel.orderMenuList)
            }
            toolbarBasket.setOnClickListener {
                findNavController().navigate(R.id.action_restaurantFragment_to_basketFragment)
            }
            restaurantFavoriteButton.setOnClickListener {
                viewModel.setFavorite(activityViewModel.cachedUser!!.uid, restaurant.id)
            }
        }

        viewModel.isFavoriteExist.observe(viewLifecycleOwner, {
            _binding!!.restaurantFavoriteButton.text = "즐겨찾기 삭제"
            _binding!!.restaurantFavoriteButton.setOnClickListener {
                val favorite = viewModel.favoriteList.find {
                    it.restaurantDto.id == restaurant.id
                }
                viewModel.deleteFavorite(favorite!!.id)
            }
        })

        viewModel.isFavoriteNotExist.observe(viewLifecycleOwner, {
            _binding!!.restaurantFavoriteButton.text = "즐겨찾기 담기"
        })

        viewModel.isDeleteFavoriteFinished.observe(viewLifecycleOwner, {
            _binding!!.restaurantFavoriteButton.text = "즐겨찾기 담기"
        })

        viewModel.isPutFavoriteFinished.observe(viewLifecycleOwner, {
            _binding!!.restaurantFavoriteButton.text = "즐겨찾기 삭제"
            _binding!!.restaurantFavoriteButton.setOnClickListener {
                val favorite = viewModel.favoriteList.find {
                    it.restaurantDto.id == restaurant.id
                }
                viewModel.deleteFavorite(favorite!!.id)
            }
        })

        viewModel.isSubmitOrderFinished.observe(viewLifecycleOwner, {
            Log.e("주문", "성공")
            activityViewModel.orderMenuList.clear()
            findNavController().navigate(R.id.action_restaurantFragment_to_basketFragment)
        })

        viewModel.isSubmitOrderFailed.observe(viewLifecycleOwner, {
            Log.e("주문", "실패")
            activityViewModel.orderMenuList.clear()
        })
        
        activityViewModel.totalPrice.observe(viewLifecycleOwner, {
            _binding!!.restaurantSubmitButton.text = "담기(${it}원)"
        })

//        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
//            override fun handleOnBackPressed() {
//                activityViewModel.orderMenuList.clear()
//                requireActivity().onBackPressed()
//            }
//        })
    }
}