package ua.acclorite.book_story.presentation.screens.settings.nested.reader

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import ua.acclorite.book_story.R
import ua.acclorite.book_story.presentation.core.components.common.GoBackButton
import ua.acclorite.book_story.presentation.core.components.common.LazyColumnWithScrollbar
import ua.acclorite.book_story.presentation.core.components.top_bar.collapsibleTopAppBarScrollBehavior
import ua.acclorite.book_story.presentation.core.navigation.LocalNavigator
import ua.acclorite.book_story.presentation.screens.settings.nested.reader.components.ReaderSettingsCategory

@Composable
fun ReaderSettingsRoot() {
    ReaderSettings()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ReaderSettings() {
    val onNavigate = LocalNavigator.current
    val (scrollBehavior, lazyListState) = TopAppBarDefaults.collapsibleTopAppBarScrollBehavior()

    Scaffold(
        Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .windowInsetsPadding(WindowInsets.navigationBars),
        containerColor = MaterialTheme.colorScheme.surface,
        topBar = {
            LargeTopAppBar(
                title = {
                    Text(stringResource(id = R.string.reader_settings))
                },
                navigationIcon = {
                    GoBackButton(onNavigate = onNavigate)
                },
                scrollBehavior = scrollBehavior,
                colors = TopAppBarDefaults.largeTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    scrolledContainerColor = MaterialTheme.colorScheme.surfaceContainer
                )
            )
        }
    ) { paddingValues ->
        LazyColumnWithScrollbar(
            Modifier
                .fillMaxSize()
                .padding(top = paddingValues.calculateTopPadding()),
            state = lazyListState
        ) {
            ReaderSettingsCategory()
        }
    }
}