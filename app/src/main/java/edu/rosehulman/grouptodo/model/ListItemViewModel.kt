package edu.rosehulman.grouptodo.model

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import edu.rosehulman.grouptodo.Constants
import edu.rosehulman.grouptodo.R
import kotlin.random.Random


class ListItemViewModel : ViewModel() {

    var listItems = ArrayList<ListItem>()
    var currentPos = 0
    var showComplete = false

    fun getItemAt(pos: Int) = listItems[pos]
    fun size() = listItems.size
    fun getCurrent() = getItemAt(currentPos)

    private lateinit var subscription: ListenerRegistration

    lateinit var ref: CollectionReference
    lateinit var currentGroupID: String
    lateinit var notifyFunction: () -> Unit
    fun addListener(groupId: String, observer: () -> Unit ){
//        val uid = Firebase.auth.currentUser!!.uid
        currentGroupID = groupId
        notifyFunction = observer
        ref = Firebase.firestore.collection(Group.COLLECTION_PATH).document(groupId).collection(ListItem.COLLECTION_PATH)
        val query = if(showComplete) ref else ref.whereEqualTo("finished", false)
        subscription = query
            .orderBy("date")
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

    }

    fun removeListener() {
        subscription.remove() // removes from the map
    }

    fun updateCurrentItem(name: String, date: String, group: String){
        Log.d(Constants.TAG, "picker group $group")
        if (group == currentGroupID){
            listItems[currentPos].name = name
            listItems[currentPos].date = date
            ref.document(getCurrent().id).set(getCurrent())
        } else {
            var ref = Firebase.firestore.collection(Group.COLLECTION_PATH)
            var query = ref.whereEqualTo("name", group)
            query.addSnapshotListener { snapshot: QuerySnapshot?, error: FirebaseFirestoreException? ->
                error?.let {
                    return@addSnapshotListener
                }
                snapshot?.documents?.forEach{
                    Log.d(Constants.TAG, "group id: ${it.id}")
                    if (it.id == currentGroupID){
                        listItems[currentPos].name = name
                        listItems[currentPos].date = date
                        ref.document(getCurrent().id).set(getCurrent())
                    } else {
                        removeCurrentItem()
                        Firebase.firestore.collection(Group.COLLECTION_PATH).document(it.id).collection(ListItem.COLLECTION_PATH).add(ListItem(name, date))
                    }
                }
            }
        }

    }

    fun removeCurrentItem(){
        ref.document(getCurrent().id).delete()
        currentPos = 0
    }

    fun updatePos(pos: Int){
        currentPos = pos
    }

    fun addItem(name: String, date: String, group: String){
        Log.d(Constants.TAG, "picker group $group")

        var ref = Firebase.firestore.collection(Group.COLLECTION_PATH)
        var query = ref.whereEqualTo("name", group)
        query.addSnapshotListener { snapshot: QuerySnapshot?, error: FirebaseFirestoreException? ->
            error?.let {
                return@addSnapshotListener
            }
            snapshot?.documents?.forEach{
                Log.d(Constants.TAG, "group id: ${it.id}")
                Firebase.firestore.collection(Group.COLLECTION_PATH).document(it.id).collection(ListItem.COLLECTION_PATH).add(ListItem(name, date))
            }
        }
    }
    fun toggleCurrentItem() {
        listItems[currentPos].isFinished = !listItems[currentPos].isFinished
        ref.document(getCurrent().id).set(getCurrent())
    }

    fun toggleShowComplete() {
        showComplete = !showComplete
        removeListener()
        addListener(currentGroupID, notifyFunction)
        Log.d(Constants.TAG, "Show toggled!")
    }

}