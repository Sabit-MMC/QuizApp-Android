package com.example.quizapp.presentation.quiz.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.quizapp.tools.SoundPlayerHelper

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnimatedTimerBar(
    timeLeft: Int,
    totalTime: Int = 15
) {
    val context = LocalContext.current
    val soundPlayer = remember { SoundPlayerHelper(context) }
    val animatedProgress by animateFloatAsState(
        targetValue = timeLeft / totalTime.toFloat(),
        animationSpec = tween(durationMillis = 500),
        label = "progress_animation"
    )

    LaunchedEffect(timeLeft) {
        if (timeLeft == 15) {
            soundPlayer.playSound("15_seconds_start")
        } else if (timeLeft == 5) {
            soundPlayer.playSound("5_seconds_left")
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            soundPlayer.release()
        }
    }

    val barColor = when {
        timeLeft > 10 -> MaterialTheme.colorScheme.primary
        timeLeft > 5 -> MaterialTheme.colorScheme.tertiary
        else -> MaterialTheme.colorScheme.error
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Vaxt qaldı: $timeLeft saniyə",
                        style = MaterialTheme.typography.labelLarge,
                        color = barColor
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            LinearProgressIndicator(
                progress = { animatedProgress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp)
                    .clip(RoundedCornerShape(20.dp)),
                color = barColor,
                trackColor = MaterialTheme.colorScheme.surface
            )
        }
    }
}

