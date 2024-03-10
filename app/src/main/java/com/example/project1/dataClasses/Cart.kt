package com.example.project1.dataClasses
import java.util.UUID

data class Cart(
    var id: String = UUID.randomUUID().toString(),
    var name: String = "",
    var price: Double = 0.0,
    var braceletMaterial: String = "",
    var cardImg: String = "",
    var caseSize: String = "",
    var dialColor: String = "",
    var powerReserver: String = "",
    var waterResistance: String = "",
    var quantity:Int= 1
) {
    constructor() : this( "", "", 0.0, "", "", "", "", "", "",1)
}
