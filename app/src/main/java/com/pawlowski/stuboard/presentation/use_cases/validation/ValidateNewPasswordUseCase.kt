package com.pawlowski.stuboard.presentation.use_cases.validation

import com.pawlowski.stuboard.R
import com.pawlowski.stuboard.presentation.utils.UiText
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ValidateNewPasswordUseCase @Inject constructor() {
    operator fun invoke(password: String): ValidationResult
    {
        return if(password.isEmpty())
            ValidationResult(isCorrect = false, errorMessage = UiText.StringResource(R.string.password_cannot_be_empty))
        else if(password.length < MIN_PASSWORD_LENGTH)
            ValidationResult(isCorrect = false, errorMessage = UiText.StringResource(R.string.password_to_short))
        else if(false)
        {
            //TODO: If it's not strong enough
            ValidationResult(isCorrect = false, errorMessage = UiText.StringResource(R.string.password_is_to_weak))
        }
        else
        {
            ValidationResult(isCorrect = true)
        }
    }

    companion object {
        const val MIN_PASSWORD_LENGTH = 6
    }
}