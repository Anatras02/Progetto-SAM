package it.unipi.sam.abalderi.components.home_activity

import android.content.Intent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun OptionCard(title: String, description: String, intent: Intent) {
    val context = LocalContext.current;

    Card(
        elevation = 5.dp,
        modifier = Modifier
            .fillMaxWidth(),
        onClick = {
            context.startActivity(intent)
        }
    ) {
        Column(
            modifier = Modifier
                .padding(10.dp)
        ) {
            Text(title, style= MaterialTheme.typography.h5)
            Text(description)
        }
    }
}