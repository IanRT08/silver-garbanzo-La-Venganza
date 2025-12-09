package mx.edu.utez.silvergarbanzo2.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "users")
data class User(
    @PrimaryKey
    @SerializedName("id") val id: Int = 0,
    @SerializedName("nombre") val nombre: String,
    @SerializedName("apellidos") val apellidos: String,
    @SerializedName("correo") val correo: String,
    @SerializedName("foto_perfil") val fotoPerfil: String? = null,
    @SerializedName("fecha_registro") val fechaRegistro: String = "",
    @SerializedName("token") val token: String? = null,
    @SerializedName("total_publicaciones") val totalPublicaciones: Int = 0,
    @SerializedName("total_visitas") val totalVisitas: Int = 0
) {
    // Propiedad computada para mostrar nombre completo
    val nombreCompleto: String
        get() = "$nombre $apellidos"
}