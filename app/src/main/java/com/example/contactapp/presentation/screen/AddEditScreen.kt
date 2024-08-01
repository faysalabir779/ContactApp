package com.example.contactapp.presentation.screen

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.contactapp.navigation.Routes
import com.example.contactapp.presentation.ContactState
import java.io.InputStream

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditScreen(
    state: ContactState,
    navController: NavController,
    onEvent: () -> Unit
) {
    val context = LocalContext.current


    val pickMedia =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
            if (uri != null) {
                val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
                val byte = inputStream?.readBytes()
                if (byte != null) {
                    state.image.value = byte
                }
            }

            // "Pick Media"
            //prothome remember diye jeta call korsi oita diye image er uri pacchi
            //tarpor condition check kortesi, jodi uri null na hoye tahole input stream variable e
            //directly image access korte parteci evabe. the oi image take byte convert kortesi byte variable e
            //tarpor byte ta null na hoile oitake state e boshay dicchi karon state e image er type ByteArray


        }

    Scaffold(topBar = {
        TopAppBar(navigationIcon = {
            IconButton(onClick = { navController.navigate(Routes.AllContacts.route) }) {
                Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = null)
            }
        }, title = {
            Text(text = "Add Contact")
        })
    }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .padding(horizontal = 15.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(onClick = { pickMedia.launch("image/*") }) {
                Text(text = "Pick Image")
            }
            OutlinedTextField(
                value = state.name.value, onValueChange = { state.name.value = it },
                label = {
                    Text(text = "Name")
                },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = state.number.value, onValueChange = { state.number.value = it },
                label = {
                    Text(text = "Number")
                },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = state.email.value, onValueChange = { state.email.value = it },
                label = {
                    Text(text = "Email")
                },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            Spacer(modifier = Modifier.height(8.dp))

            Button(onClick = {
                if (state.name.value.isNotEmpty() &&
                    state.number.value.isNotEmpty() &&
                    state.email.value.isNotEmpty()
                ) {
                    onEvent()
                    onEvent
                    navController.popBackStack()
                } else {
                    Toast.makeText(
                        context,
                        "Empty Fields",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }) {
                Text(text = "Save Contact")
            }
        }
    }

}