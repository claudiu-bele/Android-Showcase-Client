package dk.claudiub.babbeltest.coroutine

import androidx.test.core.app.ActivityScenario
import dk.claudiub.babbeltest.core.coroutine.DispatcherProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.rules.ExternalResource

/** [org.junit.rules.TestRule] for switching the main coroutine dispatcher(s) to custom [kotlinx.coroutines.CoroutineDispatcher]s returned by
 * the [DispatcherProvider] passed as parameter. It is built to allow for blocking dispatchers in particular.
 *
 * @see [TestingDispatcherProvider] the test-related Dispatcher provider, where all Dispatchers point to the same
 * [kotlinx.coroutines.test.TestCoroutineDispatcher]
 *
 * @see [CoroutineTest] for usage
 */
class DispatcherRule(
    val dispatcherProvider: DispatcherProvider
) : ExternalResource() {

    /**
     * @see .ActivityScenarioRule
     * @param activityOptions an activity options bundle to be passed along with the intent to start
     * activity.
     */
    @Throws(Throwable::class)
    override fun before() {
        Dispatchers.setMain(dispatcherProvider.ui())
    }

    override fun after() {
        Dispatchers.resetMain()
        dispatcherProvider.tearDown()
    }

}

/**
 * Constructs an [DispatcherRule] of a given [DispatcherProvider].
 *
 * @param intent an intent to start activity or null to use the default one
 * @param activityOptions an activity options bundle to be passed along with the intent to start
 *        activity
 * @return ActivityScenarioRule which you can use to access to [ActivityScenario] from your tests
 */
fun coroutineDispatcherRule(
    dispatcherProvider: DispatcherProvider
): DispatcherRule = DispatcherRule(dispatcherProvider)
