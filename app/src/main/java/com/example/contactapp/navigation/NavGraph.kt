package com.example.contactapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.contactapp.presentation.ContactViewModel
import com.example.contactapp.presentation.screen.AddEditScreen
import com.example.contactapp.presentation.screen.AllContactScreen

@Composable
fun NavGraph(viewModel: ContactViewModel, navController: NavHostController) {

    val state by viewModel.state.collectAsState()

    NavHost(navController = navController, startDestination = Routes.AllContacts.route) {
        composable(Routes.AllContacts.route) {
            AllContactScreen(viewModel = viewModel, state, navController)
        }
        composable(Routes.AddNewContact.route) {
            AddEditScreen(
                state = state,
                navController = navController
            ) { viewModel.saveContact() }
        }
    }
}