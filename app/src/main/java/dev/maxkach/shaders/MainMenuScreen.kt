package dev.maxkach.shaders

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

sealed class ShaderScreen {
    data object MainMenu : ShaderScreen()
    data object RedTintSample : ShaderScreen()
    data object MovingGradientSample : ShaderScreen()
    data object GlitchStep1 : ShaderScreen()
    data object GlitchStep2 : ShaderScreen()
    data object GlitchStep3 : ShaderScreen()
    data object GlitchStep4 : ShaderScreen()
    data object GlitchStep5 : ShaderScreen()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainMenuScreen(
    onNavigate: (ShaderScreen) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                colors = topAppBarColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                title = {
                    Text(
                        text = "AGSL Shader Samples",
                        style = MaterialTheme.typography.titleLarge
                    )
                },
            )
        },
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                SampleButton(
                    title = "1. Red Tint Shader",
                    onClick = { onNavigate(ShaderScreen.RedTintSample) }
                )

                SampleButton(
                    title = "2. Moving Gradient",
                    onClick = { onNavigate(ShaderScreen.MovingGradientSample) }
                )

                SampleButton(
                    title = "3. Horizontal Wave Static",
                    onClick = { onNavigate(ShaderScreen.GlitchStep1) }
                )

                SampleButton(
                    title = "4. Horizontal Wave Animated",
                    onClick = { onNavigate(ShaderScreen.GlitchStep2) }
                )

                SampleButton(
                    title = "5. Slice Glitch Static",
                    onClick = { onNavigate(ShaderScreen.GlitchStep3) }
                )

                SampleButton(
                    title = "6. Slice Glitch Animated",
                    onClick = { onNavigate(ShaderScreen.GlitchStep4) }
                )

                SampleButton(
                    title = "7. Full RGB Split + Noise",
                    onClick = { onNavigate(ShaderScreen.GlitchStep5) }
                )
            }
        }
    )
}

@Composable
private fun SampleButton(
    title: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(vertical = 8.dp)
        )
    }
}
