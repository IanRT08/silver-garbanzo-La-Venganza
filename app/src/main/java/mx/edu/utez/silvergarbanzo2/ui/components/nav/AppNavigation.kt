package mx.edu.utez.silvergarbanzo2.ui.components.nav

import android.app.Application
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import mx.edu.utez.silvergarbanzo2.data.local.AppDatabase
import mx.edu.utez.silvergarbanzo2.data.model.User
import mx.edu.utez.silvergarbanzo2.data.remote.ApiService
import mx.edu.utez.silvergarbanzo2.data.remote.RetrofitClient
import mx.edu.utez.silvergarbanzo2.data.repository.PostRepository
import mx.edu.utez.silvergarbanzo2.data.repository.UserRepository
import mx.edu.utez.silvergarbanzo2.ui.screens.*
import mx.edu.utez.silvergarbanzo2.viewmodel.LoginViewModel
import mx.edu.utez.silvergarbanzo2.viewmodel.PostViewModel
import mx.edu.utez.silvergarbanzo2.viewmodel.RegisterViewModel
import mx.edu.utez.silvergarbanzo2.viewmodel.factories.LoginViewModelFactory
import mx.edu.utez.silvergarbanzo2.viewmodel.factories.PostViewModelFactory
import mx.edu.utez.silvergarbanzo2.viewmodel.factories.RegisterViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val application = context.applicationContext as Application

    // Usuario actual (debería venir de tu sesión/SharedPreferences)
    var currentUser by remember {
        mutableStateOf(
            User(
                id = 1,
                nombre = "Usuario",
                apellidos = "Ejemplo",
                correo = "usuario@ejemplo.com"
            )
        )
    }

    // 1. Dependencias
    val apiService = RetrofitClient.apiService
    val database = AppDatabase.getDatabase(context)
    val userRepository = UserRepository(apiService as ApiService)
    val postRepository = PostRepository(apiService, database.postDao())

    // 2. ViewModels
    val registerViewModel: RegisterViewModel =
        viewModel(factory = RegisterViewModelFactory(userRepository))
    val loginViewModel: LoginViewModel =
        viewModel(factory = LoginViewModelFactory(userRepository))
    val postViewModel: PostViewModel =
        viewModel(factory = PostViewModelFactory(postRepository))

    // Lógica para ocultar barra de navegación
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
            // --- LOGIN ---
            composable(Screen.Login.route) {
                LoginScreen(
                    viewModel = loginViewModel,
                    onNavigateToRegister = {
                        navController.navigate(Screen.Register.route)
                    },
                    onLoginSuccess = {
                        navController.navigate(Screen.Tarjetas.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    }
                )
            }

            // --- REGISTRO ---
            composable(Screen.Register.route) {
                RegisterScreen(
                    viewModel = registerViewModel,
                    onRegistrationSuccess = {
                        navController.navigate(Screen.Login.route) {
                            popUpTo(Screen.Register.route) { inclusive = true }
                        }
                    },
                    onNavigateBack = {
                        navController.popBackStack()
                    }
                )
            }

            // --- HOME/FEED (TARJETAS) ---
            composable(Screen.Tarjetas.route) {
                HomeScreen(
                    viewModel = postViewModel,
                    onPostClick = { postId ->
                        navController.navigate("post_detail/$postId")
                    }
                )
            }

            // --- PERFIL ---
            composable(Screen.Profile.route) {
                ProfileScreen(
                    user = currentUser,
                    postViewModel = postViewModel,
                    onCreatePostClick = {
                        navController.navigate(Screen.Post.route)
                    },
                    onEditPostClick = { postId ->
                        navController.navigate("edit_post/$postId")
                    }
                )
            }

            // --- CREAR PUBLICACIÓN ---
            composable(Screen.Post.route) {
                CreatePostScreen(
                    viewModel = postViewModel,
                    onNavigateBack = {
                        navController.popBackStack()
                    },
                    onPostCreated = {
                        navController.popBackStack()
                    }
                )
            }

            // --- MAPA ---
            composable(Screen.Map.route) {
                MapScreen(
                    postViewModel = postViewModel,
                    currentUserId = currentUser.id,
                    onMarkerClick = { postId ->
                        navController.navigate("post_detail/$postId")
                    }
                )
            }

            // --- MIS LUGARES (mismo que perfil por ahora) ---
            composable(Screen.MyLocations.route) {
                ProfileScreen(
                    user = currentUser,
                    postViewModel = postViewModel,
                    onCreatePostClick = {
                        navController.navigate(Screen.Post.route)
                    },
                    onEditPostClick = { postId ->
                        navController.navigate("edit_post/$postId")
                    }
                )
            }

            // --- DETALLE DE PUBLICACIÓN ---
            composable(
                route = "post_detail/{postId}",
                arguments = listOf(navArgument("postId") { type = NavType.IntType })
            ) { backStackEntry ->
                val postId = backStackEntry.arguments?.getInt("postId") ?: 0
                val post = postViewModel.posts.find { it.id == postId }

                if (post != null) {
                    PostDetailScreen(
                        post = post,
                        onNavigateBack = {
                            navController.popBackStack()
                        },
                        onNavigateToMap = {
                            navController.navigate(Screen.Map.route)
                        },
                        onVisitClick = {
                            postViewModel.recordVisit(postId)
                        }
                    )
                }
            }

            // --- EDITAR PUBLICACIÓN ---
            composable(
                route = "edit_post/{postId}",
                arguments = listOf(navArgument("postId") { type = NavType.IntType })
            ) { backStackEntry ->
                val postId = backStackEntry.arguments?.getInt("postId") ?: 0
                val post = postViewModel.posts.find { it.id == postId }

                if (post != null) {
                    EditPostScreen(
                        post = post,
                        viewModel = postViewModel,
                        onNavigateBack = {
                            navController.popBackStack()
                        },
                        onPostUpdated = {
                            navController.popBackStack()
                        }
                    )
                }
            }
        }
    }
}