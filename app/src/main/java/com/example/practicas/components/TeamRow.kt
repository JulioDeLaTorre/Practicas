package com.example.practicas.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.segundodia.components.MainButton
import com.example.segundodia.components.Space
import com.example.segundodia.components.TextView
@Composable
fun TeamRow2(team: Team2, navController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = team.cardBackgroundColor
        ),
        onClick = { navController.navigate(team.route) }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Image(
                painter = painterResource(id = team.logoResId),
                contentDescription = "Casco ${team.name}",
                modifier = Modifier
                    .size(width = 120.dp, height = 100.dp)
                    .weight(1f)
            )

            Spacer(modifier = Modifier.size(16.dp))

            Column(
                modifier = Modifier.weight(1.5f),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = team.name,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = team.buttonColor
                )
                Spacer(modifier = Modifier.height(8.dp))
                MainButton(
                    name = team.Fullname,
                    team.buttonColor,
                    team.buttonTextColor
                ) {
                    navController.navigate(team.route)
                }
            }
        }
    }
}
data class Team2(
    val name: String,
    val Fullname: String,
    val cardBackgroundColor: Color,
    val logoResId: Int,
    val buttonColor: Color,
    val buttonTextColor: Color,
    val route: String
)

@Composable
fun TeamInfo(
    team: Int,
    texto: String,
    cardBackgroundColor: Color,
    fontcolor : Color
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = cardBackgroundColor
        )) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = team),
                contentDescription = "Kansas estadio",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )
            Space(5)
            TextView(texto, 16,fontcolor)
        }
    }
}

@Composable
fun TeamInfo(
    team: Int,
    texto: String,
    cardBackgroundColor: Color
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = cardBackgroundColor
        )) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = team),
                contentDescription = "Kansas estadio",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )
            Space(5)
            TextView(texto, 16)
        }
    }
}