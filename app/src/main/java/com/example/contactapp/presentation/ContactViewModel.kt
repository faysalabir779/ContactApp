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
    private val isSortedByName = MutableStateFlow(true)
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

    fun saveContact() {
        val contact = Contact(
            name = state.value.name.value,
            number = state.value.number.value,
            email = state.value.email.value,
            dateOfCreation = System.currentTimeMillis().toLong(),
            isActive = true
        )
        viewModelScope.launch {
            database.dao.upsertContact(contact)
        }
    }
}