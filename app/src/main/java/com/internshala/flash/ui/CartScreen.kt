package com.internshala.flash.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.internshala.flash.R
import com.internshala.flash.data.InternetItem
import com.internshala.flash.data.InternetItemwithQn

@Composable
fun CartScreen(
    flashViewmodel: FlashViewmodel,
    onhomeButtonClicked: () -> Unit
) {

    val cartItems by flashViewmodel.cardItems.collectAsState()
    val cartItemsWithQuantity = cartItems.groupBy {it}
        .map {
            (item , cartItems) -> InternetItemwithQn(
            item, cartItems.size
            )
    }

    if (cartItems.isNotEmpty()) {
        LazyColumn (
            contentPadding = PaddingValues(
                horizontal = 10.dp,
            ),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ){
            item {
                Image(
                    painter = painterResource(R.drawable.scaletoppage),
                    contentDescription = "Sale Img",
                    modifier = Modifier.fillMaxWidth().size(250.dp)
                )
            }
            item{
                Text(
                    text = "Review Items",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            items(cartItemsWithQuantity) {
                Cartcard(it.item, flashViewmodel,
                    it.quantity)
            }

            item{
                Text(
                    text = "Bill Details",
                    fontSize = 21.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            val totalPrice = cartItems.sumOf {
                it.itemPrice * 75/100
            }
            val handlingCharge = totalPrice*1/100
            val deliveryCharge = 30
            val grandTotal = totalPrice + handlingCharge + deliveryCharge

            item{
                Card (
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(236,236,236,255)
                    ),
                ){
                    Column (
                        modifier = Modifier.padding(10.dp)
                    ){
                        BillRow("Item Total",totalPrice, fontWeight = FontWeight.Normal)
                        BillRow("handling Charge",handlingCharge, fontWeight = FontWeight.Normal)
                        BillRow("Delivery Charge",deliveryCharge, fontWeight = FontWeight.Normal)
                        HorizontalDivider(
                            modifier = Modifier.padding(vertical = 5.dp),
                            thickness = 1.dp,
                            color = Color.DarkGray
                        )
                        BillRow("Grand Total",grandTotal, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    } else {
        Column (
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ){
            Image(
                painter = painterResource(R.drawable.emptycart),
                contentDescription = "Empty Cart",
                modifier = Modifier.fillMaxWidth().size(210.dp)
            )
            Text(
                text = "Your Cart is Empty",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            FilledTonalButton(
                onClick = {
                    onhomeButtonClicked()
                }
            ) {
                Text(text = "Browse Products")
            }

        }
    }
}


@Composable
fun Cartcard(
    cartItems: InternetItem,
    flashViewmodel: FlashViewmodel,
    cartItemwithQn: Int
) {
    Row (
        modifier = Modifier.fillMaxWidth().height(80.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = cartItems.imageUrl,
            contentDescription = "item img",
            modifier = Modifier.fillMaxWidth().padding(start = 5.dp).weight(4f)
        )

        Column (
            modifier = Modifier.fillMaxHeight().padding(5.dp).weight(4f),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(
                text = cartItems.itemName,
                fontSize = 15.sp,
                maxLines = 1,
            )
            Text(
                text = "Qantity: $cartItemwithQn",
                fontSize = 14.sp,
                maxLines = 1,
            )
        }

        Column (
            modifier = Modifier.fillMaxHeight().padding(5.dp).weight(3f),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(
                text = "Rs. ${cartItems.itemPrice}",
                fontSize = 19.sp,
                maxLines = 1,
                color = Color.Gray,
                textDecoration = TextDecoration.LineThrough
            )
            Text(
                text = "Rs. ${cartItems.itemPrice * 75/100}",
                fontSize = 14.sp,
                maxLines = 1,
                color = Color(224,116,105,255)
            )
        }

        Column (
            modifier = Modifier.fillMaxHeight().weight(3f),
            verticalArrangement = Arrangement.SpaceEvenly
        ){
            Text(
                text = "Qantity: $cartItemwithQn",
                fontSize = 14.sp,
               textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Card (
                modifier = Modifier.clickable {
                    flashViewmodel.removeFromCart(olditem = cartItems)
                }.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = Color(254,116,105,255)
                )
            ) {
                Text(
                    text = "Remove",
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                    color = Color.White,
                    modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp)
                )
            }
        }
    }
}

@Composable
fun BillRow(
    itemName: String,
    itemPrice: Int,
    fontWeight: FontWeight
) {
    Row (
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = itemName,
            fontWeight = fontWeight
        )

        Text(
            text = "$itemPrice",
            fontWeight = fontWeight
        )
    }
}