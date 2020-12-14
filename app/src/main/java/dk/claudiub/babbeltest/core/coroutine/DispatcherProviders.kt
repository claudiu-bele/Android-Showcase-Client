package dk.claudiub.babbeltest.core.coroutine

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers


interface DispatcherProvider {
    fun io(): CoroutineDispatcher
    fun computation(): CoroutineDispatcher
    fun ui(): CoroutineDispatcher


    /** Called on [org.junit.After], inside custom rules
     *
     */
    fun tearDown() {}
}

class DispatcherProviderImpl : DispatcherProvider {
    override fun computation() = Dispatchers.Default
    override fun ui() = Dispatchers.Main
    override fun io() = Dispatchers.IO

}
