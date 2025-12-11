package mx.edu.utez.silvergarbanzo2.ui.screens

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import mx.edu.utez.silvergarbanzo2.data.model.Post
import mx.edu.utez.silvergarbanzo2.viewmodel.PostViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditPostScreen(
    post: Post,
    viewModel: PostViewModel,
    onNavigateBack: () -> Unit,
    onPostUpdated: () -> Unit
) {
    val context = LocalContext.current

    // Estados locales para edición
    var titulo by remember { mutableStateOf(post.titulo) }
    var descripcion by remember { mutableStateOf(post.descripcion) }
    var fechaVisita by remember { mutableStateOf(post.fechaVisita) }
    var direccion by remember { mutableStateOf(post.direccion ?: "") }
    var esPrivado by remember { mutableStateOf(post.esPrivado) }
    var showDatePicker by remember { mutableStateOf(false) }
    var selectedImageUris by remember { mutableStateOf<List<Uri>>(emptyList()) }
    var existingImages by remember { mutableStateOf(post.imagenes) }

    // Observar el estado del ViewModel
    val isLoading = viewModel.isLoading
    val errorMessage = viewModel.errorMessage

    // Launcher para seleccionar nuevas imágenes
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents()
    ) { uris ->
        selectedImageUris = uris
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Editar Publicación") },
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
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // CAMPOS EDITABLES

            // Título
            OutlinedTextField(
                value = titulo,
                onValueChange = { titulo = it },
                label = { Text("Título *") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                enabled = !isLoading
            )

            // Descripción
            OutlinedTextField(
                value = descripcion,
                onValueChange = { descripcion = it },
                label = { Text("Descripción *") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3,
                maxLines = 5,
                enabled = !isLoading
            )

            // Fecha de visita
            OutlinedTextField(
                value = formatDateForDisplay(fechaVisita),
                onValueChange = { },
                label = { Text("Fecha de visita *") },
                modifier = Modifier.fillMaxWidth(),
                readOnly = true,
                enabled = !isLoading,
                trailingIcon = {
                    IconButton(onClick = { showDatePicker = true }) {
                        Icon(Icons.Default.DateRange, contentDescription = "Cambiar fecha")
                    }
                }
            )

            if (showDatePicker) {
                val datePickerState = rememberDatePickerState()
                DatePickerDialog(
                    onDismissRequest = { showDatePicker = false },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                datePickerState.selectedDateMillis?.let { millis ->
                                    val sdf = SimpleDateFormat(
                                        "yyyy-MM-dd'T'HH:mm:ss",
                                        Locale.getDefault()
                                    )
                                    fechaVisita = sdf.format(Date(millis))
                                }
                                showDatePicker = false
                            }
                        ) {
                            Text("OK")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showDatePicker = false }) {
                            Text("Cancelar")
                        }
                    }
                ) {
                    DatePicker(state = datePickerState)
                }
            }

            // Dirección manual
            OutlinedTextField(
                value = direccion,
                onValueChange = { direccion = it },
                label = { Text("Dirección") },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Opcional") },
                enabled = !isLoading
            )

            Divider()

            // UBICACIÓN (NO EDITABLE)
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.Lock,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Ubicación GPS (no editable)",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Text(
                        text = "Latitud: ${post.latitud}",
                        style = MaterialTheme.typography.bodySmall
                    )
                    Text(
                        text = "Longitud: ${post.longitud}",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            Divider()

            // GESTIÓN DE IMÁGENES
            Text(
                text = "Imágenes (solo lectura)",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Nota: La edición de imágenes no está disponible. Para cambiar las imágenes, crea una nueva publicación.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            // Imágenes existentes (solo visualización)
            if (existingImages.isNotEmpty()) {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    itemsIndexed(existingImages) { index, imageUrl ->
                        Box(
                            modifier = Modifier.size(100.dp)
                        ) {
                            Image(
                                painter = rememberAsyncImagePainter(imageUrl),
                                contentDescription = "Imagen ${index + 1}",
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(RoundedCornerShape(8.dp)),
                                contentScale = ContentScale.Crop
                            )
                        }
                    }
                }
            }

            Divider()

            // Switch de privacidad
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Publicación privada",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = if (esPrivado) "Solo tú puedes verla" else "Visible para todos",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Switch(
                    checked = esPrivado,
                    onCheckedChange = { esPrivado = it },
                    enabled = !isLoading
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Mensaje de error
            errorMessage?.let { error ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Text(
                        text = error,
                        modifier = Modifier.padding(12.dp),
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )
                }
            }

            // Botón de guardar cambios
            Button(
                onClick = {
                    if (titulo.isNotBlank() && descripcion.isNotBlank() && fechaVisita.isNotBlank()) {
                        // Llamar al método de actualización del ViewModel
                        viewModel.updatePost(
                            postId = post.id,
                            titulo = titulo,
                            descripcion = descripcion,
                            fechaVisita = fechaVisita,
                            direccion = direccion.ifBlank { null },
                            esPrivado = esPrivado
                        )

                        // Mostrar mensaje y navegar de vuelta
                        Toast.makeText(context, "Publicación actualizada", Toast.LENGTH_SHORT).show()
                        onPostUpdated()
                    } else {
                        Toast.makeText(
                            context,
                            "Completa los campos obligatorios",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                enabled = !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Color.White
                    )
                } else {
                    Text("Guardar Cambios")
                }
            }
        }
    }
}

// Helper para formatear fecha legible
private fun formatDateForDisplay(isoDate: String): String {
    return try {
        val parts = isoDate.split("T")[0].split("-")
        "${parts[2]}/${parts[1]}/${parts[0]}"
    } catch (e: Exception) {
        isoDate
    }
}