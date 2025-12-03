package mx.edu.utez.silvergarbanzo2.ui.components.nav

import android.app.Application
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import mx.edu.utez.silvergarbanzo2.data.remote.ApiService
import mx.edu.utez.silvergarbanzo2.data.remote.RetrofitClient
import mx.edu.utez.silvergarbanzo2.data.repository.UserRepository
import mx.edu.utez.silvergarbanzo2.ui.screens.LoginScreen
import mx.edu.utez.silvergarbanzo2.ui.screens.RegisterScreen
import mx.edu.utez.silvergarbanzo2.viewmodel.LoginViewModel
import mx.edu.utez.silvergarbanzo2.viewmodel.RegisterViewModel
import mx.edu.utez.silvergarbanzo2.viewmodel.factories.LoginViewModelFactory
import mx.edu.utez.silvergarbanzo2.viewmodel.factories.RegisterViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val application = context.applicationContext as Application

    // 1. Dependencias
    val apiService = RetrofitClient.apiService
    val userRepository = UserRepository(apiService as ApiService)
    // 2. ViewModels
    val registerViewModel: RegisterViewModel =
        viewModel(factory = RegisterViewModelFactory(userRepository))
    val loginViewModel: LoginViewModel = viewModel(factory = LoginViewModelFactory(userRepository))

    // Lógica para ocultar barra de navegación en Login y Registro
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry.value?.destination?.route
    val showBottomBar = navBarItems.any { it.route == currentRoute }

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                AppBottomNavBar(navController = navController)
            }
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Login.route,
            modifier = Modifier.padding(padding)
        ) {
            // --- PANTALLA DE LOGIN ---
            composable(Screen.Login.route) {
                LoginScreen(
                    viewModel = loginViewModel,
                    onNavigateToRegister = {
                        navController.navigate(Screen.Register.route)
                    },
                    onLoginSuccess = {
                        // Navegar a publicaciones y limpiar el historial para que 'Atrás' cierre la app
                        navController.navigate(Screen.Tarjetas.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    }
                )
            }

            // --- PANTALLA DE REGISTRO ---
            composable(Screen.Register.route) {
                RegisterScreen(
                    viewModel = registerViewModel,
                    onRegistrationSuccess = {
                        navController.navigate(Screen.Register.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    },
                    onNavigateBack = {
                        // El usuario presionó "Volver"
                        navController.popBackStack()
                    }
                )
            }

        }

    }
}
