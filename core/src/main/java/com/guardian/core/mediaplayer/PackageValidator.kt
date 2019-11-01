package com.guardian.core.mediaplayer

import androidx.media.MediaBrowserServiceCompat

/**
 * An abstraction for the package validator
 */
interface PackageValidator {
    /**
     * Checks whether the caller attempting to connect to a [MediaBrowserServiceCompat] is known.
     * See [MediaService.onGetRoot] for where this is utilized.
     *
     * @param callingPackage The package name of the caller.
     * @param callingUid The user id of the caller.
     * @return `true` if the caller is known, `false` otherwise.
     */
    fun isKnownCaller(callingPackage: String, callingUid: Int): Boolean
}