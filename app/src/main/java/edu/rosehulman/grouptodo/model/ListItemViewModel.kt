package edu.rosehulman.grouptodo.model

import androidx.lifecycle.ViewModel

class ListItemViewModel : ViewModel() {

    var listItems = ArrayList<ListItem>()
    var currentPos = 0

    fun getItemAt(pos: Int) = listItems[pos]

    fun size() = listItems.size









}