package mx.edu.utez.silvergarbanzo2.data.remote

import mx.edu.utez.silvergarbanzo2.data.model.LoginRequest
import mx.edu.utez.silvergarbanzo2.data.model.UserRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService{
    @POST("api/usuarios/registro")
    suspend fun registrarUsuario(@Body user: UserRequest): Response<Void>

    @POST("api/usuarios/login")
    suspend fun loginUsuario(@Body credentials: LoginRequest): Response<Void>
}