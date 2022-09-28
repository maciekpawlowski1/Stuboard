package com.pawlowski.stuboard.presentation.use_cases

fun interface DeleteManyEditingEventsUseCase: suspend (List<Int>) -> Unit