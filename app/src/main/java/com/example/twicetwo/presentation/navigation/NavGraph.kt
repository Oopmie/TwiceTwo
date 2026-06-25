package com.example.twicetwo.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import androidx.navigation.compose.NavHost
import com.example.twicetwo.presentation.activity.ActivityDetailsScreen
import com.example.twicetwo.presentation.activity.CreateActivityScreen
import com.example.twicetwo.presentation.auth.LoginScreen
import com.example.twicetwo.presentation.auth.RegisterScreen
import com.example.twicetwo.presentation.home.HomeScreen
import com.example.twicetwo.presentation.profile.EditProfileScreen
import com.example.twicetwo.presentation.profile.ProfileScreen

@Composable
fun AppNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        composable("login") {
            LoginScreen(navController)
        }

        composable("register") {
            RegisterScreen(navController)
        }

        composable("home") {
            HomeScreen(navController)
        }

        composable ("profile") {
            ProfileScreen(navController)
        }

        composable("edit_profile") {
            EditProfileScreen(navController)
        }

        composable ("create_activity") {
            CreateActivityScreen(navController)
        }
        composable ("activity_details/{id}") {
            backStackEntry ->

            val id = backStackEntry.arguments?.getString("id") ?: ""

            ActivityDetailsScreen (navController, id)
        }
    }
}