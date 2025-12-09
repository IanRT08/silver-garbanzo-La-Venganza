package mx.edu.utez.silvergarbanzo2.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class EditPostViewModel : ViewModel() {

    // Campos del formulario
    var title by mutableStateOf("Mi visita a Teques")
    var description by mutableStateOf("Fue un día increíble en el lago...")
    var visitDate by mutableStateOf("25-11-2025")
    var address by mutableStateOf("Lago de Tequesquitengo, Morelos")
    var isPublic by mutableStateOf(true)

    // Estados
    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)
    var successMessage by mutableStateOf<String?>(null)

    // Simulación de guardado
    fun onSaveChangesClick() {
        isLoading = true
        errorMessage = null
        successMessage = null

        // Aquí después iría lógica de API/BD
        if (title.isBlank() || description.isBlank()) {
            isLoading = false
            errorMessage = "Título y descripción son obligatorios"
            return
        }

        // Simulación exitosa
        isLoading = false
        successMessage = "Cambios guardados correctamente"
    }

    // Para precargar datos reales (cuando vengas de otra pantalla)
    fun loadPostData(
        title: String,
        description: String,
        visitDate: String,
        address: String,
        isPublic: Boolean
    ) {
        this.title = title
        this.description = description
        this.visitDate = visitDate
        this.address = address
        this.isPublic = isPublic
    }
}