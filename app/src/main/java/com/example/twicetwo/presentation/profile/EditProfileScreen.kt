package com.example.twicetwo.presentation.profile

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.twicetwo.data.local.SessionManager
import com.example.twicetwo.data.remote.RetrofitInstance
import com.example.twicetwo.data.repository.TwiceRepositoryImpl
import com.example.twicetwo.domain.repository.ProfileViewModelFactory

@Composable
fun EditProfileScreen(
    navController: NavController
) {
    val repository = TwiceRepositoryImpl(
        RetrofitInstance.api
    )

    val viewModel: ProfileViewModel = viewModel(
        factory = ProfileViewModelFactory(repository)
    )

    val user = viewModel.user

    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.loadUser(context)
    }

    var username by remember {
        mutableStateOf(user?.username ?: "")
    }

    var firstName by remember {
        mutableStateOf(user?.firstName ?: "")
    }

    var age by remember {
        mutableStateOf(user?.age?.toString() ?: "")
    }

    var city by remember {
        mutableStateOf(user?.city ?: "")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {

        Text(
            text = "Редактирование профиля",
            style = MaterialTheme.typography.headlineMedium
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

        Button(
            onClick = {
                user?.let { current ->
                    println("SAVE CLICKED")
                    val updatedUser = current.copy(
                        username = username,
                        firstName = firstName,
                        age = age.toIntOrNull() ?: current.age,
                        city = city
                    )

                    println("BEFORE UPDATE = $updatedUser")

                    viewModel.updateProfile(updatedUser) { result ->

                        println("AFTER UPDATE = $result")

                        SessionManager.currentUser = result

                        navController.popBackStack()
                    }
                }
            }
        ) {
            Text("Сохранить")
        }
    }
}