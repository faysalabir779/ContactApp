package com.example.contactapp.presentation.screen

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Call
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import com.example.contactapp.R
import com.example.contactapp.data.database.Contact
import com.example.contactapp.data.sharedPreference.Permission
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
    var contactToDelete by remember { mutableStateOf<Contact?>(null) }
    var isDelete by remember { mutableStateOf(false) }


    //for the permission request
    RequestCallPermission()


    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            LargeTopAppBar(
                scrollBehavior = scrollBehavior,
                title = {
                    Text(text = "All Contacts")
                },
                actions = {
                    IconButton(onClick = { viewModel.changeSorting() }) {
                        Icon(
                            painter = painterResource(id = R.drawable.descending),
                            modifier = Modifier.size(28.dp),
                            contentDescription = null
                        )
                    }
                }

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
                        ContactCard(
                            contact = contact,
                            state = state,
                            navController = navController,
                            onLongClick = {
                                contactToDelete = contact
                                isDelete = true
                            }
                        )
                    }
                }

            } else {
                NoContact()
            }
            if (isDelete && contactToDelete != null) {
                DeleteDialogue(
                    isOpen = isDelete,
                    label = "Are You Sure, you want to delete this contact?",
                    onDismissRequest = { isDelete = false },
                    onConfirm = {
                        contactToDelete?.let {
                            state.id.value = it.id
                            state.name.value = it.name
                            state.number.value = it.number
                            state.email.value = it.email
                            state.dateOfCreation.value = it.dateOfCreation
                            viewModel.deleteContact()
                        }
                        isDelete = false
                    },
                    onCancel = { isDelete = false }
                )
            }
        }
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ContactCard(
    contact: Contact,
    state: ContactState,
    navController: NavHostController,
    onLongClick: () -> Unit
) {
    val context = LocalContext.current



    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp)
            .clip(RoundedCornerShape(15.dp))
            .combinedClickable(
                onClick = {
                    state.id.value = contact.id
                    state.name.value = contact.name
                    state.number.value = contact.number
                    state.email.value = contact.email
                    state.dateOfCreation.value = contact.dateOfCreation
                    state.image.value = contact.image!!
                    navController.navigate(Routes.AddNewContact.route)
                },
                onLongClick = onLongClick
            )

    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp, top = 10.dp, bottom = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    var bitmap: Bitmap? = null
                    if (contact.image != null) {
                        bitmap =
                            BitmapFactory.decodeByteArray(contact.image, 0, contact.image!!.size)
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

                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(text = contact.name, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Spacer(modifier = Modifier.height(5.dp))
                        Text(text = contact.number)
                    }
                }
                IconButton(onClick = {
                    val intent = Intent(Intent.ACTION_CALL)
                    intent.data = android.net.Uri.parse("tel:${contact.number}")
                    context.startActivity(intent)
                }) {
                    Icon(Icons.Filled.Call, contentDescription = null)
                }
//            Column {
////                IconButton(onClick = {isDelete = true}) {
////                    Icon(imageVector = Icons.Filled.Delete, contentDescription = null)
////                }
//
//            }
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

@Composable
fun RequestCallPermission() {
    val context = LocalContext.current
    var hasCallPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CALL_PHONE
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasCallPermission = isGranted
    }

    LaunchedEffect(Unit) {
        if (!hasCallPermission) {
            permissionLauncher.launch(Manifest.permission.CALL_PHONE)
        }
    }
}