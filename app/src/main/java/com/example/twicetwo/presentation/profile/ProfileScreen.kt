package com.example.twicetwo.presentation.profile

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.twicetwo.data.local.SessionManager
import com.example.twicetwo.data.remote.RetrofitInstance
import com.example.twicetwo.data.repository.TwiceRepositoryImpl
import com.example.twicetwo.domain.repository.ProfileViewModelFactory

@Composable
fun ProfileScreen(
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

    var showDialog by remember {
        mutableStateOf(false)
    }

    var selectedImageUri by remember(user?.avatar) {
        mutableStateOf(
            user?.avatar?.let {
                Uri.parse(it)
            }
        )
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri ->

        if (uri != null) {

            context.contentResolver.takePersistableUriPermission(
                uri,
                Intent.FLAG_GRANT_READ_URI_PERMISSION
            )

            selectedImageUri = uri

            viewModel.updateAvatar(uri.toString())
        }
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {

        if (selectedImageUri != null) {

            val bitmap = rotateImageIfRequired(context, selectedImageUri)

            bitmap?.let {
                Image(
                    bitmap = it.asImageBitmap(),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(140.dp)
                        .clip(CircleShape)
                )
            }

        } else {
            Surface(
                modifier = Modifier
                    .size(140.dp)
                    .clip(CircleShape)
            ) {}
        }

        Spacer(modifier = Modifier.height(10.dp))

        Button(
            onClick = {
                launcher.launch(arrayOf("image/*"))
            }
        ) {
            Text("Загрузить фото")
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = user?.username?: ""
        )

        Text(
            text = user?.email ?: ""
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text("Имя: ${user?.firstName ?: ""}")
        Text("Возраст: ${user?.age ?: ""}")
        Text("Город: ${user?.city ?: ""}")
        Text("Пол: ${user?.gender ?: ""}")
        Text("Телефон: ${user?.phoneNumber ?: ""}")

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                navController.navigate("edit_profile")
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Изменить профиль")
        }

        Spacer(modifier = Modifier.height(10.dp))

        if (user != null && !user.hasSubscription) {
            Button(
                onClick = {
                    viewModel.buySubscription {
                        showDialog = true
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Оформить подписку")
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Button(
            onClick = {
                SessionManager.logout(context)

                navController.navigate("login") {
                    popUpTo(0) {
                        inclusive = true
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Выйти")
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = {
                showDialog = false
            },
            confirmButton = {
                Button(
                    onClick = {
                        showDialog = false
                    }
                ) {
                    Text("OK")
                }
            },
            title = {
                Text("Подписка оформлена")
            },
            text = {
                Text("Теперь вам доступны PRO мероприятия")
            }
        )
    }
}

fun rotateImageIfRequired(
    context: Context,
    uri: Uri?
): Bitmap? {

    if (uri == null) return null

    return try {
        val inputStream = context.contentResolver.openInputStream(uri) ?: return null
        val bitmap = BitmapFactory.decodeStream(inputStream) ?: return null

        val exifInputStream = context.contentResolver.openInputStream(uri) ?: return bitmap
        val exif = ExifInterface(exifInputStream)

        val orientation = exif.getAttributeInt(
            ExifInterface.TAG_ORIENTATION,
            ExifInterface.ORIENTATION_NORMAL
        )

        val matrix = Matrix()

        when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> matrix.postRotate(90f)
            ExifInterface.ORIENTATION_ROTATE_180 -> matrix.postRotate(180f)
            ExifInterface.ORIENTATION_ROTATE_270 -> matrix.postRotate(270f)
        }

        Bitmap.createBitmap(
            bitmap,
            0,
            0,
            bitmap.width,
            bitmap.height,
            matrix,
            true
        )

    } catch (e: Exception) {
        null
    }
}