package mx.edu.utez.silvergarbanzo2.data.remote

import mx.edu.utez.silvergarbanzo2.data.model.LoginRequest
import mx.edu.utez.silvergarbanzo2.data.model.Post
import mx.edu.utez.silvergarbanzo2.data.model.User
import mx.edu.utez.silvergarbanzo2.data.model.UserRequest
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.PartMap
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    //Login y regitrp
    @POST("auth/register")
    suspend fun registrarUsuario(@Body user: UserRequest): Response<ResponseBody>

    @POST("auth/login")
    suspend fun loginUsuario(@Body loginRequest: LoginRequest): Response<User>

    //Publicaciones o posts
    @GET("posts")
    suspend fun getPublicPosts(): Response<List<Post>>

    @GET("posts/user/{userId}")
    suspend fun getUserPosts(@Path("userId") userId: Int): Response<List<Post>>

    @GET("posts/top")
    suspend fun getTopVisitedPosts(@Query("limit") limit: Int = 5): Response<List<Post>>

    @Multipart
    @POST("posts")
    suspend fun createPost(
        @PartMap postData: Map<String, @JvmSuppressWildcards RequestBody>,
        @Part imagenes: List<MultipartBody.Part>
    ): Response<Post>

    @PUT("posts/{id}")
    suspend fun updatePost(
        @Path("id") id: Int,
        @Body post: Post
    ): Response<ResponseBody>

    @DELETE("posts/{id}")
    suspend fun deletePost(@Path("id") id: Int): Response<ResponseBody>

    @POST("posts/{id}/visit")
    suspend fun incrementVisitCount(@Path("id") id: Int): Response<ResponseBody>

    // ========== USERS ==========
    @GET("users/{id}")
    suspend fun getUserProfile(@Path("id") id: Int): Response<User>

    @PUT("users/{id}")
    suspend fun updateUserProfile(
        @Path("id") id: Int,
        @Body user: User
    ): Response<User>

    // Para autenticación con token
    @GET("posts/private")
    suspend fun getPrivatePosts(
        @Header("Authorization") token: String
    ): Response<List<Post>>
}