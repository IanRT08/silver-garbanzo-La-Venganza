package mx.edu.utez.silvergarbanzo2.data.model

import com.google.gson.annotations.SerializedName

class LoginRequest(
    @SerializedName("correo") val correo: String,
    @SerializedName("password") val contrasena: String
)