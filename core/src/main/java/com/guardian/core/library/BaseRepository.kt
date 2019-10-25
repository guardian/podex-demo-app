package com.guardian.core.library

import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

/**
 * A repository that has an associated [CoroutineScope]
 */
abstract class BaseRepository {
    /**
     * [CoroutineScope] tied to this [BaseRepository].
     *
     * This scope is bound to [Dispatchers.IO]
     */
    val BaseRepository.repositoryScope: CoroutineScope by lazy {
        IOBoundCoroutineScope()
    }

    internal class IOBoundCoroutineScope : CoroutineScope {
        override val coroutineContext: CoroutineContext = Dispatchers.IO
    }
}

fun <T> Flowable<T>.subscribeOnIoObserveOnMain(): Flowable<T> {
    return this.subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
}

