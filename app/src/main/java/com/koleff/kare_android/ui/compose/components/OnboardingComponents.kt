package com.koleff.kare_android.ui.compose.components

import androidx.annotation.FloatRange
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.koleff.kare_android.R
import com.koleff.kare_android.common.manager.data.OnboardingDataGenerator
import com.koleff.kare_android.ui.style.OnboardingDataUI
import com.koleff.kare_android.ui.theme.LocalExtendedColors
import com.koleff.kare_android.ui.theme.Poppins
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


@DelicateCoroutinesApi
@ExperimentalPagerApi
@Composable
fun OnboardingPager(
    modifier: Modifier = Modifier,
    items: List<OnboardingDataUI>,
    pagerState: PagerState,
    onNavigateToFormsScreen: () -> Unit,
    onSkipAction: () -> Unit
) {
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp

    Box(modifier = modifier.fillMaxSize()) {
        HorizontalPager(
            modifier = Modifier
                .fillMaxWidth()
                .height(screenHeight * 0.6f),
            state = pagerState
        ) { page ->
            Image(
                modifier = Modifier.fillMaxWidth(),
                painter = painterResource(id = items[page].image),
                contentDescription = items[page].title,
                contentScale = ContentScale.Crop,
            )
        }

        OnboardingBottomSheet(
            modifier = Modifier
                .fillMaxWidth()
                .height(screenHeight * 0.45f)
                .align(Alignment.BottomCenter),
            items = items,
            pagerState = pagerState,
            onNavigateToFormsScreen = onNavigateToFormsScreen,
            onSkipAction = onSkipAction
        )
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun OnboardingBottomSheet(
    modifier: Modifier = Modifier,
    items: List<OnboardingDataUI>,
    pagerState: PagerState,
    onNavigateToFormsScreen: () -> Unit,
    onSkipAction: () -> Unit
) {
    val titleTextColor = LocalExtendedColors.current.title
    val titleTextStyle = MaterialTheme.typography.headlineMedium.copy(
        color = titleTextColor,
        fontFamily = Poppins,
        fontWeight = FontWeight.ExtraBold
    )

    val descriptionTextColor = LocalExtendedColors.current.subtitle
    val descriptionTextStyle = MaterialTheme.typography.titleMedium.copy(
        color = descriptionTextColor,
        fontFamily = Poppins,
        fontWeight = FontWeight.ExtraLight,
    )

    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        ),
        shape = RoundedCornerShape(topStart = 40.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OnboardingPagerIndicator(items = items, currentPage = pagerState.currentPage)

            Text(
                text = items[pagerState.currentPage].title,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp, horizontal = 10.dp),
                color = titleTextColor,
                textAlign = TextAlign.Center,
                style = titleTextStyle
            )

            Text(
                text = items[pagerState.currentPage].desc,
                modifier = Modifier.padding(horizontal = 10.dp),
                style = descriptionTextStyle,
                textAlign = TextAlign.Center
            )
        }

        OnboardingFooterRow(
            pagerState = pagerState,
            items = items,
            onNavigateToFormsScreen = onNavigateToFormsScreen,
            onSkipAction = onSkipAction
        )
    }
}

@OptIn(ExperimentalPagerApi::class)
@Preview
@Composable
private fun OnboardingBottomSheetPreview() {
    val items = OnboardingDataGenerator.generateOnboardingData(MaterialTheme.colorScheme.primary)
    val pagerState = rememberOnboardingPagerState(
        pageCount = items.size,
        initialOffscreenLimit = 2,
        infiniteLoop = false,
        initialPage = 0,
    )

    OnboardingBottomSheet(
        modifier = Modifier.fillMaxWidth(),
        items = items,
        pagerState = pagerState,
        onNavigateToFormsScreen = {},
        onSkipAction = {}
    )
}

