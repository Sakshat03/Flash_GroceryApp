package com.internshala.flash.ui

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.internshala.flash.R
import com.internshala.flash.data.DataSource

@Composable
fun StartScreen (
    flashViewmodel: FlashViewmodel,
    onCategoryClicked: (Int) -> Unit) {
    val context = LocalContext.current

    LazyVerticalGrid(
        columns = GridCells.Adaptive(128.dp),
        verticalArrangement = Arrangement.spacedBy(15.dp),
        horizontalArrangement = Arrangement.spacedBy(15.dp),
        contentPadding = PaddingValues(10.dp)
    ) {

        item(
            span = { GridItemSpan(2) }
        ) {
            Column {
                Image(
                    painter = painterResource( R.drawable.delivery),
                    contentDescription = "category banner",
                    modifier = Modifier.fillMaxWidth().size(210.dp)
                )

                Card (
                    colors = CardDefaults.cardColors(
                        containerColor = Color(108,194,111,255)
                    ),
                    modifier = Modifier.fillMaxWidth().padding(vertical = 5.dp)
                ){
                    Text(
                        text = "Shop By Category",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 10.dp)
                        )
                }
            }
        }

    items (DataSource.loadCategories()) {
        CategoryCard(
            context = context,
            stringResourceId = it.stringResourceId,
            imageResourceId = it.imageResourceId,
            flashViewmodel = flashViewmodel,
            onCategoryClicked = onCategoryClicked
        )
         }
    }
}

@Composable
fun CategoryCard(
    context: Context,
   stringResourceId : Int,
    imageResourceId: Int,
    flashViewmodel: FlashViewmodel,
    onCategoryClicked: (Int) -> Unit
    ) {
    val categoryName = stringResource(stringResourceId)
    Card (modifier = Modifier.clickable {

        flashViewmodel.updateClickText("This is Clicked ")
        Toast.makeText(context , categoryName, Toast.LENGTH_SHORT).show()
        onCategoryClicked(stringResourceId)
    },
        colors = CardDefaults.cardColors(
            containerColor = Color(248,221,248,255)
        )
        ){
        Column (modifier = Modifier.padding(5.dp)) {
            Text(
               text = stringResource(stringResourceId),
                textAlign = TextAlign.Center,
                fontSize = 15.sp,
                modifier = Modifier.width(150.dp)
                )
        }
        Image(painter = painterResource(
           imageResourceId),
            contentDescription = "Sweets",
            modifier = Modifier.fillMaxWidth().height(110.dp),

            )
    }
}