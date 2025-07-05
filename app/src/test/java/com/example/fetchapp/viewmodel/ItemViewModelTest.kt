package com.example.fetchapp.viewmodel

import app.cash.turbine.test
import com.example.fetchapp.model.Item
import com.example.fetchapp.repository.ItemRepository
import com.example.fetchapp.viewmodel.ItemUiState
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import junit.framework.TestCase.assertFalse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import java.io.IOException


// Fake repository simulating success
class FakeItemRepository : ItemRepository {
    override suspend fun getFilteredSortedItems(): Map<Int, List<Item>> {
        delay(100) // Simulate loading delay
        return mapOf(
            1 to listOf(Item(1, 1, "Item 1")),
            2 to listOf(Item(2, 2, "Item 2"))
        )
    }
}

// Test dispatcher rule

@OptIn(ExperimentalCoroutinesApi::class)
class MainDispatcherRule(
    val testDispatcher: TestDispatcher = UnconfinedTestDispatcher()
) : TestWatcher() {
    override fun starting(description: Description) {
        Dispatchers.setMain(testDispatcher)
    }

    override fun finished(description: Description) {
        Dispatchers.resetMain()
    }
}

class ItemViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var viewModel: ItemViewModel

    @Before
    fun setup() {
        viewModel = ItemViewModel(FakeItemRepository())
    }

    @Test
    fun `uiState emits Loading then Success`() = runTest {
        viewModel.uiState.test {
            // First should be Loading
            assertTrue(awaitItem() is ItemUiState.Loading)

            // Then Success with expected data
            val success = awaitItem()
            assertTrue(success is ItemUiState.Success)

            val result = (success as ItemUiState.Success).groupedItems
            assertEquals(setOf(1, 2), result.keys)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `uiState emits Error on failure`() = runTest {
        val fakeRepository = object : ItemRepository {
            override suspend fun getFilteredSortedItems(): Map<Int, List<Item>> {
                delay(100)
                throw IOException("Simulated network error")
            }
        }

        val viewModel = ItemViewModel(fakeRepository)

        viewModel.uiState.test {
            assertTrue(awaitItem() is ItemUiState.Loading)

            val errorItem = awaitItem()
            assertTrue(errorItem is ItemUiState.Error)
            assertEquals("Check your internet connection.", (errorItem as ItemUiState.Error).message)

            cancelAndConsumeRemainingEvents()
        }
    }
}

