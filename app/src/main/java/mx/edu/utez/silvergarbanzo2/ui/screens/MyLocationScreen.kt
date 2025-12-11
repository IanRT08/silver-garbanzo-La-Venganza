package mx.edu.utez.silvergarbanzo2.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import mx.edu.utez.silvergarbanzo2.data.model.User
import mx.edu.utez.silvergarbanzo2.ui.components.cards.PostCard
import mx.edu.utez.silvergarbanzo2.viewmodel.PostViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyLocationsScreen(
    user: User,
    postViewModel: PostViewModel,
    onCreatePostClick: () -> Unit,
    onPostClick: (Int) -> Unit
) {
    // Cargar publicaciones del usuario al iniciar
    LaunchedEffect(user.id) {
        postViewModel.loadUserPosts(user.id)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mis Lugares") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onCreatePostClick,
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Crear lugar")
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when {
                postViewModel.isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                postViewModel.errorMessage != null -> {
                    Column(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Error: ${postViewModel.errorMessage}",
                            color = MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = { postViewModel.loadUserPosts(user.id) }) {
                            Text("Reintentar")
                        }
                    }
                }

                postViewModel.posts.isEmpty() -> {
                    Column(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "No tienes lugares registrados",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "¡Crea tu primer lugar!",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(postViewModel.posts) { post ->
                            PostCard(
                                post = post,
                                onVisitClick = {
                                    postViewModel.recordVisit(post.id)
                                },
                                onPostClick = {
                                    onPostClick(post.id)
                                }
                            )
                        }

                        item {
                            Spacer(modifier = Modifier.height(80.dp))
                        }
                    }
                }
            }
        }
    }
}