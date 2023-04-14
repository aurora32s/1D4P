package com.core.designsystem.modifiers

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.PagerState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import kotlin.math.absoluteValue

@OptIn(ExperimentalFoundationApi::class)
fun Modifier.pagerHingeTransition(page: Int, pagerState: PagerState) = graphicsLayer {
    val pageOffset = pagerState.calculateCurrentOffsetForPage(page)

    translationX = pageOffset * size.width
    translationY = pageOffset * size.height
    transformOrigin = TransformOrigin(0f, 0f)

    when {
        pageOffset < -1f -> alpha = 0f
        pageOffset in -1f..1f -> alpha = 1 - pageOffset.absoluteValue
        else -> alpha = 0f
    }
}

@OptIn(ExperimentalFoundationApi::class)
private fun PagerState.calculateCurrentOffsetForPage(page: Int): Float {
    return (currentPage - page) + currentPageOffsetFraction
}