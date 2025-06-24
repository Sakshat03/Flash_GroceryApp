package com.internshala.flash.ui

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.internshala.flash.R
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider

@Composable
fun Login(
    flashViewmodel: FlashViewmodel
) {

    val context = LocalContext.current
    val otp by flashViewmodel.otp.collectAsState()
    val verificationId by flashViewmodel.verificationId.collectAsState()
    val loading by flashViewmodel.loading.collectAsState()

    val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
        }

        override fun onVerificationFailed(e: FirebaseException) {
        }

        override fun onCodeSent(
            verificationId: String,
            token: PhoneAuthProvider.ForceResendingToken,
        ) {
            flashViewmodel.setVerificationId(verificationId)
           Toast.makeText(context,"Success",Toast.LENGTH_SHORT).show()
            flashViewmodel.resettimer()
            flashViewmodel.runTimer()
            flashViewmodel.setloading(false)
        }
    }


    Box {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(R.drawable.login),
                    contentDescription = "login",
                    modifier = Modifier
                        .padding(top = 50.dp, bottom = 10.dp)
                        .size(120.dp),
                )
                if (verificationId.isEmpty()) {
                    NumberScreen(
                        flashViewmodel = flashViewmodel,
                        callbacks = callbacks
                    )
                } else {
                    OtpScreen(
                        otp = otp,
                        flashViewmodel = flashViewmodel,
                        callbacks = callbacks
                    )
                }

            }

        if (verificationId.isNotEmpty()) {
            IconButton (onClick = {
                flashViewmodel.setVerificationId("")
                flashViewmodel.setOtp("")
            }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                )
            }
        }

        if (loading) {
            Column (
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize().background(color = Color(255,255,255,190))
            ) {
                CircularProgressIndicator()
                Text(text = "Loading...")
            }
        }
        }
    }