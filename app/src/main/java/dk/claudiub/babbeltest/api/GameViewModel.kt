package dk.claudiub.babbeltest.api

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import dk.claudiub.babbeltest.app.TranslatedItem
import dk.claudiub.babbeltest.core.AsyncResource

abstract class GameViewModel : ViewModel() {

    // region data
    /** When app is started
     */
    abstract fun isStarted(): LiveData<Boolean>

    /** Current card position. Managed from 0 to 1, when reaching one markExpired() will be called
     */
    abstract fun getCurrentCardPosition() : Float

    abstract fun getCurrentCard() : TranslatedItem?

    abstract fun getHighScore() : Int

    abstract fun getHighScoreLiveData(): LiveData<Int>

    abstract fun getCurrentCardPositionLiveData(): LiveData<Float>

    abstract fun getCurrentCardLiveData(): LiveData<AsyncResource<TranslatedItem>>
    // endregion

    /** Also restarts, returns the first card.
     */
    abstract fun start()

    // region actions
    /** Selected right translation
     *
     */
    abstract fun markAsRight()

    /** Selected wrong translation
     *
     */
    abstract fun markAsWrong()

    /** For when the card expires on the screen
     *
     */
    abstract fun markExpired()
    // endregion

}