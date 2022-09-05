package com.pawlowski.stuboard.presentation.use_cases.validation

import android.util.Patterns
import com.pawlowski.stuboard.R
import com.pawlowski.stuboard.presentation.utils.UiText
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ValidateEmailUseCase @Inject constructor() {
    operator fun invoke(email: String): ValidationResult
    {
        return if(email.isEmpty())
            ValidationResult(isCorrect = false, errorMessage = UiText.StringResource(R.string.email_cannot_be_empty))
        else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
            ValidationResult(isCorrect = false, errorMessage = UiText.StringResource(R.string.email_is_incorrect))
        else
            ValidationResult(isCorrect = true)

    }
}