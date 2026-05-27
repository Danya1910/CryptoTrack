package com.example.cryptotrack.presentation.states

import com.example.cryptotrack.domain.model.Search

data class SearchState (
    val suggestions: Search? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
)