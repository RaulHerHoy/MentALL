package com.example.mentall

import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api



data class BottomItem(val route: Route, val label: String, val icon: ImageVector)

@Composable
fun MentALLRoot() {
    val nav = rememberNavController()
    var isLogged by rememberSaveable { mutableStateOf(false) }

    val start = if (isLogged) "main" else Route.Login.path

    NavHost(navController = nav, startDestination = start) {

        composable(Route.Login.path) {
            LoginScreen(
                onLogin = {
                    isLogged = true
                    nav.navigate("main") { popUpTo(Route.Login.path) { inclusive = true } }
                },
                onGoRegister = { nav.navigate(Route.Register.path) }
            )
        }

        composable(Route.Register.path) {
            RegisterScreen(
                onRegisterDone = {
                    isLogged = true
                    nav.navigate("main") { popUpTo(Route.Login.path) { inclusive = true } }
                },
                onBack = { nav.popBackStack() }
            )
        }

        // ✅ Todo lo logado vive aquí: bottom bar fija
        composable("main") {
            MainScaffold(
                nav = rememberNavController(),
                onLogout = {
                    isLogged = false
                    nav.navigate(Route.Login.path) { popUpTo("main") { inclusive = true } }
                }
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MainScaffold(nav: NavHostController, onLogout: () -> Unit) {

    val items = listOf(
        BottomItem(Route.Home, "Home", Icons.Default.Home),
        BottomItem(Route.EmotionEntry, "Registro", Icons.Default.AddCircle),
        BottomItem(Route.Activities, "Actividades", Icons.Default.List),
        BottomItem(Route.Sos, "SOS", Icons.Default.Warning),
        BottomItem(Route.Skills, "Skills", Icons.Default.Share),
    )


    // ✅ Esto lee la ruta actual del NavController
    val backStackEntry by nav.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("MentALL") },
                actions = {
                    IconButton(
                        onClick = {
                            ThemeController.darkTheme.value = !ThemeController.darkTheme.value
                        }
                    ) {
                        Icon(
                            imageVector = if (ThemeController.darkTheme.value)
                                Icons.Default.LightMode
                            else
                                Icons.Default.DarkMode,
                            contentDescription = "Cambiar tema"
                        )
                    }

                }
            )
        },
        bottomBar = {
            NavigationBar {
                items.forEach { it ->
                    NavigationBarItem(
                        selected = currentRoute == it.route.path,
                        onClick = {
                            nav.navigate(it.route.path) {
                                // ✅ Esto evita que se acumulen pantallas
                                popUpTo(Route.Home.path) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { Icon(it.icon, contentDescription = it.label) },
                        label = { Text(it.label) }
                    )
                }
            }
        }
    ) { innerPadding ->

        // ✅ NavHost DENTRO del Scaffold (la barra nunca desaparece)
        NavHost(
            navController = nav,
            startDestination = Route.Home.path,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Route.Home.path) {
                HomeScreen(
                    onGoEmotionEntry = { nav.navigate(Route.EmotionEntry.path) },
                    onGoActivities = { nav.navigate(Route.Activities.path) },
                    onGoSos = { nav.navigate(Route.Sos.path) },
                    onGoProfile = { nav.navigate(Route.Profile.path) } // ✅ nuevo
                )

            }
            composable(Route.EmotionEntry.path) {
                EmotionEntryScreen(onBack = { /* opcional */ })
            }
            composable(Route.Activities.path) {
                ActivitiesScreen()
            }
            composable(Route.Sos.path) {
                SosScreen()
            }
            composable(Route.Profile.path) {
                ProfileScreen(onLogout = onLogout)
            }

            // 🔹 Skills como pantalla "modal" pero sigue con bottom bar fija
            composable(Route.Skills.path) {
                SkillsScreen()
            }
        }
    }
}

