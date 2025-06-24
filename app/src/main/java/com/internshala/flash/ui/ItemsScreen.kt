package com.internshala.flash.ui

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.internshala.flash.R
import com.internshala.flash.data.InternetItem

@Composable
fun ItemsScreen(flashViewmodel: FlashViewmodel,
               items: List<InternetItem>
               ) {
    val flashUiState by flashViewmodel.uiState.collectAsState()
    val selectedcatergory = stringResource(id = flashUiState.selectedCategory)
    val database = items.filter {
        it.itemCategory.lowercase() == selectedcatergory.lowercase()
    }

    LazyVerticalGrid(
        columns = GridCells.Adaptive(128.dp),
        contentPadding = PaddingValues(10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item(
            span = { GridItemSpan(2) }
        ) {
            Column {
                Image(
                    painter = painterResource( R.drawable.scaletoppage),
                    contentDescription = "catergory banner",
                    modifier = Modifier.fillMaxWidth().size(210.dp)
                )

                Card (
                    colors = CardDefaults.cardColors(
                        containerColor = Color(108,194,111,255)
                    ),
                    modifier = Modifier.fillMaxWidth().padding(vertical = 5.dp)
                ){
                    Text(
                        text = "${stringResource(flashUiState.selectedCategory)} (${database.size})",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 10.dp)
                    )
                }
            }
        }

        items(database){
            ItemsCard(
                stringResourceId = it.itemName,
                imageResourceId = it.imageUrl,
                itemQuantity = it.itemQuantity,
                itemPrice = it.itemPrice,
                flashViewmodel = flashViewmodel
            )
        }
    }
}

@Composable
fun ItemsCard(
    stringResourceId: String,
    imageResourceId: String,
    itemQuantity: String,
    itemPrice: Int,
    flashViewmodel: FlashViewmodel
) {
    val context = LocalContext.current

    Column(
        modifier = Modifier.width(150.dp)){
        Card (
            colors = CardDefaults.cardColors(
                containerColor = Color(248,221,248,255)
            )
        ){
            Box {
                AsyncImage(
                    model = imageResourceId,
                    contentDescription = stringResourceId,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxWidth().height(110.dp)
                )
                Row (
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.End
                ) {
                    Card  (
                        colors = CardDefaults.cardColors(
                            containerColor = Color(244,67,54,255))
                    ){
                        Text(
                            text = "25% Off",
                            color = Color.White,
                            fontSize = 10.sp,
                            modifier = Modifier.padding(
                                vertical = 5.dp,
                                horizontal = 2.dp
                            )
                        )
                    }
                }
            }
        }
        Text(
            text = stringResourceId,
            fontSize = 15.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    vertical = 5.dp
                ),
            maxLines = 1,
            textAlign = TextAlign.Left
        )
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    vertical = 5.dp
                ),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "Rs. $itemPrice",
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                    color = Color.DarkGray,
                    textDecoration = TextDecoration.LineThrough,
                )
                Text(
                    text = "Rs. ${itemPrice*75/100}",
                    fontSize = 18.sp,
                    maxLines = 1,
                    textAlign = TextAlign.Center,
                    color = Color(225,116,105,255),

                )
            }
            Text(
                text = itemQuantity,
                fontSize = 20.sp,
                maxLines = 1,
                color = Color(114,114,114,255)
            )

        }
        Card (
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    flashViewmodel.addToDatabase(
                        InternetItem(
                            itemName = stringResourceId,
                            imageUrl = imageResourceId,
                            itemQuantity = itemQuantity,
                            itemPrice = itemPrice,
                            itemCategory = "",
                        )
                    )
                    Toast
                        .makeText(context, "Added To Cart", Toast.LENGTH_SHORT).show()
                },
            colors = CardDefaults.cardColors(
                containerColor = Color(100, 194, 111, 255)
            )
        ) {
            Row (
                modifier =  Modifier
                    .fillMaxWidth()
                    .height(25.dp)
                    .padding(
                        horizontal = 5.dp
                    ),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,

            ){
                Text(
                    text = "Add To Cart",
                    fontSize = 12.sp,
                    color = Color.White,

                )
            }
        }
    }
}

@Composable
fun InternetItemScreen(
    flashViewmodel: FlashViewmodel,
    itemUiState: FlashViewmodel.ItemUiState
    ) {
    when (itemUiState) {
        is FlashViewmodel.ItemUiState.Loading -> {
            LoadingScreen()
        }
        is FlashViewmodel.ItemUiState.Success -> {
           ItemsScreen(flashViewmodel = flashViewmodel, items = itemUiState.items)
        }
        else ->  {
            ErrorScreen(flashViewmodel = flashViewmodel)
        }
    }
}

@Composable
fun LoadingScreen() {
    Box (
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(R.drawable.loadingimg),
            contentDescription = "IAmge"
        )
    }
}

@Composable
fun ErrorScreen(flashViewmodel: FlashViewmodel) {
    Column  (
       horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ){
        Image(
            painter = painterResource(R.drawable.errorimg),
            contentDescription = "image",
            modifier = Modifier.fillMaxWidth().
            size(200.dp),
        )

        Text(
            text = "Oops! Internet Unavailable. Please turn on Internet and restart the app",
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(20.dp).
            fillMaxWidth()
        )

       Button(
           onClick = {
                flashViewmodel.getFlashItems()
           }
       ) {
           Text(
               text = "Retry",
               fontSize = 15.sp

           )
       }
    }
}