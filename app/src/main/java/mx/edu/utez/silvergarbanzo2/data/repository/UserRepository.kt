package mx.edu.utez.silvergarbanzo2.data.repository

import mx.edu.utez.silvergarbanzo2.data.model.LoginRequest
import mx.edu.utez.silvergarbanzo2.data.model.UserRequest
import mx.edu.utez.silvergarbanzo2.data.remote.ApiService

class UserRepository(private val api: ApiService) {
    suspend fun registrarUsuario(user: UserRequest): Result<String> {
        return try {
            val response = api.registrarUsuario(user)
            if (response.isSuccessful) {
                Result.success("Usuario registrado con éxito")
            } else {
                Result.failure(Exception("Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun login(correo: String, contrasena: String): Result<Boolean> {
        return try {
            val request = LoginRequest(correo, contrasena)
            val response = api.loginUsuario(request)

            if (response.isSuccessful) {
                Result.success(true)
            } else {
                Result.failure(Exception("Credenciales incorrectas"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}