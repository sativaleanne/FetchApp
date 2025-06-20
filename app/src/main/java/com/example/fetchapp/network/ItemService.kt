package com.example.fetchapp.network

import com.example.fetchapp.model.Item
import retrofit2.http.GET

interface ItemService {
    @GET("hiring.json")
    suspend fun getItems(): List<Item>
}