package mx.edu.utez.silvergarbanzo2.ui.components.cards

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import mx.edu.utez.silvergarbanzo2.data.model.Post
import mx.edu.utez.silvergarbanzo2.data.model.User

@Composable
fun PostCard(
    post: Post,
    onVisitClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column {

            if (post.imagenes.isNotEmpty()) {
                val pagerState = rememberPagerState(
                    pageCount = { post.imagenes.size }
                )

                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp)
                ) { page ->
                    Image(
                        painter = rememberAsyncImagePainter(post.imagenes[page]),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .background(Color.LightGray),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Sin imágenes")
                }
            }

            Column(
                modifier = Modifier.padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {

                Text(
                    text = post.titulo,
                    style = MaterialTheme.typography.titleLarge
                )

                Text(
                    text = post.descripcion,
                    style = MaterialTheme.typography.bodyMedium
                )

                post.direccion?.let {
                    Text(
                        text = "📍 $it",
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                Text(
                    text = post.fechaVisita,
                    style = MaterialTheme.typography.bodySmall
                )

                Text(
                    text = "${post.contadorVisitas} visitas",
                    style = MaterialTheme.typography.bodySmall
                )

                Text(
                    text = "Por: ${post.id}",
                    style = MaterialTheme.typography.bodySmall
                )

                Button(
                    onClick = onVisitClick,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Visitar")
                }
            }
        }
    }
}
