package edu.rosehulman.grouptodo.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.ServerTimestamp

data class User (
    var name: String = "",
    var storageUriString: String = "",
    var hasCompletedSetup: Boolean = false
    ) {


    @get:Exclude
    var id=""

    @ServerTimestamp
    var created: Timestamp? = null

        companion object{
            const val COLLECTION_PATH = "users"
            fun from(snapshot: DocumentSnapshot): User{
                val user = snapshot.toObject(User::class.java)!! //data only
                user.id = snapshot.id
                return user
            }
        }
}