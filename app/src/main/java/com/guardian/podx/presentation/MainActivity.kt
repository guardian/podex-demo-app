package com.guardian.podx.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.guardian.podx.R
import com.guardian.podx.dagger.FragmentInjectionFactory
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

/**
 * A simple activity who's layout contains the navigation component responsible for displaying the
 * UI.
 */

class MainActivity : AppCompatActivity(), HasAndroidInjector {
    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>

    @Inject
    lateinit var fragmentInjectionFactory: FragmentInjectionFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        supportFragmentManager.fragmentFactory = fragmentInjectionFactory
        setContentView(R.layout.layout_mainactivity)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun androidInjector(): AndroidInjector<Any> = dispatchingAndroidInjector
}
