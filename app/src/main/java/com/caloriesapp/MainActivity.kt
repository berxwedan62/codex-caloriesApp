package com.caloriesapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                CaloriesApp()
            }
        }
    }
}

@Composable
private fun CaloriesApp() {
    var showPrivacyNote by rememberSaveable { mutableStateOf(false) }

    Scaffold(modifier = Modifier.fillMaxSize()) { paddingValues ->
        if (showPrivacyNote) {
            PrivacyNoteScreen(
                paddingValues = paddingValues,
                onBack = { showPrivacyNote = false }
            )
        } else {
            HomeScreen(
                paddingValues = paddingValues,
                onShowPrivacyNote = { showPrivacyNote = true }
            )
        }
    }
}

@Composable
private fun HomeScreen(
    paddingValues: PaddingValues,
    onShowPrivacyNote: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Calories App",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Ready for Google Play internal testing."
        )
        Button(onClick = onShowPrivacyNote) {
            Text("View Privacy Note")
        }
    }
}

@Composable
private fun PrivacyNoteScreen(
    paddingValues: PaddingValues,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Privacy Note",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "Calories App stores your entries only on your device. " +
                "We do not collect, transmit, or sell personal data."
        )
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "If cloud sync or analytics is added in future versions, " +
                "this note will be updated before release."
        )
        TextButton(onClick = onBack) {
            Text("Back")
        }
    }
}
