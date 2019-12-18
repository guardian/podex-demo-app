package com.guardian.core.mediaservicetest

import android.content.ComponentName
import android.content.Context
import android.media.session.PlaybackState
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.guardian.core.mediaplayer.MediaService
import com.guardian.core.mediaplayer.common.MediaSessionConnection
import com.guardian.core.testutils.InstrumentationMockedFeedDataSources
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import timber.log.Timber

/**
 * Instrumented test, which will execute on an Android device.
 *
 * The Media Service is complex and highly dependant on framework classes, so it is more
 * straightforward to test it with instrumentation.
 */
@RunWith(AndroidJUnit4::class)
class MediaServiceTest {

    private val mediaServiceTestScope = CoroutineScope(Dispatchers.IO)
    private val testRunningMutex = Mutex(false)

    @Before
    fun setupMediaTests() {
        Timber.plant(object : Timber.Tree() {
            override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
                println("Unit Test: $message")
                t?.printStackTrace()
            }
        })

        val testMusic1 = this.javaClass
            .classLoader!!
            .getResourceAsStream("test_music_1.mp3")

        InstrumentationMockedFeedDataSources.writeTestData(testMusic1)
    }

    @After
    fun teardownMediaTests() {
        Timber.uprootAll()
        if (testRunningMutex.isLocked) {
            testRunningMutex.unlock()
        }
    }

    @Test
    fun testPlaybackOfLocalMedia() {
        // aquire the lock to start the test that will run in the background
        testRunningMutex.tryLock()

        // run on the main thread as transport controls cannot be created off a prepared thread
        InstrumentationRegistry.getInstrumentation().runOnMainSync {

            // instantiate a local mediasessionconnection
            val connection = getMediaSessionConnection()

            // switch to the background with the media session
            mediaServiceTestScope.launch {
                // wait until the connection to the media browser is done
                while (connection.isConnected.value != true) {}

                connection.transportControls.playFromMediaId(
                    InstrumentationMockedFeedDataSources.testFeedItem2.feedItemAudioUrl,
                    null
                )

                // until the play command is registered the player is still in its initial state
                while (connection.playbackState.value?.state == PlaybackState.STATE_NONE) {}

                Timber.i("state log ${connection.playbackState.value?.state}")
                assertThat(
                    connection.playbackState.value?.state,
                    `is`(PlaybackState.STATE_BUFFERING)
                )

                while (connection.playbackState.value?.state == PlaybackState.STATE_BUFFERING) {}

                Timber.i("state log ${connection.playbackState.value?.state}")
                assertThat(connection.playbackState.value?.state, `is`(PlaybackState.STATE_PLAYING))
                testRunningMutex.unlock()
            }
        }

        // check if the test is still running in 10 seconds
        mediaServiceTestScope.launch {
            delay(10000)
            assertThat(testRunningMutex.isLocked, `is`(false))
        }

        // wait until the test is completed before cleaning up
        while (testRunningMutex.isLocked) {}
    }

    /**
     * needs to be run on the Main looper or any other prepared looper.
     */
    private fun getMediaSessionConnection(): MediaSessionConnection {
        val testApplicationContext = ApplicationProvider.getApplicationContext<Context>()

        return MediaSessionConnection(
            testApplicationContext,
            ComponentName(
                testApplicationContext,
                MediaService::class.java
            )
        )
    }
}