@OptIn(ExperimentalPagerApi::class, DelicateCoroutinesApi::class)
@Composable
fun OnboardingFooterRow(
    pagerState: PagerState,
    items: List<OnboardingDataUI>,
    onNavigateToFormsScreen: () -> Unit,
    onSkipAction: () -> Unit
) {
    val buttonColor = items[pagerState.currentPage].mainColor

    Row(
        modifier = Modifier.fillMaxSize().padding(vertical = 10.dp, horizontal = 10.dp),
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        if (pagerState.currentPage != 0 && pagerState.currentPage != items.lastIndex) {
            PreviousPageButton(
                onClick = {
                    GlobalScope.launch {
                        pagerState.scrollToPage(
                            pagerState.currentPage - 1,
                            pageOffset = 0f
                        )
                    }
                }
            )
        } else if (pagerState.currentPage == 0) {
            SkipOnboardingButton(
                onSkipAction = onSkipAction
            )
        }

        if (pagerState.currentPage != items.lastIndex) {
            NextPageButton(
                buttonColor = buttonColor,
                onClick = {
                    GlobalScope.launch {
                        pagerState.scrollToPage(
                            pagerState.currentPage + 1,
                            pageOffset = 0f
                        )
                    }
                }
            )
        } else {
            GetStartedButton(
                buttonColor = buttonColor,
                onNavigateToFormsScreen = onNavigateToFormsScreen,
            )
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Preview
@Composable
private fun OnboardingFooterRowPreview() {
    val items = OnboardingDataGenerator.generateOnboardingData(MaterialTheme.colorScheme.primary)
    val pagerState = rememberOnboardingPagerState(
        pageCount = items.size,
        initialOffscreenLimit = 2,
        infiniteLoop = false,
        initialPage = 0,
    )

    OnboardingFooterRow(
        pagerState = pagerState,
        items = items,
        onNavigateToFormsScreen = {},
        onSkipAction = {}
    )
}

@Composable
fun PreviousPageButton(
    onClick: () -> Unit
) {
    val labelTextColor = LocalExtendedColors.current.label
    val labelTextStyle = MaterialTheme.typography.labelLarge.copy(
        color = labelTextColor,
        fontFamily = Poppins,
        fontWeight = FontWeight.SemiBold
    )

    TextButton(onClick = {
        onClick()
    }) {
        Text(
            text = "Previous",
            style = labelTextStyle,
            textAlign = TextAlign.Left
        )
    }
}

@Preview
@Composable
private fun PreviousPageButtonPreview() {
    PreviousPageButton(onClick = { })
}

@Composable
fun SkipOnboardingButton(onSkipAction: () -> Unit) {
    val labelTextColor = LocalExtendedColors.current.label
    val labelTextStyle = MaterialTheme.typography.labelLarge.copy(
        color = labelTextColor,
        fontFamily = Poppins,
        fontWeight = FontWeight.SemiBold
    )

    TextButton(onClick = {
        onSkipAction()
    }) {
        Text(
            text = "Skip Now",
            style = labelTextStyle,
            textAlign = TextAlign.Right
        )
    }
}

@Preview
@Composable
private fun SkipOnboardingButtonPreview() {
    SkipOnboardingButton(onSkipAction = { })
}

@Composable
fun NextPageButton(
    buttonColor: Color,
    onClick: () -> Unit
) {
    OutlinedButton(
        onClick = {
            onClick()
        },
        border = BorderStroke(
            width = 14.dp,
            color = buttonColor
        ),
        shape = RoundedCornerShape(50), // = 50% percent / shape = CircleShape
        colors = ButtonDefaults.outlinedButtonColors(contentColor = buttonColor),
        modifier = Modifier.size(65.dp)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_vector_arrow_forward),
            contentDescription = "Forward arrow",
            tint = buttonColor,
            modifier = Modifier.size(20.dp)
        )
    }
}

@Preview
@Composable
private fun NextPageButtonPreview() {
    NextPageButton(buttonColor = MaterialTheme.colorScheme.primary, onClick = { })
}

@Composable
fun GetStartedButton(
    buttonColor: Color,
    onNavigateToFormsScreen: () -> Unit
) {
    val buttonTextColor = LocalExtendedColors.current.title
    val buttonTextStyle = MaterialTheme.typography.titleMedium.copy(
        color = buttonTextColor,
        fontFamily = Poppins,
        fontWeight = FontWeight.Bold
    )

    Button(
        onClick = {
            onNavigateToFormsScreen()
        },
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(
            containerColor = buttonColor
        ),
        contentPadding = PaddingValues(vertical = 12.dp),
        elevation = ButtonDefaults.elevatedButtonElevation(
            defaultElevation = 0.dp
        )
    ) {
        Text(
            text = "Get Started",
            style = buttonTextStyle
        )
    }
}

@Preview
@Composable
private fun GetStartedButtonPreview() {
    val buttonColor = MaterialTheme.colorScheme.primary
    GetStartedButton(onNavigateToFormsScreen = {}, buttonColor = buttonColor)
}


@Composable
fun OnboardingPagerIndicator(currentPage: Int, items: List<OnboardingDataUI>) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.padding(top = 20.dp)
    ) {
        repeat(items.size) {
            OnboardingIndicator(isSelected = it == currentPage, color = items[it].mainColor)
        }
    }
}

@Composable
fun OnboardingIndicator(isSelected: Boolean, color: Color) {
    val width = animateDpAsState(targetValue = if (isSelected) 40.dp else 10.dp)

    Box(
        modifier = Modifier
            .padding(4.dp)
            .height(10.dp)
            .width(width.value)
            .clip(CircleShape)
            .background(
                if (isSelected) color else Color.Gray.copy(alpha = 0.5f)
            )
    )
}

@Preview
@Composable
private fun OnboardingIndicatorPreview() {
    OnboardingIndicator(isSelected = true, color = MaterialTheme.colorScheme.primary)
}

