package com.example.fetchapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fetchapp.FetchAppApplication
import com.example.fetchapp.ui.composables.ListCard
import com.example.fetchapp.viewmodel.ItemViewModel
import com.example.fetchapp.viewmodel.ItemViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {

    val appContainer = (LocalContext.current.applicationContext as FetchAppApplication).container
    val viewModel: ItemViewModel = viewModel(factory = ItemViewModelFactory(appContainer.itemRepository))

    val itemsState = viewModel.items.collectAsState()
    val isLoading = viewModel.isLoading.collectAsState()
    val error = viewModel.error.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Fetch List App") }
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize()
            .padding(16.dp)) {

            when {
                isLoading.value -> {
                    CircularProgressIndicator(modifier = Modifier.padding(16.dp))
                }
                error.value != null -> {
                    Text(
                        text = error.value ?: "Unknown error",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(16.dp)
                    )
                }
                else -> {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(itemsState.value.entries.sortedBy { it.key }) { (listId, itemList) ->
                            ListCard(listId = listId, itemList = itemList)
                        }
                    }
                }
            }
        }
    }
}


