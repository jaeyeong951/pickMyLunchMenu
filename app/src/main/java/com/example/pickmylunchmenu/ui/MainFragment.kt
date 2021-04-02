package com.example.pickmylunchmenu.ui

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.example.pickmylunchmenu.MainActivityViewModel
import com.example.pickmylunchmenu.R
import com.example.pickmylunchmenu.base.BaseFragment
import com.example.pickmylunchmenu.databinding.DialogSubmitReviewBinding
import com.example.pickmylunchmenu.databinding.FragmentMainBinding
import com.example.pickmylunchmenu.dto.HistoryDto
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
import com.naver.maps.map.overlay.CircleOverlay
import com.tedpark.tedpermission.rx2.TedRx2Permission
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.disposables.CompositeDisposable
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@AndroidEntryPoint
class MainFragment : BaseFragment<FragmentMainBinding, MainViewModel>() {

    companion object {
        const val SelectedRestaurantKey = "selected_restaurant_key"
    }

    override val viewModel: MainViewModel by hiltNavGraphViewModels(R.id.navigation)

    private val compositeDisposable = CompositeDisposable()

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private var restaurantListBottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>? = null

    private lateinit var adapter : RestaurantListAdapter

    private var height : Int = 0

    override fun inflateBinder(inflater: LayoutInflater, container: ViewGroup?) {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
    }

