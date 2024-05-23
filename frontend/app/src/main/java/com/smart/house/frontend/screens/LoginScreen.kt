package com.smart.house.frontend.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.smart.house.frontend.R
import com.smart.house.frontend.service.ApiService
import com.smart.house.frontend.service.AuthRequest
import com.smart.house.frontend.service.LoginResponse
import com.smart.house.frontend.service.RegisterResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun LoginScreen(navController: NavController, apiService: ApiService) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }

    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.house),
            contentDescription = null
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Добро пожаловать!")

        Spacer(modifier = Modifier.height(16.dp))

        BasicTextField(
            value = username,
            onValueChange = { username = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            decorationBox = { innerTextField ->
                if (username.isEmpty()) {
                    Text(text = "Имя пользователя")
                }
                innerTextField()
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        BasicTextField(
            value = email,
            onValueChange = { email = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            decorationBox = { innerTextField ->
                if (email.isEmpty()) {
                    Text(text = "Почта пользователя")
                }
                innerTextField()
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        BasicTextField(
            value = password,
            onValueChange = { password = it },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            decorationBox = { innerTextField ->
                if (password.isEmpty()) {
                    Text(text = "Пароль")
                }
                innerTextField()
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { sendLoginRequest(navController, apiService, context, username, password, email) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Войти")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { sendRegisterRequest(navController, apiService, context, username, password, email) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Зарегистрироваться")
        }
    }
}

fun sendLoginRequest(
    navController: NavController,
    apiService: ApiService,
    context: Context,
    username: String,
    password: String,
    email: String,
) {
    val call = apiService.login(AuthRequest(username, password, email))
    call.enqueue(object : Callback<LoginResponse> {
        override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
            if (response.isSuccessful && response.body()?.token != null) {
                // Перевести пользователя на дальнейшую страницу
                navigateToNextScreen(navController)
            } else {
                println(response.body())
                Toast.makeText(context, "Ошибка входа", Toast.LENGTH_SHORT).show()
            }
        }

        override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
            t.printStackTrace()
            Toast.makeText(context, "Сетевая ошибка", Toast.LENGTH_SHORT).show()
        }
    })
}

fun sendRegisterRequest(
    navController: NavController,
    apiService: ApiService,
    context: Context,
    username: String,
    password: String,
    email: String
) {
    val call = apiService.register(AuthRequest(username, password, email))
    call.enqueue(object : Callback<RegisterResponse> {
        override fun onResponse(
            call: Call<RegisterResponse>,
            response: Response<RegisterResponse>
        ) {
            if (response.isSuccessful && response.body()?.success == true) {
                // Перевести пользователя на дальнейшую страницу
                navigateToNextScreen(navController)
            } else {
                Toast.makeText(context, "Ошибка регистрации", Toast.LENGTH_SHORT).show()
            }
        }

        override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
            Toast.makeText(context, "Сетевая ошибка", Toast.LENGTH_SHORT).show()
        }
    })
}

private fun navigateToNextScreen(navController: NavController) {
    navController.navigate("housesScreen")
}
