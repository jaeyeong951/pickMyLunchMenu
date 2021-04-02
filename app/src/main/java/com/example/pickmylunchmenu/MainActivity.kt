package com.example.pickmylunchmenu

import android.graphics.Rect
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    val viewModel by viewModels<MainActivityViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val layout = findViewById<LinearLayout>(R.id.single_activity_layout)

        window.apply {
            navigationBarColor = ContextCompat.getColor(this@MainActivity, R.color.white)
            statusBarColor = ContextCompat.getColor(this@MainActivity, R.color.transparent)
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN

            decorView.viewTreeObserver.addOnGlobalLayoutListener {
                val r = Rect()
                window.decorView.getWindowVisibleDisplayFrame(r)
                val height = layout.context.resources.displayMetrics.heightPixels
                val diff = height - r.bottom
                val nav_height = resources.getDimensionPixelSize(resources.getIdentifier("status_bar_height", "dimen", "android"))
                if (diff != 0) {
                    if (layout.paddingBottom != diff) {
                        layout.setPadding(0, 0, 0, diff + nav_height)
                    }
                } else {
                    if (layout.paddingBottom != 0) {
                        layout.setPadding(0, 0, 0, nav_height)
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.testValue.observe(this, {
            Log.e("in main activity", it.toString())
        })
    }
}