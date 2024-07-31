package com.example.contactapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHost
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.contactapp.presentation.ContactState
import com.example.contactapp.presentation.screen.AddEditScreen

@Composable
fun NavGraph(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Routes.AddNewContact.route){
        composable(Routes.HomeScreen.route){

        }
        composable(Routes.AddNewContact.route) {
        }
    }
}