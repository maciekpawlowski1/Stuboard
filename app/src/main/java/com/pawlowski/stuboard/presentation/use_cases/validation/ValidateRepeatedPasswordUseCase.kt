package com.pawlowski.stuboard.presentation.use_cases.validation

import com.pawlowski.stuboard.R
import com.pawlowski.stuboard.presentation.utils.UiText
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ValidateRepeatedPasswordUseCase @Inject constructor() {
    operator fun invoke(password: String, repeatedPassword: String): ValidationResult
    {
        return if(repeatedPassword.isEmpty())
            ValidationResult(isCorrect = false, errorMessage = UiText.StringResource(R.string.field_is_required))
        else if(repeatedPassword != password)
            ValidationResult(isCorrect = false, errorMessage = UiText.StringResource(R.string.passwords_are_not_the_same))
        else
            ValidationResult(isCorrect = true)
    }
}