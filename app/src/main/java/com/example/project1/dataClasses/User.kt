package com.example.project1.dataClasses

data class User(
    var uid: String = "",
    var email: String = "",
    var cart: MutableList<Cart> = mutableListOf()

) {
    constructor() : this("", "")
}