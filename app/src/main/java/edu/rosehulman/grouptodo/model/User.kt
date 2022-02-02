package edu.rosehulman.grouptodo.model

data class User (
    var name: String = "",
    var age: Int = -1,
    var major: String = "unknown",
    var hasCompletedSetup: Boolean = false
    ) {
        companion object{
            const val COLLECTION_PATH = "users"
        }
}