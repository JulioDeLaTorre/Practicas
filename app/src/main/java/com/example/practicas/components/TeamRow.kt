package com.example.practicas.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
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

@Composable
fun TeamRow(team: Team, navController: NavController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Image(
            painter = painterResource(id = team.logoResId),
            contentDescription = "Casco ${team.name}",
            modifier = Modifier
                .size(150.dp)
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
                fontSize = 24.sp,
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

data class Team(
    val name: String,
    val Fullname: String,
    val logoResId: Int,
    val buttonColor: Color,
    val buttonTextColor: Color,
    val route: String
)
