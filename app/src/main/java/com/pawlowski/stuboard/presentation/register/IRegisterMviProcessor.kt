package com.pawlowski.stuboard.presentation.register

import com.pawlowski.stuboard.presentation.mvi_abstract.MviProcessor

abstract class IRegisterMviProcessor: MviProcessor<
        RegisterUiState,
        RegisterIntent,
        RegisterSingleEvent>()