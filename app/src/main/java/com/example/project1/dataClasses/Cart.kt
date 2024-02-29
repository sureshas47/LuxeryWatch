package com.example.project1.dataClasses

data class Cart(
    var name: String = "",
    var price: Double = 0.0,
    var braceletMaterial: String = "",
    var cardImg: String = "",
    var caseSize: String = "",
    var dialColor: String = "",
    var powerReserver: String = "",
    var waterResistance: String = ""
) {
    constructor() : this("", 0.0, "", "", "", "", "", "")
}
