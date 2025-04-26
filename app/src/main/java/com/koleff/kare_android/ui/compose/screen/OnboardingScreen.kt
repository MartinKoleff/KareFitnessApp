package com.koleff.kare_android.ui.compose.screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.koleff.kare_android.common.OnboardingDataGenerator
import com.koleff.kare_android.ui.compose.components.OnboardingPager
import com.koleff.kare_android.ui.compose.components.rememberOnboardingPagerState
import com.koleff.kare_android.ui.view_model.OnboardingViewModel
import kotlinx.coroutines.DelicateCoroutinesApi

@OptIn(ExperimentalPagerApi::class, DelicateCoroutinesApi::class)
@Composable
fun OnboardingScreen(
    onboardingViewModel: OnboardingViewModel = hiltViewModel()
) {
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
            onSkipAction = { onboardingViewModel.skip() },
            onNavigateToFormsScreen = { onboardingViewModel.navigateToFormsScreen() }
        )
    }
}

@Preview
@Composable
private fun OnboardingScreensPreview() {
    OnboardingScreen()
}
