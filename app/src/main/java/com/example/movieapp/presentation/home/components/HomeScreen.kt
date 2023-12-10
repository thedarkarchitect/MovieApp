package com.example.movieapp.presentation.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Movie
import androidx.compose.material.icons.rounded.Upcoming
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.movieapp.presentation.home.MovieListState
import com.example.movieapp.presentation.home.MovieListUiEvent
import com.example.movieapp.ui.theme.MovieAppTheme
import com.example.movieapp.util.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onEvent: (MovieListUiEvent) -> Unit,
    state: MovieListState,
    navController: NavController
) {
    val bottomNavController = rememberNavController()

    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                bottomNavController = bottomNavController ,
                onEvent = onEvent
            )
        },
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = if (state.isCurrentPopularScreen){
                            "Popular Movies"
                        } else {
                            "Upcoming Movies"
                        }
                    )
                },
                modifier = Modifier.shadow(2.dp),
                colors = TopAppBarDefaults.topAppBarColors(
                    MaterialTheme.colorScheme.inverseOnSurface
                )
            )
        }

    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ){
            NavHost(
                navController = bottomNavController,
                startDestination = Screen.PopularMovieList.route
            ) {
                composable(Screen.PopularMovieList.route) {
//                    PopularMoviesScreen()
                }
                composable(Screen.UpcomingMovieList.route) {
//                    UpcomingMoviesScreen()
                }
            }
        }
    }

}

@Composable
fun BottomNavigationBar(
    bottomNavController: NavController,
    onEvent: (MovieListUiEvent) -> Unit
) {

    val items = listOf(
        BottomItem(
            title = "Popular",
            icon = Icons.Rounded.Movie
        ),
        BottomItem(
            title = "Upcoming",
            icon = Icons.Rounded.Upcoming
        )
    )

    val selected = rememberSaveable {
        mutableIntStateOf(0)
    }

    NavigationBar {
        Row(
            modifier = Modifier.background(MaterialTheme.colorScheme.inverseOnSurface)
        ) {
            items.forEachIndexed { index, bottomItem ->
                NavigationBarItem(
                    selected = selected.intValue == index,
                    onClick = {
                              selected.intValue = index
                        when(selected.intValue){
                            0 -> {
                                onEvent(MovieListUiEvent.Navigate)
                                bottomNavController.popBackStack()
                                bottomNavController.navigate(Screen.PopularMovieList.route)
                            }
                            1 -> {
                                onEvent(MovieListUiEvent.Navigate)
                                bottomNavController.popBackStack()
                                bottomNavController.navigate(Screen.UpcomingMovieList.route)
                            }
                        }
                    },
                    icon = {
                        Icon(
                           imageVector = bottomItem.icon,
                            contentDescription = bottomItem.title,
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    },
                    label = {
                        Text(
                            text = bottomItem.title,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                )
            }
        }
    }

}

data class BottomItem( //bottom bar structure
    val title: String,
    val icon: ImageVector
)

@Preview
@Composable
fun PreviewHomeScreen() {
    MovieAppTheme {
        HomeScreen(
            onEvent = {},
            state = MovieListState(),
            navController = rememberNavController()
        )
    }
}