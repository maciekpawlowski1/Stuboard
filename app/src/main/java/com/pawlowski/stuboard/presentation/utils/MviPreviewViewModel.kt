package com.pawlowski.stuboard.presentation.utils

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.annotation.OrbitInternal
import org.orbitmvi.orbit.syntax.ContainerContext

@OptIn(OrbitInternal::class)
abstract class MviPreviewViewModel<S : Any, E : Any>: ContainerHost<S, E> {
    abstract fun stateForPreview(): StateFlow<S>

    override val container: Container<S, E> = object : Container<S, E> {
        override val settings: Container.Settings
            get() = TODO("Not yet implemented")
        override val sideEffectFlow: Flow<E>
            get() = TODO("Not yet implemented")
        override val stateFlow: StateFlow<S>
            get() = stateForPreview()

        override suspend fun orbit(orbitIntent: suspend ContainerContext<S, E>.() -> Unit) {
            TODO("Not yet implemented")
        }

    }
}