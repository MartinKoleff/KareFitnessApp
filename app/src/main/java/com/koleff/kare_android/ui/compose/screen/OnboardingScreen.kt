package com.koleff.kare_android.ui.compose.screen

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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.koleff.kare_android.R
import com.koleff.kare_android.ui.style.OnboardingDataUI
import com.koleff.kare_android.ui.theme.Poppins
import com.koleff.kare_android.ui.view_model.OnboardingViewModel
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@DelicateCoroutinesApi
@ExperimentalPagerApi
@Composable
fun OnBoardingPager(
    modifier: Modifier = Modifier,
    item: List<OnboardingDataUI>,
    pagerState: PagerState,
    onNavigateToFormsScreen: () -> Unit,
    onSkipAction: () -> Unit
) {

    Box(modifier = modifier) {

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            HorizontalPager(state = pagerState) { page ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(item[page].backgroundColor),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top
                ) {

                    Image(
                        painter = painterResource(id = item[page].image),
                        contentDescription = item[page].title,
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                }
            }
        }

        Box(modifier = Modifier.align(Alignment.BottomCenter)) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(340.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 10.dp
                ),
                shape = RoundedCornerShape(topStart = 80.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    PagerIndicator(items = item, currentPage = pagerState.currentPage)
                    Text(
                        text = item[pagerState.currentPage].title,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 20.dp, end = 30.dp),
//                            color = Color(0xFF292D32),
                        color = item[pagerState.currentPage].mainColor,
                        fontFamily = Poppins,
                        textAlign = TextAlign.Right,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold
                    )

                    Text(
                        text = item[pagerState.currentPage].desc,
                        modifier = Modifier.padding(top = 20.dp, start = 40.dp, end = 20.dp),
                        color = Color.Gray,
                        fontFamily = Poppins,
                        fontSize = 17.sp,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.ExtraLight
                    )

                }
            }


            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(30.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    //Go to previous page button
                    if (pagerState.currentPage != 0 && pagerState.currentPage != item.lastIndex) {
                        TextButton(onClick = {
                            GlobalScope.launch {
                                pagerState.scrollToPage(
                                    pagerState.currentPage - 1,
                                    pageOffset = 0f
                                )
                            }
                        }) {
                            Text(
                                text = "Previous",
                                color = Color(0xFF292D32),
                                fontFamily = Poppins,
                                textAlign = TextAlign.Left,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    } else if (pagerState.currentPage == 0) {

                        //Skip onboarding button
                        TextButton(onClick = {
                            onSkipAction()
                        }) {
                            Text(
                                text = "Skip Now",
                                color = Color(0xFF292D32),
                                fontFamily = Poppins,
                                textAlign = TextAlign.Right,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    if (pagerState.currentPage != item.lastIndex) {

                        //Go to next page button
                        OutlinedButton(
                            onClick = {
                                GlobalScope.launch {
                                    pagerState.scrollToPage(
                                        pagerState.currentPage + 1,
                                        pageOffset = 0f
                                    )
                                }
                            },
                            border = BorderStroke(
                                14.dp,
                                item[pagerState.currentPage].mainColor
                            ),
                            shape = RoundedCornerShape(50), // = 50% percent
                            //or shape = CircleShape
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = item[pagerState.currentPage].mainColor),
                            modifier = Modifier.size(65.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_vector_arrow_forward),
                                contentDescription = "",
                                tint = item[pagerState.currentPage].mainColor,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    } else {

                        //Get started button
                        Button(
                            onClick = {
                                onNavigateToFormsScreen()
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = item[pagerState.currentPage].mainColor
                            ),
                            contentPadding = PaddingValues(vertical = 12.dp),
                            elevation = ButtonDefaults.elevatedButtonElevation(
                                defaultElevation = 0.dp
                            )
                        ) {
                            Text(
                                text = "Get Started",
                                color = Color.White,
                                fontSize = 16.sp
                            )
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun PagerIndicator(currentPage: Int, items: List<OnboardingDataUI>) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.padding(top = 20.dp)
    ) {
        repeat(items.size) {
            Indicator(isSelected = it == currentPage, color = items[it].mainColor)
        }
    }
}

@Composable
fun Indicator(isSelected: Boolean, color: Color) {
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

@OptIn(ExperimentalPagerApi::class)
@Composable
fun rememberPagerState(
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

@Preview
@Composable
private fun OnboardingScreensPreview() {
    OnboardingScreen()
}


@OptIn(ExperimentalPagerApi::class, DelicateCoroutinesApi::class)
@Composable
fun OnboardingScreen(
    onboardingViewModel: OnboardingViewModel = hiltViewModel()
) {
    val items = ArrayList<OnboardingDataUI>()

    items.add(
        OnboardingDataUI(
            R.drawable.fruit,
            "Track Your Progress",
            "Monitor your fitness journey with detailed stats. Keep track of your workouts, progress, and milestones all in one place.",
            backgroundColor = Color(0xFF0189C5),
            mainColor = Color(0xFF64B5F6)
        )
    )

    items.add(
        OnboardingDataUI(
            R.drawable.food,
            "Personalized Workouts",
            "Enjoy tailored workouts that fit your goals and lifestyle. Create routines and get step-by-step guidance for every exercise.",
            backgroundColor = Color(0xFFE4AF19),
            mainColor = Color(0xFFFFE082)
        )
    )

    items.add(
        OnboardingDataUI(
            R.drawable.cooking,
            "Achieve Your Goals",
            "Set your fitness goals and crush them. Whether it's building strength, losing weight, or staying active, we've got you covered!",
            backgroundColor = Color(0xFF96E172),
            mainColor = Color(0xFF81C784)
        )
    )


    val pagerState = rememberPagerState(
        pageCount = items.size,
        initialOffscreenLimit = 2,
        infiniteLoop = false,
        initialPage = 0,
    )

    Surface(modifier = Modifier.fillMaxSize()) {
        OnBoardingPager(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color.Blue),
            item = items,
            pagerState = pagerState,
            onSkipAction = { onboardingViewModel.skip() },
            onNavigateToFormsScreen = { onboardingViewModel.navigateToFormsScreen() }
        )
    }
}
//This code is taken from:
//https://github.com/UIStackYT/Onboarding_Screen/blob/master/app/src/main/res/drawable-v24/fruit.jpg
//https://www.youtube.com/watch?v=Yfh2pfi_TeI
//https://www.google.com/search?client=safari&sca_esv=01c9daa3c61022a2&rls=en&q=github+android+compose+onboarding+screen&udm=2&fbs=AEQNm0Aa4sjWe7Rqy32pFwRj0UkWd8nbOJfsBGGB5IQQO6L3JyJJclJuzBPl12qJyPx7ESJehObpS5jg6J88CCM-RK72sNV8xvbUxy-SoOtM-WmPLIjZzuRzEJJ0u2V8OeDS2QzrFq0l6uL0u5ydk68vXkBqxln9Kbinx1HZnJEg4P6VfVQ98eE&sa=X&ved=2ahUKEwi05OuW5fmKAxWKQPEDHY99NKgQtKgLegQIERAB&biw=1440&bih=772&dpr=2#vhid=FfqjFtjxKVhGRM&vssid=mosaic

//Inspiration for Onboarding:
//https://mir-s3-cdn-cf.behance.net/project_modules/1400/c1023894294605.5e7b4f5db0a44.jpg
//https://www.google.com/search?client=safari&sca_esv=01c9daa3c61022a2&rls=en&q=fitness+onboarding+ui+app&udm=2&fbs=AEQNm0Aa4sjWe7Rqy32pFwRj0UkWd8nbOJfsBGGB5IQQO6L3JyJJclJuzBPl12qJyPx7ESJehObpS5jg6J88CCM-RK72sNV8xvbUxy-SoOtM-WmPLIjZzuRzEJJ0u2V8OeDS2QzrFq0l6uL0u5ydk68vXkBqxln9Kbinx1HZnJEg4P6VfVQ98eE&sa=X&ved=2ahUKEwjni-DY4_mKAxWdQvEDHb2dEOQQtKgLegQIGBAB&biw=1440&bih=772&dpr=2#vhid=ss8PlYK1loeoaM&vssid=mosaic
//https://www.google.com/url?sa=i&url=https%3A%2F%2Fdribbble.com%2Fshots%2F8575014-Mobile-app-onboarding-process&psig=AOvVaw2vl3euHXo4jxYzaofmGEwE&ust=1737790309827000&source=images&cd=vfe&opi=89978449&ved=0CBQQjRxqFwoTCLi2wsLrjYsDFQAAAAAdAAAAABAQ