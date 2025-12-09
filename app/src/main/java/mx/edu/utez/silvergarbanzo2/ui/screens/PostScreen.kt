package mx.edu.utez.silvergarbanzo2.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import mx.edu.utez.silvergarbanzo2.viewmodel.PostViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostScreen(
    viewModel: PostViewModel
) {
    val scrollState = rememberScrollState()

    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                        viewModel.fechaVisita = formatter.format(Date(millis))
                    }
                    showDatePicker = false
                }) {
                    Text("Aceptar")
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

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            Text(
                text = "Crear publicación",
                style = MaterialTheme.typography.headlineSmall
            )

            OutlinedTextField(
                value = viewModel.titulo,
                onValueChange = { viewModel.titulo = it },
                label = {
                    Text("Título *")
                },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = viewModel.descripcion,
                onValueChange = { viewModel.descripcion = it },
                label = {
                    Text("Descripción *")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                maxLines = 5
            )

            OutlinedTextField(
                value = viewModel.fechaVisita,
                onValueChange = { },
                readOnly = true,
                label = { Text("Fecha de visita *") },
                placeholder = { Text("DD/MM/AAAA") },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showDatePicker = true }
            )

            Button(
                onClick = {
                    // se conecta con el GPS real
                    viewModel.updateLocation(
                        lat = 18.8500,
                        lng = -99.2000,
                        address = "Ubicación detectada automáticamente"
                    )
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Capturar ubicación actual")
            }

            OutlinedTextField(
                value = viewModel.currentAddress ?: "",
                onValueChange = { viewModel.currentAddress = it },
                label = { Text("Dirección (opcional)") },
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {
                    // Selector de imágenes
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Agregar imágenes (${viewModel.selectedImages.size})")
            }

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Switch(
                    checked = viewModel.isPrivate,
                    onCheckedChange = { viewModel.isPrivate = it }
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (viewModel.isPrivate) "Privado" else "Público"
                )
            }

            viewModel.errorMessage?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error
                )
            }

            Button(
                onClick = { viewModel.createNewPost() },
                enabled = !viewModel.isLoading &&
                        viewModel.titulo.isNotBlank() &&
                        viewModel.descripcion.isNotBlank() &&
                        viewModel.fechaVisita.isNotBlank(),
                modifier = Modifier.fillMaxWidth()
            ) {
                if (viewModel.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("Publicar")
                }
            }
        }
    }
}
