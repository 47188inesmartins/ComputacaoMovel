package com.example.myapplication.network

import android.util.Log
import com.example.myapplication.model.MarsPhoto
import com.example.myapplication.model.PhotosRolls
import com.example.myapplication.model.Picture
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

const val TAG = "Firebase"
class FireBase{
    val db = Firebase.firestore

    fun savePhotos(mars: MarsPhoto, photo: Picture,numberRolls: Int){
        val images = hashMapOf(
            "rolls" to Json.encodeToString(numberRolls),
            "marspics" to Json.encodeToString(mars),
            "pics" to Json.encodeToString(photo)
        )


        db.collection("cmove")
            .add(images)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
            }
    }

    suspend fun readPhoto(mars: MarsPhoto, photo: Picture, numberRolls: Int): PhotosRolls? {
        return try {
            val result = db.collection("saved_images")
                .get()
                .await()

            val images = result.last().data as Map<String, String>
            val marsPhoto = images["marspics"]?.let { Json.decodeFromString<MarsPhoto>(it) }
            val picsumPhoto = images["pics"]?.let { Json.decodeFromString<Picture>(it) }
            val rolls = images["rolls"]?.let { Json.decodeFromString<Int>(it) }

            if (marsPhoto != null && picsumPhoto != null&& rolls!= null) {
                PhotosRolls(marsPhoto, picsumPhoto,rolls)
            }else{
                null
            }
        } catch (exception: Exception) {
            Log.w(TAG, "Error getting documents.", exception)
            null
        }
    }

}