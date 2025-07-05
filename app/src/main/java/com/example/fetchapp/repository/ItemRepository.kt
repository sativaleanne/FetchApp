package com.example.fetchapp.repository

import com.example.fetchapp.model.Item
import com.example.fetchapp.network.ItemService

// Interface for fetching items in a filtered and sorted format.
interface ItemRepository {
    suspend fun getFilteredSortedItems(): Map<Int, List<Item>>
}

// Implementation of ItemRepository that fetches the data.
class NetworkItemRepository(
    private val itemService: ItemService
) : ItemRepository {

    // Sort and filter out Null and Blank values
    override suspend fun getFilteredSortedItems(): Map<Int, List<Item>> {
        return itemService.getItems()
            // Remove null or blank
            .filter { !it.name.isNullOrBlank() }
            // Group by listId
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