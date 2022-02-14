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

    val ref: CollectionReference = Firebase.firestore.collection(Group.COLLECTION_PATH)
    fun addListener(fragmentName: String, observer: () -> Unit ){
//        val uid = Firebase.auth.currentUser!!.uid
        var query = ref.whereArrayContains("members", Firebase.auth.uid.toString())
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

    fun updateCurrentItem(name: String){
        groups[currentPos].name = name
        //groups[currentPos].user = user
        ref.document(getCurrent().id).set(getCurrent())
    }

    fun updateCurrentMembers(member: String){
        groups[currentPos].members.add(member)
        ref.document(getCurrent().id).set(getCurrent())
    }

    fun updatePos(pos: Int){
        currentPos = pos
    }

    fun removeCurrentGroup(){
        ref.document(getCurrent().id).delete()
        currentPos = 0
    }

    fun addGroup(group: Group?){
        val newGroup = group ?: Group("New Group")
        // elvin ?: if not null, then do the rest
        groups.add(newGroup)
    }

    fun getRandom() = Random.nextInt(100)


}