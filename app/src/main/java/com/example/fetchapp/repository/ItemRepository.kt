package com.example.fetchapp.repository

import com.example.fetchapp.model.Item
import com.example.fetchapp.network.ItemService

interface ItemRepository {
    suspend fun getFilteredSortedItems(): Map<Int, List<Item>>
}

class NetworkItemRepository(
    private val itemService: ItemService
) : ItemRepository {

    // Sort and filter out Null and Blank values
    override suspend fun getFilteredSortedItems(): Map<Int, List<Item>> {
        return itemService.getItems()
            .filter { !it.name.isNullOrBlank() }
            .groupBy { it.listId }
            .toSortedMap()
            .mapValues { entry ->
                entry.value.sortedBy { extractItemNumber(it.name) }
            }
    }

    // Sort name by number helper
    private fun extractItemNumber(name: String?): Int {
        return name?.substringAfter("Item")?.trim()?.toIntOrNull() ?: Int.MAX_VALUE
    }
}