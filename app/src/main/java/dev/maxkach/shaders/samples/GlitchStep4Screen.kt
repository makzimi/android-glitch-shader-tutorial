package dev.maxkach.shaders.samples

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import dev.maxkach.shaders.product.GLITCH_STEP_4_SHADER
import dev.maxkach.shaders.product.ProductCardStateCreator

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun GlitchStep4Screen(
    onBackPressed: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedImage by remember { mutableIntStateOf(0) }
    var selectedColor by remember { mutableIntStateOf(0) }

    GlitchStepScreen(
        state = ProductCardStateCreator.create(selectedImage, selectedColor),
        stepTitle = "Step 4: Slice Glitch Animated",
        shaderSource = GLITCH_STEP_4_SHADER,
        onColorClicked = { newColor ->
            selectedColor = newColor
            selectedImage = newColor
        },
        onImageChanged = { newImage ->
            selectedImage = newImage
            selectedColor = newImage
        },
        onBackPressed = onBackPressed,
        modifier = modifier,
        hasSlices = true,
        hasFrameDuration = true,
    )
}
