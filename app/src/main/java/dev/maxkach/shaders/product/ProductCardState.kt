package dev.maxkach.shaders.product

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import kotlinx.collections.immutable.ImmutableList

@Immutable
data class ProductCardState(
    val title: String,
    val description: String,
    val images: ImagesState,
    val colors: ColorsState,
) {
    data class ImagesState(
        val imagesRes: ImmutableList<ImageState>,
        val currentImage: Int = 0,
    ) {
        data class ImageState(
            val imageRes: Int,
            val outOfStock: Boolean,
        )
    }

    data class ColorsState(
        val colors: ImmutableList<ColorState>,
        val currentColor: Int = 0,
    ) {
        data class ColorState(
            val colorName: String,
            val color: Color,
            val outOfStock: Boolean,
        )
    }
}
