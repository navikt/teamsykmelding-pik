package no.nav.syfo.util

import java.util.concurrent.Executors.newCachedThreadPool
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Runnable
import kotlinx.coroutines.asCoroutineDispatcher

val Dispatchers.Unbounded
    get() = UnboundedDispatcher.unboundedDispatcher

class UnboundedDispatcher private constructor() : CoroutineDispatcher() {
    companion object {
        val unboundedDispatcher = UnboundedDispatcher()
    }

    private val threadPool = newCachedThreadPool()
    private val dispatcher = threadPool.asCoroutineDispatcher()

    override fun dispatch(context: CoroutineContext, block: Runnable) {
        dispatcher.dispatch(context, block)
    }
}
