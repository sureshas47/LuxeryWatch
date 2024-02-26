package com.example.project1

data class Cart(
    var name: String = "",
    var price: Double = 0.0,
    var braceletMaterial: String = "",
    var cardImg: String = "",
    var caseSize: String = "",
    var detailImg: String = "",
    var dialColor: String = "",
    var powerReserver: String = "",
    var waterResistance: String = ""
) {
    // No-argument constructor required by Firebase
    constructor() : this("", 0.0, "", "", "", "", "", "", "")
}
