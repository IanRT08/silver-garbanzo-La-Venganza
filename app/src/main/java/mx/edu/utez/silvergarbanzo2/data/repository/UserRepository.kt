package mx.edu.utez.silvergarbanzo2.data.repository

import mx.edu.utez.silvergarbanzo2.data.model.LoginRequest
import mx.edu.utez.silvergarbanzo2.data.model.User
import mx.edu.utez.silvergarbanzo2.data.model.UserRequest
import mx.edu.utez.silvergarbanzo2.data.remote.ApiService

class UserRepository(private val api: ApiService) {

    suspend fun registrarUsuario(user: UserRequest): Result<String> {
        return try {
            val response = api.registrarUsuario(user)

            println("🔍 Register Response code: ${response.code()}")
            println("🔍 Register Response body: ${response.body()}")

            if (response.isSuccessful) {
                val mensaje = response.body()?.mensaje ?: "Usuario registrado con éxito"
                Result.success(mensaje)
            } else {
                val errorBody = response.errorBody()?.string()
                println("❌ Register Error: $errorBody")
                Result.failure(Exception("Error: ${response.code()} - $errorBody"))
            }
        } catch (e: Exception) {
            println("❌ Register Exception: ${e.message}")
            e.printStackTrace()
            Result.failure(e)
        }
    }

    suspend fun login(correo: String, contrasena: String): Result<User> {
        return try {
            val request = LoginRequest(correo, contrasena)
            val response = api.loginUsuario(request)

            println("🔍 Response code: ${response.code()}")
            println("🔍 Response body: ${response.body()}")

            if (response.isSuccessful) {
                val apiResponse = response.body()

                if (apiResponse != null) {
                    // ✅ El servidor ahora envía: { data: { token, usuario }, mensaje, token }
                    val loginData = apiResponse.data

                    if (loginData != null) {
                        val user = loginData.usuario.copy(token = loginData.token)
                        println("✅ Login exitoso: ${user.nombreCompleto}")
                        return Result.success(user)
                    }

                    // Fallback: si token viene directo en apiResponse
                    if (apiResponse.token != null) {
                        println("🟡 Token en apiResponse directamente")
                        // Aquí podrías intentar construir el usuario si viene en otro formato
                    }
                }

                Result.failure(Exception("Respuesta vacía del servidor"))
            } else {
                val errorBody = response.errorBody()?.string()
                println("❌ Login Error: $errorBody")
                Result.failure(Exception("Credenciales incorrectas"))
            }
        } catch (e: Exception) {
            println("❌ Login Exception: ${e.message}")
            e.printStackTrace()
            Result.failure(e)
        }
    }
}