package com.elijahneville.vegez.data

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class VegetableRepository {
    private val api = Retrofit.Builder()
        .baseUrl("http://10.0.2.2:8080/vegetable-rmi/") // Use 10.0.2.2 for Android Emulator to access localhost
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(VegetableApi::class.java)

    suspend fun addVegetable(name: String, price: Double) = api.addVegetable(name, price)

    suspend fun updateVegetable(name: String, price: Double) = api.updateVegetable(name, price)

    suspend fun deleteVegetable(name: String) = api.deleteVegetable(name)

    suspend fun calculateCost(name: String, quantity: Double) = api.getCost(name, quantity)

    suspend fun calculateReceipt(clerkId: String, name: String, quantity: Double) = 
        api.calculateReceipt(clerkId, name, quantity)
}
