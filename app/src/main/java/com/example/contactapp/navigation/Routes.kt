package com.example.contactapp.navigation

sealed class Routes(val route: String){
    data object HomeScreen: Routes("HomeScreen")
    data object AddNewContact: Routes("AddNewContact")
}