package com.example.fetchapp.data

import com.example.fetchapp.network.ItemService
import com.example.fetchapp.repository.ItemRepository
import com.example.fetchapp.repository.NetworkItemRepository
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// Interface defines abstraction layer for dependency injection.
interface AppContainer {
    val itemRepository: ItemRepository
}

// Implementation of AppContainer. Sets up Retrofit and provides instance of ItemRepository.
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