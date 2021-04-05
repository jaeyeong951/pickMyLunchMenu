package com.example.pickmylunchmenu.base

import android.app.Dialog
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.example.pickmylunchmenu.util.LoadingIndicator

abstract class BaseFragment<VB : ViewBinding, VM : BaseViewModel> : Fragment(){
    private var mLoadingIndicator: Dialog? = null

    abstract val viewModel : VM

    var _binding: VB? = null
    private val binding get() = _binding!!

    open var lightStatusBar : Boolean = true

    abstract fun initView()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        inflateBinder(inflater, container)
        mLoadingIndicator = context?.let { LoadingIndicator(it) }
        Log.e("onCreateView","onCreateView")
        return binding.root
    }

    abstract fun inflateBinder(inflater: LayoutInflater,
                               container: ViewGroup?)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadingIndicatorObserving()
        initView()
        Log.e("onViewCreated","onViewCreated")
    }

    override fun onResume() {
        super.onResume()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(lightStatusBar){
                Log.e("base lightStatus", lightStatusBar.toString())
                var flags = (activity as? AppCompatActivity)?.window?.decorView?.systemUiVisibility
                flags = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) flags!! or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
                else flags!! or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                (activity as? AppCompatActivity)?.window?.decorView?.systemUiVisibility = flags
//                (activity as? AppCompatActivity)?.window?.decorView?.systemUiVisibility = SYSTEM_UI_FLAG_LIGHT_STATUS_BAR or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
            }
            else {
                var flags = (activity as? AppCompatActivity)?.window?.decorView?.systemUiVisibility
                flags = flags!! and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
                (activity as? AppCompatActivity)?.window?.decorView?.systemUiVisibility = flags
            }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.e("onDestroyView",this.tag.toString())
        _binding = null
    }

    fun <T>setResult(key: String, result: T){
        findNavController().previousBackStackEntry?.savedStateHandle?.set(key, result)
    }

    fun <T>setResult(returnTo: Int, key: String, result: T){
        findNavController().getBackStackEntry(returnTo).savedStateHandle.set(key, result)
    }

    fun <T>getResult(key: String, observer: ((T) -> Unit)){
        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<T>(key)?.observe(viewLifecycleOwner, { t ->
            findNavController().currentBackStackEntry?.savedStateHandle?.remove<T>(key)
            observer(t)
        })
    }

    private fun loadingIndicatorObserving() {
        viewModel.startLoadingIndicatorEvent.observe(viewLifecycleOwner, Observer {
            startLoadingIndicator()
        })
        viewModel.stopLoadingIndicatorEvent.observe(viewLifecycleOwner, Observer {
            stopLoadingIndicator()
        })
    }

    private fun stopLoadingIndicator() {
        mLoadingIndicator?.let {
            if (it.isShowing) it.cancel()
        }
    }

    private fun startLoadingIndicator() {
        stopLoadingIndicator()
        activity?.let {
            if (!it.isFinishing) {
                mLoadingIndicator = LoadingIndicator(activity)
                mLoadingIndicator?.show()
            }
        }
    }
}