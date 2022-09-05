package com.pawlowski.stuboard.presentation.login

import com.pawlowski.stuboard.presentation.mvi_abstract.MviProcessor

abstract class ILoginMviProcessor: MviProcessor<
        LoginUiState,
        LoginIntent,
        LoginSingleEvent
        >()