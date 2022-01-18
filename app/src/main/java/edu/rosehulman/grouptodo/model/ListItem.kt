package edu.rosehulman.grouptodo.model

data class ListItem(var name: String="", var date: String="", var isFinished: Boolean = false, var member: ArrayList<String> = ArrayList<String>()) {

    override fun toString(): String {
        return if (name.isNotBlank()) "'$name', from $date" else ""
    }
}
