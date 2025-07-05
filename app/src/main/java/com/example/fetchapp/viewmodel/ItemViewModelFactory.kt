package com.example.fetchapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.fetchapp.repository.ItemRepository

// Creates an instance of ItemViewModel with parameter ItemRepository.
class ItemViewModelFactory(
    private val itemRepository: ItemRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ItemViewModel(itemRepository) as T
    }
}