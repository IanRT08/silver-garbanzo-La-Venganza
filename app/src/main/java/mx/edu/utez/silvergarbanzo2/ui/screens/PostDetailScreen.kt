package mx.edu.utez.silvergarbanzo2.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import mx.edu.utez.silvergarbanzo2.data.model.Post

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostDetailScreen(
    post: Post,
    onNavigateBack: () -> Unit,
    onNavigateToMap: () -> Unit,
    onVisitClick: () -> Unit
){
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle del Lugar") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            // GALERÍA DE IMÁGENES
            if (post.imagenes.isNotEmpty()) {
                Box {
                    val pagerState = rememberPagerState(
                        pageCount = { post.imagenes.size }
                    )

                    HorizontalPager(
                        state = pagerState,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp)
                    ) { page ->
                        Image(
                            painter = rememberAsyncImagePainter(post.imagenes[page]),
                            contentDescription = "Imagen ${page + 1}",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }

                    // Contador de imágenes
                    Surface(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(16.dp),
                        shape = MaterialTheme.shapes.small,
                        color = Color.Black.copy(alpha = 0.6f)
                    ) {
                        Text(
                            text = "${pagerState.currentPage + 1}/${post.imagenes.size}",
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            color = Color.White,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }

                    // Indicadores de página
                    if (post.imagenes.size > 1) {
                        Row(
                            Modifier
                                .align(Alignment.BottomCenter)
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            repeat(post.imagenes.size) { index ->
                                Box(
                                    modifier = Modifier
                                        .size(10.dp)
                                        .clip(CircleShape)
                                        .background(
                                            if (pagerState.currentPage == index)
                                                Color.White
                                            else
                                                Color.White.copy(alpha = 0.5f)
                                        )
                                )
                            }
                        }
                    }
                }
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                        .background(Color.LightGray),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Sin imágenes disponibles", color = Color.Gray)
                }
            }
            // CONTENIDO
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // TÍTULO
                Text(
                    text = post.titulo,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
                // DESCRIPCIÓN
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Text(
                        text = post.descripcion,
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }

                Divider()

                // INFORMACIÓN DE UBICACIÓN
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "📍 Ubicación",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )

                        if (post.direccion != null) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    Icons.Default.LocationOn,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = post.direccion,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                        Text(
                            text = "Coordenadas:",
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Lat: ${post.latitud}",
                            style = MaterialTheme.typography.bodySmall
                        )
                        Text(
                            text = "Lng: ${post.longitud}",
                            style = MaterialTheme.typography.bodySmall
                        )

                        if (post.ciudad != null || post.pais != null) {
                            Text(
                                text = "${post.ciudad ?: ""}, ${post.pais ?: ""}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Button(
                            onClick = onNavigateToMap,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(Icons.Default.LocationOn, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Ver en Mapa")
                        }
                    }
                }
                // INFORMACIÓN ADICIONAL
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        InfoRow("Fecha de visita:", formatDate(post.fechaVisita))
                        InfoRow("Publicado:", formatDate(post.fechaCreacion))
                        InfoRow("Visitas:", post.contadorVisitas.toString())
                        InfoRow("Creado por:", "Usuario #${post.usuarioId}")
                        InfoRow(
                            "Privacidad:",
                            if (post.esPrivado) "Privado" else "Público"
                        )
                    }
                }
                // BOTÓN DE ACCIÓN
                Button(
                    onClick = onVisitClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text(
                        text = "✓ Marcar como Visitado",
                        style = MaterialTheme.typography.titleMedium
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

private fun formatDate(dateString: String): String {
    return try {
        val parts = dateString.split("T")[0].split("-")
        "${parts[2]}/${parts[1]}/${parts[0]}"
    } catch (e: Exception) {
        dateString
    }
}
