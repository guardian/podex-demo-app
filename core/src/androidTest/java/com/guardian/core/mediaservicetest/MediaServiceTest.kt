package com.guardian.core.mediaservicetest

import android.content.ComponentName
import android.content.Context
import android.support.v4.media.MediaBrowserCompat
import androidx.test.annotation.UiThreadTest
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.guardian.core.mediaplayer.MediaService
import com.guardian.core.mediaplayer.common.MediaSessionConnection
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
    @Test
    @UiThreadTest
    @Throws(TimeoutException::class)
    fun testWithBoundService() {

        val testApplicationContext = ApplicationProvider.getApplicationContext<Context>()

        val connection = MediaSessionConnection (
            testApplicationContext,
            ComponentName(
                testApplicationContext,
                MediaService::class.java
            )
        )

        connection.subscribe("unused",
            object : MediaBrowserCompat.SubscriptionCallback() {
            }
        )


        println(connection.isConnected)
    }
}