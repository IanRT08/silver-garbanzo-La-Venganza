package mx.edu.utez.silvergarbanzo2.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import mx.edu.utez.silvergarbanzo2.ui.components.cards.PostCard
import mx.edu.utez.silvergarbanzo2.viewmodel.PostViewModel

@Composable
fun PostsScreen(
    viewModel: PostViewModel
) {

    LaunchedEffect(Unit) {
        viewModel.loadPublicPosts()
    }

    if (viewModel.isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    viewModel.errorMessage?.let {
        Text(text = it, color = MaterialTheme.colorScheme.error)
        return
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(viewModel.posts) { post ->
            PostCard(
                post = post,
                onVisitClick = {
                    viewModel.recordVisit(post.id)
                }
            )
        }
    }
}
