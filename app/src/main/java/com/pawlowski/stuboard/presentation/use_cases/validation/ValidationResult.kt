package com.pawlowski.stuboard.presentation.use_cases.validation

import com.pawlowski.stuboard.presentation.utils.UiText

data class ValidationResult(
    val isCorrect: Boolean,
    val errorMessage: UiText? = null,
)