@OptIn(ExperimentalPagerApi::class, DelicateCoroutinesApi::class)
@Preview(name = "NEXUS_7", device = Devices.NEXUS_7)
@Preview(name = "NEXUS_7_2013", device = Devices.NEXUS_7_2013)
@Preview(name = "NEXUS_5", device = Devices.NEXUS_5)
@Preview(name = "NEXUS_6", device = Devices.NEXUS_6)
@Preview(name = "NEXUS_9", device = Devices.NEXUS_9)
@Preview(name = "NEXUS_10", device = Devices.NEXUS_10)
@Preview(name = "NEXUS_5X", device = Devices.NEXUS_5X)
@Preview(name = "NEXUS_6P", device = Devices.NEXUS_6P)
@Preview(name = "PIXEL_C", device = Devices.PIXEL_C)
@Preview(name = "PIXEL", device = Devices.PIXEL)
@Preview(name = "PIXEL_XL", device = Devices.PIXEL_XL)
@Preview(name = "PIXEL_2", device = Devices.PIXEL_2)
@Preview(name = "PIXEL_2_XL", device = Devices.PIXEL_2_XL)
@Preview(name = "PIXEL_3", device = Devices.PIXEL_3)
@Preview(name = "PIXEL_3_XL", device = Devices.PIXEL_3_XL)
@Preview(name = "PIXEL_3A", device = Devices.PIXEL_3A)
@Preview(name = "PIXEL_3A_XL", device = Devices.PIXEL_3A_XL)
@Preview(name = "PIXEL_4", device = Devices.PIXEL_4)
@Preview(name = "PIXEL_4_XL", device = Devices.PIXEL_4_XL)
@Preview(name = "AUTOMOTIVE_1024p", device = Devices.AUTOMOTIVE_1024p)
@Composable
private fun OnboardingPagerPreview() {
    val items = OnboardingDataGenerator.generateOnboardingData(MaterialTheme.colorScheme.primary)
    val pagerState = rememberOnboardingPagerState(
        pageCount = items.size,
        initialOffscreenLimit = 2,
        infiniteLoop = false,
        initialPage = 0,
    )

    Surface(modifier = Modifier.fillMaxSize()) {
        OnboardingPager(
            items = items,
            pagerState = pagerState,
            onSkipAction = { },
            onNavigateToFormsScreen = { }
        )
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun rememberOnboardingPagerState(
    @androidx.annotation.IntRange(from = 0) pageCount: Int,
    @androidx.annotation.IntRange(from = 0) initialPage: Int = 0,
    @FloatRange(from = 0.0, to = 1.0) initialPageOffset: Float = 0f,
    @androidx.annotation.IntRange(from = 1) initialOffscreenLimit: Int = 1,
    infiniteLoop: Boolean = false
): PagerState = rememberSaveable(saver = PagerState.Saver) {
    PagerState(
        pageCount = pageCount,
        currentPage = initialPage,
        currentPageOffset = initialPageOffset,
        offscreenLimit = initialOffscreenLimit,
        infiniteLoop = infiniteLoop
    )
}

//This code is taken from:
//https://github.com/UIStackYT/Onboarding_Screen/blob/master/app/src/main/res/drawable-v24/fruit.jpg
//https://www.youtube.com/watch?v=Yfh2pfi_TeI
//https://www.google.com/search?client=safari&sca_esv=01c9daa3c61022a2&rls=en&q=github+android+compose+onboarding+screen&udm=2&fbs=AEQNm0Aa4sjWe7Rqy32pFwRj0UkWd8nbOJfsBGGB5IQQO6L3JyJJclJuzBPl12qJyPx7ESJehObpS5jg6J88CCM-RK72sNV8xvbUxy-SoOtM-WmPLIjZzuRzEJJ0u2V8OeDS2QzrFq0l6uL0u5ydk68vXkBqxln9Kbinx1HZnJEg4P6VfVQ98eE&sa=X&ved=2ahUKEwi05OuW5fmKAxWKQPEDHY99NKgQtKgLegQIERAB&biw=1440&bih=772&dpr=2#vhid=FfqjFtjxKVhGRM&vssid=mosaic

//Inspiration for Onboarding:
//https://mir-s3-cdn-cf.behance.net/project_modules/1400/c1023894294605.5e7b4f5db0a44.jpg
//https://www.google.com/search?client=safari&sca_esv=01c9daa3c61022a2&rls=en&q=fitness+onboarding+ui+app&udm=2&fbs=AEQNm0Aa4sjWe7Rqy32pFwRj0UkWd8nbOJfsBGGB5IQQO6L3JyJJclJuzBPl12qJyPx7ESJehObpS5jg6J88CCM-RK72sNV8xvbUxy-SoOtM-WmPLIjZzuRzEJJ0u2V8OeDS2QzrFq0l6uL0u5ydk68vXkBqxln9Kbinx1HZnJEg4P6VfVQ98eE&sa=X&ved=2ahUKEwjni-DY4_mKAxWdQvEDHb2dEOQQtKgLegQIGBAB&biw=1440&bih=772&dpr=2#vhid=ss8PlYK1loeoaM&vssid=mosaic
//https://www.google.com/url?sa=i&url=https%3A%2F%2Fdribbble.com%2Fshots%2F8575014-Mobile-app-onboarding-process&psig=AOvVaw2vl3euHXo4jxYzaofmGEwE&ust=1737790309827000&source=images&cd=vfe&opi=89978449&ved=0CBQQjRxqFwoTCLi2wsLrjYsDFQAAAAAdAAAAABAQ
