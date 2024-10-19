package ua.acclorite.book_story.presentation.screens.book_info.components

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Done
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import ua.acclorite.book_story.R
import ua.acclorite.book_story.presentation.core.components.common.AnimatedVisibility
import ua.acclorite.book_story.presentation.core.components.common.GoBackButton
import ua.acclorite.book_story.presentation.core.components.common.IconButton
import ua.acclorite.book_story.presentation.core.components.top_bar.TopAppBar
import ua.acclorite.book_story.presentation.core.components.top_bar.TopAppBarData
import ua.acclorite.book_story.presentation.core.navigation.LocalNavigator
import ua.acclorite.book_story.presentation.core.navigation.NavigationIconButton
import ua.acclorite.book_story.presentation.screens.book_info.data.BookInfoEvent
import ua.acclorite.book_story.presentation.screens.book_info.data.BookInfoViewModel
import ua.acclorite.book_story.presentation.screens.history.data.HistoryEvent
import ua.acclorite.book_story.presentation.screens.history.data.HistoryViewModel
import ua.acclorite.book_story.presentation.screens.library.data.LibraryEvent
import ua.acclorite.book_story.presentation.screens.library.data.LibraryViewModel
import ua.acclorite.book_story.presentation.ui.DefaultTransition
import ua.acclorite.book_story.presentation.ui.Transitions

/**
 * Book Info Top Bar.
 *
 * @param listState [LazyListState].
 * @param snackbarState [SnackbarHostState].
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookInfoTopBar(
    listState: LazyListState,
    snackbarState: SnackbarHostState
) {
    val state = BookInfoViewModel.getState()
    val onEvent = BookInfoViewModel.getEvent()
    val onLibraryEvent = LibraryViewModel.getEvent()
    val onHistoryEvent = HistoryViewModel.getEvent()
    val onNavigate = LocalNavigator.current
    val context = LocalContext.current

    val firstVisibleItemIndex by remember { derivedStateOf { listState.firstVisibleItemIndex } }

    TopAppBar(
        containerColor = MaterialTheme.colorScheme.surface.copy(0f),
        scrollBehavior = null,
        isTopBarScrolled = listState.canScrollBackward,

        shownTopBar = 0,
        topBars = listOf(
            TopAppBarData(
                contentID = 0,
                contentNavigationIcon = {
                    if (
                        state.value.editTitle ||
                        state.value.editAuthor ||
                        state.value.editDescription
                    ) {
                        IconButton(
                            icon = Icons.AutoMirrored.Outlined.ArrowBack,
                            contentDescription = R.string.exit_editing_content_desc,
                            disableOnClick = true
                        ) {
                            if (state.value.editTitle) {
                                onEvent(BookInfoEvent.OnShowHideEditTitle)
                            }

                            if (state.value.editAuthor) {
                                onEvent(BookInfoEvent.OnShowHideEditAuthor)
                            }

                            if (state.value.editDescription) {
                                onEvent(BookInfoEvent.OnShowHideEditDescription)
                            }
                        }
                    } else {
                        GoBackButton(onNavigate = onNavigate, enabled = !state.value.updating) {
                            onEvent(BookInfoEvent.OnCancelTextUpdate)
                        }
                    }
                },
                contentTitle = {
                    DefaultTransition(
                        firstVisibleItemIndex > 0 && !state.value.editTitle
                    ) {
                        Text(
                            state.value.book.title,
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onSurface,
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 1
                        )
                    }
                },
                contentActions = {
                    IconButton(
                        icon = Icons.Outlined.Refresh,
                        contentDescription = R.string.refresh_book_content_desc,
                        disableOnClick = false,
                        enabled = !state.value.updating && !state.value.checkingForUpdate
                    ) {
                        onEvent(
                            BookInfoEvent.OnCheckForTextUpdate(
                                snackbarState,
                                context
                            )
                        )
                    }

                    Box {
                        DefaultTransition(
                            visible = !state.value.editTitle &&
                                    !state.value.editAuthor &&
                                    !state.value.editDescription
                        ) {
                            NavigationIconButton {
                                onEvent(BookInfoEvent.OnShowHideMoreBottomSheet(true))
                            }
                        }
                        AnimatedVisibility(
                            visible = state.value.editTitle ||
                                    state.value.editAuthor ||
                                    state.value.editDescription,
                            enter = Transitions.DefaultTransitionIn,
                            exit = fadeOut(tween(200))
                        ) {
                            IconButton(
                                icon = Icons.Outlined.Done,
                                contentDescription = R.string.apply_changes_content_desc,
                                disableOnClick = true,
                                enabled = !state.value.updating &&
                                        (state.value.titleValue.isNotBlank() &&
                                                state.value.titleValue.trim() !=
                                                state.value.book.title.trim()) ||

                                        (state.value.authorValue.isNotBlank() &&
                                                state.value.authorValue.trim() !=
                                                state.value.book.author.getAsString()?.trim()) ||

                                        (state.value.descriptionValue.isNotBlank() &&
                                                state.value.descriptionValue.trim() !=
                                                state.value.book.description?.trim()),

                                color = if ((state.value.titleValue.isNotBlank() &&
                                            state.value.titleValue.trim() !=
                                            state.value.book.title.trim()) ||

                                    (state.value.authorValue.isNotBlank() &&
                                            state.value.authorValue.trim() !=
                                            state.value.book.author.getAsString()?.trim()) ||

                                    (state.value.descriptionValue.isNotBlank() &&
                                            state.value.descriptionValue.trim() !=
                                            state.value.book.description?.trim())
                                ) MaterialTheme.colorScheme.onSurface
                                else MaterialTheme.colorScheme.onSurfaceVariant
                            ) {
                                onEvent(
                                    BookInfoEvent.OnUpdateData(
                                        refreshList = {
                                            onLibraryEvent(LibraryEvent.OnUpdateBook(it))
                                            onHistoryEvent(HistoryEvent.OnUpdateBook(it))
                                        }
                                    )
                                )
                            }
                        }
                    }
                }
            )
        )
    )
}