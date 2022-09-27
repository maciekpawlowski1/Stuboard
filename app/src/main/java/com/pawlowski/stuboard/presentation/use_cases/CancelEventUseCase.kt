package com.pawlowski.stuboard.presentation.use_cases

import com.pawlowski.stuboard.domain.models.Resource

fun interface CancelEventUseCase: suspend (Int) -> Resource<Unit>