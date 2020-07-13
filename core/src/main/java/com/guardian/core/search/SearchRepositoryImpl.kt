package com.guardian.core.search

import com.guardian.core.library.subscribeOnIoObserveOnMain
import com.guardian.core.search.api.itunes.ItunesSearchApi
import com.guardian.core.search.api.spoofedtestfeed.SpoofedTestFeedApi
import io.reactivex.Flowable
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class SearchRepositoryImpl
@Inject
constructor(
    private val itunesSearchApi: ItunesSearchApi,
    private val spoofedTestFeedApi: SpoofedTestFeedApi
) :
    SearchRepository {

    override fun doSearch(term: String): Flowable<List<SearchResult>> {

        //todo proper itunes search integration
        // return itunesSearchApi.search(term)
        //     .map { searchResultSetApiObject: SearchResultSetApiObject ->
        //         spoofedTestFeedApi.search() + searchResultSetApiObject.results
        //             .filter {
        //             it.feedUrlString != null && it.feedUrlString.isNotEmpty()
        //             // todo filter invalid feed urls out
        //         }.map {
        //             SearchResult(
        //                 it.name ?: "",
        //                 it.atworkUrl600String ?: "",
        //                 it.feedUrlString ?: ""
        //             )
        //         }
        //     }.toFlowable()
        //     .subscribeOnIoObserveOnMain()
        return Flowable.just(spoofedTestFeedApi.search())
            .subscribeOnIoObserveOnMain()
    }
}