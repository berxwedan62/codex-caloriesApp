package com.example.calories.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    modifier: Modifier = Modifier,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        SettingsCard(title = "Daily Calories") {
            OutlinedTextField(
                value = uiState.dailyCalorieTarget.toString(),
                onValueChange = viewModel::onDailyCalorieTargetChanged,
                label = { Text("Daily calorie target") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
            )
        }

        SettingsCard(title = "Data") {
            Button(onClick = viewModel::onResetDemoDataClick) {
                Text("Reset demo data")
            }
        }

        SettingsCard(title = "Units") {
            UnitPreference.entries.forEach { option ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = uiState.unitPreference == option,
                        onClick = { viewModel.onUnitPreferenceChanged(option) },
                    )
                    Text(option.name.lowercase().replaceFirstChar(Char::uppercase))
                }
            }
        }

        SettingsCard(title = "Premium (Placeholder)") {
            SettingToggleRow(
                label = "Enable premium preview",
                checked = uiState.isPremiumEnabled,
                onCheckedChange = viewModel::onPremiumChanged,
            )
        }

        SettingsCard(title = "Export (Placeholder)") {
            SettingToggleRow(
                label = "Enable export preview",
                checked = uiState.exportEnabled,
                onCheckedChange = viewModel::onExportChanged,
            )
        }

        SettingsCard(title = "App Info") {
            Text("Calories App")
            Text("Version 1.0.0")
            Text(
                "A simple demo app for tracking meals and calories.",
                style = MaterialTheme.typography.bodySmall,
            )
        }

        SettingsCard(title = "About") {
            Text(
                "This settings screen keeps core preferences local using DataStore.",
                style = MaterialTheme.typography.bodySmall,
            )
        }
    }
}

@Composable
private fun SettingsCard(
    title: String,
    content: @Composable () -> Unit,
) {
    Card {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(title, style = MaterialTheme.typography.titleMedium)
            content()
        }
    }
}

@Composable
private fun SettingToggleRow(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(label)
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}
