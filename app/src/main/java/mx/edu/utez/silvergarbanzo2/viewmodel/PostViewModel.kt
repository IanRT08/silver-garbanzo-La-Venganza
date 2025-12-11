package mx.edu.utez.silvergarbanzo2.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import mx.edu.utez.silvergarbanzo2.data.model.Post
import mx.edu.utez.silvergarbanzo2.data.repository.PostRepository
import java.io.File

class PostViewModel(private val repository: PostRepository) : ViewModel() {

    var posts by mutableStateOf<List<Post>>(emptyList())
        private set
    var isLoading by mutableStateOf(false)
        private set
    var errorMessage by mutableStateOf<String?>(null)
        private set

    // Para el formulario de nueva publicación
    var titulo by mutableStateOf("")
    var descripcion by mutableStateOf("")
    var fechaVisita by mutableStateOf("")
    var selectedImages by mutableStateOf<List<File>>(emptyList())
    var isPrivate by mutableStateOf(false)

    // Ubicación (se obtendrá del GPS)
    var currentLatitude by mutableStateOf(0.0)
    var currentLongitude by mutableStateOf(0.0)
    var currentAddress by mutableStateOf<String?>(null)

    fun loadPublicPosts() {
        isLoading = true
        errorMessage = null
        viewModelScope.launch {
            val result = repository.getPublicPosts()
            isLoading = false
            result.onSuccess { postList ->
                posts = postList
            }.onFailure { error ->
                errorMessage = error.message
            }
        }
    }

    fun createNewPost() {
        isLoading = true
        errorMessage = null
        viewModelScope.launch {
            val result = repository.createPostWithUris(
                titulo = titulo,
                descripcion = descripcion,
                latitud = currentLatitude,
                longitud = currentLongitude,
                fechaVisita = fechaVisita,
                direccion = currentAddress,
                esPrivado = isPrivate,
                imageFiles = selectedImages
            )
            isLoading = false
            result.onSuccess { newPost ->
                posts = posts + newPost
                clearForm()
            }.onFailure { error ->
                errorMessage = error.message
            }
        }
    }

    // Método para actualizar una publicación
    fun updatePost(
        postId: Int,
        titulo: String,
        descripcion: String,
        fechaVisita: String,
        direccion: String?,
        esPrivado: Boolean
    ) {
        isLoading = true
        errorMessage = null
        viewModelScope.launch {
            // Buscar el post actual
            val currentPost = posts.find { it.id == postId }

            if (currentPost == null) {
                errorMessage = "Publicación no encontrada"
                isLoading = false
                return@launch
            }

            // Crear post actualizado (manteniendo ubicación e imágenes)
            val updatedPost = currentPost.copy(
                titulo = titulo,
                descripcion = descripcion,
                fechaVisita = fechaVisita,
                direccion = direccion,
                esPrivado = esPrivado
            )

            val result = repository.updatePost(updatedPost)
            isLoading = false

            result.onSuccess { success ->
                if (success) {
                    // Actualizar en la lista local
                    posts = posts.map { post ->
                        if (post.id == postId) updatedPost else post
                    }
                    errorMessage = null
                } else {
                    errorMessage = "Error al actualizar"
                }
            }.onFailure { error ->
                errorMessage = error.message
            }
        }
    }

    fun deletePost(postId: Int) {
        isLoading = true
        errorMessage = null
        viewModelScope.launch {
            val result = repository.deletePost(postId)
            isLoading = false
            result.onSuccess {
                posts = posts.filter { it.id != postId }
                errorMessage = null
            }.onFailure { error ->
                errorMessage = error.message
            }
        }
    }

    fun recordVisit(postId: Int) {
        viewModelScope.launch {
            repository.incrementVisitCount(postId)
            // Actualizar en la lista local
            posts = posts.map { post ->
                if (post.id == postId) {
                    post.copy(contadorVisitas = post.contadorVisitas + 1)
                } else {
                    post
                }
            }
        }
    }

    private fun clearForm() {
        titulo = ""
        descripcion = ""
        fechaVisita = ""
        selectedImages = emptyList()
        isPrivate = false
        currentLatitude = 0.0
        currentLongitude = 0.0
        currentAddress = null
    }

    fun addImage(file: File) {
        selectedImages = selectedImages + file
    }

    fun removeImage(index: Int) {
        selectedImages = selectedImages.toMutableList().apply {
            removeAt(index)
        }
    }

    fun updateLocation(lat: Double, lng: Double, address: String? = null) {
        currentLatitude = lat
        currentLongitude = lng
        currentAddress = address
    }

    fun loadUserPosts(userId: Int) {
        isLoading = true
        errorMessage = null
        viewModelScope.launch {
            val result = repository.getUserPosts(userId)
            isLoading = false
            result.onSuccess { postList ->
                posts = postList
                errorMessage = null
            }.onFailure { error ->
                errorMessage = error.message
            }
        }
    }
}
