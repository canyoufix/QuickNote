package com.canyoufix.quicknote.presentation.components

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.canyoufix.quicknote.R
import com.canyoufix.quicknote.presentation.navigation.routes.Route

@Composable
fun QuickNoteBottomBar(
    currentRoute: Route?,
    onNavigate: (Route) -> Unit,
){
    val size = Modifier.size(28.dp)

    NavigationBar(
        modifier = Modifier
            .clip(RoundedCornerShape(topStart = 18.dp, topEnd = 18.dp))
    ) {
        NavigationBarItem(
            icon = {
                Icon(
                    painter = painterResource(R.drawable.ic_note_stack),
                    contentDescription = null,
                    modifier = size
                )
            },
            onClick = { onNavigate(Route.List) },
            selected = currentRoute == Route.List,
            label = {
                Text(text = stringResource(id = R.string.notes))
            },
        )
        NavigationBarItem(
            icon = {
                Icon(
                    painter = painterResource(R.drawable.ic_delete),
                    contentDescription = null,
                    modifier = size
                )
            },
            onClick = { onNavigate(Route.Trash) },
            selected = currentRoute == Route.Trash,
            label = {
                Text(text = stringResource(id = R.string.trash))
            },
        )
        NavigationBarItem(
            icon = {
                Icon(
                    painter = painterResource(R.drawable.ic_settings),
                    contentDescription = null,
                    modifier = size
                )
            },
            onClick = { onNavigate(Route.Settings) },
            selected = currentRoute == Route.Settings,
            label = {
                Text(text = stringResource(id = R.string.settings))
            },
        )
    }
}


/*
sealed class BottomBarTab(
    val route: Route,
    val icon: Int,
    val label: Int
){
    object Notes : BottomBarTab(Route.List, R.drawable.ic_note_stack, R.string.notes)
    object Trash : BottomBarTab(Route.Trash, R.drawable.ic_delete, R.string.trash)
    object Settings : BottomBarTab(Route.Settings, R.drawable.ic_settings, R.string.settings)

    companion object {
        val allTabs = listOf(Notes, Trash, Settings)
    }
}

@Composable
fun QuickNoteBottomBar(
    currentRoute: Route?,
    onNavigate: (Route) -> Unit,
){
    NavigationBar(
        modifier = Modifier
            .clip(RoundedCornerShape(topStart = 18.dp, topEnd = 18.dp))
    ) {
        BottomBarTab.allTabs.forEach { tab ->
            NavigationBarItem(
                icon = {
                    Icon(
                        painter = painterResource(tab.icon),
                        contentDescription = null,
                        modifier = Modifier.size(28.dp)
                    )
                },
                label = { Text(text = stringResource(tab.label)) },
                selected = currentRoute == tab.route,
                onClick = { onNavigate(tab.route) }
            )
        }
    }
}
*/