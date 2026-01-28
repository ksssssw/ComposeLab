package com.onestorecorp.android.nav3_sample_app.navigation.strategy

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.scene.Scene
import androidx.navigation3.scene.SceneStrategy
import androidx.navigation3.scene.SceneStrategyScope

class BottomNavScene<T : Any>(
    private val originalScene: Scene<T>,
    private val bottomBarContent: @Composable () -> Unit,
) : Scene<T> {
    override val key: Any = originalScene.key
    override val entries: List<NavEntry<T>> = originalScene.entries
    override val previousEntries: List<NavEntry<T>> = originalScene.previousEntries

    override val content: @Composable (() -> Unit) = {
        Column(modifier = Modifier.Companion.fillMaxSize()) {
            // 원래 Scene 내용
            Box(modifier = Modifier.Companion.weight(1f)) {
                originalScene.content()
            }
            // Bottom Navigation
            bottomBarContent()
        }
    }

    companion object {
        internal const val NAV_BAR = "show_bottom_nav"

        /**
         * [Scene]에 [BottomNavScene]표시해야 함을 나타내는 메타데이터를 추가하는 헬퍼 함수
         */
        fun showNavBar() = mapOf(NAV_BAR to true)
    }
}

// 2. BottomNav를 보여줄지 결정하는 SceneStrategy
class BottomNavSceneStrategy<T : Any>(
    private val bottomBarContent: @Composable () -> Unit,
) : SceneStrategy<T> {

    override fun SceneStrategyScope<T>.calculateScene(entries: List<NavEntry<T>>): Scene<T>? {
        return null // entries 기반 처리는 하지 않음
    }

    override fun SceneStrategyScope<T>.calculateScene(scene: Scene<T>): Scene<T> {
        // Scene의 metadata를 확인해서 BottomNav를 추가할지 결정
        val showBottomNav = scene.metadata.containsKey(BottomNavScene.Companion.NAV_BAR)

        return if (showBottomNav) {
            BottomNavScene(scene, bottomBarContent)
        } else {
            scene
        }
    }
}

// 3. Composable 함수로 만들기
@Composable
fun <T : Any> rememberBottomNavSceneStrategy(
    bottomBarContent: @Composable () -> Unit,
): BottomNavSceneStrategy<T> {
    return remember(bottomBarContent) {
        BottomNavSceneStrategy(bottomBarContent)
    }
}