package com.internshala.flash.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.internshala.flash.R
import com.internshala.flash.data.InternetItem
import com.google.firebase.auth.FirebaseAuth

enum class FlashAppScreen(val title: String) {
    Start("Flash Cart"),
    Items("Choose The Items"),
    Cart("Your Cart");

}

var canNavigateBack = false
val auth = FirebaseAuth.getInstance()

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlashApp(
flashViewmodel: FlashViewmodel = viewModel(),
navController: NavHostController = rememberNavController()
) {
    val user by flashViewmodel.user.collectAsState()
    val logoutChecked by flashViewmodel.logoutChecked.collectAsState()

    auth.currentUser?.let { flashViewmodel.setUser(it) }

    val isVisible by flashViewmodel.isVisible.collectAsState()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = FlashAppScreen.valueOf(
        backStackEntry?.destination?.route ?: FlashAppScreen.Start.name
    )
    canNavigateBack = navController.previousBackStackEntry != null
    val cartItems by flashViewmodel.cardItems.collectAsState()

    if (isVisible) {
        OfferScreen()
    } else if (user == null) {
        Login(flashViewmodel = flashViewmodel)
    }
    else {
        Scaffold (
            topBar = {
                TopAppBar(
                    title = {
                        Row (
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                            ) {

                            Text(
                                text = currentScreen.title,
                                fontSize = 20.sp,
                                fontStyle = FontStyle.Italic,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                            if (currentScreen == FlashAppScreen.Cart) {
                                Text(
                                    text = "${cartItems.size}",
                                    fontSize = 20.sp,
                                    fontStyle = FontStyle.Italic,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black
                                )
                            }
                        }
                            Row (
                                modifier = Modifier.clickable {
                                    flashViewmodel.setLogOutStatus(true)
                                }
                            ){
                                Image(
                                    painter = painterResource(R.drawable.logout),
                                    contentDescription = "Logout",
                                    modifier = Modifier.size(25.dp)
                                )
                                Text(
                                    text = "Log Out",
                                    fontSize = 18.sp,
                                    modifier = Modifier.padding(
                                        end = 16.dp,
                                        start = 2.dp
                                    )
                                )
                            }
                        }
                    },
                    navigationIcon = {
                        if (canNavigateBack) {
                            IconButton(
                                onClick = {
                                    navController.navigateUp()
                                }
                            )
                            {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Arrow Back",

                                    )
                            }
                        }
                    }
                )
            },
            bottomBar = {
                FlashAppBar(navController = navController,
                    currentScreen = currentScreen,
                    cartItems = cartItems
                )
            }
        ){ it ->
            NavHost(navController = navController,
                startDestination = FlashAppScreen.Start.name,
                Modifier.padding(it)) {
                composable(route = FlashAppScreen.Start.name) {
                    StartScreen(
                        flashViewmodel = flashViewmodel,
                        onCategoryClicked = {
                            flashViewmodel.updateselectedCategory(it)
                            navController.navigate(FlashAppScreen.Items.name)
                        }
                    )
                }
                composable(route = FlashAppScreen.Items.name) {
                    InternetItemScreen(
                        flashViewmodel = flashViewmodel,
                        itemUiState = flashViewmodel.itemUiState
                    )
                }

                composable(route= FlashAppScreen.Cart.name) {
                    CartScreen(
                        flashViewmodel = flashViewmodel,
                        onhomeButtonClicked = {
                            navController.navigate(FlashAppScreen.Start.name){
                                popUpTo(0)
                            }
                        }
                    )
                }
            }
        }
        if (logoutChecked) {
            AlertCheck(onYesButtonPressed = {
                    flashViewmodel.setLogOutStatus(false)
                    auth.signOut()
                    flashViewmodel.cleardata()
                },
                onNoButtonPressed = {
                    flashViewmodel.setLogOutStatus(false)
                },
            )
          }
        }
    }

@Composable
fun FlashAppBar(navController: NavHostController,
                currentScreen: FlashAppScreen,
                cartItems: List<InternetItem>
                ) {
    Row (
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 70.dp,
                vertical = 10.dp
            )
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.clickable {
                navController.navigate(FlashAppScreen.Start.name) {
                    popUpTo(0)
                }
            }
        ) {
            Icon(
                imageVector = Icons.Outlined.Home,
                contentDescription = "Home"
            )
            Text(
                text = "Home",
                fontSize = 10.sp,
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.clickable {
                if (currentScreen != FlashAppScreen.Cart) {
                    navController.navigate(FlashAppScreen.Cart.name) {
                    }
                }
            }
        ) {
            Box {
                Icon(
                    imageVector = Icons.Outlined.ShoppingCart,
                    contentDescription = "Shopping Cart"
                )
                if (cartItems.isNotEmpty()
                    )
                Card (
                    modifier = Modifier.align(
                        alignment = Alignment.TopEnd
                    ),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.Red
                    )
                ){
                    Text(
                        text = cartItems.size.toString(),
                        fontSize = 10.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(
                            horizontal = 1.dp
                        )
                    )
                }
            }
        Text(
            text = "Home",
            fontSize = 10.sp,
        )
      }
    }
}

@Composable
fun AlertCheck(
    onYesButtonPressed: () -> Unit,
    onNoButtonPressed: () -> Unit
) {
    AlertDialog(
        title = {
            Text(text = "LogOut?", fontWeight = FontWeight.Bold
            )
        },
        containerColor = Color.White,
        text = {
            Text(text = "Are you sure you want to log out?")
        },
        confirmButton = {
            TextButton(onClick = {
                onYesButtonPressed()
            }) {
                Text(text = "Yes")
            }
        },
        dismissButton = {
            TextButton(onClick = {
                onNoButtonPressed()
            }) {
                Text(text = "No")
            }
        },
        onDismissRequest = {
            onNoButtonPressed()
        }
    )
}