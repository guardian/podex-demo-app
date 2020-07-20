package com.guardian.core

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner
import com.guardian.core.coretestapplication.CoreComponentTestApplication

class CoreInstrumentationTestRunner : AndroidJUnitRunner() {
    override fun newApplication(
        cl: ClassLoader?,
        className: String?,
        context: Context?
    ): Application {
        return super.newApplication(cl, CoreComponentTestApplication::class.java.name, context)
    }
}
