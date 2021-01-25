package dk.claudiub.babbeltest.api_impl

import android.animation.ValueAnimator
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dk.claudiub.babbeltest.api.ErrorCode
import dk.claudiub.babbeltest.api.GameViewModel
import dk.claudiub.babbeltest.api.TranslationsRepository
import dk.claudiub.babbeltest.app.TranslatedItem
import dk.claudiub.babbeltest.core.AsyncResource
import dk.claudiub.babbeltest.core.coroutine.DispatcherProvider
import kotlinx.coroutines.launch
import kotlin.random.Random

class GameViewModelImpl(
    val dispatcherProvider: DispatcherProvider,
    val translationsRepository: TranslationsRepository
) : GameViewModel() {



    companion object {
        const val DURATION_CARD = 5000L
    }

    private var cards = mutableListOf<TranslatedItem>()
    private var startedInternal: Boolean = false
    private var score: Int = 0
    private var cardPosition: Float = 0f
    private var currentCard: TranslatedItem? = null

    /** All system messages tied to this VM come here. set to null after receiving to consume the message
     *
     */
    val messageLiveData = MutableLiveData<String>()
    var startedLiveData = MutableLiveData<Boolean>(startedInternal)
    var scoreLiveData = MutableLiveData<Int>(score)
    var currentCardLiveData = MutableLiveData<AsyncResource<TranslatedItem>>()
    var cardPositionLiveData = MutableLiveData<Float>()

    init {
        start()
    }

    override fun isStarted(): LiveData<Boolean> = startedLiveData

    override fun start() {
        currentCardLiveData.value = AsyncResource.loading()

        updateStarted(true)
        updateScore(0)
        pullCard()
    }

    override fun getCurrentCardPosition(): Float {
        return cardPosition;
    }

    override fun getCurrentCard(): TranslatedItem? = currentCard

    override fun getHighScore(): Int = score

    override fun getHighScoreLiveData(): LiveData<Int> = scoreLiveData

    override fun getCurrentCardPositionLiveData(): LiveData<Float> = cardPositionLiveData

    override fun getCurrentCardLiveData(): LiveData<AsyncResource<TranslatedItem>> = currentCardLiveData

    override fun markAsRight() {
        currentCard?.let { current ->
            if(isTranslationRight(current)) {
                updateScore(score + 1)
            } else {
                updateScore(score - 1)
            }
            pullCard()
        }
    }

    override fun markAsWrong() {
        currentCard?.let { current ->
            if(isTranslationRight(current)) {
                updateScore(score - 1)
            } else {
                updateScore(score + 1)
            }
            pullCard()
        }
    }

    override fun markExpired() {
        currentCard?.let { current ->
            updateScore(score - 1)
            pullCard()
        }
    }

    private fun isTranslationRight(translatedItem: TranslatedItem) : Boolean{
        return cards.firstOrNull {
            it.englishText == translatedItem.englishText &&
                    it.translatedText == translatedItem.translatedText
        } != null
    }

    private fun updateScore(newScore: Int) {
        this.score = newScore
        this.scoreLiveData.value = newScore
    }

    // TODO replace with own impl that doesn't rely on Looper.getMainLooper()
    var animator: ValueAnimator? = null

    private fun pullCard(){
        animator?.cancel()
        viewModelScope.launch(dispatcherProvider.io()) {
            try {

                val items = translationsRepository.getTranslations()
                if (items.isEmpty()) {
                    currentCard = null
                    currentCardLiveData.postValue(AsyncResource.error(ErrorCode.GET_TRANSLATIONS_NO_ITEMS.toErrorData()))
                } else {
                    val englishTextIndex = Random.nextInt(0, items.size)
                    val spanishTextIndex = Random.nextInt(0, items.size)
                    val newTranslation = TranslatedItem(
                        items[englishTextIndex].englishText,
                        items[spanishTextIndex].translatedText
                    )
                    currentCard = newTranslation
                    currentCardLiveData.postValue(AsyncResource.success(newTranslation))
                    Handler(Looper.getMainLooper()).post {
                        animator = ValueAnimator.ofFloat(0f, 1f).apply {
                            addUpdateListener {
                                cardPosition = it.animatedFraction
                                cardPositionLiveData.postValue(cardPosition)
                                if(cardPosition == 1f){
                                    markExpired()
                                }
                            }
                            duration = DURATION_CARD
                            start()
                        }
                    }
                }
            } catch (ex: Exception) {
                currentCardLiveData.postValue(AsyncResource.error(ErrorCode.GET_TRANSLATIONS_REPO_UNKNOWN.toErrorData(ex)))
            }
        }

    }

    private fun updateStarted(started: Boolean) {
        this.startedInternal = started
        this.startedLiveData.value = started
    }


}