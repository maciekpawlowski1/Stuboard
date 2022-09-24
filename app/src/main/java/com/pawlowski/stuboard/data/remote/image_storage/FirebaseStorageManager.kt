package com.pawlowski.stuboard.data.remote.image_storage

import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import com.google.firebase.storage.FirebaseStorage
import com.pawlowski.stuboard.domain.models.Resource
import com.pawlowski.stuboard.presentation.utils.UiText
import kotlinx.coroutines.tasks.await
import java.io.ByteArrayOutputStream
import java.util.*
import javax.inject.Inject

class FirebaseStorageManager @Inject constructor(
    private val firebaseStorage: FirebaseStorage,
    private val contentResolver: ContentResolver
): IImageUploadManager {
    override suspend fun uploadNewImage(uri: Uri, userUid: String): Resource<String>
    {
        return try {
            val reducedImage = reduceImageSize(uri)
            val result = firebaseStorage.reference.child("event_images").child(userUid).child(UUID.randomUUID().toString()).putBytes(reducedImage).await()
            val downloadUrl = result.storage.downloadUrl.await()
            Resource.Success(downloadUrl.toString())
        }
        catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(UiText.StaticText(e.localizedMessage?:"Error with uploading image"))
        }
    }

    private fun reduceImageSize(uri: Uri): ByteArray
    {
        val bitmap = getBitmapFromUri(uri)
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 25, byteArrayOutputStream)
        return byteArrayOutputStream.toByteArray()
    }

    private fun getBitmapFromUri(selectedPhotoUri: Uri): Bitmap {
        return when {
            Build.VERSION.SDK_INT < 28 -> MediaStore.Images.Media.getBitmap(
                contentResolver,
                selectedPhotoUri
            )
            else -> {
                val source = ImageDecoder.createSource(contentResolver, selectedPhotoUri)
                ImageDecoder.decodeBitmap(source)
            }
        }
    }
}