package dev.maxkach.shaders.samples

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import dev.maxkach.shaders.R
import dev.maxkach.shaders.product.ProductCardState
import dev.maxkach.shaders.product.ProductCardState.ColorsState
import dev.maxkach.shaders.product.ProductCardState.ImagesState
import dev.maxkach.shaders.product.glitchShader
import kotlin.math.absoluteValue

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun GlitchStepScreen(
    state: ProductCardState,
    stepTitle: String,
    shaderSource: String,
    onColorClicked: (Int) -> Unit,
    onImageChanged: (Int) -> Unit,
    onBackPressed: () -> Unit,
    modifier: Modifier = Modifier,
    hasSlices: Boolean = false,
    defaultSlices: Float = 16f,
    hasFrameDuration: Boolean = false,
    defaultFrameDuration: Int = 16,
    hasNoiseIntensity: Boolean = false,
    defaultNoiseIntensity: Float = 1f,
    hasColorBars: Boolean = false,
    defaultColorBarsEnabled: Boolean = false,
    hasRgbSplit: Boolean = false,
    defaultRgbSplitIntensity: Float = 1f,
) {
    var glitchIntensity by remember { mutableFloatStateOf(1f) }
    var slicesCount by remember { mutableFloatStateOf(defaultSlices) }
    var frameDurationMs by remember { mutableIntStateOf(defaultFrameDuration) }
    var noiseIntensity by remember { mutableFloatStateOf(defaultNoiseIntensity) }
    var colorBarsEnabled by remember { mutableStateOf(defaultColorBarsEnabled) }
    var rgbSplitIntensity by remember { mutableFloatStateOf(defaultRgbSplitIntensity) }

    val scrollState = rememberScrollState()

    Scaffold(
        modifier = modifier,
        topBar = { TopBar(stepTitle, onBackPressed) },
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .verticalScroll(scrollState)
                    .padding(
                        top = innerPadding.calculateTopPadding() + 12.dp,
                        bottom = innerPadding.calculateBottomPadding() + 12.dp,
                    ),
            ) {
                Images(
                    images = state.images,
                    modifier = Modifier,
                    onImageChanged = onImageChanged,
                    shaderModifier = Modifier.glitchShader(
                        shaderSource = shaderSource,
                        intensity = glitchIntensity,
                        slices = slicesCount,
                        frameDuration = frameDurationMs,
                        noiseIntensity = noiseIntensity,
                        colorBarsEnabled = colorBarsEnabled,
                        rgbSplitIntensity = rgbSplitIntensity
                    )
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                        .padding(horizontal = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Intensity:",
                            style = MaterialTheme.typography.bodyMedium,
                        )
                        Slider(
                            value = glitchIntensity,
                            onValueChange = { glitchIntensity = it },
                            valueRange = 0f..2f,
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            text = "%.1f".format(glitchIntensity),
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    }

                    if (hasSlices) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "Slices:",
                                style = MaterialTheme.typography.bodyMedium,
                            )
                            Slider(
                                value = slicesCount,
                                onValueChange = { slicesCount = it },
                                valueRange = 1f..100f,
                                modifier = Modifier.weight(1f)
                            )
                            Text(
                                text = "%d".format(slicesCount.toInt()),
                                style = MaterialTheme.typography.bodyMedium,
                            )
                        }
                    }

                    if (hasFrameDuration) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "Frame:",
                                style = MaterialTheme.typography.bodyMedium,
                            )
                            Slider(
                                value = frameDurationMs.toFloat(),
                                onValueChange = { frameDurationMs = it.toInt() },
                                valueRange = 16f..600f,
                                modifier = Modifier.weight(1f)
                            )
                            Text(
                                text = "%dms".format(frameDurationMs),
                                style = MaterialTheme.typography.bodyMedium,
                            )
                        }
                    }

                    if (hasNoiseIntensity) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "Noise:",
                                style = MaterialTheme.typography.bodyMedium,
                            )
                            Slider(
                                value = noiseIntensity,
                                onValueChange = { noiseIntensity = it },
                                valueRange = 0f..10f,
                                modifier = Modifier.weight(1f)
                            )
                            Text(
                                text = "%.1f".format(noiseIntensity),
                                style = MaterialTheme.typography.bodyMedium,
                            )
                        }
                    }

                    if (hasColorBars) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "Color Bars:",
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.weight(1f)
                            )
                            Switch(
                                checked = colorBarsEnabled,
                                onCheckedChange = { colorBarsEnabled = it }
                            )
                        }
                    }

                    if (hasRgbSplit) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "RGB Split:",
                                style = MaterialTheme.typography.bodyMedium,
                            )
                            Slider(
                                value = rgbSplitIntensity,
                                onValueChange = { rgbSplitIntensity = it },
                                valueRange = 0f..5f,
                                modifier = Modifier.weight(1f)
                            )
                            Text(
                                text = "%.1f".format(rgbSplitIntensity),
                                style = MaterialTheme.typography.bodyMedium,
                            )
                        }
                    }
                }

                ColorControls(
                    state = state.colors,
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .padding(horizontal = 20.dp),
                    onColorClicked = onColorClicked,
                )

                AboutProduct(
                    description = state.description,
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .padding(horizontal = 20.dp)
                )
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(
    title: String,
    onBackPressed: () -> Unit,
    modifier: Modifier = Modifier,
) {
    TopAppBar(
        modifier = modifier,
        colors = topAppBarColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
        title = {
            Text(
                text = title,
                modifier = modifier,
                style = MaterialTheme.typography.titleLarge
            )
        },
        navigationIcon = {
            IconButton(onClick = onBackPressed) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_back),
                    contentDescription = "Back"
                )
            }
        }
    )
}

