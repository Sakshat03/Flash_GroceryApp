package com.internshala.flash.ui

import android.app.Activity
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

@Composable
fun NumberScreen(
    flashViewmodel: FlashViewmodel,
    callbacks: PhoneAuthProvider. OnVerificationStateChangedCallbacks
) {

    val phoneNumber by flashViewmodel.phoneNumber.collectAsState()
    val context = LocalContext.current

    Text(
        text = "Login",
        fontSize = 22.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(vertical = 5.dp),
    )

    Text(
        text = "Enter Your Phone Number To Login",
        fontSize = 19.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(vertical = 5.dp),
    )

    Text(
        text = "This phone number is used tp communicate with you and also help to save your activities",
        fontSize = 15.sp,
        fontWeight = FontWeight.Normal,
        color = Color.Gray,
        modifier = Modifier.padding(vertical = 5.dp),
    )

    OutlinedTextField(
        value = phoneNumber,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number
        ),
        onValueChange = {
            flashViewmodel.setPhoneNumber(it)
        },
        label = {
            Text(text = "Phone Number") },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true
    )

    Button(
        onClick = {
            val options = PhoneAuthOptions.newBuilder(auth)
                .setPhoneNumber("+91${phoneNumber}") // Phone number to verify
                .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                .setActivity(context as Activity) // Activity (for callback binding)
                .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
                .build()
            PhoneAuthProvider.verifyPhoneNumber(options)
            flashViewmodel.setloading(true)
        },
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Send Otp",
            fontSize = 20.sp,
            textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth()
        )
    }
}