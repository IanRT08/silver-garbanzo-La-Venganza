package mx.edu.utez.silvergarbanzo2.data.model

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

data class Post(
    @SerializedName("id") val id: Int = 0,
    @SerializedName("usuario_id") val usuarioId: Int,
    @SerializedName("titulo") val titulo: String,
    @SerializedName("descripcion") val descripcion: String,
    @SerializedName("latitud") val latitud: Double,
    @SerializedName("longitud") val longitud: Double,
    @SerializedName("direccion") val direccion: String? = null,
    @SerializedName("ciudad") val ciudad: String? = null,
    @SerializedName("pais") val pais: String? = null,
    @SerializedName("fecha_creacion") val fechaCreacion: String = "",
    @SerializedName("fecha_visita") val fechaVisita: String,
    @SerializedName("contador_visitas") val contadorVisitas: Int = 0,
    @SerializedName("imagenes") val imagenesString: String = "",
    @SerializedName("es_privado") val esPrivado: Boolean = false
) {
    val imagenes: List<String>
        get() = try {
            if (imagenesString.isNotEmpty()) {
                Gson().fromJson(imagenesString, Array<String>::class.java).toList()
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            emptyList()
        }
}