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
        if (group == currentGroupID){
            listItems[currentPos].name = name
            listItems[currentPos].date = date
            ref.document(getCurrent().id).set(getCurrent())
        } else if (group == R.string.select_group.toString()) {
            listItems[currentPos].name = name
            listItems[currentPos].date = date
            ref.document(getCurrent().id).set(getCurrent())
        } else {
            removeCurrentItem()
            addItem(name, date, group)
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
        if (group == currentGroupID){
            ref.add(ListItem(name, date))
        } else {
            // currently using group name instead of document id so just have to fix that
            Firebase.firestore.collection(Group.COLLECTION_PATH).document(group).collection(ListItem.COLLECTION_PATH).add(ListItem(name, date))
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