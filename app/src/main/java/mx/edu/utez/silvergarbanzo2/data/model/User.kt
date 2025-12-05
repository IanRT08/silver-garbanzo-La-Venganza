package mx.edu.utez.silvergarbanzo2.data.model

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("id") val id: Int = 0,
    @SerializedName("nombre") val nombre: String,
    @SerializedName("apellidos") val apellidos: String,
    @SerializedName("correo") val correo: String,
    @SerializedName("foto_perfil") val fotoPerfil: String? = null,
    @SerializedName("fecha_registro") val fechaRegistro: String = "",
    @SerializedName("token") val token: String? = null,
    @SerializedName("total_publicaciones") val totalPublicaciones: Int = 0,
    @SerializedName("total_visitas") val totalVisitas: Int = 0
)
