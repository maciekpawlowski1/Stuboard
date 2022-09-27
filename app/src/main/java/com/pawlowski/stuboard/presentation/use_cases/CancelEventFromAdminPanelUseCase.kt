package com.pawlowski.stuboard.presentation.use_cases

import com.pawlowski.stuboard.domain.models.Resource

fun interface CancelEventFromAdminPanelUseCase: suspend (String) -> Resource<Unit>