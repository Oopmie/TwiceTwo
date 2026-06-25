package com.example.twicetwo.presentation.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.twicetwo.data.repository.TwiceRepositoryImpl
import com.example.twicetwo.data.remote.RetrofitInstance

@Composable
fun RegisterScreen(
    navController: NavController
) {
    val repository = TwiceRepositoryImpl(RetrofitInstance.api)
    val viewModel = remember {
        AuthViewModel(repository)
    }

    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var firstName by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {

        Text(
            text = "Регистрация",
            style = MaterialTheme.typography.headlineLarge
        )

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = username,
            onValueChange = {
                username = it
            },
            label = {
                Text("Username")
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
            },
            label = {
                Text("Email")
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
            },
            label = {
                Text("Password")
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = firstName,
            onValueChange = {
                firstName = it
            },
            label = {
                Text("Имя")
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = age,
            onValueChange = {
                age = it
            },
            label = {
                Text("Возраст")
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = city,
            onValueChange = {
                city = it
            },
            label = {
                Text("Город")
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = gender,
            onValueChange = {
                gender = it
            },
            label = {
                Text("Пол")
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = phoneNumber,
            onValueChange = {
                phoneNumber = it
            },
            label = {
                Text("Номер телефона")
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))

        val isFormValid =
            username.isNotBlank() &&
                    email.isNotBlank() &&
                    password.isNotBlank() &&
                    firstName.isNotBlank() &&
                    age.isNotBlank() &&
                    city.isNotBlank() &&
                    gender.isNotBlank() &&
                    phoneNumber.isNotBlank()

        Button(
            onClick = {
                viewModel.register(
                    context = navController.context,
                    username = username,
                    email = email,
                    password = password,
                    firstName = firstName,
                    age = age.toIntOrNull() ?: 18,
                    city = city,
                    gender = gender,
                    phoneNumber = phoneNumber,
                    onSuccess = {
                        navController.navigate("home"){
                            popUpTo("register") { inclusive = true }
                        }
                    }
                )
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = isFormValid
        ) {
            Text("Зарегистрироваться")
        }

        Spacer(modifier = Modifier.height(10.dp))

        TextButton(
            onClick = {
                navController.navigate("login")
            }
        ) {
            Text("Уже есть аккаунт? Войти")
        }
    }
}