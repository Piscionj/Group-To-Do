package edu.rosehulman.grouptodo.model

import androidx.lifecycle.ViewModel
import kotlin.random.Random


class ListItemViewModel : ViewModel() {

    var listItems = ArrayList<ListItem>()
    var currentPos = 0

    fun getItemAt(pos: Int) = listItems[pos]
    fun size() = listItems.size
    fun getCurrent() = getItemAt(currentPos)


    fun updateCurrentItem(name: String, date: String){
        listItems[currentPos].name = name
        listItems[currentPos].date = date
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