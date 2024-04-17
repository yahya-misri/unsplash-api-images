package com.example.androiddemo.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

abstract class BaseActivity<V:ViewDataBinding>:AppCompatActivity() {
    private lateinit var viewDataBinding: ViewDataBinding
    abstract fun getLayoutID(): Int
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewDataBinding = DataBindingUtil.setContentView(this, getLayoutID())

    }

    fun getViewBinding(): V {
        return viewDataBinding as V
    }

    override fun onResume() {
        super.onResume()

    }
}