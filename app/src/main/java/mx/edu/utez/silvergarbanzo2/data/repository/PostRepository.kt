package mx.edu.utez.silvergarbanzo2.data.repository

import mx.edu.utez.silvergarbanzo2.data.model.Post
import mx.edu.utez.silvergarbanzo2.data.remote.ApiService
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class PostRepository(
    private val apiService: ApiService
) {

    suspend fun getPublicPosts(): Result<List<Post>> {
        return try {
            val response = apiService.getPublicPosts()
            if (response.isSuccessful) {
                Result.success(response.body() ?: emptyList())
            } else {
                Result.failure(Exception("Error: ${response.code()} - ${response.errorBody()?.string()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun createPost(
        postData: Map<String, RequestBody>,
        imageFiles: List<File>
    ): Result<Post> {
        return try {
            val imageParts = imageFiles.mapIndexed { index, file ->
                val requestFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
                MultipartBody.Part.createFormData(
                    "imagenes",
                    "imagen_${index}_${System.currentTimeMillis()}.jpg",
                    requestFile
                )
            }

            val response = apiService.createPost(postData, imageParts)

            if (response.isSuccessful) {
                val post = response.body()
                if (post != null) {
                    Result.success(post)
                } else {
                    Result.failure(Exception("Respuesta vacía del servidor"))
                }
            } else {
                Result.failure(Exception("Error ${response.code()}: ${response.errorBody()?.string()}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Error de conexión: ${e.message}"))
        }
    }

    suspend fun createPostWithUris(
        titulo: String,
        descripcion: String,
        latitud: Double,
        longitud: Double,
        fechaVisita: String,
        direccion: String? = null,
        esPrivado: Boolean = false,
        imageFiles: List<File>
    ): Result<Post> {
        val postData = mapOf(
            "titulo" to titulo.toRequestBody("text/plain".toMediaTypeOrNull()),
            "descripcion" to descripcion.toRequestBody("text/plain".toMediaTypeOrNull()),
            "latitud" to latitud.toString().toRequestBody("text/plain".toMediaTypeOrNull()),
            "longitud" to longitud.toString().toRequestBody("text/plain".toMediaTypeOrNull()),
            "fecha_visita" to fechaVisita.toRequestBody("text/plain".toMediaTypeOrNull()),
            "es_privado" to esPrivado.toString().toRequestBody("text/plain".toMediaTypeOrNull()),
            "direccion" to (direccion ?: "").toRequestBody("text/plain".toMediaTypeOrNull())
        )

        return createPost(postData, imageFiles)
    }

    suspend fun updatePost(post: Post): Result<Boolean> {
        return try {
            val response = apiService.updatePost(post.id, post)
            if (response.isSuccessful) {
                Result.success(true)
            } else {
                Result.failure(Exception("Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deletePost(postId: Int): Result<Boolean> {
        return try {
            val response = apiService.deletePost(postId)
            if (response.isSuccessful) {
                Result.success(true)
            } else {
                Result.failure(Exception("Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun incrementVisitCount(postId: Int) {
        try {
            apiService.incrementVisitCount(postId)
        } catch (e: Exception) {
            // Ignorar errores silenciosamente
        }
    }

    suspend fun getUserPosts(userId: Int): Result<List<Post>> {
        return try {
            val response = apiService.getUserPosts(userId)
            if (response.isSuccessful) {
                Result.success(response.body() ?: emptyList())
            } else {
                Result.failure(Exception("Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getTopVisitedPosts(limit: Int = 5): Result<List<Post>> {
        return try {
            val response = apiService.getTopVisitedPosts(limit)
            if (response.isSuccessful) {
                Result.success(response.body() ?: emptyList())
            } else {
                Result.failure(Exception("Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}