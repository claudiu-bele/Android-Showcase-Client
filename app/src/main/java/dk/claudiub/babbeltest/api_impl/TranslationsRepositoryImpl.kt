package dk.claudiub.babbeltest.api_impl

import dk.claudiub.babbeltest.api.TranslationsRepository
import dk.claudiub.babbeltest.api.TranslationsUseCase

class TranslationsRepositoryImpl(val translationsUseCase: TranslationsUseCase) : TranslationsRepository{

    override fun getTranslationUseCase(): TranslationsUseCase {
        return translationsUseCase
    }
    override suspend fun getTranslations() = getTranslationUseCase().getTranslations()
}