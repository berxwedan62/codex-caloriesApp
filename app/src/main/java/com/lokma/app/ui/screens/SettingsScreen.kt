package com.lokma.app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Switch
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.lokma.app.LokmaApplication
import com.lokma.app.ui.viewmodel.SettingsViewModel

@Composable
fun SettingsScreen() {
    val app = LocalContext.current.applicationContext as LokmaApplication
    val vm: SettingsViewModel = viewModel(factory = SettingsViewModel.factory(app.container.settingsRepository))
    val settings by vm.settings.collectAsStateWithLifecycle()

    var targetInput by remember(settings.dailyCalorieTarget) { mutableStateOf(settings.dailyCalorieTarget.toString()) }
    var warningThresholdInput by remember(settings.calorieWarningThreshold) {
        mutableStateOf(settings.calorieWarningThreshold.toString())
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text("Settings")
        OutlinedTextField(
            value = targetInput,
            onValueChange = { targetInput = it },
            label = { Text("Daily calorie target") },
            modifier = Modifier.fillMaxWidth()
        )
        Button(onClick = { targetInput.toIntOrNull()?.let { vm.updateTarget(it) } }) { Text("Save target") }

        OutlinedTextField(
            value = warningThresholdInput,
            onValueChange = { warningThresholdInput = it },
            label = { Text("Calorie Warning Threshold") },
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = {
                warningThresholdInput.toIntOrNull()
                    ?.takeIf { it >= 0 }
                    ?.let { vm.updateWarningThreshold(it) }
            }
        ) {
            Text("Save warning threshold")
        }

        Text("Use metric units")
        Switch(checked = settings.useMetricUnits, onCheckedChange = { vm.toggleMetric(it) })

        Text("Premium unlocked: ${settings.premiumUnlocked}")
    }
}

@Preview(showBackground = true)
@Composable
private fun SettingsPreview() {
    Column(modifier = Modifier.padding(16.dp)) { Text("Settings preview") }
}
