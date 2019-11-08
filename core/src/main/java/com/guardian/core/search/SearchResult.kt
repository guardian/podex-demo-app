package com.guardian.core.search

/**
 * The data class that represents the result from an api that returns information about a podcast
 * feed.
 */

data class SearchResult (
    val title : String,
    val imageUrlString : String,
    val feedUrlString : String
)