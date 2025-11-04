package com.canyoufix.quicknote.presentation.navigation

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.canyoufix.quicknote.presentation.components.QuickNoteBottomBar
import com.canyoufix.quicknote.presentation.navigation.routes.Route
import com.canyoufix.quicknote.presentation.screens.ListScreen
import com.canyoufix.quicknote.presentation.screens.NewNoteScreen
import com.canyoufix.quicknote.presentation.screens.SettingsScreen
import com.canyoufix.quicknote.presentation.screens.TrashScreen

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun QuickNoteNavigation() {
    val backStack = rememberNavBackStack(Route.List)
    val currentRoute = backStack.last()

    val showBottomBar = when (currentRoute) {
        Route.List, Route.Trash, Route.Settings -> true
        else -> false
    }


    Scaffold(
        contentWindowInsets = ScaffoldDefaults.contentWindowInsets
            .only(WindowInsetsSides.Bottom + WindowInsetsSides.Horizontal),
        bottomBar = {
            if(showBottomBar){
                QuickNoteBottomBar(
                    currentRoute = backStack.lastOrNull() as? Route,
                    onNavigate = { route ->
                        backStack.add(route)

                    }
                )
            }
        }
    ) { innerPadding ->
        SharedTransitionScope {
            NavDisplay(
                backStack = backStack,
                entryDecorators = listOf(
                    rememberSaveableStateHolderNavEntryDecorator(),
                    rememberViewModelStoreNavEntryDecorator(),
                ),
                // NavigationBarPadding
                modifier = Modifier.padding(innerPadding),
                entryProvider = entryProvider {
                    entry<Route.List> {
                        ListScreen(
                            onAddClick = { backStack.add(Route.NewNote) },
                        )
                    }

                    entry<Route.NewNote> {
                        NewNoteScreen(
                            onBackClick = { backStack.removeAt(backStack.lastIndex) },
                        )
                    }

                    entry<Route.Trash> {
                        TrashScreen(
                            //onBackClick = { backStack.add(Route.Trash) },
                        )
                    }

                    entry<Route.Settings> {
                        SettingsScreen(
                            // onBackClick = { backStack.add(Route.Settings) },
                        )
                    }
                },
            )
        }
    }
}