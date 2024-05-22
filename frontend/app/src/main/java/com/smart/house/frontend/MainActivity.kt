package com.smart.house.frontend

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.smart.house.frontend.screens.DevicesScreen
import com.smart.house.frontend.screens.HousesScreen
import com.smart.house.frontend.theme.SmartHouseTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SmartHouseTheme {
                AppNavigator()
            }
        }
    }
}

@Composable
fun AppNavigator() {
//    навигация по приложению, по клику по одному из домов происходит переход на другой экран
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "housesScreen") {
        composable("housesScreen") { HousesScreen(navController) }
        composable("devicesScreen") { DevicesScreen(navController) }
    }
}
