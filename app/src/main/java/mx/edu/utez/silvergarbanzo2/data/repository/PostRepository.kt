package mx.edu.utez.silvergarbanzo2.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mx.edu.utez.silvergarbanzo2.data.dao.PostDao
import mx.edu.utez.silvergarbanzo2.data.model.Post
import mx.edu.utez.silvergarbanzo2.data.remote.ApiService
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class PostRepository(
    private val apiService: ApiService,
    private val postDao: PostDao
) {

    suspend fun getPublicPosts(): Result<List<Post>> {
        return try {
            val response = apiService.getPublicPosts()
            if (response.isSuccessful) {
                val posts = response.body() ?: emptyList()
                // Guardar en caché local
                withContext(Dispatchers.IO) {
                    posts.forEach { postDao.insertPost(it) }
                }
                Result.success(posts)
            } else {
                Result.failure(Exception("Error: ${response.code()} - ${response.errorBody()?.string()}"))
            }
        } catch (e: Exception) {
            // Fallback a caché local
            try {
                val cachedPosts = postDao.getPublicPosts()
                if (cachedPosts.isNotEmpty()) {
                    Result.success(cachedPosts)
                } else {
                    Result.failure(e)
                }
            } catch (dbException: Exception) {
                Result.failure(e)
            }
        }
    }

    suspend fun createPost(
        postData: Map<String, RequestBody>,
        imageFiles: List<File>
    ): Result<Post> {
        return try {
            // Convertir archivos a MultipartBody.Part
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
                    withContext(Dispatchers.IO) {
                        postDao.insertPost(post)
                    }
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
        // Crear map de datos para multipart
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
                withContext(Dispatchers.IO) {
                    postDao.updatePost(post)
                }
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
                withContext(Dispatchers.IO) {
                    val post = postDao.getPostById(postId)
                    post?.let { postDao.deletePost(it) }
                }
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
            // Primero actualizar en servidor
            apiService.incrementVisitCount(postId)
            // Luego en caché local
            withContext(Dispatchers.IO) {
                postDao.incrementVisitCount(postId)
            }
        } catch (e: Exception) {
            // Si falla el servidor, al menos actualizar local
            withContext(Dispatchers.IO) {
                postDao.incrementVisitCount(postId)
            }
        }
    }

    suspend fun getUserPosts(userId: Int): Result<List<Post>> {
        return try {
            val response = apiService.getUserPosts(userId)
            if (response.isSuccessful) {
                val posts = response.body() ?: emptyList()
                withContext(Dispatchers.IO) {
                    posts.forEach { postDao.insertPost(it) }
                }
                Result.success(posts)
            } else {
                // Fallback a caché
                val cachedPosts = postDao.getPostsByUser(userId)
                Result.success(cachedPosts)
            }
        } catch (e: Exception) {
            // Fallback a caché
            val cachedPosts = postDao.getPostsByUser(userId)
            if (cachedPosts.isNotEmpty()) {
                Result.success(cachedPosts)
            } else {
                Result.failure(e)
            }
        }
    }

    suspend fun getTopVisitedPosts(limit: Int = 5): Result<List<Post>> {
        return try {
            val response = apiService.getTopVisitedPosts(limit)
            if (response.isSuccessful) {
                Result.success(response.body() ?: emptyList())
            } else {
                // Intentar obtener del caché local ordenando por visitas
                val cachedPosts = postDao.getPublicPosts()
                    .sortedByDescending { it.contadorVisitas }
                    .take(limit)
                Result.success(cachedPosts)
            }
        } catch (e: Exception) {
            val cachedPosts = postDao.getPublicPosts()
                .sortedByDescending { it.contadorVisitas }
                .take(limit)
            Result.success(cachedPosts)
        }
    }
}

// Helper extension (si no está en otro archivo)
private fun String.toRequestBody(mediaType: okhttp3.MediaType? = null): RequestBody {
    return this.toRequestBody(mediaType)
}