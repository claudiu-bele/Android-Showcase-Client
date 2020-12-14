package dk.claudiub.babbeltest.app

import com.google.gson.annotations.SerializedName

/** Item from json
 *
 */
data class TranslatedItem(
    @SerializedName("text_eng")
    val englishText: String,
    @SerializedName("text_spa")
    val translatedText: String
) {

}