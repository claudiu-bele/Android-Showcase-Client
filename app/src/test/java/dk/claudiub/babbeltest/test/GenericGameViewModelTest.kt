package dk.claudiub.babbeltest.test

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.gson.Gson
import dk.claudiub.babbeltest.TestTranslationsUseCase
import dk.claudiub.babbeltest.api.GameViewModel
import dk.claudiub.babbeltest.api.TranslationsRepository
import dk.claudiub.babbeltest.api.TranslationsUseCase
import dk.claudiub.babbeltest.api_impl.GameViewModelImpl
import dk.claudiub.babbeltest.api_impl.TranslationsRepositoryImpl
import dk.claudiub.babbeltest.coroutine.DispatcherRule
import dk.claudiub.babbeltest.coroutine.TestingDispatcherProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import kotlin.test.assertTrue

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class GenericGameViewModelTest {
    private val dispatcherProvider = TestingDispatcherProvider()

    @get:Rule
    val dispatcherRule = DispatcherRule(dispatcherProvider)

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    lateinit var vm: GameViewModel

    @Before
    fun setupVm() {
        // given fresh VM
        val gson = Gson()
        val useCase: TranslationsUseCase = TestTranslationsUseCase(gson, dispatcherProvider)
        val repo: TranslationsRepository = TranslationsRepositoryImpl(useCase)
        vm = GameViewModelImpl(dispatcherProvider, repo)
        vm.start()
    }

    // region started vm
    @Test
    fun `vm initialised`() = dispatcherProvider.ui().runBlockingTest {
        // Given
        vm

        // When
        val isInitialised = ::vm.isInitialized

        // Then
        assertTrue { isInitialised }
    }

    @Test
    fun `vm started`() = dispatcherProvider.ui().runBlockingTest {
        // Given
        vm

        // Then
        assertTrue { vm.isStarted().value == true }
    }

    @Test
    fun `vm has card`() = dispatcherProvider.ui().runBlockingTest {
        // Given
        vm

        // Then
        assertTrue { vm.getCurrentCard() != null }
    }
    // endregion

    // region right answer
    @Test
    fun `vm right answer score change`() = dispatcherProvider.ui().runBlockingTest {
        // Given
        vm

        // When
        val oldScore = vm.getHighScore()
        vm.markAsRight()
        val newScore = vm.getHighScore()

        // Then
        assertTrue { newScore != oldScore }
    }

    @Test
    fun `vm right answer card change`() = dispatcherProvider.ui().runBlockingTest {
        // Given
        vm

        // When
        val oldCard = vm.getCurrentCard()
        vm.markAsRight()
        val newCard = vm.getCurrentCard()

        // Then
        assertTrue { oldCard != newCard }
    }
    // endregion

    // region wrong answer
    @Test
    fun `vm wrong answer score change`() = dispatcherProvider.ui().runBlockingTest {
        // Given
        vm

        // When
        val score = vm.getHighScore()
        vm.markAsWrong()
        val newScore = vm.getHighScore()

        // Then
        assertTrue { score != newScore }
    }

    @Test
    fun `vm wrong answer card change`() = dispatcherProvider.ui().runBlockingTest {
        // Given
        vm

        // When
        val oldCard = vm.getCurrentCard()
        vm.markAsWrong()
        val newCard = vm.getCurrentCard()

        // Then
        assertTrue { oldCard != newCard }
    }
    // endregion

    // region expired answer
    @Test
    fun `vm expired decreases score by 1`() = dispatcherProvider.ui().runBlockingTest {
        // Given
        vm

        // When
        val score = vm.getHighScore()
        vm.markExpired()
        val newScore = vm.getHighScore()

        // Then
        assertTrue { score == newScore + 1 }
    }

    @Test
    fun `vm expired answer card change`() = dispatcherProvider.ui().runBlockingTest {
        // Given
        vm

        // When
        val oldCard = vm.getCurrentCard()
        vm.markExpired()
        val newCard = vm.getCurrentCard()

        // Then
        assertTrue { oldCard != newCard }
    }
    // endregion
}