package com.elijahneville.vegez.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elijahneville.vegez.data.ReceiptData
import com.elijahneville.vegez.data.VegetableRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class VegetableViewModel : ViewModel() {
    private val repository = VegetableRepository()

    private val _statusMessage = MutableStateFlow<String?>(null)
    val statusMessage: StateFlow<String?> = _statusMessage

    private val _calculatedCost = MutableStateFlow<Double?>(null)
    val calculatedCost: StateFlow<Double?> = _calculatedCost

    private val _receiptData = MutableStateFlow<ReceiptData?>(null)
    val receiptData: StateFlow<ReceiptData?> = _receiptData

    fun addVegetable(name: String, price: Double) {
        viewModelScope.launch {
            try {
                val response = repository.addVegetable(name, price)
                if (response.isSuccessful && response.body()?.success == true) {
                    _statusMessage.value = "Vegetable added successfully"
                } else {
                    _statusMessage.value = "Error: ${response.body()?.error ?: "Unknown error"}"
                }
            } catch (e: Exception) {
                _statusMessage.value = "Network error: ${e.message}"
            }
        }
    }

    fun updateVegetable(name: String, price: Double) {
        viewModelScope.launch {
            try {
                val response = repository.updateVegetable(name, price)
                if (response.isSuccessful && response.body()?.success == true) {
                    _statusMessage.value = "Vegetable updated successfully"
                } else {
                    _statusMessage.value = "Error: ${response.body()?.error ?: "Unknown error"}"
                }
            } catch (e: Exception) {
                _statusMessage.value = "Network error: ${e.message}"
            }
        }
    }

    fun deleteVegetable(name: String) {
        viewModelScope.launch {
            try {
                val response = repository.deleteVegetable(name)
                if (response.isSuccessful && response.body()?.success == true) {
                    _statusMessage.value = "Vegetable deleted successfully"
                } else {
                    _statusMessage.value = "Error: ${response.body()?.error ?: "Unknown error"}"
                }
            } catch (e: Exception) {
                _statusMessage.value = "Network error: ${e.message}"
            }
        }
    }

    fun calculateCost(name: String, quantity: Double) {
        viewModelScope.launch {
            try {
                val response = repository.calculateCost(name, quantity)
                if (response.isSuccessful && response.body()?.success == true) {
                    _calculatedCost.value = response.body()?.data
                } else {
                    _statusMessage.value = "Error: ${response.body()?.error ?: "Unknown error"}"
                }
            } catch (e: Exception) {
                _statusMessage.value = "Network error: ${e.message}"
            }
        }
    }

    fun calculateReceipt(clerkId: String, name: String, quantity: Double) {
        viewModelScope.launch {
            try {
                val response = repository.calculateReceipt(clerkId, name, quantity)
                if (response.isSuccessful && response.body()?.success == true) {
                    _receiptData.value = response.body()?.data
                } else {
                    _statusMessage.value = "Error: ${response.body()?.error ?: "Unknown error"}"
                }
            } catch (e: Exception) {
                _statusMessage.value = "Network error: ${e.message}"
            }
        }
    }

    fun clearStatus() {
        _statusMessage.value = null
    }
}
