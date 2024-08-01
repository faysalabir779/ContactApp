package com.example.contactapp.presentation.screen

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.contactapp.R
import com.example.contactapp.data.database.Contact
import com.example.contactapp.navigation.Routes
import com.example.contactapp.presentation.ContactState
import com.example.contactapp.presentation.ContactViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AllContactScreen(
    viewModel: ContactViewModel,
    state: ContactState,
    navController: NavHostController
) {

    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val lazyListState = rememberLazyListState()
    val isFABExpanded by remember {
        derivedStateOf { lazyListState.firstVisibleItemIndex == 0 }
    }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            LargeTopAppBar(
                scrollBehavior = scrollBehavior,
                title = {
                    Text(text = "All Contacts")
                },
                navigationIcon = {
                    IconButton(onClick = { viewModel.changeSorting() }) {
                        Icons.Filled.ArrowBack
                    }
                },

                )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { navController.navigate(Routes.AddNewContact.route) },
                text = {
                    Text(text = "Add Contact")
                },
                icon = {
                    Icon(imageVector = Icons.Filled.Add, contentDescription = null)
                },
                expanded = isFABExpanded
            )
        }
    ) {
        Column(modifier = Modifier.padding(it)) {
            if (state.contact.isNotEmpty()) {
                LazyColumn(modifier = Modifier.fillMaxSize(), state = lazyListState) {
                    items(state.contact) { contact ->
                        ContactCard(contact, {
                            state.id.value = contact.id
                            state.name.value = contact.name
                            state.number.value = contact.number
                            state.email.value = contact.email
                            state.dateOfCreation.value = contact.dateOfCreation
                            viewModel.deleteContact()
                        }, state, navController)
                    }
                }

            } else {
                NoContact()
            }
        }
    }
}


@Composable
fun ContactCard(
    contact: Contact,
    onDeleteButton: () -> Unit,
    state: ContactState,
    navController: NavHostController,
) {
    val context = LocalContext.current
    ElevatedCard(
        onClick = {
            state.id.value = contact.id
            state.name.value = contact.name
            state.number.value = contact.number
            state.email.value = contact.email
            state.dateOfCreation.value = contact.dateOfCreation
            navController.navigate(Routes.AddNewContact.route)
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 10.dp, top = 15.dp, bottom = 15.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            var bitmap: Bitmap? = null
            if (contact.image != null) {
                bitmap = BitmapFactory.decodeByteArray(contact.image, 0, contact.image!!.size)
            }


            if (bitmap != null) {
                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier
                        .size(53.dp)
                        .clip(
                            CircleShape
                        )
                        .scale(1.3f)
                )
            } else {
                Image(
                    painter = painterResource(id = R.drawable.user),
                    contentDescription = null,
                    modifier = Modifier
                        .size(53.dp)
                        .clip(
                            CircleShape
                        )
                        .scale(1.3f)
                )
            }

            Column {
                Text(text = contact.name)
                Text(text = contact.number)
            }
            Column {
                IconButton(onClick = onDeleteButton) {
                    Icon(imageVector = Icons.Filled.Delete, contentDescription = null)
                }
                IconButton(onClick = {
                    val intent = Intent(Intent.ACTION_CALL)
                    intent.data = android.net.Uri.parse("tel:${contact.number}")
                    context.startActivity(intent)
                }) {
                    Icon(Icons.Filled.Call, contentDescription = null)
                }
            }
        }

    }
}

@Composable
fun NoContact() {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.fillMaxSize(0.4f))
        Text(text = "No Contact")
    }
}