package com.example.twicetwo.presentation.activity

import android.net.Uri
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.twicetwo.data.local.SessionManager
import com.example.twicetwo.data.model.ActivityDto
import com.example.twicetwo.data.remote.RetrofitInstance
import com.example.twicetwo.data.repository.TwiceRepositoryImpl
import kotlinx.coroutines.launch

@Composable
fun CreateActivityScreen(
    navController: NavController
) {
    val repository = TwiceRepositoryImpl(
        RetrofitInstance.api
    )

    val context = LocalContext.current

    var selectedImageUri by remember {
        mutableStateOf<Uri?>(null)
    }

    val launcher = rememberLauncherForActivityResult (
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            selectedImageUri = uri
        }
    }

    var title by remember {
        mutableStateOf("")
    }

    var description by remember {
        mutableStateOf("")
    }

    var location by remember {
        mutableStateOf("")
    }

    var date by remember {
        mutableStateOf("")
    }

    var time by remember {
        mutableStateOf("")
    }

    var maxParticipants by remember {
        mutableStateOf("")
    }

    var ageRestriction by remember {
        mutableStateOf("")
    }

    var isPro by remember {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier
            .verticalScroll(state = ScrollState(0))
            .fillMaxSize()
            .padding(20.dp)
    ) {

        Text(
            text = "Создание мероприятия",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = title,
            onValueChange = {
                title = it
            },
            label = {
                Text("Название")
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        if (selectedImageUri != null) {

            val bitmap = MediaStore.Images.Media.getBitmap(
                context.contentResolver,
                selectedImageUri
            )

            Image(
                bitmap = bitmap.asImageBitmap(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clip(RoundedCornerShape(16.dp))
            )

        } else {

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clip(RoundedCornerShape(16.dp))
            ) {}
        }

        Spacer(modifier = Modifier.height(10.dp))

        Button(
            onClick = {
                launcher.launch("image/*")
            }
        ) {
            Text("Выбрать фото мероприятия")
        }

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = description,
            onValueChange = {
                description = it
            },
            label = {
                Text("Описание")
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = location,
            onValueChange = {
                location = it
            },
            label = {
                Text("Место")
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = date,
            onValueChange = {
                date = it
            },
            label = {
                Text("Дата")
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = time,
            onValueChange = {
                time = it
            },
            label = {
                Text("Время")
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = maxParticipants,
            onValueChange = {
                maxParticipants = it
            },
            label = {
                Text("Макс. участников")
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = ageRestriction,
            onValueChange = {
                ageRestriction = it
            },
            label = {
                Text("Возрастное ограничение")
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        Row {
            Checkbox(
                checked = isPro,
                onCheckedChange = {
                    isPro = it
                }
            )

            Text("PRO мероприятие")
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {

                val newActivity = ActivityDto(
                    title = title,
                    description = description,
                    about = "",
                    organizerId = SessionManager.currentUser?.id ?: "",
                    startDate = date,
                    startTime = time,
                    location = location,
                    maxParticipants = maxParticipants.toIntOrNull() ?: 10,
                    ageRestriction = ageRestriction.toIntOrNull() ?: 18,
                    imageUrl = selectedImageUri?.toString() ?: "",
                    organizerName = SessionManager.currentUser?.username ?: "",
                    isPro = isPro
                )

                kotlinx.coroutines.CoroutineScope(
                    kotlinx.coroutines.Dispatchers.IO
                ).launch {

                    repository.createActivity(
                        newActivity
                    )
                }

                navController.popBackStack()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Создать")
        }
    }
}