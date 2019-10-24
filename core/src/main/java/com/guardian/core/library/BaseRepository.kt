package com.guardian.core.library

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
