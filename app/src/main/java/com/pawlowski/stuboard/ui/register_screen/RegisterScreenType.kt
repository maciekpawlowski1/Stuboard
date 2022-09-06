package com.pawlowski.stuboard.ui.register_screen

enum class RegisterScreenType(val progress: Float, val progressText: String) {
    FIRST_BOTH(0.33f, "1 of 3"),
    SECOND_NORMAL(0.66f, "2 of 3"),
    SECOND_ORGANISATION(0.66f, "2 of 3"),
    THIRD_NORMAL(1f, "3 of 3"),
    THIRD_ORGANISATION(1f, "3 of 3")
}