package mx.edu.utez.silvergarbanzo2.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import mx.edu.utez.silvergarbanzo2.ui.components.cards.PostCard
import mx.edu.utez.silvergarbanzo2.viewmodel.PostViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: PostViewModel,
    onPostClick: (Int)-> Unit //Para navegar al detalle
){
    //Cargar posts cuando inicias
    LaunchedEffect(Unit) {
        viewModel.loadPublicPosts()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Publicaciones Públicas")},
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { padding->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ){
            when {
                viewModel.isLoading->{
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                viewModel.errorMessage != null ->{
                    Column(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ){
                        Text(
                            text = "Error: ${viewModel.errorMessage}",
                            color = MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = {viewModel.loadPublicPosts()}) {
                            Text("Reintentar")
                        }
                    }
                }
                viewModel.posts.isEmpty()->{
                    Text(
                        text = "No hay publicaciones disponibles",
                        modifier = Modifier.align(Alignment.Center),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
                else->{
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ){
                        items(viewModel.posts){ post ->
                            PostCard(
                                post = post,
                                onVisitClick = {
                                    viewModel.recordVisit(post.id)
                                },
                                onPostClick={
                                    onPostClick(post.id)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}