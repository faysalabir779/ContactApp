package com.example.contactapp.presentation.screen

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.contactapp.R
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

    var bitmap: Bitmap? = null
    if (state.image.value != null) {
        bitmap = BitmapFactory.decodeByteArray(state.image.value, 0, state.image!!.value.size)
    }


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
            IconButton(onClick = {
                state.id.value = 0
                state.name.value = ""
                state.number.value = ""
                state.email.value = ""
                state.dateOfCreation.value = 0
                state.image.value = ByteArray(0)
                navController.navigate(Routes.AllContacts.route)
            }) {
                Icon(
                    painter = painterResource(id = R.drawable.back),
                    contentDescription = null,
                    modifier = Modifier.size(27.dp)
                )
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
            Spacer(modifier = Modifier.height(10.dp))
            Box(
                modifier = Modifier
                    .size(150.dp)
                    .clip(CircleShape)
                    .clickable { pickMedia.launch("image/*") }
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                Color(0xFF17375A),
                                Color(0xFF10203D),
                                Color(0xFF0C121F),
                            )

                        ),
                        shape = RoundedCornerShape(15.dp)
                    )
                    .border(
                        width = 0.5.dp,
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                Color(0xFFFFFFFFF),
                                Color(0xFFFFFFFFF).copy(alpha = 0.2f)
                            )
                        ),
                        shape = CircleShape
                    ), contentAlignment = Alignment.Center
            ) {
                if (bitmap != null) {
                    Image(
                        bitmap = bitmap.asImageBitmap(),
                        contentDescription = null,
                        modifier = Modifier.scale(1.29f)

                    )
                } else {
                    Icon(
                        painter = painterResource(id = R.drawable.image),
                        contentDescription = null,
                        modifier = Modifier
                            .size(70.dp), tint = Color.White
                    )
                }
            }

            TextButton(onClick = { pickMedia.launch("image/*") }) {
                Text(text = "Pick Image", color = Color.White, fontSize = 18.sp)
            }
            OutlinedTextField(
                value = state.name.value, onValueChange = { state.name.value = it },
                label = {
                    Text(text = "Name")
                },
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.id),
                        contentDescription = null,
                        modifier = Modifier.padding(start = 20.dp, end = 15.dp).size(23.dp)
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(15.dp),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = state.number.value, onValueChange = { state.number.value = it },
                label = {
                    Text(text = "Number")
                },
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.telephone),
                        contentDescription = null,
                        modifier = Modifier.padding(start = 20.dp, end = 15.dp).size(23.dp)
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                shape = RoundedCornerShape(15.dp),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = state.email.value, onValueChange = { state.email.value = it },
                label = {
                    Text(text = "Email")
                },
                leadingIcon = {
                    Icon(
                        Icons.Filled.Email,
                        contentDescription = null,
                        modifier = Modifier.padding(start = 20.dp, end = 15.dp)
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(15.dp),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(15.dp))

            Button(
                onClick = {
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
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                Color(0xFF17375A),
                                Color(0xFF0C121F)
                            )

                        ),
                        shape = RoundedCornerShape(15.dp)
                    )
                    .border(
                        width = 0.5.dp,
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                Color(0xFFFFFFFFF),
                                Color(0xFFFFFFFFF).copy(alpha = 0.2f)
                            )
                        ),
                        shape = RoundedCornerShape(15.dp)
                    ),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent
                )
            ) {
                Text(text = "Save Contact", color = Color.White)
            }
        }
    }

}