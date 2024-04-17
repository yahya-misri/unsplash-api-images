package com.example.androiddemo.ui

import Injection
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import com.example.androiddemo.R
import com.example.androiddemo.base.BaseActivity
import com.example.androiddemo.databinding.ActivityHomeBinding
import com.example.androiddemo.networking.ApiHelper
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HomeActivity : BaseActivity<ActivityHomeBinding>() {
    private val TAG = HomeActivity::class.java.simpleName
    private lateinit var activityHomeBinding: ActivityHomeBinding
    override fun getLayoutID(): Int {
        return R.layout.activity_home
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityHomeBinding = getViewBinding()
        activityHomeBinding.recvmain.layoutManager =
            GridLayoutManager(this, 2)


        // Get the view model
        val viewModel by viewModels<PhotoViewModel>(
            factoryProducer = { Injection.provideViewModelFactory(owner = this) }
        )

        val items = viewModel.items
        val photoAdapter = PhotoAdapter(this)
        activityHomeBinding.recvmain.adapter=photoAdapter
        lifecycleScope.launch {

            repeatOnLifecycle(Lifecycle.State.STARTED) {
                items.collectLatest {
                    photoAdapter.submitData(it)
                }
            }
        }


        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                photoAdapter.loadStateFlow.collect {
                }
            }
        }
    }

}
