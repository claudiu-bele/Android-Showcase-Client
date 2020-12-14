package dk.claudiub.babbeltest.api_impl

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dk.claudiub.babbeltest.api.TranslationsUseCase
import dk.claudiub.babbeltest.app.TranslatedItem
import dk.claudiub.babbeltest.core.coroutine.DispatcherProvider
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.lang.reflect.Type
import java.nio.charset.Charset

class GsonTranslationsUseCase(val context: Context, val gson: Gson, val dispatcherProvider: DispatcherProvider) :
    TranslationsUseCase {

    override suspend fun getTranslations(): List<TranslatedItem> =
        withContext(dispatcherProvider.io()) {
            val REVIEW_TYPE: Type = object : TypeToken<List<TranslatedItem>>() {}.getType()
            val data: List<TranslatedItem> =
                gson.fromJson(getJsonFromAssets(context, "words_v2.json"), REVIEW_TYPE) // contains the whole reviews list
            return@withContext data
        }

    companion object {
        fun getJsonFromAssets(context: Context, fileName: String): String? {
            val jsonString: String
            val `is`: InputStream = context.getAssets().open(fileName)
            val size: Int = `is`.available()
            val buffer = ByteArray(size)
            `is`.read(buffer)
            `is`.close()
            return String(buffer, Charset.forName("UTF-8"))
        }
    }


}