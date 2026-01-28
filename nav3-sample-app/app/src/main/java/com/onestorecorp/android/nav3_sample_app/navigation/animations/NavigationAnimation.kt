package com.onestorecorp.android.nav3_sample_app.navigation.animations

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.navigation3.scene.Scene
import androidx.navigation3.ui.NavDisplay.popTransitionSpec
import androidx.navigation3.ui.NavDisplay.predictivePopTransitionSpec
import androidx.navigation3.ui.NavDisplay.transitionSpec
import androidx.navigationevent.NavigationEvent

private const val defaultDurationMils = 300

fun <T : Any> fadeAnimatedTransform():
        AnimatedContentTransitionScope<Scene<T>>.() -> ContentTransform = {
    ContentTransform(
        targetContentEnter = fadeIn(
            animationSpec = tween(
                durationMillis = 300,
                easing = FastOutSlowInEasing
            )
        ),
        initialContentExit = fadeOut(
            animationSpec = tween(
                durationMillis = defaultDurationMils,
                easing = FastOutSlowInEasing
            )
        ),
    )
}

fun <T : Any> predictFadeAnimatedTransform():
        AnimatedContentTransitionScope<Scene<T>>.(@NavigationEvent.SwipeEdge Int) -> ContentTransform = {
    ContentTransform(
        targetContentEnter = fadeIn(
            animationSpec = tween(
                durationMillis = defaultDurationMils,
                easing = FastOutSlowInEasing
            )
        ),
        initialContentExit = fadeOut(
            animationSpec = tween(
                durationMillis = defaultDurationMils,
                easing = FastOutSlowInEasing
            )
        ),
    )
}

fun horizontalSlideTransition(
    durationMillis: Int = defaultDurationMils
): Map<String, Any> {
    return transitionSpec {
        // Slide new content up, keeping the old content in place underneath
        slideInHorizontally(
            initialOffsetX = { it },
            animationSpec = tween(durationMillis)
        ) togetherWith ExitTransition.KeepUntilTransitionsFinished
    } + popTransitionSpec {
        // Slide old content down, revealing the new content in place underneath
        EnterTransition.None togetherWith
                slideOutHorizontally(
                    targetOffsetX = { -it },
                    animationSpec = tween(durationMillis)
                )
    } + predictivePopTransitionSpec {
        // Slide old content down, revealing the new content in place underneath
        EnterTransition.None togetherWith
                slideOutHorizontally(
                    targetOffsetX = { it },
                    animationSpec = tween(durationMillis)
                )
    }
}