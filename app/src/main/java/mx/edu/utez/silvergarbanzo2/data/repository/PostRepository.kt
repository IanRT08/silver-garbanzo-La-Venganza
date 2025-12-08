package mx.edu.utez.silvergarbanzo2.data.repository

import mx.edu.utez.silvergarbanzo2.data.dao.PostDao
import mx.edu.utez.silvergarbanzo2.data.model.Post
import mx.edu.utez.silvergarbanzo2.data.remote.ApiService
import okhttp3.MultipartBody
import okhttp3.RequestBody

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
                posts.forEach { postDao.insertPost(it) }
                Result.success(posts)
            } else {
                Result.failure(Exception("Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            // Fallback a caché local
            val cachedPosts = postDao.getPublicPosts()
            if (cachedPosts.isNotEmpty()) {
                Result.success(cachedPosts)
            } else {
                Result.failure(e)
            }
        }
    }

    suspend fun createPost(
        postData: Map<String, RequestBody>,
        images: List<MultipartBody.Part>
    ): Result<Post> {
        return try {
            val response = apiService.createPost(postData, images)
            if (response.isSuccessful) {
                val post = response.body()
                if (post != null) {
                    postDao.insertPost(post)
                    Result.success(post)
                } else {
                    Result.failure(Exception("Respuesta vacía"))
                }
            } else {
                Result.failure(Exception("Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updatePost(post: Post): Result<Boolean> {
        return try {
            val response = apiService.updatePost(post.id, post)
            if (response.isSuccessful) {
                postDao.updatePost(post)
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
                val post = postDao.getPostById(postId)
                post?.let { postDao.deletePost(it) }
                Result.success(true)
            } else {
                Result.failure(Exception("Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun incrementVisitCount(postId: Int) {
        postDao.incrementVisitCount(postId)
        // También sincronizar con el servidor
        apiService.incrementVisitCount(postId)
    }

    suspend fun getUserPosts(userId: Int): Result<List<Post>> {
        return try {
            val response = apiService.getUserPosts(userId)
            if (response.isSuccessful) {
                val posts = response.body() ?: emptyList()
                posts.forEach { postDao.insertPost(it) }
                Result.success(posts)
            } else {
                // Fallback a caché
                val cachedPosts = postDao.getPostsByUser(userId)
                Result.success(cachedPosts)
            }
        } catch (e: Exception) {
            val cachedPosts = postDao.getPostsByUser(userId)
            Result.success(cachedPosts)
        }
    }
}
