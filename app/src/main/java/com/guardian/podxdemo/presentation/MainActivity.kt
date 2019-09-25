package com.guardian.podxdemo.presentation

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import com.example.podxdemo.R
import com.guardian.podxdemo.dagger.FragmentInjectionFactory
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

class MainActivity : AppCompatActivity(), HasAndroidInjector {
    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>

    @Inject
    lateinit var fragmentInjectionFactory: FragmentInjectionFactory

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState, persistentState)
        supportFragmentManager.fragmentFactory = fragmentInjectionFactory
        setContentView(R.layout.layout_mainactivity)
    }

    override fun androidInjector(): AndroidInjector<Any> = dispatchingAndroidInjector
}