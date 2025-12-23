package dev.maxkach.shaders.product

import android.graphics.RuntimeShader
import androidx.compose.animation.core.withInfiniteAnimationFrameMillis
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ShaderBrush

private const val GRADIENT_SRC = """
uniform float2 resolution;
uniform float  time;
uniform float4 color1;
uniform float4 color2;

vec4 main(vec2 fragCoord) {
    vec2 uv = fragCoord / resolution;
    float wave = sin(time * 0.8 + uv.x * 4.0);
    float t = 0.5 + 0.5 * wave;
    return mix(color1, color2, t);
}
"""

@Composable
fun MovingGradient(
    modifier: Modifier = Modifier,
    startColor: Color,
    endColor: Color,
    speed: Float = 1f,
) {
    val shader = remember { RuntimeShader(GRADIENT_SRC) }
    val brush = remember { ShaderBrush(shader) }

    val time by produceState(0f, speed) {
        while (true) {
            withInfiniteAnimationFrameMillis { frameTimeMillis ->
                value = frameTimeMillis / 1000f * speed
            }
        }
    }

    Spacer(
        modifier = modifier
            .fillMaxSize(0.8f)
            .clip(MaterialTheme.shapes.large)
            .drawWithCache {
                shader.setFloatUniform("resolution", size.width, size.height)
                shader.setFloatUniform("time", time)
                shader.setFloatUniform(
                    "color1",
                    startColor.red,
                    startColor.green,
                    startColor.blue,
                    startColor.alpha
                )
                shader.setFloatUniform(
                    "color2",
                    endColor.red,
                    endColor.green,
                    endColor.blue,
                    endColor.alpha
                )

                onDrawBehind {
                    drawCircle(brush)
                }
            },
    )
}
