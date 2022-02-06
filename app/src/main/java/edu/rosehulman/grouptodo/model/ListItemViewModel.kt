package edu.rosehulman.grouptodo.model

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlin.random.Random


class ListItemViewModel : ViewModel() {

    var listItems = ArrayList<ListItem>()
    var currentPos = 0

    fun getItemAt(pos: Int) = listItems[pos]
    fun size() = listItems.size
    fun getCurrent() = getItemAt(currentPos)




    val subscriptions = HashMap<String, ListenerRegistration>()

    lateinit var ref: CollectionReference
    fun addListener(fragmentName: String, groupId: String, observer: () -> Unit ){
//        val uid = Firebase.auth.currentUser!!.uid
        ref = Firebase.firestore.collection(Group.COLLECTION_PATH).document(groupId).collection(ListItem.COLLECTION_PATH)
        val subscription = ref
            .addSnapshotListener { snapshot: QuerySnapshot?, error: FirebaseFirestoreException? ->
                error?.let {
                    return@addSnapshotListener
                }
                listItems.clear()
                snapshot?.documents?.forEach{
                    listItems.add(ListItem.from(it))
                }
                observer()
            }
        subscriptions[fragmentName] = subscription
    }

    fun removeListener(fragmentName: String) {
        subscriptions[fragmentName]?.remove() //tells firebase to stop sending query snapshots
        subscriptions.remove(fragmentName) // removes from the map
    }

    fun updateCurrentItem(name: String, date: String){
        listItems[currentPos].name = name
        listItems[currentPos].date = date
        ref.document(getCurrent().id).set(getCurrent())
    }

    fun updatePos(pos: Int){
        currentPos = pos
    }

    fun addItem(listItem: ListItem?){
        val random = getRandom()
        val newQuote = listItem ?: ListItem("name$random", "Select a Date")
        // elvin ?: if not null, then do the rest
        listItems.add(newQuote)
    }

    fun getRandom() = Random.nextInt(100)

    fun toggleCurrentItem() {
        listItems[currentPos].isFinished = !listItems[currentPos].isFinished
    }



}