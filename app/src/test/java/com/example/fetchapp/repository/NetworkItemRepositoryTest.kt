package com.example.fetchapp.repository

import com.example.fetchapp.model.Item
import com.example.fetchapp.network.ItemService
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

// Fake Items for testing blank, null, and order
class FakeItemService : ItemService {
    override suspend fun getItems(): List<Item> {
        return listOf(
            Item(1, 1, "Item 10"),
            Item(2, 1, "Item 2"),
            Item(3, 2, "Item 5"),
            Item(4, 2, "Item 1"),
            Item(5, 2, ""),
            Item(6, 3, null)
        )
    }
}

class NetworkItemRepositoryTest {

    private lateinit var repository: NetworkItemRepository

    @Before
    fun setup() {
        val fakeService = FakeItemService()
        repository = NetworkItemRepository(fakeService)
    }

    @Test
    fun checkFilteredSortedItemsCorrect() = runTest {
        val result = repository.getFilteredSortedItems()

        assertEquals(2, result[1]?.size)
        assertEquals(2, result[2]?.size)
        assertFalse(result.containsKey(3))

        // check sort order
        assertEquals(listOf("Item 2", "Item 10"), result[1]?.map { it.name })
    }
}
