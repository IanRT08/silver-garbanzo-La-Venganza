package mx.edu.utez.silvergarbanzo2.data.model

import com.google.gson.annotations.SerializedName

//Publicaciones
data class Post(
    @SerializedName("id") val id: Int = 0,
    @SerializedName("usuario_id") val usuarioId: Int,
    @SerializedName("titulo") val titulo: String,
    @SerializedName("descripcion") val descripcion: String,
    @SerializedName("latitud") val latitud: Double,
    @SerializedName("longitud") val longitud: Double,
    @SerializedName("direccion") val direccion: String? = null,
    @SerializedName("fecha_creacion") val fechaCreacion: String,
    @SerializedName("fecha_visita") val fechaVisita: String,
    @SerializedName("contador_visitas") val contadorVisitas: Int = 0,
    @SerializedName("imagenes") val imagenes: List<String> = emptyList(), //URIs
    @SerializedName("es_privado") val esPrivado: Boolean = false
)