package mx.edu.utez.silvergarbanzo2.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import mx.edu.utez.silvergarbanzo2.data.model.User
import mx.edu.utez.silvergarbanzo2.data.repository.UserRepository

class LoginViewModel(private val repository: UserRepository) : ViewModel() {
    var correo by mutableStateOf("")
    var password by mutableStateOf("")
    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)

    var user by mutableStateOf<User?>(null)
        private set

    var isLoginSuccess by mutableStateOf(false)

    fun onLoginClick() {
        if (correo.isBlank() || password.isBlank()) {
            errorMessage = "Ingresa correo y contraseña"
            return
        }

        isLoading = true
        errorMessage = null

        viewModelScope.launch {
            val result = repository.login(correo, password)
            isLoading = false

            result.onSuccess { loggedUser ->
                user = loggedUser
                isLoginSuccess = true
            }.onFailure { error ->
                errorMessage = error.message ?: "Error al iniciar sesión"
            }
        }
    }
}