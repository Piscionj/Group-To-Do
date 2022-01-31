package edu.rosehulman.grouptodo.model

data class Group(var name: String="") {



    companion object {
        const val COLLECTION_PATH = "groups"
    }
}
