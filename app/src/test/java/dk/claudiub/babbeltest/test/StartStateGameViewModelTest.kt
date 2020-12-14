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
class StartStateGameViewModelTest {
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
        vm.markAsRight()
    }

    @Test
    fun `vm current answer is tune with expectations`() = dispatcherProvider.ui().runBlockingTest {
        // Given
        vm

        // Then
        assertTrue { vm.getHighScore() != 0 }
    }

    @Test
    fun `vm start restarts score`() = dispatcherProvider.ui().runBlockingTest {
        // Given
        vm

        // When
        vm.start()

        // Then
        assertTrue { vm.getHighScore() == 0 }
        vm.markAsRight()
    }
}