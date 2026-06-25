package com.example.twicetwo.presentation.auth

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.twicetwo.data.repository.TwiceRepositoryImpl
import com.example.twicetwo.data.remote.RetrofitInstance

@Composable
fun LoginScreen(navController: NavController) {
    val repository = TwiceRepositoryImpl(RetrofitInstance.api)
    val viewModel = remember { AuthViewModel(repository) }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp), verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "TwiceTwo",
            style = MaterialTheme.typography.headlineLarge
        )
        Spacer(modifier = Modifier.height(20.dp))
        OutlinedTextField(
            value =
            email, onValueChange = {
            email = it
        }, label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(
            value = password, onValueChange = {
            password = it
        }, label = { Text("Password") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(20.dp))
        Button(onClick = {
            viewModel.login(
                context = navController.context,
                email = email,
                password = password,
                onSuccess = { navController.navigate("home"){
                    popUpTo ("login") { inclusive = true }
                } },
                onError = {
                    Toast.makeText(
                        navController.context,
                        "Неверный логин или пароль",
                        Toast.LENGTH_SHORT
                    ).show()
                })
        }, modifier = Modifier.fillMaxWidth()) {
            Text("Войти")
        }
        Spacer(
            modifier =
                Modifier.height(10.dp)
        )
        TextButton(
            onClick =
                { navController.navigate("register") }) {
            Text("Нет аккаунта? Зарегистрироваться")
        }
    }
}