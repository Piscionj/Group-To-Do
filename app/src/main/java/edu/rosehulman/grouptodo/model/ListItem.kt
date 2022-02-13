package edu.rosehulman.grouptodo.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.ServerTimestamp

data class ListItem(var name: String="", var date: String="", var isFinished: Boolean = false) {

    override fun toString(): String {
        return if (name.isNotBlank()) "'$name', from $date, $isFinished" else ""
    }

    @get:Exclude
    var id=""

    @ServerTimestamp
    var created: Timestamp? = null

    companion object{
        const val COLLECTION_PATH = "list items"

        fun from(snapshot: DocumentSnapshot): ListItem {
            val li = snapshot.toObject(ListItem::class.java)!! //data only
            li.id = snapshot.id
            return li
        }
    }
}
