package com.core.ui.post

import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.unit.Constraints

enum class PostItemType(val isShowContent: Boolean) {
    LINEAR(false), GRID(true)
}

abstract class PostItem(
    val isPost: Boolean,
    val isFirstItem: Boolean,
    val isLastItem: Boolean
) {
    var maxWidth: Int = 0
    var bottomPadding: Int = 0
    lateinit var day: Placeable
    lateinit var dot: Placeable
    var delBtn: Placeable? = null
    var addBtn: Placeable? = null
    var images: Placeable? = null
    lateinit var tags: List<Placeable>
    var ellipse: Placeable? = null

    abstract val imagesWidth: Int
    abstract val postHeight: Int

    open fun measurePolicy(measureables: List<Measurable>, constraints: Constraints, bottomPadding: Int = 0) {
        maxWidth = constraints.maxWidth
        this.bottomPadding = bottomPadding
        day = measureables.find { it.layoutId == "Day" }!!.measure(constraints)
        delBtn = measureables.find { it.layoutId == "DelBtn" }?.measure(constraints)
        addBtn = measureables.find { it.layoutId == "AddBtn" }?.measure(constraints)
        tags = measureables.filter { it.layoutId == "Tag" }.map { it.measure(constraints) }

        images = measureables.find { it.layoutId == "Images" }?.measure(
            constraints.copy(minWidth = imagesWidth, maxWidth = imagesWidth)
        )
        dot = measureables.find { it.layoutId == "Dot" }!!.measure(
            constraints.copy(minWidth = dotSize, maxWidth = dotSize, minHeight = dotSize, maxHeight = dotSize)
        )
        ellipse = measureables.find { it.layoutId == "Ellipse" }?.measure(constraints)
    }

    val lineX get() = if (isFirstItem) day.height / 2 else 0
    val dotX get() = -dotSize / 2
    val dotY get() = (day.height - dotSize) / 2
    val dayX get() = paddingLineAndDay

    open val imageX get() = dayX + day.width + paddingDayAndImages
    open val imageY = 0
    open val addBtnX get() = maxWidth - (addBtn!!.width)
    open val addBtnY get() = (day.height - addBtn!!.height) / 2
    open val delBtnX get() = maxWidth - delBtn!!.width
    open val delBtnY = 0
    open val tagsY get() = imageY + (images?.height ?: 0) + paddingImagesAndTags
    open val tagsListEndX get() = imageX + imagesWidth

    open fun Placeable.PlacementScope.place() {}
    fun placeExtraComponent(placementScope: Placeable.PlacementScope) {
        placementScope.place()
    }

    companion object {
        const val dotSize: Int = 15
        const val paddingLineAndDay: Int = 20
        const val paddingDayAndImages: Int = 23
        const val paddingImagesAndTags: Int = 12
        const val paddingTags: Int = 4
        const val paddingImagesAndDelBtn = 23
        const val paddingTagsAndContent = 32

        fun providePostItem(
            isPost: Boolean, isFirstItem: Boolean, isLastItem: Boolean,
            postItemType: PostItemType
        ): PostItem {
            return when (postItemType) {
                PostItemType.LINEAR -> LinearPostItem(isPost, isFirstItem, isLastItem)
                PostItemType.GRID -> GridPostItem(isPost, isFirstItem, isLastItem)
            }
        }
    }
}

class LinearPostItem(
    isPost: Boolean,
    isFirstItem: Boolean,
    isLastItem: Boolean
) : PostItem(isPost, isFirstItem, isLastItem) {
    override val imagesWidth: Int
        get() = maxWidth - (dayX + day.width + paddingDayAndImages + (delBtn?.width ?: 0) + paddingImagesAndDelBtn)
    override val postHeight: Int
        get() = bottomPadding + if (isPost) (images?.height ?: 0) + (tags.firstOrNull()?.height ?: 0) else day.height
    override val delBtnY: Int
        get() = ((images?.height ?: 0) - day.height) / 2
}

class GridPostItem(
    isPost: Boolean,
    isFirstItem: Boolean,
    isLastItem: Boolean
) : PostItem(isPost, isFirstItem, isLastItem) {
    var content: Placeable? = null
    private val contentX get() = imageX
    private val contentY get() = tagsY + (tags.firstOrNull()?.height ?: 0) + paddingTagsAndContent

    override val imagesWidth: Int
        get() = maxWidth - (dayX + day.width + paddingDayAndImages)
    override val postHeight: Int
        get() = bottomPadding + day.height + if (isPost) contentY + (content?.height ?: 0) else 0
    override val delBtnY: Int
        get() = (day.height - delBtn!!.height) / 2

    override val imageY: Int
        get() = day.height

    override fun measurePolicy(measureables: List<Measurable>, constraints: Constraints, bottomPadding: Int) {
        super.measurePolicy(measureables, constraints, bottomPadding)
        content = measureables.find { it.layoutId == "Content" }?.measure(
            constraints.copy(maxWidth = imagesWidth)
        )
    }

    override fun Placeable.PlacementScope.place() {
        content?.placeRelative(x = contentX, y = contentY)
    }
}