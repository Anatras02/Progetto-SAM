package it.unipi.sam.abalderi.components.equipments_list_activity

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.SocialDistance
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import it.unipi.sam.abalderi.R

@Composable
fun Filter(label: String) {
    var text by remember { mutableStateOf("") }

    OutlinedTextField(
        value = text,
        onValueChange = {
            viewModelScope.launch {
                for (i in 1..1000000) Log.v("log", i.toString())
            }


            text = it
        },
        label = { Text(label) },
    )
}