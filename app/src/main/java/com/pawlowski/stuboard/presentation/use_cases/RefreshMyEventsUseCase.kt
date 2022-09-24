package com.pawlowski.stuboard.presentation.use_cases

import com.pawlowski.stuboard.domain.models.Resource

fun interface RefreshMyEventsUseCase: suspend () -> Resource<Unit>