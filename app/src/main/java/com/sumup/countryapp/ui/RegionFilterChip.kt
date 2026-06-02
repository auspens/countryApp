package com.sumup.countryapp.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
private fun regionFilterChipColors() = FilterChipDefaults.filterChipColors(
    containerColor = MaterialTheme.colorScheme.surface,
    labelColor = MaterialTheme.colorScheme.onSurface,
    selectedContainerColor = MaterialTheme.colorScheme.primary,
    selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
)

@Composable
private fun regionFilterChipBorder(selected: Boolean) = FilterChipDefaults.filterChipBorder(
    enabled = true,
    selected = selected,
    borderColor = MaterialTheme.colorScheme.outline,
    selectedBorderColor = MaterialTheme.colorScheme.primary,
)

@Composable
internal fun RegionFilterChip(region: String, filter: String, clickAction: () -> Unit) {
    val selected = region == filter
    FilterChip(
        selected = selected,
        onClick = clickAction,
        label = {
            Text(
                text = region,
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp),
            )
        },
        colors = regionFilterChipColors(),
        border = regionFilterChipBorder(selected = selected),
        modifier = Modifier.padding(6.dp)
    )
}