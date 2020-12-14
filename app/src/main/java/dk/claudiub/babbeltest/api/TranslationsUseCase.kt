package dk.claudiub.babbeltest.api

import dk.claudiub.babbeltest.app.TranslatedItem

interface TranslationsUseCase {
    suspend fun getTranslations(): List<TranslatedItem>
}