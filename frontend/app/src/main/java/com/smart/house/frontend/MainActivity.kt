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
import com.smart.house.frontend.screens.LoginScreen
import com.smart.house.frontend.service.ApiService
import com.smart.house.frontend.theme.SmartHouseTheme
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

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

    val authRetrofit = Retrofit.Builder()
        .baseUrl("http://10.0.2.2:8080")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiService = authRetrofit.create(ApiService::class.java)

    NavHost(navController = navController, startDestination = "loginScreen") {
        composable("loginScreen") { LoginScreen(navController, apiService) }
        composable("housesScreen") { HousesScreen(navController) }
        composable("devicesScreen") { DevicesScreen(navController) }
    }
}
