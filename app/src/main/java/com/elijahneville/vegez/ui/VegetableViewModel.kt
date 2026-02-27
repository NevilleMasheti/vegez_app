package com.elijahneville.vegez.ui

import androidx.lifecycle.ViewModel
import com.elijahneville.vegez.data.VegetableRepository
import kotlinx.coroutines.flow.StateFlow

class VegetableViewModel : ViewModel() {
    private val repository = VegetableRepository()
    val vegetables: StateFlow<Map<String, Double>> = repository.vegetables

    fun addVegetable(name: String, price: Double) {
        repository.addVegetable(name, price)
    }

    fun updateVegetable(name: String, price: Double) {
        repository.updateVegetable(name, price)
    }

    fun deleteVegetable(name: String) {
        repository.deleteVegetable(name)
    }

    fun calculateCost(name: String, quantity: Double): Double? {
        return repository.calculateCost(name, quantity)
    }
}
