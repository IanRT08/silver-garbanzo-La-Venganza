package mx.edu.utez.silvergarbanzo2.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import mx.edu.utez.silvergarbanzo2.R
import mx.edu.utez.silvergarbanzo2.ui.components.buttons.PrimaryButton
import mx.edu.utez.silvergarbanzo2.ui.components.images.CircleImage
import mx.edu.utez.silvergarbanzo2.ui.components.inputs.PasswordField
import mx.edu.utez.silvergarbanzo2.ui.components.inputs.UserInputField
import mx.edu.utez.silvergarbanzo2.ui.components.texts.Link
import mx.edu.utez.silvergarbanzo2.ui.components.texts.Title
import mx.edu.utez.silvergarbanzo2.viewmodel.LoginViewModel

@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    onLoginSuccess: () -> Unit,
    onNavigateToRegister: () -> Unit
) {
    // Observar si el login fue exitoso para navegar
    LaunchedEffect(viewModel.isLoginSuccess) {
        if (viewModel.isLoginSuccess) {
            onLoginSuccess()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){

        CircleImage(R.drawable.silvergarbanzologo)

        Title(text = "Iniciar Sesión")

        Spacer(modifier = Modifier.height(32.dp))

        UserInputField(
            value = viewModel.correo,
            onValueChange = { viewModel.correo = it },
            label = "Correo"
        )

        Spacer(modifier = Modifier.height(8.dp))

        PasswordField(
            value = viewModel.password,
            onValueChange = { viewModel.password = it },
            label = "Contraseña"
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (viewModel.errorMessage != null) {
            Text(
                text = viewModel.errorMessage!!,
                color = MaterialTheme.colorScheme.error
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        if (viewModel.isLoading) {
            CircularProgressIndicator()
        } else {
            PrimaryButton(
                text = "Iniciar Sesión",
                onClick = { viewModel.onLoginClick() }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Link(
            text = "¿No tienes cuenta? Regístrate aquí",
            onClick = onNavigateToRegister
        )

    }

}