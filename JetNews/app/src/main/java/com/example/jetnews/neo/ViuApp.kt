package com.example.jetnews.neo

import androidx.annotation.DrawableRes
import androidx.compose.Composable
import androidx.compose.state
import androidx.compose.unaryPlus
import androidx.ui.animation.Crossfade
import androidx.ui.core.Modifier
import androidx.ui.core.Text
import androidx.ui.core.dp
import androidx.ui.foundation.shape.corner.RoundedCornerShape
import androidx.ui.graphics.Color
import androidx.ui.layout.*
import androidx.ui.material.*
import androidx.ui.material.surface.Surface
import com.example.jetnews.R
import com.example.jetnews.ui.*
import com.example.jetnews.ui.article.ArticleScreen
import com.example.jetnews.ui.home.HomeScreen
import com.example.jetnews.ui.interests.InterestsScreen

// Author: avinash-savant, (18/12/19), JetNews 
@Composable
fun ViuApp() {
    val (drawerState, onDrawerStateChange) = +state { DrawerState.Closed }

    MaterialTheme(
        colors = lightThemeColors,
        typography = themeTypography
    ) {
        ModalDrawerLayout(
            drawerState = drawerState,
            onStateChange = onDrawerStateChange,
            gesturesEnabled = drawerState == DrawerState.Opened,
            drawerContent = {
                AppDrawer(
                    currentScreen = JetnewsStatus.currentScreen,
                    closeDrawer = { onDrawerStateChange(DrawerState.Closed) }
                )
            },
            bodyContent = { AppContent { onDrawerStateChange(DrawerState.Opened) } }
        )
    }
}

@Composable
private fun AppDrawer(
    currentScreen: Screen,
    closeDrawer: () -> Unit
) {
    Column(modifier = Expanded) {
        HeightSpacer(24.dp)
        Row(modifier = Spacing(16.dp)) {
            VectorImage(
                id = R.drawable.ic_jetnews_logo,
                tint = (+MaterialTheme.colors()).primary
            )
            WidthSpacer(8.dp)
            VectorImage(id = R.drawable.ic_jetnews_wordmark)
        }
        Divider(color = Color(0x14333333))
        DrawerButton(
            icon = R.drawable.ic_home,
            label = "Home",
            isSelected = currentScreen == Screen.Home
        ) {
            navigateTo(Screen.Home)
            closeDrawer()
        }

        DrawerButton(
            icon = R.drawable.ic_interests,
            label = "Interests",
            isSelected = currentScreen == Screen.Interests
        ) {
            navigateTo(Screen.Interests)
            closeDrawer()
        }
    }
}

@Composable
private fun AppContent(openDrawer: () -> Unit) {
    Crossfade(JetnewsStatus.currentScreen) { screen ->
        Surface(color = (+MaterialTheme.colors()).background) {
            when (screen) {
                is Screen.Home -> HomeScreen { openDrawer() }
                is Screen.Interests -> InterestsScreen { openDrawer() }
                is Screen.Article -> ArticleScreen(postId = screen.postId)
            }
        }
    }
}

@Composable
private fun DrawerButton(
    modifier: Modifier = Modifier.None,
    @DrawableRes icon: Int,
    label: String,
    isSelected: Boolean,
    action: () -> Unit
) {
    val colors = +MaterialTheme.colors()
    val textIconColor = if (isSelected) {
        colors.primary
    } else {
        colors.onSurface.copy(alpha = 0.6f)
    }
    val backgroundColor = if (isSelected) {
        colors.primary.copy(alpha = 0.12f)
    } else {
        colors.surface
    }

    Surface(
        modifier = modifier wraps Spacing(left = 8.dp, top = 8.dp, right = 8.dp),
        color = backgroundColor,
        shape = RoundedCornerShape(4.dp)
    ) {
        Button(onClick = action, style = TextButtonStyle()) {
            Row {
                VectorImage(
                    modifier = Gravity.Center,
                    id = icon,
                    tint = textIconColor
                )
                WidthSpacer(16.dp)
                Text(
                    text = label,
                    style = (+MaterialTheme.typography()).body2.copy(
                        color = textIconColor
                    )
                )
            }
        }
    }
}