    @SuppressLint("MissingPermission")
    override fun initView() {
        _binding!!.bottomSheetRestaurantListId.bottomSheetRestaurantRefreshButton.scaleX = 0F
        _binding!!.bottomSheetRestaurantListId.bottomSheetRestaurantRefreshButton.scaleY = 0F
        height = resources.getDimensionPixelSize(resources.getIdentifier("status_bar_height", "dimen", "android"))
        val mapFragment = childFragmentManager.findFragmentById(R.id.main_map) as MapFragment?
            ?: MapFragment.newInstance().also {
                childFragmentManager.beginTransaction().add(R.id.main_map, it).commit()
            }

        mapFragment.getMapAsync { naverMap ->
            naverMap.uiSettings.apply {
                isZoomControlEnabled = false
            }
            compositeDisposable.add(TedRx2Permission.with(context).setPermissions(android.Manifest.permission.ACCESS_FINE_LOCATION)
                .request()
                .subscribe{ result ->
                    if(result.isGranted) {
                        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
                        fusedLocationClient.lastLocation.addOnSuccessListener { it ->
                            if (it != null) {
                                naverMap.locationOverlay.isVisible = true
                                naverMap.locationOverlay.position = LatLng(it.latitude, it.longitude)
                                CircleOverlay().apply {
                                    center = LatLng(it.latitude, it.longitude)
                                    radius = 350.0
                                    map = naverMap
                                    outlineColor = Color.parseColor("#884B47FF")
                                    outlineWidth = 7
                                    color = Color.parseColor("#00000000")
                                }
                                naverMap.moveCamera(CameraUpdate.toCameraPosition(CameraPosition(LatLng(it.latitude, it.longitude), 15.0)))
                                if(!viewModel.isLoaded) {
                                    viewModel.getRestaurantNearByMe(it.latitude, it.longitude, 0.35)
//                                    viewModel.getRestaurantNearByMe(35.154324, 129.057481, 0.09)
                                    viewModel.isLoaded = true
                                }
                                else viewModel.callIsFinished()
                                viewModel.isLoadRestaurantFinished.observe(viewLifecycleOwner, {
                                    adapter = RestaurantListAdapter { v, position ->
                                        val bundle = bundleOf("restaurant" to viewModel.restaurantListModified[position])
                                        findNavController().navigate(R.id.action_mainFragment_to_reviewFragment, bundle)
                                    }
                                    _binding!!.bottomSheetRestaurantListId.bottomSheetRestaurantListTitle.text = "근처 ${viewModel.restaurantList.size}개의 식당"
                                    _binding!!.bottomSheetRestaurantListId.bottomSheetRestaurantListTitleSub.text = "총 ${viewModel.totalReview}개의 리뷰"
                                    _binding!!.bottomSheetRestaurantListId.bottomSheetRestaurantListRecyclerView.adapter = adapter
                                    adapter.restaurantList = viewModel.restaurantListModified

                                    for(i in 0 until viewModel.markerList.size) {
                                        viewModel.markerList[i].apply {
                                            map = naverMap
                                            setOnClickListener {
                                                val bundle = bundleOf("restaurant" to viewModel.restaurantList[i])
                                                findNavController().navigate(R.id.action_mainFragment_to_reviewFragment, bundle)
                                                true
                                            }
                                        }
                                    }
                                })
                                viewModel.isMoveCameraCalled.observe(viewLifecycleOwner, {
                                    naverMap.moveCamera(
                                        CameraUpdate
                                            .toCameraPosition(CameraPosition(LatLng(it.second, it.first), 19.0))
                                            .animate(CameraAnimation.Easing, 1000)
                                            .finishCallback {
                                                Log.e("camera update", "finished")
                                            }
                                            .cancelCallback {
                                                Log.e("camera update", "canceled")
                                            })
                                })
                            }
                        }
                    }
                    else {

                    }
                })
        }
        viewModel.isRandomListFinished.observe(viewLifecycleOwner, {
            _binding!!.bottomSheetRestaurantListId.bottomSheetRestaurantListRecyclerView.scheduleLayoutAnimation()
            viewModel.restaurantListModified = ArrayList(it)
            adapter.restaurantList = viewModel.restaurantListModified
        })
        viewModel.isSaveHistoryFinished.observe(viewLifecycleOwner, {
            Toast.makeText(requireContext(), "식사가 기록되었습니다.", Toast.LENGTH_SHORT).show()
            _binding!!.mainReviewSubmitButton.visibility = View.GONE
            _binding!!.bottomSheetRestaurantListId.bottomSheet.visibility = View.VISIBLE
        })

        _binding!!.bottomSheetRestaurantListId.bottomSheetRestaurantButton.setOnClickListener {
            viewModel.pick5RandomRestaurantBasedOnReview()
        }
        _binding!!.bottomSheetRestaurantListId.bottomSheetRestaurantRefreshButton.setOnClickListener {
            viewModel.restaurantListModified = viewModel.restaurantList
            viewModel.callIsFinished()
        }
        _binding!!.mainFab.setOnClickListener {
            _binding!!.mainFab.scaleX = 0F
            _binding!!.mainFab.scaleY = 0F
            findNavController().navigate(R.id.action_mainFragment_to_historyFragment)
        }
        _binding!!.mainReviewSubmitButton.setOnClickListener {
            viewModel.historyDto?.let { history -> ReviewSubmitDialog(history, viewModel).show(childFragmentManager, "submit review") }
        }
        restaurantListBottomSheetBehavior = BottomSheetBehavior.from(_binding!!.bottomSheetRestaurantListId.root).apply {
            addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    viewModel.expanded = newState == BottomSheetBehavior.STATE_EXPANDED
                }

                override fun onSlide(bottomSheet: View, slideOffset: Float) {
                    val params = _binding!!.bottomSheetRestaurantListId.bottomSheetRestaurantListTitle.layoutParams as ConstraintLayout.LayoutParams
                    params.topMargin = (48 + height * slideOffset).toInt()
                    _binding!!.bottomSheetRestaurantListId.bottomSheetRestaurantListTitle.layoutParams = params
                    _binding!!.mainFab.scaleX = slideOffset - 1
                    _binding!!.mainFab.scaleY = slideOffset - 1
                    _binding!!.bottomSheetRestaurantListId.bottomSheetRestaurantRefreshButton.scaleX = slideOffset
                    _binding!!.bottomSheetRestaurantListId.bottomSheetRestaurantRefreshButton.scaleY = slideOffset
                }
            })
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if(restaurantListBottomSheetBehavior!!.state == BottomSheetBehavior.STATE_EXPANDED) restaurantListBottomSheetBehavior!!.state = BottomSheetBehavior.STATE_COLLAPSED
                else {
                    isEnabled = false
                    requireActivity().onBackPressed()
                }
            }
        })
        getResult<HistoryDto>(SelectedRestaurantKey) {
            viewModel.callCameraUpdate(it.lat.toDouble(), it.lng.toDouble())
            _binding!!.mainReviewSubmitButton.visibility = View.VISIBLE
            _binding!!.bottomSheetRestaurantListId.bottomSheet.visibility = View.GONE
            restaurantListBottomSheetBehavior!!.state = BottomSheetBehavior.STATE_COLLAPSED
            viewModel.expanded = false
            viewModel.historyDto = it
        }
    }

    override fun onResume() {
        super.onResume()
        if(viewModel.expanded) {
            val params = _binding!!.bottomSheetRestaurantListId.bottomSheetRestaurantListTitle.layoutParams as ConstraintLayout.LayoutParams
            params.topMargin = 48 + height
            _binding!!.bottomSheetRestaurantListId.bottomSheetRestaurantListTitle.layoutParams = params
            restaurantListBottomSheetBehavior!!.state = BottomSheetBehavior.STATE_EXPANDED
            _binding!!.bottomSheetRestaurantListId.bottomSheetRestaurantRefreshButton.scaleX = 1.0F
            _binding!!.bottomSheetRestaurantListId.bottomSheetRestaurantRefreshButton.scaleY = 1.0F
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }

    class ReviewSubmitDialog(private var historyDto: HistoryDto, private var viewModel: MainViewModel) : DialogFragment() {
        private var dialogViewBinding : DialogSubmitReviewBinding? = null

        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            dialogViewBinding = DialogSubmitReviewBinding.inflate(inflater, container, false)
            dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)
            return dialogViewBinding!!.root
        }

        @RequiresApi(Build.VERSION_CODES.O)
        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)
            dialogViewBinding!!.bottomSheetRestaurantButton.setOnClickListener {
                viewModel.insertHistory(
                    HistoryDto(
                    name = historyDto.name,
                    rating = historyDto.rating,
                    myRating = dialogViewBinding!!.submitReviewStar.rating,
                    date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일")),
                    comment = dialogViewBinding!!.submitReviewCommentEdit.text.toString())
                )

                dialog?.dismiss()
            }
        }

        override fun onResume() {
            super.onResume()
            val wm = requireActivity().getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val display = wm.defaultDisplay
            val size = Point()
            display.getSize(size)
            val params: ViewGroup.LayoutParams? = dialog?.window?.attributes
            val deviceWidth = size.x
            params?.width = (deviceWidth * 0.9).toInt()
            dialog?.window?.attributes = params as WindowManager.LayoutParams
        }
    }
}