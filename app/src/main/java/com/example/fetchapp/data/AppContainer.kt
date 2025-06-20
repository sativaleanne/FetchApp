package com.example.fetchapp.data

import com.example.fetchapp.network.ItemService
import com.example.fetchapp.repository.ItemRepository
import com.example.fetchapp.repository.NetworkItemRepository
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

interface AppContainer {
    val itemRepository: ItemRepository
}

class DefaultAppContainer : AppContainer {
    private val baseUrl = "https://hiring.fetch.com/"

    private val retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(baseUrl)
        .build()

    private val itemService: ItemService by lazy {
        retrofit.create(ItemService::class.java)
    }

    override val itemRepository: ItemRepository by lazy {
        NetworkItemRepository(itemService)
    }
}