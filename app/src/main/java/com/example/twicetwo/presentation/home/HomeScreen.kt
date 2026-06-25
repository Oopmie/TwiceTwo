package com.example.twicetwo.presentation.home

import android.graphics.BitmapFactory
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.twicetwo.data.local.SessionManager
import com.example.twicetwo.data.remote.RetrofitInstance
import com.example.twicetwo.data.repository.TwiceRepositoryImpl
import kotlinx.coroutines.launch
import com.example.twicetwo.domain.repository.loadBitmapFromUri
import androidx.compose.ui.graphics.asImageBitmap

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController
) {
    val scope = rememberCoroutineScope()
    val repository = TwiceRepositoryImpl(RetrofitInstance.api)

    val viewModel = remember {
        HomeViewModel(repository)
    }

    val drawerState = rememberDrawerState(
        initialValue = DrawerValue.Closed
    )

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {

                Spacer(modifier = Modifier.height(20.dp))

                TextButton(
                    onClick = {
                        navController.navigate("profile")
                    }
                ) {
                    Text("Профиль")
                }

                TextButton(
                    onClick = {
                        navController.navigate("create_activity")
                    }
                ) {
                    Text("Создать мероприятие")
                }

                TextButton(
                    onClick = {
                        SessionManager.logout(navController.context)

                        navController.navigate("login") {
                            popUpTo("home") { inclusive = true }
                        }
                    }
                ) {
                    Text("Выйти")
                }
            }
        }
    ) {

        Column(
            modifier = Modifier.fillMaxSize()
        ) {

            TopAppBar(
                title = {
                    OutlinedTextField(
                        value = viewModel.searchQuery,
                        onValueChange = {
                            viewModel.updateSearch(it)
                        },
                        placeholder = {
                            Text("Поиск")
                        }
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            scope.launch {
                                if (drawerState.isClosed) {
                                    drawerState.open()
                                } else {
                                    drawerState.close()
                                }
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = null
                        )
                    }
                }
            )

            LazyColumn (modifier = Modifier.weight(1f)){

                items(
                    viewModel.filteredActivities()
                ) { activity ->

                    val context = LocalContext.current

                    val bitmap = remember(activity.imageUrl) {
                        activity.imageUrl.takeIf { it.isNotEmpty() }?.let {
                            BitmapFactory.decodeFile(it)
                        }
                    }

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp)
                            .clickable {
                                navController.navigate(
                                    "activity_details/${activity.id}"
                                )
                            },
                        colors = CardDefaults.cardColors(
                            containerColor =
                                if (activity.isPro)
                                    MaterialTheme.colorScheme.primaryContainer
                                else
                                    MaterialTheme.colorScheme.surface
                        ),
                        border = BorderStroke(1.dp, color = MaterialTheme.colorScheme.primaryContainer)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.padding(16.dp)
                                .fillMaxWidth()) {

                            Column(
                                modifier = Modifier.padding(end = 16.dp)
                            ) {

                                if (activity.isPro) {
                                    Text("PRO")
                                }

                                Text(
                                    text = activity.title,
                                    style = MaterialTheme.typography.titleLarge
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                Text(activity.organizerName)

                                Text(activity.startDate)

                                Text(activity.startTime)

                                Text(activity.location)
                            }
                            if (bitmap != null) {
                                Image(
                                    bitmap = bitmap.asImageBitmap(),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .height(100.dp)
                                        .width(170.dp)
                                        .clip(RoundedCornerShape(12.dp)),
                                    contentScale = ContentScale.Crop
                                )
                            } else {
                                Box(
                                    modifier = Modifier
                                        .height(100.dp)
                                        .width(170.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(Color.Gray)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}