package com.elijahneville.vegez.data

import retrofit2.Response
import retrofit2.http.*

interface VegetableApi {
    @FormUrlEncoded
    @POST("api/vegetable/add")
    suspend fun addVegetable(
        @Field("vegetableName") name: String,
        @Field("pricePerUnit") price: Double,
        @Field("unit") unit: String? = null
    ): Response<ApiResponse<Unit>>

    @FormUrlEncoded
    @POST("api/vegetable/update")
    suspend fun updateVegetable(
        @Field("vegetableName") name: String,
        @Field("pricePerUnit") price: Double,
        @Field("unit") unit: String? = null
    ): Response<ApiResponse<Unit>>

    @FormUrlEncoded
    @POST("api/vegetable/delete")
    suspend fun deleteVegetable(
        @Field("vegetableName") name: String
    ): Response<ApiResponse<Unit>>

    @GET("api/vegetable/cost")
    suspend fun getCost(
        @Query("vegetableName") name: String,
        @Query("quantity") quantity: Double
    ): Response<ApiResponse<Double>>

    @FormUrlEncoded
    @POST("api/receipt/calculate")
    suspend fun calculateReceipt(
        @Field("clerkId") clerkId: String,
        @Field("vegetableName") vegetableName: String,
        @Field("quantity") quantity: Double
    ): Response<ApiResponse<ReceiptData>>
}

data class ApiResponse<T>(
    val success: Boolean,
    val data: T?,
    val error: String?
)

data class ReceiptData(
    val totalCost: Double,
    val amountGiven: Double,
    val changeDue: Double,
    val cashierName: String
)
