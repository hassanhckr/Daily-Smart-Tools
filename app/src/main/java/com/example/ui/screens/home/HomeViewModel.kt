package com.example.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppPreferences
import com.example.model.ToolItem
import com.example.model.ToolsData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class HomeUiState(
    val searchQuery: String = "",
    val allTools: List<ToolItem> = ToolsData.allTools,
    val favoriteToolIds: Set<String> = emptySet(),
    val recentToolIds: List<String> = emptyList(),
    val filteredTools: List<ToolItem> = ToolsData.allTools
)

class HomeViewModel(private val appPreferences: AppPreferences) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")

    val uiState: StateFlow<HomeUiState> = combine(
        _searchQuery,
        appPreferences.favoritesFlow,
        appPreferences.recentsFlow
    ) { query, favorites, recents ->
        val filtered = if (query.isBlank()) {
            ToolsData.allTools
        } else {
            ToolsData.allTools.filter {
                it.title.contains(query, ignoreCase = true) ||
                it.description.contains(query, ignoreCase = true) ||
                it.category.contains(query, ignoreCase = true)
            }
        }

        HomeUiState(
            searchQuery = query,
            allTools = ToolsData.allTools,
            favoriteToolIds = favorites,
            recentToolIds = recents,
            filteredTools = filtered
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = HomeUiState()
    )

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    fun toggleFavorite(toolId: String) {
        viewModelScope.launch {
            appPreferences.toggleFavorite(toolId)
        }
    }

    fun recordToolUsage(toolId: String) {
        viewModelScope.launch {
            appPreferences.recordToolUsage(toolId)
        }
    }
}
