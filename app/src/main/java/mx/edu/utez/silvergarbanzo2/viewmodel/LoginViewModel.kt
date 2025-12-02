package mx.edu.utez.silvergarbanzo2.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import mx.edu.utez.silvergarbanzo2.data.repository.UserRepository

class LoginViewModel(private val repository: UserRepository) : ViewModel() {
    var correo by mutableStateOf("")
    var password by mutableStateOf("")
    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)

    // Estado para notificar a la UI que debe navegar
    var isLoginSuccess by mutableStateOf(false)

    fun onLoginClick() {
        if (correo.isBlank() || password.isBlank()) {
            errorMessage = "Ingresa matrícula y contraseña"
            return
        }

        isLoading = true
        errorMessage = null

        viewModelScope.launch {
            val result = repository.login(correo, password)
            isLoading = false

            result.onSuccess {
                isLoginSuccess = true // Esto disparará la navegación en la UI
            }.onFailure { error ->
                errorMessage = error.message ?: "Error al iniciar sesión"
            }
        }
    }
}