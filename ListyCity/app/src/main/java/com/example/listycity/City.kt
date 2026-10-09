package com.example.listycity

data class City(
    val name: String= "",
    val province: String= "",
    // Read from the Firestore document ID; it stays the same after a rename.
    val id: String = ""
)