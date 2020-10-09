package com.guardian.core.dagger

import android.content.Context
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.audio.AudioAttributes
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.guardian.core.R
import com.guardian.core.mediaplayer.PackageValidator
import com.guardian.core.mediaplayer.PackageValidatorImpl
import dagger.Module
import dagger.Provides

@Module
class MediaServiceModule {
    @Provides
    fun providePackageValidator(context: Context): PackageValidator =
        PackageValidatorImpl(context, R.xml.allowed_media_browser_callers)

    // todo get the user agent application name from application
    @Provides
    fun provideDataSourceFactory(context: Context): DataSource.Factory = DefaultDataSourceFactory(
        context, Util.getUserAgent(context, "PodX"), null
    )

    @Provides
    fun provideExoPlayer(context: Context): ExoPlayer {
        /**
         * Configure ExoPlayer to handle audio focus for us.
         * See [Player.AudioComponent.setAudioAttributes] for details.
         */

        val uAmpAudioAttributes = AudioAttributes.Builder()
            .setContentType(C.CONTENT_TYPE_MUSIC)
            .setUsage(C.USAGE_MEDIA)
            .build()

        return ExoPlayerFactory.newSimpleInstance(context).apply {
            setAudioAttributes(uAmpAudioAttributes, true)
            playWhenReady = true
        }
    }
}
