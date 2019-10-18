package com.guardian.core.mediaplayer.library

import android.content.Context
import android.support.v4.media.MediaMetadataCompat
import com.guardian.core.mediametadata.MediaMetadataRepository
import javax.inject.Inject

class FeedSource
@Inject constructor(private val mediaMetadataRepository: MediaMetadataRepository) :
    AbstractMusicSource() {

    init {
        state = STATE_INITIALIZING
    }

    private lateinit var context: Context

    fun setupGlide(context: Context) {
        this.context = context
    }

    override suspend fun load() {
        // todo don't even really need to load given our architechture
        state = STATE_INITIALIZED
    }

    override fun find(predicate: (MediaMetadataCompat) -> Boolean): MediaMetadataCompat? {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

    override fun filter(predicate: (MediaMetadataCompat) -> Boolean): List<MediaMetadataCompat> {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

    override fun shuffled(): List<MediaMetadataCompat> {
        TODO("remove this")
    }
}
