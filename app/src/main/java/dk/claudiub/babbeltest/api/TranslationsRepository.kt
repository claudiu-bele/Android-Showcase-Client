package dk.claudiub.babbeltest.api

interface TranslationsRepository {

    fun getTranslationUseCase() : TranslationsUseCase

    suspend fun getTranslations() = getTranslationUseCase().getTranslations()
}