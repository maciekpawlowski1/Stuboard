package com.pawlowski.stuboard.presentation.use_cases.validation

import com.pawlowski.stuboard.R
import com.pawlowski.stuboard.presentation.utils.UiText
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ValidateNameOrSurnameUseCase @Inject constructor() {
    operator fun invoke(nameOrSurname: String): ValidationResult
    {
        return if(nameOrSurname.isEmpty())
            ValidationResult(isCorrect = false, errorMessage = UiText.StringResource(R.string.field_is_required))
        else if(nameOrSurname.length < MIN_NAME_OR_SURNAME_LENGTH)
            ValidationResult(isCorrect = false, errorMessage = UiText.StringResource(R.string.input_to_short))
        else
            ValidationResult(isCorrect = true)
    }

    companion object {
        const val MIN_NAME_OR_SURNAME_LENGTH = 2
    }
}