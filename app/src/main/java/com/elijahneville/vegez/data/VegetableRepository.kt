package com.elijahneville.vegez.data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class VegetableRepository {
    private val _vegetables = MutableStateFlow<Map<String, Double>>(emptyMap())
    val vegetables: StateFlow<Map<String, Double>> = _vegetables

    fun addVegetable(name: String, price: Double) {
        val current = _vegetables.value.toMutableMap()
        current[name] = price
        _vegetables.value = current
    }

    fun updateVegetable(name: String, price: Double) {
        val current = _vegetables.value.toMutableMap()
        if (current.containsKey(name)) {
            current[name] = price
            _vegetables.value = current
        }
    }

    fun deleteVegetable(name: String) {
        val current = _vegetables.value.toMutableMap()
        current.remove(name)
        _vegetables.value = current
    }

    fun getPrice(name: String): Double? {
        return _vegetables.value[name]
    }

    fun calculateCost(name: String, quantity: Double): Double? {
        val price = getPrice(name)
        return price?.let { it * quantity }
    }
}
