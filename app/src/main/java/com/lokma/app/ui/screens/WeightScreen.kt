package com.lokma.app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
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
import com.lokma.app.ui.viewmodel.WeightViewModel

@Composable
fun WeightScreen() {
    val app = LocalContext.current.applicationContext as LokmaApplication
    val vm: WeightViewModel = viewModel(factory = WeightViewModel.factory(app.container.weightRepository))
    val entries by vm.entries.collectAsStateWithLifecycle()

    var input by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text("Weight")
        OutlinedTextField(value = input, onValueChange = { input = it }, label = { Text("Today (kg)") }, modifier = Modifier.fillMaxWidth())
        Button(onClick = {
            input.toFloatOrNull()?.let {
                vm.addOrUpdateToday(it)
                input = ""
            }
        }) { Text("Save") }

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(entries, key = { it.id }) { item ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Row(modifier = Modifier.fillMaxWidth().padding(10.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("${item.date} • ${item.weightKg} kg")
                        Button(onClick = { vm.delete(item.id) }) { Text("Delete") }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun WeightPreview() {
    Column(modifier = Modifier.padding(16.dp)) { Text("Weight preview") }
}
