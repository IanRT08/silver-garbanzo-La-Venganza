package mx.edu.utez.silvergarbanzo2.data.model

import com.google.gson.annotations.SerializedName

data class CreatePostRequest(
    @SerializedName("titulo") val titulo: String,
    @SerializedName("descripcion") val descripcion: String,
    @SerializedName("latitud") val latitud: Double,
    @SerializedName("longitud") val longitud: Double,
    @SerializedName("direccion") val direccion: String? = null,
    @SerializedName("fecha_visita") val fechaVisita: String,
    @SerializedName("es_privado") val esPrivado: Boolean = false
    //Las imágenes se envían como Multipart separado
)