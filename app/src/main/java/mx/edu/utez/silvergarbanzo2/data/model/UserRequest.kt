package mx.edu.utez.silvergarbanzo2.data.model

import com.google.gson.annotations.SerializedName

class UserRequest(
    @SerializedName("nombre") val nombre: String,
    @SerializedName("apellidos") val apellidos: String,
    @SerializedName("correo") val correo: String,
    @SerializedName("password") val contrasena: String
)