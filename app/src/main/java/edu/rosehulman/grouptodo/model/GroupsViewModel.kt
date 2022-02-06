package edu.rosehulman.grouptodo.model

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlin.random.Random

class GroupsViewModel : ViewModel()  {
    var groups = ArrayList<Group>()
    var currentPos = 0

    fun getGroupAt(pos: Int) = groups[pos]
    fun size() = groups.size
    fun getCurrent() = getGroupAt(currentPos)

    val subscriptions = HashMap<String, ListenerRegistration>()

    lateinit var ref: CollectionReference
    fun addListener(fragmentName: String, observer: () -> Unit ){
//        val uid = Firebase.auth.currentUser!!.uid
        var query = Firebase.firestore.collection(Group.COLLECTION_PATH).whereArrayContains("members", Firebase.auth.uid.toString())
        val subscription = query
            .addSnapshotListener { snapshot: QuerySnapshot?, error: FirebaseFirestoreException? ->
                error?.let {
                    return@addSnapshotListener
                }
                groups.clear()
                snapshot?.documents?.forEach{
                    groups.add(Group.from(it))
                }
                observer()
            }
        subscriptions[fragmentName] = subscription
    }

    fun removeListener(fragmentName: String) {
        subscriptions[fragmentName]?.remove() //tells firebase to stop sending query snapshots
        subscriptions.remove(fragmentName) // removes from the map
    }

    fun updateCurrentItem(name: String, user: String){
        groups[currentPos].name = name
        //groups[currentPos].user = user
    }

    fun updatePos(pos: Int){
        currentPos = pos
    }

    fun addGroup(group: Group?){
        val random = getRandom()
        val newGroup = group ?: Group("group$random")
        // elvin ?: if not null, then do the rest
        groups.add(newGroup)
    }

    fun getRandom() = Random.nextInt(100)


}