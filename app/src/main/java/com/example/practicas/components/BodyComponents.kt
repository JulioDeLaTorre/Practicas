package com.example.segundodia.components

import android.R
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun TextView(texto: String,tamaño: Int){
    Text(modifier = Modifier.padding(20.dp),
        text = texto,
        fontSize = tamaño.sp,
        fontWeight = FontWeight.Bold,
        color = Color.Black)
}

@Composable
fun TextView(texto: String,tamaño: Int, color: Color){
    Text(modifier = Modifier.padding(20.dp),
        text = texto,
        fontSize = tamaño.sp,
        fontWeight = FontWeight.Bold,
        color = color)
}
@Composable
fun Space(espcio:Int){
    Spacer(modifier = Modifier.height(espcio.dp))
}

@Composable
fun MainButton(name:String, backColor:Color,
               ColorC: Color,onClick:()->Unit,
               ){
    Button(modifier = Modifier.clip(RoundedCornerShape(16.dp))
                              .shadow(8.dp, RoundedCornerShape(16.dp)), //profe yo se que no vimos esto pero solo es una sombra
            onClick = onClick,
            colors = ButtonDefaults.buttonColors(
            contentColor = ColorC,
            containerColor = backColor
        )) {
        Text(name,
            style = MaterialTheme.typography.labelLarge)
    }
}