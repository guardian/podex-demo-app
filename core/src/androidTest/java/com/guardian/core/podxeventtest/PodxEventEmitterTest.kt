package com.guardian.core.podxeventtest

import android.content.ComponentName
import android.content.Context
import android.media.session.PlaybackState
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.guardian.core.mediaplayer.MediaService
import com.guardian.core.mediaplayer.common.MediaSessionConnection
import com.guardian.core.mediaplayer.podx.PodXEventEmitter
import com.guardian.core.mediaplayer.podx.PodXEventEmitterImpl
import com.guardian.core.podxevent.PodXImageEvent
import com.guardian.core.podxevent.PodXWebEvent
import com.guardian.core.testutils.InstrumentationMockedEventDataSources
import com.guardian.core.testutils.InstrumentationMockedFeedDataSources
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import timber.log.Timber

/**
 * Instrumented test, which will execute on an Android device.
 */
@RunWith(AndroidJUnit4::class)
class PodxEventEmitterTest {
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

        val testMusic2 = this.javaClass
            .classLoader!!
            .getResourceAsStream("test_music_2.mp3")

        InstrumentationMockedFeedDataSources.writeTestData(testMusic1, testMusic2)
    }

    @After
    fun teardownMediaTests() {
        Timber.uprootAll()
        if (testRunningMutex.isLocked) {
            testRunningMutex.unlock()
        }
    }

    @Test
    fun testEmittingImageEventsAfterSeek() {
        // aquire the lock to start the test that will run in the background
        testRunningMutex.tryLock()

        // run on the main thread as transport controls cannot be created off a prepared thread
        InstrumentationRegistry.getInstrumentation().runOnMainSync {

            // instantiate a local mediasessionconnection
            val connection = getMediaSessionConnection()

            //instantiate an event emitter
            val podXEventEmitter = getPodXEventEmitter(connection)

            // switch to the background with the media session
            mediaServiceTestScope.launch {

                // wait until the connection to the media browser is done
                while (connection.isConnected.value != true) {}

                connection.transportControls.playFromMediaId(
                    InstrumentationMockedFeedDataSources.testFeedItem1.feedItemAudioUrl,
                    null
                )

                podXEventEmitter.registerCurrentFeedItem(InstrumentationMockedFeedDataSources.testFeedItem1)

                // until the play command is registered the player is still in its initial state
                while (connection.playbackState.value?.state == PlaybackState.STATE_NONE) {}
                while (connection.playbackState.value?.state == PlaybackState.STATE_BUFFERING) {}

                //seek to a position in playback which has valid podXevents
                connection.transportControls.seekTo(8000)
                while (podXEventEmitter.podXImageEventLiveData.value?.size == 0) {}

                assertThat(podXEventEmitter.podXImageEventLiveData.value?.size, `is`(2))


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

    @Test
    fun testEmittingWebEventsAfterSeek() {
        // aquire the lock to start the test that will run in the background
        testRunningMutex.tryLock()

        // run on the main thread as transport controls cannot be created off a prepared thread
        InstrumentationRegistry.getInstrumentation().runOnMainSync {

            // instantiate a local mediasessionconnection
            val connection = getMediaSessionConnection()

            //instantiate an event emitter
            val podXEventEmitter = getPodXEventEmitter(connection)

            // switch to the background with the media session
            mediaServiceTestScope.launch {

                // wait until the connection to the media browser is done
                while (connection.isConnected.value != true) {}

                connection.transportControls.playFromMediaId(
                    InstrumentationMockedFeedDataSources.testFeedItem1.feedItemAudioUrl,
                    null
                )

                podXEventEmitter.registerCurrentFeedItem(InstrumentationMockedFeedDataSources.testFeedItem1)

                // until the play command is registered the player is still in its initial state
                while (connection.playbackState.value?.state == PlaybackState.STATE_NONE) {}
                while (connection.playbackState.value?.state == PlaybackState.STATE_BUFFERING) {}

                //seek to a position in playback which has valid podXevents
                connection.transportControls.seekTo(8000)
                while (podXEventEmitter.podXWebEventLiveData.value?.size == 0) {}

                assertThat(podXEventEmitter.podXWebEventLiveData.value?.size, `is`(2))


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

    @Test
    fun testEmittingImageAndWebEventsAfterSeekViaLiveData() {
        // aquire the lock to start the test that will run in the background
        testRunningMutex.tryLock()

        //store observed changes to the emitter with these vars
        var observedImageEvents = listOf<PodXImageEvent>()
        var observedWebEvents = listOf<PodXWebEvent>()

        // run on the main thread as transport controls cannot be created off a prepared thread
        InstrumentationRegistry.getInstrumentation().runOnMainSync {

            // instantiate a local mediasessionconnection
            val connection = getMediaSessionConnection()

            //instantiate an event emitter
            val podXEventEmitter = getPodXEventEmitter(connection)

            //observe the emitter
            podXEventEmitter.podXWebEventLiveData.observeForever {
                observedWebEvents = it
            }

            podXEventEmitter.podXImageEventLiveData.observeForever {
                observedImageEvents = it
            }

            // switch to the background with the media session
            mediaServiceTestScope.launch {
                // wait until the connection to the media browser is done
                while (connection.isConnected.value != true) {}

                connection.transportControls.playFromMediaId(
                    InstrumentationMockedFeedDataSources.testFeedItem1.feedItemAudioUrl,
                    null
                )

                podXEventEmitter.registerCurrentFeedItem(InstrumentationMockedFeedDataSources.testFeedItem1)

                // until the play command is registered the player is still in its initial state
                while (connection.playbackState.value?.state == PlaybackState.STATE_NONE) {}
                while (connection.playbackState.value?.state == PlaybackState.STATE_BUFFERING) {}

                //seek to a position in playback which has valid podXevents
                connection.transportControls.seekTo(8000)

                //todo: verify that the observed updates will be complete by the time this happens
                while (connection.playbackState.value?.position != 8000L) {}

                assert(observedImageEvents.containsAll(
                    InstrumentationMockedEventDataSources.testFeedItem1ImageList
                ))

                assert(observedWebEvents.containsAll(
                    InstrumentationMockedEventDataSources.testFeedItem1WebList
                ))

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

    /**
     * needs to be run on the Main looper or any other prepared looper.
     */
    private fun getPodXEventEmitter(mediaSessionConnection: MediaSessionConnection)
        : PodXEventEmitter {
        return PodXEventEmitterImpl(mediaSessionConnection,
            InstrumentationMockedEventDataSources.podXEventRepository)
    }
}