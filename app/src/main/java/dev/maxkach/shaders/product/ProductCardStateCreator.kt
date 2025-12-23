package dev.maxkach.shaders.product

import androidx.compose.ui.graphics.Color
import dev.maxkach.shaders.R
import dev.maxkach.shaders.product.ProductCardState.ColorsState
import dev.maxkach.shaders.product.ProductCardState.ColorsState.ColorState
import dev.maxkach.shaders.product.ProductCardState.ImagesState
import dev.maxkach.shaders.product.ProductCardState.ImagesState.ImageState
import kotlinx.collections.immutable.persistentListOf

object ProductCardStateCreator {
    fun create(
        selectedImage: Int,
        selectedColor: Int,
    ): ProductCardState {
        return ProductCardState(
            title = "Cool Cat Snapback",
            description = "Transform your cat into the ultimate street style icon with this premium snapback cap. Featuring an adjustable strap for the perfect fit, breathable mesh panels for comfort, and an embroidered paw logo that screams sophistication. Whether your feline is lounging in the sun or prowling the neighborhood, this snapback adds instant swagger to any adventure. Made with durable materials and designed specifically for cats who refuse to compromise on style. Your cat will be turning heads and earning respect from every pet on the block. Hip-hop culture meets feline fashion in this must-have accessory.",
            images = ImagesState(
                imagesRes = persistentListOf(
                    ImageState(R.drawable.cat_red, false),
                    ImageState(R.drawable.cat_blue, false),
                    ImageState(R.drawable.cat_pink, false),
                    ImageState(R.drawable.cat_diamond, false),
                    ImageState(R.drawable.cat_green, false),
                ),
                currentImage = selectedImage,

            ),
            colors = ColorsState(
                colors = persistentListOf(
                    ColorState("Red", Color(0xFFDC143C), false),
                    ColorState("blue", Color(0xFF001F54), false),
                    ColorState("Pink", Color(0xFFFFC0CB), false),
                    ColorState("Diamond", Color(0xFFAA9FAF), false),
                    ColorState("Green", Color(0xFF228B22), true),
                ),
                currentColor = selectedColor,
            ),
        )
    }
}