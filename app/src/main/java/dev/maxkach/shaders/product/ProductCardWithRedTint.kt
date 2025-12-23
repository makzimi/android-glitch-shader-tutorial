package dev.maxkach.shaders.product

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import dev.maxkach.shaders.R
import dev.maxkach.shaders.product.ProductCardState.ColorsState
import dev.maxkach.shaders.product.ProductCardState.ImagesState
import kotlin.math.absoluteValue

@Composable
fun ProductCardWithRedTint(
    state: ProductCardState,
    modifier: Modifier = Modifier,
    onColorClicked: (Int) -> Unit = { },
    onImageChanged: (Int) -> Unit = { },
) {
    Scaffold(
        modifier = modifier,
        topBar = { TopBar(state.title) },
        content = { innerPadding ->
            val transition = rememberInfiniteTransition()
            val intensity by transition.animateFloat(
                initialValue = 0f,
                targetValue = 0.5f,
                animationSpec = infiniteRepeatable(
                    animation = tween(durationMillis = 500),
                    repeatMode = RepeatMode.Reverse
                ),
            )

            Column(
                modifier = Modifier.padding(
                    top = innerPadding.calculateTopPadding() + 12.dp,
                    bottom = innerPadding.calculateBottomPadding() + 12.dp,
                )
                    .redTintShader { intensity }
                ,
            ) {
                Images(
                    images = state.images,
                    modifier = Modifier,
                    onImageChanged = onImageChanged,
                )
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

@OptIn(androidx.compose.foundation.ExperimentalFoundationApi::class)
@Composable
private fun Images(
    images: ImagesState,
    modifier: Modifier = Modifier,
    onImageChanged: (Int) -> Unit = { },
) {
    val pagerState = rememberPagerState(
        initialPage = images.currentImage,
        pageCount = { images.imagesRes.size }
    )

    // Sync pager with external state changes
    androidx.compose.runtime.LaunchedEffect(images.currentImage) {
        if (pagerState.currentPage != images.currentImage) {
            pagerState.animateScrollToPage(images.currentImage)
        }
    }

    // Notify when pager changes
    androidx.compose.runtime.LaunchedEffect(pagerState) {
        androidx.compose.runtime.snapshotFlow { pagerState.currentPage }.collect { page ->
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
            contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 40.dp),
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

@Preview
@Composable
private fun ProductCardPreview() {
    Surface {
        var selectedImage by remember { mutableIntStateOf(0) }
        var selectedColor by remember { mutableIntStateOf(0) }

        ProductCardWithRedTint(
            modifier = Modifier.fillMaxSize(),
            state = ProductCardStateCreator.create(selectedImage, selectedColor),
            onColorClicked = { newColor ->
                selectedColor = newColor
                selectedImage = newColor
            },
            onImageChanged = { newImage ->
                selectedImage = newImage
                selectedColor = newImage
            },
        )
    }
}
