package mx.edu.utez.silvergarbanzo2.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import mx.edu.utez.silvergarbanzo2.data.model.UserRequest
import mx.edu.utez.silvergarbanzo2.data.repository.UserRepository

class RegisterViewModel(private val repository: UserRepository) : ViewModel() {

    // Estados para los campos de texto
    var nombre by mutableStateOf("")
    var apellidos by mutableStateOf("")
    var correo by mutableStateOf("")
    var password by mutableStateOf("")
    var confirmPassword by mutableStateOf("")

    // Estado para la UI (Carga y mensajes)
    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)
    var successMessage by mutableStateOf<String?>(null)
    var isRegisterSuccess by mutableStateOf(false)

    fun onRegisterClick() {
        // 1. Validaciones Locales
        if (nombre.isBlank() || apellidos.isBlank() || correo.isBlank()) {
            errorMessage = "Todos los campos son obligatorios"
            return
        }
        if (password != confirmPassword) {
            errorMessage = "Las contraseñas no coinciden"
            return
        }

        // 2. Preparar datos y llamar al repo
        isLoading = true
        errorMessage = null
        successMessage = null
        isRegisterSuccess = false

        val newUser = UserRequest(
            nombre = nombre,
            apellidos = apellidos,
            correo = correo,
            contrasena = password
        )

        viewModelScope.launch {
            val result = repository.registrarUsuario(newUser)
            isLoading = false
            result.onSuccess { msg ->
                successMessage = msg
                limpiarFormulario()
                isRegisterSuccess = true
            }.onFailure { error ->
                errorMessage = error.message ?: "Error desconocido"
            }
        }
    }

    private fun limpiarFormulario() {
        nombre = ""
        apellidos = ""
        correo = ""
        password = ""
        confirmPassword = ""
    }
}