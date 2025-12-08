package mx.edu.utez.silvergarbanzo2.utils

import android.content.Context
import android.net.Uri
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

object ImageUtils {
    fun uriToMultipart(
        context: Context,
        uri: Uri,
        partName: String = "imagenes"
    ): MultipartBody.Part? {
        return try {
            val file = File(uri.path ?: return null)
            val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
            MultipartBody.Part.createFormData(partName, file.name, requestFile)
        } catch (e: Exception) {
            null
        }
    }

    fun getImageFile(context: Context, fileName: String): File {
        val storageDir = context.getExternalFilesDir("images")
        return File(storageDir, fileName)
    }
}
