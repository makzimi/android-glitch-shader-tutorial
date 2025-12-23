package dev.maxkach.shaders

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import dev.maxkach.shaders.samples.GlitchStep1Screen
import dev.maxkach.shaders.samples.GlitchStep2Screen
import dev.maxkach.shaders.samples.GlitchStep3Screen
import dev.maxkach.shaders.samples.GlitchStep4Screen
import dev.maxkach.shaders.samples.GlitchStep5Screen
import dev.maxkach.shaders.samples.MovingGradientSampleScreen
import dev.maxkach.shaders.samples.RedTintSampleScreen
import dev.maxkach.shaders.ui.theme.ShadersTheme

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ShadersTheme {
                Surface {
                    ShaderSamplesApp()
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun ShaderSamplesApp() {
    var currentScreen by remember { mutableStateOf<ShaderScreen>(ShaderScreen.MainMenu) }

    // Handle system back button
    BackHandler(enabled = currentScreen != ShaderScreen.MainMenu) {
        currentScreen = ShaderScreen.MainMenu
    }

    when (currentScreen) {
        ShaderScreen.MainMenu -> {
            MainMenuScreen(
                onNavigate = { screen -> currentScreen = screen },
                modifier = Modifier.fillMaxSize()
            )
        }
        ShaderScreen.RedTintSample -> {
            RedTintSampleScreen(
                onBackPressed = { currentScreen = ShaderScreen.MainMenu },
                modifier = Modifier.fillMaxSize()
            )
        }
        ShaderScreen.MovingGradientSample -> {
            MovingGradientSampleScreen(
                onBackPressed = { currentScreen = ShaderScreen.MainMenu },
                modifier = Modifier.fillMaxSize()
            )
        }
        ShaderScreen.GlitchStep1 -> {
            GlitchStep1Screen(
                onBackPressed = { currentScreen = ShaderScreen.MainMenu },
                modifier = Modifier.fillMaxSize()
            )
        }
        ShaderScreen.GlitchStep2 -> {
            GlitchStep2Screen(
                onBackPressed = { currentScreen = ShaderScreen.MainMenu },
                modifier = Modifier.fillMaxSize()
            )
        }
        ShaderScreen.GlitchStep3 -> {
            GlitchStep3Screen(
                onBackPressed = { currentScreen = ShaderScreen.MainMenu },
                modifier = Modifier.fillMaxSize()
            )
        }
        ShaderScreen.GlitchStep4 -> {
            GlitchStep4Screen(
                onBackPressed = { currentScreen = ShaderScreen.MainMenu },
                modifier = Modifier.fillMaxSize()
            )
        }
        ShaderScreen.GlitchStep5 -> {
            GlitchStep5Screen(
                onBackPressed = { currentScreen = ShaderScreen.MainMenu },
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

