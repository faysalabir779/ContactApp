package com.example.contactapp.navigation

sealed class Routes(val route: String){
    data object AllContacts: Routes("AllContacts")
    data object AddNewContact: Routes("AddNewContact")
}