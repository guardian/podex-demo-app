package com.guardian.core.mediaservicetest

import android.content.Context
import android.content.Intent
import android.os.IBinder
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ServiceTestRule
import com.guardian.core.mediaplayer.MediaService
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.TimeoutException

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class MediaServiceTest {
    @get:Rule
    val serviceRule = ServiceTestRule()

    @Test
    @Throws(TimeoutException::class)
    fun testWithBoundService() {
        // Create the service Intent.
        val serviceIntent = Intent(
                ApplicationProvider.getApplicationContext<Context>(),
                MediaService::class.java
            )

        System.out.println("${serviceIntent == null} test")

        // Bind the service and grab a reference to the binder.
        val binder: IBinder = serviceRule.bindService(serviceIntent)

        // Get the reference to the service, or you can call
        // public methods on the binder directly.


        // Verify that the service is working correctly.
    }
}