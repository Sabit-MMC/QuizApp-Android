package com.example.quizapp.data.tools

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State

@Stable
interface Lens<CurrentScale> {
    @Composable
    fun <Existence> select(observe: (CurrentScale) -> Existence): State<Existence>

    fun <LowerScale> zoom(focus: (CurrentScale) -> LowerScale): Lens<LowerScale>
}