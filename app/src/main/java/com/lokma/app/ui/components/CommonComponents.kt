package com.lokma.app.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.lokma.app.data.local.entity.FoodItem

@Composable
fun FoodRow(
    food: FoodItem,
    onFavoriteToggle: (() -> Unit)? = null,
    trailing: @Composable (() -> Unit)? = null
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(food.name, style = MaterialTheme.typography.titleMedium)
                Text("${food.caloriesPer100g} kcal / 100g", style = MaterialTheme.typography.bodyMedium)
            }
            Row {
                onFavoriteToggle?.let {
                    IconButton(onClick = it) {
                        Icon(
                            imageVector = if (food.isFavorite) Icons.Filled.Star else Icons.Outlined.StarOutline,
                            contentDescription = "Favorite",
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
                trailing?.invoke()
            }
        }
    }
}
