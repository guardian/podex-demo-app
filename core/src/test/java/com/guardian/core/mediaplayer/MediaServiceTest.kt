package com.guardian.core.mediaplayer

import android.os.Build
import com.guardian.core.mediaplayer.daggermocks.CoreComponentTestApplication
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.android.controller.ServiceController
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.P], application = CoreComponentTestApplication::class)
class MediaServiceTest {
    lateinit var mediaServiceController: ServiceController<MediaService>

    @Before
    fun setUpAndroidInjector() {
        mediaServiceController = Robolectric.buildService(MediaService::class.java)
    }

    @Test
    fun testWithMediaService() {
        mediaServiceController.create()
    }
}