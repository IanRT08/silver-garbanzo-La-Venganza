package mx.edu.utez.silvergarbanzo2.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import mx.edu.utez.silvergarbanzo2.ui.components.buttons.PrimaryButton
import mx.edu.utez.silvergarbanzo2.ui.components.inputs.UserInputField
import mx.edu.utez.silvergarbanzo2.ui.components.texts.Title
import mx.edu.utez.silvergarbanzo2.viewmodel.EditPostViewModel


@Composable
fun EditPostScreen(
    viewModel: EditPostViewModel,
    onNavigateBack: () -> Unit
) {

    val isLoading = viewModel.isLoading

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Title(text = "Editar Publicación")
            Spacer(modifier = Modifier.height(16.dp))

            // Título
            UserInputField(
                value = viewModel.title,
                onValueChange = { viewModel.title = it },
                label = "Título"
            )

            // Descripción
            OutlinedTextField(
                value = viewModel.description,
                onValueChange = { viewModel.description = it },
                label = { Text("Descripción") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Fecha
            UserInputField(
                value = viewModel.visitDate,
                onValueChange = { viewModel.visitDate = it },
                label = "Fecha de visita"
            )

            // Dirección
            UserInputField(
                value = viewModel.address,
                onValueChange = { viewModel.address = it },
                label = "Dirección"
            )

            Text(
                "Gestión de Imágenes (Agregar/Eliminar)",
                modifier = Modifier.padding(vertical = 16.dp)
            )

            // Privacidad
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Text("Publicación Pública")
                Spacer(modifier = Modifier.weight(1f))
                Switch(
                    checked = viewModel.isPublic,
                    onCheckedChange = { viewModel.isPublic = it }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            if (isLoading) {
                CircularProgressIndicator()
            } else {

                // Guardar cambios
                PrimaryButton(
                    text = "Guardar Cambios",
                    onClick = { viewModel.onSaveChangesClick() }
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Cancelar
                PrimaryButton(
                    text = "Cancelar",
                    onClick = onNavigateBack
                )
            }

            // Mensajes
            viewModel.errorMessage?.let {
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = it, color = MaterialTheme.colorScheme.error)
            }

            viewModel.successMessage?.let {
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = it, color = MaterialTheme.colorScheme.primary)
            }
        }
    }
}