# FetchApp

A native Android app in Kotlin that retrieves data from https://hiring.fetch.com/hiring.json.

## Features

- Retrieves item list from remote API
- Groups items by `listId`
- Sorts items:
    - First by `listId`
    - Then by numeric order of the value in `name` (e.g., `"Item 3"`, `"Item 10"`, `"Item 25"`)
- Filters out any items where `name` is blank or Null
- Displays items in expandable, scrollable cards by `listId`
- Responsive UI built with Jetpack Compose
- Unit tests added for Repository and ViewModel

## How to Run

1. Clone Repo
2. Open in Android Studio
3. Build and Run

## Architecture

```plaintext
---------------------
  ItemService.kt       <-- Retrofit API service
---------------------
          ↓
---------------------
  ItemRepository.kt    <-- Business logic: filtering, sorting
---------------------
          ↓
---------------------
  ItemViewModel.kt     <-- ViewModel exposes StateFlow
---------------------
          ↓
---------------------
  MainScreen.kt        <-- Compose UI
  ListCard.kt        
---------------------
```

## Testing
Unit tests:

NetworkItemRepositoryTest — verifies filtering and sorting logic

ItemViewModelTest — verifies loading state, success and error flows

## Author
Sativa Maciel
