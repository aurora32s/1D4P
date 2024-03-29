package com.core.ui.toolbar

import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.mapSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp

@Stable
interface ToolbarState {
    val height: Float
    val progress: Float
    val consumed: Float
    var scrollTopLimitReached: Boolean
    var scrollOffset: Float
}

abstract class ScrollFlagState(heightRange: IntRange) : ToolbarState {
    init {
        require(heightRange.first >= 0 && heightRange.last >= heightRange.first) {
            "Illegal State 0 <= min height < max height"
        }
    }

    protected val minHeight = heightRange.first
    protected val maxHeight = heightRange.last
    protected val rangeOfDiff = maxHeight - minHeight
    protected var _consumed = 0f
    protected abstract var _scrollOffset: Float

    final override val height: Float
        get() = (maxHeight - scrollOffset).coerceIn(minHeight.toFloat(), maxHeight.toFloat())

    final override val progress: Float
        get() = 1 - (maxHeight - height) / rangeOfDiff

    final override val consumed: Float
        get() = _consumed

    final override var scrollTopLimitReached = true
}

/**
 * Scroll 시 App bar 가 상단에 고정되는 Collapsed Toolbar 의 State
 */
class ExitUnitCollapsedState(
    heightRange: IntRange,
    scrollOffset: Float = 0f
) : ScrollFlagState(heightRange) {
    override var _scrollOffset by mutableStateOf(
        value = scrollOffset.coerceIn(0f, rangeOfDiff.toFloat()),
        policy = structuralEqualityPolicy()
    )

    /**
     * Scroll 된 정도
     * 즉, Toolbar 의 줄어든 길이
     * 때문에 height = maxHeight - scrollOffset 입니다.
     */
    override var scrollOffset: Float
        get() = _scrollOffset
        set(value) {
            if (scrollTopLimitReached) {
                val oldOffset = _scrollOffset
                _scrollOffset = value.coerceIn(0f, rangeOfDiff.toFloat())
                _consumed = oldOffset - _scrollOffset
            } else {
                _consumed = 0f
            }
        }

    companion object {
        private const val MIN_HEIGHT = "MIN_HEIGHT"
        private const val MAX_HEIGHT = "MAX_HEIGHT"
        private const val SCROLL_OFFSET = "SCROLL_OFFSET"

        val Saver: Saver<ExitUnitCollapsedState, *> = mapSaver(
            save = {
                mapOf(
                    MIN_HEIGHT to it.minHeight,
                    MAX_HEIGHT to it.maxHeight,
                    SCROLL_OFFSET to it.scrollOffset
                )
            },
            restore = {
                ExitUnitCollapsedState(
                    heightRange = (it[MIN_HEIGHT] as Int)..(it[MAX_HEIGHT] as Int),
                    scrollOffset = it[SCROLL_OFFSET] as Float
                )
            }
        )
    }
}

@Composable
fun rememberToolbarState(toolbarHeightRange: IntRange): ToolbarState {
    return rememberSaveable(saver = ExitUnitCollapsedState.Saver) {
        ExitUnitCollapsedState(toolbarHeightRange)
    }
}