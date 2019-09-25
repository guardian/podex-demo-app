package com.guardian.core.search


interface SearchRepository {
    suspend fun doSearch(term: String): List<SearchResult>
}