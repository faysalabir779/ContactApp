package com.example.contactapp.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.contactapp.data.database.Contact
import com.example.contactapp.data.database.ContactDatabase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContactViewModel @Inject constructor(var database: ContactDatabase) : ViewModel() {
    private var isSortedByName = MutableStateFlow(false)
    private val contact = isSortedByName.flatMapConcat {
        if (it) {
            database.dao.getContactsSortByName()
        } else {
            database.dao.getContactsSortByDate()
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    val _state = MutableStateFlow(ContactState())

    val state = combine(_state, contact, isSortedByName) { _state, contact, isSortedByName ->
        _state.copy(contact = contact)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ContactState())

    fun changeSorting(){
        isSortedByName.value = !isSortedByName.value
    }

    fun saveContact() {
        val contact = Contact(
            id = state.value.id.value,
            name = state.value.name.value,
            number = state.value.number.value,
            email = state.value.email.value,
            dateOfCreation = System.currentTimeMillis().toLong(),
            isActive = true,
            image = state.value.image.value
        )
        viewModelScope.launch {
            database.dao.upsertContact(contact)
            state.value.id.value = 0
            state.value.name.value = ""
            state.value.number.value = ""
            state.value.email.value = ""
            state.value.dateOfCreation.value = 0
            state.value.image.value = ByteArray(0)
        }
    }

    fun deleteContact(){
        val contact = Contact(
            id = state.value.id.value,
            name = state.value.name.value,
            number = state.value.number.value,
            email = state.value.email.value,
            dateOfCreation = state.value.dateOfCreation.value,
            isActive = true,
            image = state.value.image.value
        )
        viewModelScope.launch {
            database.dao.deleteContact(contact)
            state.value.id.value = 0
            state.value.name.value = ""
            state.value.number.value = ""
            state.value.email.value = ""
            state.value.dateOfCreation.value = 0
            state.value.image.value = ByteArray(0)


        }

    }
}