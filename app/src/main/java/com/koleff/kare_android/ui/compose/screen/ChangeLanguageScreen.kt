package com.koleff.kare_android.ui.compose.screen

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.koleff.kare_android.R
import com.koleff.kare_android.data.model.dto.KareLanguage
import com.koleff.kare_android.data.model.response.base_response.KareError
import com.koleff.kare_android.ui.compose.components.SearchBar
import com.koleff.kare_android.ui.compose.components.navigation_components.scaffolds.MainScreenScaffold
import com.koleff.kare_android.ui.compose.dialogs.ChangeLanguageDialog
import com.koleff.kare_android.ui.compose.dialogs.LoadingDialog
import com.koleff.kare_android.ui.view_model.ChangeLanguageViewModel

@Composable
fun ChangeLanguageScreen(
    languageViewModel: ChangeLanguageViewModel = hiltViewModel()
) {
    val state by languageViewModel.getSupportedLanguagesState.collectAsState()

    var supportedLanguages by remember {
        mutableStateOf(state.supportedLanguages)
    }

    LaunchedEffect(state) {
        supportedLanguages =
            state.supportedLanguages //TODO: pre-load them on app initial launch for first time...
    }

    val titleTextColor = MaterialTheme.colorScheme.onSurface
    val titleTextStyle = MaterialTheme.typography.displayMedium.copy(
        color = titleTextColor
    )

    var showConfirmDialog by remember { mutableStateOf(false) }
    var showLoadingDialog by remember { mutableStateOf(false) }
    var showErrorDialog by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<KareError?>(null) }

    val context = LocalContext.current

    LaunchedEffect(state) {
        error = state.error
        showErrorDialog = state.isError
        Log.d("ChangeLanguageScreen", "Error detected -> $showErrorDialog")

        showLoadingDialog = state.isLoading
    }

    var selectedLanguage by remember {
        mutableStateOf(KareLanguage())
    }
    val onSelectedLanguage: () -> Unit = {
        languageViewModel.changeLanguage(context, selectedLanguage)

        selectedLanguage = KareLanguage()
        showConfirmDialog = false
    }

    if (showConfirmDialog) {
        ChangeLanguageDialog(
            selectedLanguage = selectedLanguage,
            onDismiss = {
                selectedLanguage = KareLanguage()
                showConfirmDialog = false
            },
            onClick = onSelectedLanguage
        )
    }

    MainScreenScaffold(
        screenTitle = "Choose app language",
        onNavigateToDashboard = { languageViewModel.onNavigateToDashboard() },
        onNavigateToWorkouts = { languageViewModel.onNavigateToWorkouts() },
        onNavigateBackAction = { languageViewModel.onNavigateBack() },
        onNavigateToSettings = { languageViewModel.onNavigateToSettings() },
    ) { innerPadding ->
        val modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)

        if (showLoadingDialog) {
            LoadingDialog(
                modifier = modifier,
                onDismiss = { showLoadingDialog = false }
            )
        } else {
            LazyColumn(
                modifier = modifier,
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
            ) {
                item {
                    SearchBar(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        onSearch = { text ->
                            languageViewModel.onTextChange(text)
                        },
                        onToggleSearch = {
                            languageViewModel.onToggleSearch()
                        })
                }

//                //Title
//                item {
//                    Box(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(horizontal = 4.dp),
//                        contentAlignment = Alignment.Center
//                    ) {
//                        Text(
//                            modifier = Modifier.padding(
//                                all = 8.dp
//                            ),
//                            text = "Choose app language",
//                            style = titleTextStyle,
//                            maxLines = 2,
//                            overflow = TextOverflow.Ellipsis
//                        )
//                    }
//                }

                //Language lazy list
                items(supportedLanguages.size) {
                    LanguageRow(supportedLanguages[it]) {
                        selectedLanguage = supportedLanguages[it]

                        showConfirmDialog = true
                    }
                }
            }
        }
    }
}

@Composable
fun LanguageRow(language: KareLanguage, onClick: () -> Unit) {
    val cornerSize = 16.dp
    val outlineColor = MaterialTheme.colorScheme.outlineVariant

    val textColor = MaterialTheme.colorScheme.onSurface
    val titleTextStyle = MaterialTheme.typography.titleMedium.copy(
        color = textColor
    )

    val height = 100.dp
    Row(
        modifier = Modifier
            .clickable { onClick.invoke() }
            .fillMaxWidth()
            .height(height)
            .clip(RoundedCornerShape(cornerSize))
            .border(
                border = BorderStroke(3.dp, color = outlineColor),
                shape = RoundedCornerShape(cornerSize)
            )
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(cornerSize)
            ),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {

        //Country flag
        Image(
            modifier = Modifier
                .padding(start = 6.dp)
                .height(height / 2)
                .width(80.dp)
                .clip(RoundedCornerShape(4.dp))
                .border(
                    border = BorderStroke(1.dp, color = outlineColor),
                    shape = RoundedCornerShape(4.dp)
                ),
            painter = painterResource(id = language.countryImage),
            contentDescription = "Country image",
            contentScale = ContentScale.Crop
        )

        //Country name
        Text(
            modifier = Modifier.padding(horizontal = 32.dp),
            text = language.name,
            style = titleTextStyle,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )

//        val image: Painter = painterResource(id = R.drawable.ic_vector_select)
//        val iconSize = 40.dp
//        val tintColor = LocalExtendedColorScheme.current.workoutBannerColors.selectButtonColor
//
//        //On select button
//        Box(
//            modifier = Modifier.fillMaxWidth(),
//            contentAlignment = Alignment.CenterEnd
//        ) {
//            Image(
//                painter = image,
//                contentDescription = "Select",
//                modifier = Modifier
//                    .padding(end = 6.dp)
//                    .size(iconSize),
//                colorFilter = ColorFilter.tint(tintColor),
//                contentScale = ContentScale.Crop
//            )
//        }
    }
}

@Preview
@Composable
private fun LanguageRowPreview() {
    LanguageRow(
        language = KareLanguage(
            "Bulgarian",
            "BG",
            R.drawable.bulgaria_flag
        )
    ) {

    }
}
