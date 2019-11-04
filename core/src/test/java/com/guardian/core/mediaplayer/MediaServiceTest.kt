package com.guardian.core.mediaplayer

import android.content.ComponentName
import android.os.Build
import com.guardian.core.mediaplayer.ShadowMediaBrowserCompat.ShadowMediaBrowserCompat
import com.guardian.core.mediaplayer.common.MediaSessionConnection
import com.guardian.core.mediaplayer.daggermocks.CoreComponentTestApplication
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.android.controller.ServiceController
import org.robolectric.annotation.Config
import timber.log.Timber

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.P],
    application = CoreComponentTestApplication::class,
    shadows = [ShadowMediaBrowserCompat::class]
    )
class MediaServiceTest {
    lateinit var mediaServiceController: ServiceController<MediaService>

    @Before
    fun setUpAndroidInjector() {
        Timber.plant(object : Timber.Tree() {
            override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
                println("Unit Test: $message")
                t?.printStackTrace()
            }
        })

        mediaServiceController = Robolectric.buildService(MediaService::class.java)
    }

    @Test
    fun testWithMediaService() {

        mediaServiceController.create()
        val mediaService = mediaServiceController.get()
        val mediaSessionConnection = MediaSessionConnection(
            RuntimeEnvironment.systemContext,
            ComponentName(RuntimeEnvironment.systemContext, MediaService::class.java)
        )
    }
}