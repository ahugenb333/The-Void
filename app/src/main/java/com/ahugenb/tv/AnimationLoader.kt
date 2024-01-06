package com.ahugenb.tv

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition

@Composable
fun AnimationLoader(isAudioPlaying: State<Boolean>) {
    val composition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.void_animation))
    val progress by animateLottieCompositionAsState(
        composition = composition,
        isPlaying = isAudioPlaying.value,
        iterations = if (isAudioPlaying.value) LottieConstants.IterateForever else 1
    )
    LottieAnimation(
        composition = composition,
        progress = { progress }
    )
}