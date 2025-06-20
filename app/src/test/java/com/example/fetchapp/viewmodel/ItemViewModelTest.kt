package com.example.fetchapp.viewmodel

import app.cash.turbine.test
import kotlinx.coroutines.*
import com.example.fetchapp.model.Item
import com.example.fetchapp.repository.ItemRepository
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestWatcher
import org.junit.runner.Description

class FakeItemRepository : ItemRepository {
    override suspend fun getFilteredSortedItems(): Map<Int, List<Item>> {
        delay(100)
        return mapOf(
            1 to listOf(Item(1, 1, "Item 1")),
            2 to listOf(Item(2, 2, "Item 2"))
        )
    }
}

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
        val fakeRepo = FakeItemRepository()
        viewModel = ItemViewModel(fakeRepo)
    }

    @Test
    fun itemsAreLoadedCorrectly() = runTest {
        viewModel.items.test {
            skipItems(1)

            val itemsMap = awaitItem()

            assertEquals(setOf(1, 2), itemsMap.keys)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun loadingStateWorks() = runTest {
        viewModel.isLoading.test {
            val first = awaitItem()
            val second = awaitItem()

            assertTrue(first)
            assertFalse(second)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun errorIsNullOnSuccess() = runTest {
        viewModel.error.test {
            assertNull(awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }
}

