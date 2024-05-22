package com.smart.house.frontend.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ContentAlpha
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.Scaffold
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@Composable
fun DevicesScreen(navController: NavController) {
    val showDialog = remember { mutableStateOf(false) }

    Scaffold (
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                BackButton(navController)

                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Устройства",
                        style = MaterialTheme.typography.h2
                    )
                }

            }
        },
        floatingActionButton = {
            AddButton(showDialog = showDialog)
        },
        content = { padding ->
            Column(
                modifier = Modifier.padding(padding)
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    DeviceItem("чайник", true)
                    DeviceItem("чайник", false)
                }
            }
        }
    )
}

@Composable
fun DeviceItem(name: String, state: Boolean) {
    Card (
        modifier = Modifier
            .fillMaxWidth(0.5f)
            .padding(12.dp),
        shape = RoundedCornerShape(10.dp),
        elevation = 8.dp,
    ) {
        Box (
            modifier = Modifier.padding(16.dp),
        ) {
            Column (
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = name,
                    modifier = Modifier
                        .padding(bottom = 12.dp),
                    style = MaterialTheme.typography.h5                )

                CustomSwitch(state)
            }
        }
    }
}

@Composable
fun CustomSwitch(state: Boolean) {
    val checked = remember { mutableStateOf(state) }

    Switch(
        modifier = Modifier
            .scale(1.2f),
        checked = checked.value,
        onCheckedChange = {checked.value = it},
        thumbContent = {
            Icon(
                imageVector = if (checked.value) Icons.Filled.Check else Icons.Filled.Close,
                contentDescription = "back",
                modifier = Modifier.size(SwitchDefaults.IconSize)
            )
        },
        colors = SwitchDefaults.colors(
            checkedThumbColor = Color.White,
            checkedTrackColor = MaterialTheme.colors.secondary,
            checkedIconColor = MaterialTheme.colors.secondary,
            uncheckedThumbColor = Color.Gray,
            uncheckedTrackColor = Color.White,
            uncheckedIconColor = Color.White,
            disabledCheckedThumbColor = Color.Blue.copy(alpha = ContentAlpha.disabled),
            disabledUncheckedThumbColor = Color.Blue.copy(alpha = ContentAlpha.disabled),
        )
    )
}

@Composable
fun BackButton(navController: NavController) {
    Icon(
        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
        contentDescription = null,
        modifier = Modifier
            .padding(start = 8.dp)
            .clip(CircleShape)
            .clickable {
                navController.navigateUp()
            }
            .padding(16.dp)
    )
}

@Composable
fun AddButton(showDialog: MutableState<Boolean>) {
    FloatingActionButton(
        backgroundColor = MaterialTheme.colors.secondary,
        onClick = {
            showDialog.value = true
        }
    ) {
        Icon(
            imageVector = Icons.Filled.Add,
            contentDescription = "add device",
            tint = Color.White,
        )
    }
}

@Composable
fun DeviceDialog(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onAddHouse: (House) -> Unit
) {
    if (showDialog) {
        val name = remember { mutableStateOf("") }

//        AlertDialog(
//            onDismissRequest = { onDismiss() },
//            text = {
//                Column(modifier = Modifier.padding(top = 8.dp)) {
//                    Text(
//                        text = "Добавить новый дом",
//                        style = MaterialTheme.typography.h5
//                    )
//                    Spacer(modifier = Modifier.height(8.dp))
//                    OutlinedTextField(
//                        value = name.value,
//                        onValueChange = { name.value = it },
//                        label = { Text("Название") },
//                        modifier = Modifier
//                            .fillMaxWidth()
//                    )
//                    Spacer(modifier = Modifier.height(8.dp))
//                    OutlinedTextField(
//                        value = description.value,
//                        onValueChange = { description.value = it },
//                        label = { Text("Описание") },
//                        modifier = Modifier
//                            .fillMaxWidth(),
//                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
//                        keyboardActions = KeyboardActions(onDone = {
//                            onAddHouse(House(0, name.value, description.value))
//                            onDismiss()
//                        })
//                    )
//                }
//            },
//            confirmButton = {
//                Button(
//                    onClick = {
//                        onAddHouse(House(0, name.value, description.value))
//                        onDismiss()
//                    },
//                    colors = ButtonDefaults.buttonColors(
//                        backgroundColor = Color(0xfff1be1f),
//                        contentColor = Color(0xFFFFFFFF)
//                    ),
//                    modifier = Modifier.padding(bottom = 8.dp, end = 16.dp)
//                ) {
//                    Text("Добавить")
//                }
//            },
//            dismissButton = {
//                Button(
//                    colors = ButtonDefaults.buttonColors(
//                        backgroundColor = Color(0xfff1be1f),
//                        contentColor = Color(0xFFFFFFFF)
//                    ),
//                    modifier = Modifier.padding(bottom = 8.dp, end = 8.dp),
//                    onClick = { onDismiss() }) {
//                    Text("Отмена")
//                }
//            }
//        )
    }
}

@Preview(showBackground = true, name = "DevicesScreen")
@Composable
fun ScreenPreview() {
    DevicesScreen(rememberNavController())
}
