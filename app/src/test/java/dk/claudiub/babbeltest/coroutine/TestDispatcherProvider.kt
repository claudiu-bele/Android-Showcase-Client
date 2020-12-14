package dk.claudiub.babbeltest.coroutine

import dk.claudiub.babbeltest.core.coroutine.DispatcherProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher

/** move all dispatching to one instance of [TestCoroutineDispatcher], de facto run blocking
 *
 */
@ExperimentalCoroutinesApi
class TestingDispatcherProvider : DispatcherProvider {
    private var dispatcher = TestCoroutineDispatcher()

    override fun computation() = dispatcher
    override fun ui() = dispatcher
    override fun io() = dispatcher

    override fun tearDown() {
        super.tearDown()
        dispatcher.cleanupTestCoroutines()
    }

}
