package com.example.fetchapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fetchapp.FetchAppApplication
import com.example.fetchapp.ui.composables.ListCard
import com.example.fetchapp.viewmodel.ItemUiState
import com.example.fetchapp.viewmodel.ItemViewModel
import com.example.fetchapp.viewmodel.ItemViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val appContainer = (LocalContext.current.applicationContext as FetchAppApplication).container
    val viewModel: ItemViewModel = viewModel(factory = ItemViewModelFactory(appContainer.itemRepository))
    val uiState = viewModel.uiState.collectAsState().value

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Fetch List App") })
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            when (uiState) {
                is ItemUiState.Loading -> {
                    CircularProgressIndicator()
                }
                is ItemUiState.Error -> {
                    Text(
                        text = "Failed to load data: ${uiState.message}",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
                is ItemUiState.Success -> {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(uiState.groupedItems.entries.sortedBy { it.key }) { (listId, itemList) ->
                            ListCard(listId = listId, itemList = itemList)
                        }
                    }
                }
            }
        }
    }
}


