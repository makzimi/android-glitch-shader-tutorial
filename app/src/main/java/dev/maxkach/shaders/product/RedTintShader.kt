package dev.maxkach.shaders.product

import android.graphics.RenderEffect
import android.graphics.RuntimeShader
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asComposeRenderEffect
import androidx.compose.ui.graphics.graphicsLayer

@Composable
fun Modifier.redTintShader(
    intensity: () -> Float
): Modifier {
    val shader = remember { RuntimeShader(RED_TINT) }
    return graphicsLayer {
        shader.setFloatUniform("intensity", intensity())
        renderEffect = RenderEffect
            .createRuntimeShaderEffect(shader, "image")
            .asComposeRenderEffect()
    }
}

private const val RED_TINT = """
uniform shader image;
uniform float intensity;

half4 main(float2 coord) {
    half4 color = image.eval(coord);
    half4 red   = half4(1.0, 0.0, 0.0, 1.0);
    
    float t = clamp(intensity, 0.0, 1.0);
    
    return mix(color, red, t);
}
"""