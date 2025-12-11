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
    companion object {
        private const val BASE_URL = "https://chivalrously-overfat-kieth.ngrok-free.dev"
    }

    val imagenes: List<String>
        get() = try {
            if (imagenesString.isNotEmpty()) {
                val urls = Gson().fromJson(imagenesString, Array<String>::class.java).toList()
                // Convertir URLs relativas a absolutas
                urls.map { url ->
                    if (url.startsWith("http")) {
                        url // Ya es absoluta
                    } else {
                        "$BASE_URL$url" // Agregar base URL
                    }
                }
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            emptyList()
        }
}