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
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import kotlin.test.assertTrue

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class ArchTest {
    private val dispatcherProvider = TestingDispatcherProvider()

    @get:Rule
    val dispatcherRule = DispatcherRule(dispatcherProvider)

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Test
    fun `get translations use case not empty `() = dispatcherProvider.ui().runBlockingTest {
        // Given
        val gson = Gson()
        val useCase: TranslationsUseCase = TestTranslationsUseCase(gson, dispatcherProvider)

        // When
        val data = useCase.getTranslations()

        // Then
        assertTrue { data.isNotEmpty() }
    }

    @Test
    fun `get translations repo not empty `() = dispatcherProvider.ui().runBlockingTest {
        // Given
        val gson = Gson()
        val useCase: TranslationsUseCase = TestTranslationsUseCase(gson, dispatcherProvider)
        val repo: TranslationsRepository = TranslationsRepositoryImpl(useCase)

        // When
        val data = repo.getTranslations()

        // Then
        assertTrue { data.isNotEmpty() }
    }

    @Test
    fun `start game in VM and check first draw`() = dispatcherProvider.ui().runBlockingTest {
        // Given
        val gson = Gson()
        val useCase: TranslationsUseCase = TestTranslationsUseCase(gson, dispatcherProvider)
        val repo: TranslationsRepository = TranslationsRepositoryImpl(useCase)
        val vm: GameViewModel = GameViewModelImpl(dispatcherProvider, repo)


        // When
        vm.start()

        // Then
        assertTrue { vm.getCurrentCard() != null }
    }

}