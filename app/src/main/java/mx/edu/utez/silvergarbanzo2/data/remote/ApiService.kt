package mx.edu.utez.silvergarbanzo2.data.remote

import mx.edu.utez.silvergarbanzo2.data.model.*
import mx.edu.utez.silvergarbanzo2.data.model.LoginRequest
import mx.edu.utez.silvergarbanzo2.data.model.UserRequest
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    // ==================== AUTH ====================

    @POST("api/auth/register")
    suspend fun registrarUsuario(
        @Body user: UserRequest
    ): Response<ApiResponse<User>>

    @POST("api/auth/login")
    suspend fun loginUsuario(
        @Body loginRequest: LoginRequest
    ): Response<ApiResponse<LoginResponse>>

    @GET("api/auth/profile")
    suspend fun getProfile(
        @Header("Authorization") token: String
    ): Response<User>

    // ==================== POSTS ====================

    @GET("api/posts/")
    suspend fun getPublicPosts(
        @Query("page") page: Int = 1,
        @Query("per_page") perPage: Int = 20
    ): Response<List<Post>>

    @GET("api/posts/user/{userId}")
    suspend fun getUserPosts(
        @Path("userId") userId: Int
    ): Response<List<Post>>

    @GET("api/posts/top")
    suspend fun getTopVisitedPosts(
        @Query("limit") limit: Int = 5
    ): Response<List<Post>>

    @GET("api/posts/private")
    suspend fun getPrivatePosts(
        @Header("Authorization") token: String
    ): Response<List<Post>>

    @Multipart
    @POST("api/posts/")
    suspend fun createPost(
        @PartMap postData: Map<String, @JvmSuppressWildcards RequestBody>,
        @Part imagenes: List<MultipartBody.Part>
    ): Response<Post>

    @PUT("api/posts/{id}")
    suspend fun updatePost(
        @Path("id") postId: Int,
        @Body post: Post
    ): Response<Post>

    @DELETE("api/posts/{id}")
    suspend fun deletePost(
        @Path("id") postId: Int
    ): Response<ApiResponse<String>>

    @POST("api/posts/{id}/visit")
    suspend fun incrementVisitCount(
        @Path("id") postId: Int
    ): Response<ApiResponse<VisitResponse>>

    // ==================== USERS ====================

    @GET("api/users/{id}")
    suspend fun getUserProfile(
        @Path("id") userId: Int
    ): Response<User>

    @PUT("api/users/{id}")
    suspend fun updateUserProfile(
        @Path("id") userId: Int,
        @Body user: User
    ): Response<User>

    @GET("api/users/stats/{id}")
    suspend fun getUserStats(
        @Path("id") userId: Int
    ): Response<UserStatsResponse>
}

// ==================== MODELOS DE RESPUESTA ====================

data class ApiResponse<T>(
    val mensaje: String,
    val data: T? = null,
    val token: String? = null
)

data class LoginResponse(
    val mensaje: String,
    val token: String,
    val usuario: User
)

data class VisitResponse(
    val mensaje: String,
    val contador_visitas: Int
)

data class UserStatsResponse(
    val usuario: User,
    val top_posts: List<Post>
)

// ==================== MODELOS DE REQUEST ====================

data class UserRequest(
    val nombre: String,
    val apellidos: String,
    val correo: String,
    val password: String
)

data class LoginRequest(
    val correo: String,
    val password: String
)