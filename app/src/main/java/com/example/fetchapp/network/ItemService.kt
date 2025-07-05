package com.example.fetchapp.network

import com.example.fetchapp.model.Item
import retrofit2.http.GET

// Interface for defining Retrofit API service for fetching items from endpoint.
interface ItemService {
    @GET("hiring.json")
    suspend fun getItems(): List<Item>
}