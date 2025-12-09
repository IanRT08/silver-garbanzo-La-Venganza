package mx.edu.utez.silvergarbanzo2.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class ProfileViewModel : ViewModel() {
    // Información del usuario
    var fullName by mutableStateOf("Juan Pérez")
    var email by mutableStateOf("juanperez@email.com")
    var registerDate by mutableStateOf("12-10-2024")

    // Foto de perfil (puede ser URL o null)
    var photoUrl by mutableStateOf<String?>(null)

    // Estadísticas
    var totalPosts by mutableStateOf(15)
    var totalVisits by mutableStateOf(245)

    // Top 3 lugares
    val topPlaces = listOf(
        "Lago de Tequesquitengo",
        "Grutas de Tolantongo",
        "Cholula, Puebla"
    )
}