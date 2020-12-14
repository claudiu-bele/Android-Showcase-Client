package dk.claudiub.babbeltest.api

import androidx.lifecycle.LiveData
import dk.claudiub.babbeltest.app.TranslatedItem
import dk.claudiub.babbeltest.core.AsyncResource

interface GameViewModel {

    // region data
    /** When app is started
     */
    fun isStarted(): LiveData<Boolean>

    /** Current card position. Managed from 0 to 1, when reaching one markExpired() will be called
     */
    fun getCurrentCardPosition() : Float

    fun getCurrentCard() : TranslatedItem?

    fun getHighScore() : Int

    fun getHighScoreLiveData(): LiveData<Int>

    fun getCurrentCardPositionLiveData(): LiveData<Float>

    fun getCurrentCardLiveData(): LiveData<AsyncResource<TranslatedItem>>
    // endregion

    /** Also restarts, returns the first card.
     */
    fun start()

    // region actions
    /** Selected right translation
     *
     */
    fun markAsRight()

    /** Selected wrong translation
     *
     */
    fun markAsWrong()

    /** For when the card expires on the screen
     *
     */
    fun markExpired()
    // endregion

}