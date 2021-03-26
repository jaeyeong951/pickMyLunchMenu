package com.example.pickmylunchmenu.ui.history

import android.app.AlertDialog
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.pickmylunchmenu.R
import com.example.pickmylunchmenu.base.BaseFragment
import com.example.pickmylunchmenu.databinding.FragmentHistoryBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HistoryFragment : BaseFragment<FragmentHistoryBinding, HistoryViewModel>() {

    override val viewModel: HistoryViewModel by viewModels()

    private lateinit var adapter: HistoryAdapter

    override fun inflateBinder(inflater: LayoutInflater, container: ViewGroup?) {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
    }

    override fun initView() {
        val height = resources.getDimensionPixelSize(resources.getIdentifier("status_bar_height", "dimen", "android"))
        val params = _binding!!.historyToolbar.layoutParams as ConstraintLayout.LayoutParams
        params.topMargin = height
        _binding!!.historyToolbar.layoutParams = params
        adapter = HistoryAdapter { _, position ->
            MaterialAlertDialogBuilder(requireContext(), R.style.AlertDialogTheme)
                .setTitle("히스토리 삭제")
                .setMessage("해당 식사 기록을 삭제합니다.")
                .setPositiveButton("네") { dialog, _ ->
                    viewModel.deleteHistory(position)
                    dialog.cancel()
                }
                .setNegativeButton("취소") { dialog, _ ->
                    dialog.cancel()
                }
                .show()
//            androidx.appcompat.app.AlertDialog.Builder(requireContext())
//                .setTitle("히스토리 삭제")
//                .setMessage("해당 식사 기록을 삭제합니다.")
//                .setPositiveButton("네") { dialog, _ ->
//                    viewModel.deleteHistory(position)
//                    dialog.cancel()
//                }
//                .setNegativeButton("취소") { dialog, _ ->
//                    dialog.cancel()
//                }
//                .show()
        }

        viewModel.getAllHistory()

        viewModel.isLoadHistoryFinished.observe(viewLifecycleOwner, {
            adapter.reviewList = viewModel.historyList
            Log.e("history", viewModel.historyList.toString())
            _binding!!.historyRecyclerView.adapter = adapter
        })

        viewModel.inDeleteHistoryFinished.observe(viewLifecycleOwner, {
            viewModel.getAllHistory()
        })

        _binding!!.toolbarBackButton.setOnClickListener {
            findNavController().popBackStack()
        }
    }
}