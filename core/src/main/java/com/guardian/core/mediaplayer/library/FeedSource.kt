package com.guardian.core.mediaplayer.library

import android.content.Context
import android.support.v4.media.MediaMetadataCompat
import androidx.room.EmptyResultSetException
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.guardian.core.mediametadata.MediaMetadataRepository
import com.guardian.core.mediaplayer.extensions.albumArt
import com.guardian.core.mediaplayer.extensions.albumArtUri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class FeedSource
@Inject constructor(private val mediaMetadataRepository: MediaMetadataRepository) :
    AbstractMusicSource() {

    init {
        state = STATE_INITIALIZING
    }

    private lateinit var glide: RequestManager
    fun setupGlide(context: Context) {
        this.glide = Glide.with(context)
    }

    override suspend fun load() {
        // todo don't even really need to load given our architechture
        state = STATE_INITIALIZED
    }

    override suspend fun find(predicate: (MediaMetadataCompat) -> Boolean): MediaMetadataCompat? =
        withContext(Dispatchers.IO) {
            try {
                mediaMetadataRepository.getStoredMetadata()
                    .blockingFirst()
                    .firstOrNull(predicate)
            } catch (e: EmptyResultSetException) {
                Timber.e(e)
                null
            }
        }

    override suspend fun findById(id: String): MediaMetadataCompat? =
        withContext(Dispatchers.IO) {
            try {
                mediaMetadataRepository.getMetadataForId(id)
                    .blockingFirst()
                    .addArt()
            } catch (e: EmptyResultSetException) {
                Timber.e(e)
                null
            }
        }

    override suspend fun filter(predicate: (MediaMetadataCompat) -> Boolean): List<MediaMetadataCompat> {
        // todo stub
        return listOf()
    }

    override suspend fun shuffled(): List<MediaMetadataCompat> {
        TODO("remove this")
    }

    private fun MediaMetadataCompat.addArt(): MediaMetadataCompat {
        val artUri = this.albumArtUri

        return MediaMetadataCompat.Builder(this)
            .apply {
                albumArt = glide.asBitmap()
                    .load(artUri)
                    .submit()
                    .get()
            }
            .build()
    }
}