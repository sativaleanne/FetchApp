package com.example.fetchapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fetchapp.model.Item
import com.example.fetchapp.repository.ItemRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.IOException

// Sealed interface for different UI states.
sealed interface ItemUiState {
    object Loading : ItemUiState
    data class Success(val groupedItems: Map<Int, List<Item>>) : ItemUiState
    data class Error(val message: String) : ItemUiState
}

// ViewModel for fetching data and exposing UI state.
class ItemViewModel(
    private val repository: ItemRepository // Business logic
) : ViewModel() {

    // Single source of truth for UI state
    private val _uiState: MutableStateFlow<ItemUiState> = MutableStateFlow(ItemUiState.Loading)
    val uiState: StateFlow<ItemUiState> = _uiState

    // Fetch items when ViewModel is initialized.
    init {
        fetchItems()
    }

    // Fetch, filter, sort items using repository. Updates UI state accordingly.
    fun fetchItems() {
        viewModelScope.launch {
            _uiState.value = ItemUiState.Loading
            _uiState.value = try {
                val groupedItems = repository.getFilteredSortedItems()
                ItemUiState.Success(groupedItems)
            } catch (e: IOException) {
                ItemUiState.Error("Check your internet connection.")
            } catch (e: Exception) {
                ItemUiState.Error("Something went wrong.")
            }
        }
    }
}

