package com.example.project1

import java.io.Serializable

data class Product(
    val name: String = "",
    val braceletMaterial: String = "",
    val cardImg: String = "",
    val caseSize: String = "",
    val detailImg: List<String> = emptyList(),
    val dialColor: String = "",
    val powerReserver: String = "",
    val price: Double = 0.0,
    val waterResistance: String = "",
    val description: String = ""
) : Serializable
