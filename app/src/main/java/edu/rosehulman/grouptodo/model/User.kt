package edu.rosehulman.grouptodo.model

data class User (
    var name: String = "",
    var storageUriString: String = "",
    var hasCompletedSetup: Boolean = false
    ) {
        companion object{
            const val COLLECTION_PATH = "users"
        }
}