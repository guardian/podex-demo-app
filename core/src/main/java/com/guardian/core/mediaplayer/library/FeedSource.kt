package com.guardian.core.mediaplayer.library

import android.content.Context
import android.support.v4.media.MediaMetadataCompat
import androidx.room.EmptyResultSetException
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.guardian.core.R
import com.guardian.core.mediametadata.MediaMetadataRepository
import com.guardian.core.mediaplayer.extensions.albumArt
import com.guardian.core.mediaplayer.extensions.albumArtUri
import com.guardian.core.mediaplayer.extensions.title
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.util.concurrent.ExecutionException
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
                title = "title"
                try {
                    albumArt = glide.asBitmap()
                        .load(artUri.toString())
                        .error(R.drawable.image_placeholder)
                        .placeholder(R.drawable.image_placeholder)
                        .submit()
                        .get()
                } catch (e: ExecutionException) {
                    Timber.e(e.cause)
                }
            }
            .build()
    }
}