package mx.edu.utez.silvergarbanzo2.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import mx.edu.utez.silvergarbanzo2.ui.components.buttons.PrimaryButton
import mx.edu.utez.silvergarbanzo2.ui.components.inputs.UserInputField
import mx.edu.utez.silvergarbanzo2.ui.components.texts.Title
import mx.edu.utez.silvergarbanzo2.viewmodel.RegisterViewModel

@Composable
fun RegisterScreen(
    viewModel: RegisterViewModel,
    onRegistrationSuccess: () -> Unit, // Callback al terminar registro
    onNavigateBack: () -> Unit
) {
    LaunchedEffect(viewModel.isRegisterSuccess) {
        if (viewModel.isRegisterSuccess) {
            onRegistrationSuccess()
            viewModel.isRegisterSuccess = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Title(text = "Registro de usuario")

        Spacer(modifier = Modifier.height(16.dp))

        //Nombre
        UserInputField(
            value = viewModel.nombre,
            onValueChange = { viewModel.nombre = it },
            label = "Nombre"
        )

        //Apellidos
        UserInputField(
            value = viewModel.apellidos,
            onValueChange = { viewModel.apellidos = it },
            label = "Apellidos"
        )

        //Correo
        UserInputField(
            value = viewModel.correo,
            onValueChange = { viewModel.correo = it },
            label = "Correo"
        )

        //Contraseña
        UserInputField(
            value = viewModel.password,
            onValueChange = { viewModel.password = it },
            label = "Contraseña"
        )

        //Repetir Contraseña
        OutlinedTextField(
            value = viewModel.confirmPassword,
            onValueChange = { viewModel.confirmPassword = it },
            label = { Text("Repetir Contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            isError = viewModel.password != viewModel.confirmPassword && viewModel.confirmPassword.isNotEmpty(),
            supportingText = {
                if (viewModel.password != viewModel.confirmPassword && viewModel.confirmPassword.isNotEmpty()) {
                    Text("Las contraseñas no coinciden")
                }
            }
        )

        Spacer(modifier = Modifier.height(24.dp))

        if (viewModel.isLoading) {
            CircularProgressIndicator()
        } else {


            Spacer(modifier = Modifier.height(8.dp))

            // Botón para cancelar / volver al login manualmente
            PrimaryButton(
                text = "Cancelar",
                onClick = onNavigateBack
            )
        }

        // Mensajes de error/éxito
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