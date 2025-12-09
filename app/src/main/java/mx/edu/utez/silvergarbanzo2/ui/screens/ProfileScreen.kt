package mx.edu.utez.silvergarbanzo2.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import mx.edu.utez.silvergarbanzo2.ui.components.texts.Title
import mx.edu.utez.silvergarbanzo2.viewmodel.ProfileViewModel

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {

            Title(text = "Mi Perfil")

            Spacer(modifier = Modifier.height(16.dp))

            if (viewModel.photoUrl != null) {
                Image(
                    painter = rememberAsyncImagePainter(viewModel.photoUrl),
                    contentDescription = "Foto de perfil",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(120.dp)
                        .clip(MaterialTheme.shapes.medium)
                )
            } else {

                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "Sin foto")
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Nombre
            Text(
                text = viewModel.fullName,
                style = MaterialTheme.typography.titleLarge
            )

            // Correo
            Text(
                text = viewModel.email,
                style = MaterialTheme.typography.bodyMedium
            )

            // Fecha de registro
            Text(
                text = "Registrado el: ${viewModel.registerDate}",
                style = MaterialTheme.typography.bodySmall
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Estadísticas",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {

                    Text(text = "Total de publicaciones: ${viewModel.totalPosts}")
                    Text(text = "Visitas recibidas: ${viewModel.totalVisits}")

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(text = "Lugares más visitados:")
                    viewModel.topPlaces.forEach { place ->
                        Text(text = "• $place")
                    }
                }
            }
        }
    }
}
