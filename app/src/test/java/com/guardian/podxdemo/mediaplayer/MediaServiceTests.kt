package com.guardian.podxdemo.mediaplayer

import android.os.Build
import com.guardian.core.mediaplayer.MediaService
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.P])
class MediaServiceTests {
    @Test
    fun testMediaService() {
        val mediaServiceController = Robolectric.buildService(MediaService::class.java)

        mediaServiceController.create()
    }
}