package com.smart.house.frontend.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.material.ButtonDefaults
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.runtime.MutableState
import androidx.compose.ui.text.input.ImeAction
import com.smart.house.frontend.R

data class House(val value: Int, val name: String, val description: String)

// Мок-данные для списков домов
val mockHouses = listOf(
    House(1,"Домик Кена", "Description 1"),
    House(2,"Домик Барби", "Description 2"),
    House(3,"Мой дом", "Description 2"),
    House(4,"Дом мамы", "Description 2"),
    House(5,"Дом сына", "Description 2"),
)

@Composable
fun AddHouseDialog(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onAddHouse: (House) -> Unit
) {
    if (showDialog) {
        val name = remember { mutableStateOf("") }
        val description = remember { mutableStateOf("") }
        AlertDialog(
            onDismissRequest = { onDismiss() },
            title = null,
            text = {
                Column(modifier = Modifier.padding(top = 8.dp)) {
                    Text(
                        text = "Добавить новый дом",
                        style = MaterialTheme.typography.h5
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = name.value,
                        onValueChange = { name.value = it },
                        label = { Text("Название") },
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = description.value,
                        onValueChange = { description.value = it },
                        label = { Text("Описание") },
                        modifier = Modifier
                            .fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(onDone = {
                            onAddHouse(House(0, name.value, description.value))
                            onDismiss()
                        })
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        onAddHouse(House(0, name.value, description.value))
                        onDismiss()
                    },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = MaterialTheme.colors.secondary,
                        contentColor = MaterialTheme.colors.onSecondary
                    ),
                    modifier = Modifier.padding(bottom = 8.dp, end = 16.dp)
                ) {
                    Text("Добавить")
                }
            },
            dismissButton = {
                Button(
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = MaterialTheme.colors.secondary,
                        contentColor = MaterialTheme.colors.onSecondary
                    ),
                    modifier = Modifier.padding(bottom = 8.dp, end = 8.dp),
                    onClick = { onDismiss() }) {
                    Text("Отмена")
                }
            }
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HousesScreen(navController: NavController) {
    val houses = remember { mutableStateOf(mockHouses) }
    val showDialog = remember { mutableStateOf(false) }
    Scaffold(
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                contentAlignment = Alignment.Center
            ) {

                Text(
                    text = "Дома",
                    style = MaterialTheme.typography.h1
                )
            }
        },
        floatingActionButton = {
            AddHouseButton(showDialog = showDialog)
        },
        content = { padding ->
            // Здесь отображается список домов
            LazyColumn(
                modifier = Modifier.padding(padding)
            ) {
                items(houses.value) { house ->
                    HouseCard(house = house, onHouseClick = {
                        // Переход на экран DevicesScreen
                        navController.navigate("devicesScreen")
                    })
                }
            }
        }
    )

    AddHouseDialog(
        showDialog = showDialog.value,
        onDismiss = { showDialog.value = false },
        onAddHouse = { newHouse ->
            houses.value += newHouse
            showDialog.value = false
        }
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HouseCard(house: House, onHouseClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(8.dp),
        elevation = 4.dp,
        onClick = onHouseClick
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color(0xFF111111))
        ) {
            val image = painterResource(id = R.drawable.house)

            Image(
                painter = image,
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.FillBounds,
                alpha = 0.7F
            )
            Text(
                text = house.name,
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(8.dp),
                color = MaterialTheme.colors.onPrimary,
                style = MaterialTheme.typography.h2
            )
        }
    }
}

@Composable
fun AddHouseButton(showDialog: MutableState<Boolean>) {
    FloatingActionButton(
        contentColor = MaterialTheme.colors.onSecondary,
        backgroundColor = MaterialTheme.colors.secondary,
        onClick = {
            // Открываем диалоговое окно
            showDialog.value = true
        }
    ) {
        Icon(Icons.Filled.Add, contentDescription = "Add House")
    }
}


@Preview(showBackground = true)
@Composable
fun HousesScreenPreview() {
    // Создание мок-версии NavController - для превью, в основном приложении все ок
    val navController = rememberNavController()
    HousesScreen(navController)
}