@Composable
private fun AboutProduct(
    description: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = description,
        style = MaterialTheme.typography.bodyLarge,
        modifier = modifier
    )
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun Images(
    images: ImagesState,
    modifier: Modifier = Modifier,
    onImageChanged: (Int) -> Unit = { },
    shaderModifier: Modifier = Modifier
) {
    val pagerState = rememberPagerState(
        initialPage = images.currentImage,
        pageCount = { images.imagesRes.size }
    )

    LaunchedEffect(images.currentImage) {
        if (pagerState.currentPage != images.currentImage) {
            pagerState.animateScrollToPage(images.currentImage)
        }
    }

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }.collect { page ->
            if (page != images.currentImage) {
                onImageChanged(page)
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
    ) {
        HorizontalPager(
            state = pagerState,
            contentPadding = PaddingValues(horizontal = 40.dp),
            pageSpacing = 16.dp,
            modifier = Modifier.fillMaxWidth()
        ) { page ->
            val pageOffset = (pagerState.currentPage - page) + pagerState.currentPageOffsetFraction
            val scale = lerp(
                start = 0.7f,
                stop = 1f,
                fraction = 1f - pageOffset.absoluteValue.coerceIn(0f, 1f)
            )

            val translationX = pageOffset * 100f

            Image(
                painter = painterResource(images.imagesRes[page].imageRes),
                contentDescription = null,
                modifier = Modifier
                    .aspectRatio(1f)
                    .fillMaxWidth()
                    .graphicsLayer {
                        scaleX = scale
                        scaleY = scale
                        this.translationX = translationX
                    }
                    .clip(shape = RoundedCornerShape(20.dp))
                    .then(shaderModifier)
                    .then(
                        if (images.imagesRes[page].outOfStock) {
                            Modifier.alpha(0.5f)
                        } else {
                            Modifier
                        }
                    )
            )
        }

        AnimatedVisibility(
            visible = images.imagesRes[pagerState.currentPage].outOfStock,
            enter = slideInVertically() + fadeIn(),
            exit = slideOutVertically() + fadeOut(),
            modifier = Modifier
                .align(Alignment.TopCenter)
        ) {
            Text(
                text = "Out of stock 😢",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier
                    .padding(20.dp)
                    .graphicsLayer {
                        shadowElevation = 12f
                        shape = RoundedCornerShape(20.dp)
                        clip = true
                    }
                    .background(color = MaterialTheme.colorScheme.errorContainer)
                    .padding(horizontal = 20.dp, vertical = 10.dp)
            )
        }
    }
}

@Composable
private fun ColorControls(
    state: ColorsState,
    modifier: Modifier = Modifier,
    onColorClicked: (Int) -> Unit = { },
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row {
            Text(
                text = "Color: ",
                style = MaterialTheme.typography.titleLarge,
            )
            AnimatedContent(
                targetState = state.colors[state.currentColor].colorName,
                transitionSpec = {
                    fadeIn() togetherWith fadeOut()
                }
            ) { targetState ->
                Text(
                    text = targetState,
                    style = MaterialTheme.typography.titleLarge,
                )
            }
        }
        Row(
            modifier = Modifier,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            state.colors.forEachIndexed { index, colorState ->
                val selected by remember(state.currentColor) {
                    mutableStateOf(index == state.currentColor)
                }

                val sizeAnimation by animateDpAsState(
                    targetValue = if (selected) {
                        30.dp
                    } else {
                        0.dp
                    },
                    animationSpec = tween(300)
                )

                Box(
                    modifier = Modifier.size(30.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(sizeAnimation)
                            .clip(CircleShape)
                            .border(
                                width = 2.dp,
                                color = if (colorState.outOfStock) {
                                    MaterialTheme.colorScheme.error
                                } else {
                                    MaterialTheme.colorScheme.primary
                                },
                                shape = CircleShape,
                            )
                            .align(Center)
                            .clickable { onColorClicked(index) }
                    )

                    Spacer(
                        modifier = Modifier
                            .padding(5.dp)
                            .clip(CircleShape)
                            .background(color = colorState.color)
                            .size(20.dp)
                    )
                }
            }

            AnimatedVisibility(
                visible = state.colors[state.currentColor].outOfStock,
                enter = slideInHorizontally { 40 } + fadeIn(),
                exit = slideOutHorizontally { 40 } + fadeOut(),
                modifier = Modifier
                    .align(CenterVertically)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_error),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error,
                    modifier = Modifier
                )
            }
        }
    }
}
