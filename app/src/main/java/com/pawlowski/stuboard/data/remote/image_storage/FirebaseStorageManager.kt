package com.pawlowski.stuboard.data.remote.image_storage

import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import com.pawlowski.stuboard.domain.models.Resource
import com.pawlowski.stuboard.presentation.utils.UiText
import kotlinx.coroutines.tasks.await
import java.util.*
import javax.inject.Inject

class FirebaseStorageManager @Inject constructor(
    private val firebaseStorage: FirebaseStorage
): IImageUploadManager {
    override suspend fun uploadNewImage(uri: Uri, userUid: String): Resource<String>
    {
        return try {
            val result = firebaseStorage.reference.child("event_images").child(userUid).child(UUID.randomUUID().toString()).putFile(uri).await()
            val downloadUrl = result.storage.downloadUrl.await()
            Resource.Success(downloadUrl.toString())
        }
        catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(UiText.StaticText(e.localizedMessage?:"Error with uploading image"))
        }
    }
}