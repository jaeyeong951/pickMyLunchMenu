package com.example.pickmylunchmenu.ui.review

import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.pickmylunchmenu.base.BaseFragment
import com.example.pickmylunchmenu.databinding.FragmentReviewBinding
import com.example.pickmylunchmenu.dto.HistoryDto
import com.example.pickmylunchmenu.ui.MainFragment
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@AndroidEntryPoint
class ReviewFragment : BaseFragment<FragmentReviewBinding, ReviewViewModel>() {
    //    override val viewModel: MainViewModel by hiltNavGraphViewModels(R.id.navigation)
    override val viewModel: ReviewViewModel by viewModels()

    override fun inflateBinder(inflater: LayoutInflater, container: ViewGroup?) {
        _binding = FragmentReviewBinding.inflate(inflater, container, false)
    }

    private val selectedRestaurant: ReviewFragmentArgs by navArgs()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun initView() {
        val height = resources.getDimensionPixelSize(
            resources.getIdentifier(
                "status_bar_height",
                "dimen",
                "android"
            )
        )
        val params = _binding!!.reviewToolbar.layoutParams as ConstraintLayout.LayoutParams
        params.topMargin = height
        _binding!!.reviewToolbar.layoutParams = params

        _binding!!.apply {
            reviewList.adapter = ReviewAdapter().apply {
                reviewList = selectedRestaurant.restaurant.reviews
            }
            toolbarTitle.text = selectedRestaurant.restaurant.name
            reviewAvgTitle.text = selectedRestaurant.restaurant.rating.toString()
            reviewAvgStar.rating = selectedRestaurant.restaurant.rating.toFloat()
            reviewAvgTitleDesc.text = "총 ${selectedRestaurant.restaurant.user_ratings_total}개의 리뷰"
            toolbarBackButton.setOnClickListener { findNavController().popBackStack() }
            reviewSubmitButton.setOnClickListener {
                setResult(
                    MainFragment.SelectedRestaurantKey,
                    HistoryDto(
                        name = selectedRestaurant.restaurant.name,
                        rating = selectedRestaurant.restaurant.rating.toFloat(),
                        lat = selectedRestaurant.restaurant.lat,
                        lng = selectedRestaurant.restaurant.lng
                    )
                )
                findNavController().popBackStack()
            }
        }

        viewModel.isSaveHistoryFinished.observe(viewLifecycleOwner, {
            Log.e("insert finished", "good")
        })

        viewModel.isLoadHistoryFinished.observe(viewLifecycleOwner, {
            Log.e("load finished", viewModel.historyList.toString())
        })
    }
}