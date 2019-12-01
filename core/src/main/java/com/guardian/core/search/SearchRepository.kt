package com.guardian.core.search

import io.reactivex.Flowable

interface SearchRepository {
    fun doSearch(term: String): Flowable<List<SearchResult>>
}