package com.guardian.podxdemo.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.guardian.podxdemo.R
import com.guardian.podxdemo.dagger.FragmentInjectionFactory
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import timber.log.Timber
import javax.inject.Inject

class MainActivity : AppCompatActivity(), HasAndroidInjector {
    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>

    @Inject
    lateinit var fragmentInjectionFactory: FragmentInjectionFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        Timber.i("Creating main activity")
        supportFragmentManager.fragmentFactory = fragmentInjectionFactory
        setContentView(R.layout.layout_mainactivity)
    }

    override fun androidInjector(): AndroidInjector<Any> = dispatchingAndroidInjector
}