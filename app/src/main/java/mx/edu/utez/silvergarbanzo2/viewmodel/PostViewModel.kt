package mx.edu.utez.silvergarbanzo2.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import mx.edu.utez.silvergarbanzo2.data.model.Post
import mx.edu.utez.silvergarbanzo2.data.repository.PostRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody

class PostViewModel(private val repository: PostRepository) : ViewModel() {

    // Estados para la pantalla de publicaciones
    var posts by mutableStateOf<List<Post>>(emptyList())
        private set
    var isLoading by mutableStateOf(false)
        private set
    var errorMessage by mutableStateOf<String?>(null)
        private set

    // Estados para creación/edición
    var currentPost by mutableStateOf<Post?>(null)
        private set
    var isCreating by mutableStateOf(false)
        private set

    fun loadPublicPosts() {
        isLoading = true
        errorMessage = null

        viewModelScope.launch {
            val result = repository.getPublicPosts()
            isLoading = false
            result.onSuccess { postList ->
                posts = postList
            }.onFailure { error ->
                errorMessage = "Error al cargar publicaciones: ${error.message}"
            }
        }
    }

    fun loadUserPosts(userId: Int) {
        isLoading = true

        viewModelScope.launch {
            val result = repository.getUserPosts(userId)
            isLoading = false
            result.onSuccess { postList ->
                posts = postList
            }.onFailure { error ->
                errorMessage = "Error al cargar tus publicaciones: ${error.message}"
            }
        }
    }

    fun createPost(post: Post, images: List<MultipartBody.Part>) {
        isCreating = true
        viewModelScope.launch {
            // Convertir post a map para multipart
            val postData = mapOf(
                "titulo" to post.titulo.toRequestBody(),
                "descripcion" to post.descripcion.toRequestBody(),
                "latitud" to post.latitud.toString().toRequestBody(),
                "longitud" to post.longitud.toString().toRequestBody(),
                "fecha_visita" to post.fechaVisita.toRequestBody(),
                "es_privado" to post.esPrivado.toString().toRequestBody()
            )

            val result = repository.createPost(postData, images)
            isCreating = false
            result.onSuccess { newPost ->
                // Actualizar lista localmente
                posts = posts + newPost
                currentPost = null
            }.onFailure { error ->
                errorMessage = "Error al crear publicación: ${error.message}"
            }
        }
    }

    fun deletePost(postId: Int) {
        viewModelScope.launch {
            val result = repository.deletePost(postId)
            result.onSuccess {
                posts = posts.filter { it.id != postId }
            }.onFailure { error ->
                errorMessage = "Error al eliminar publicación: ${error.message}"
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
}

// Helper extension
private fun String.toRequestBody(): RequestBody {
    return RequestBody.create(MultipartBody.FORM, this)
}
