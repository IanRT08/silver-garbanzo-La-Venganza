package mx.edu.utez.silvergarbanzo2.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "posts")
data class Post(
    @PrimaryKey
    @SerializedName("id") val id: Int = 0,
    @SerializedName("usuario_id") val usuarioId: Int,
    @SerializedName("titulo") val titulo: String,
    @SerializedName("descripcion") val descripcion: String,
    @SerializedName("latitud") val latitud: Double,
    @SerializedName("longitud") val longitud: Double,
    @SerializedName("direccion") val direccion: String? = null,
    @SerializedName("ciudad") val ciudad: String? = null,
    @SerializedName("pais") val pais: String? = null,
    @SerializedName("fecha_creacion") val fechaCreacion: String = "", // Formato: "2024-01-15T10:30:00"
    @SerializedName("fecha_visita") val fechaVisita: String,
    @SerializedName("contador_visitas") val contadorVisitas: Int = 0,
    @SerializedName("imagenes") val imagenes: List<String> = emptyList(), // URLs de las imágenes
    @SerializedName("es_privado") val esPrivado: Boolean = false
)