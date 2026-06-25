package com.example.twicetwo.presentation.activity

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.twicetwo.data.local.SessionManager
import com.example.twicetwo.data.remote.RetrofitInstance
import com.example.twicetwo.data.repository.TwiceRepositoryImpl
import com.example.twicetwo.presentation.auth.AuthViewModel

@Composable
fun ActivityDetailsScreen(
    navController: NavController,
    id: String
) {
    val repository = TwiceRepositoryImpl(
        RetrofitInstance.api
    )

    val viewModel = remember {
        ActivityViewModel(repository)
    }
    val usernames = viewModel.participantsUsernames.value

    val activity = viewModel.selectedActivity
    val participants = viewModel.participants

    LaunchedEffect(Unit) {
        viewModel.loadActivity(id)
        viewModel.loadParticipants(id)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {

        Text(
            text = activity?.title ?: "",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = activity?.description ?: ""
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "Дата: ${activity?.startDate ?: ""}"
        )

        Text(
            text = "Место: ${activity?.location ?: ""}"
        )

        Text(
            text = "Организатор: ${activity?.organizerName ?: ""}"
        )

        Text(
            text = "Возраст: ${activity?.ageRestriction ?: ""}+"
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Участники: ${participants.size}/${activity?.maxParticipants ?: 0}"
        )

        Spacer(modifier = Modifier.height(20.dp))

        if (SessionManager.currentUser?.hasSubscription == true) {

            Text(
                text = "Список участников",
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(10.dp))

            Column {
                usernames.forEach { name ->

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp)
                        ) {
                            Text(
                                text = name
                            )
                        }
                    }
                }
            }

        } else {

            Text(
                text = "Полный список участников доступен только по PRO подписке"
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                val currentUser =
                    SessionManager.currentUser ?: return@Button

                viewModel.joinActivity(
                    activityId = id,
                    userId = currentUser.id ?: ""
                )
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Записаться")
        }
    }
}