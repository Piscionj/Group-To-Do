package edu.rosehulman.grouptodo.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.ServerTimestamp

data class Group(var name: String = "", var creator: String = "", var members: ArrayList<String> = ArrayList()) {

    @get:Exclude
    var id=""

    @ServerTimestamp
    var created: Timestamp? = null

    companion object{
        const val COLLECTION_PATH = "groups"

        fun from(snapshot: DocumentSnapshot): Group{
            val gp = snapshot.toObject(Group::class.java)!! //data only
            gp.id = snapshot.id
            return gp
        }
    }
}

