package it.unipi.sam.abalderi.components.equipments_list_activity

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.SocialDistance
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import it.unipi.sam.abalderi.R

@Composable
fun EquipmentCard(id: Int, name: String, distance: Float?) {
    Card(
        elevation = 5.dp,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .padding(10.dp)
        ) {
            Text(name, style = MaterialTheme.typography.h5)
            if(distance != null) {
                Row {
                    Text("${stringResource(R.string.distance)}: ${distance.toInt()} m")
                }
            }
            Row(
                modifier = Modifier.padding(top = 20.dp),
            ) {
                Button(onClick = {}) {
                    Text("Segnala")
                }
                TextButton(onClick = {}) {
                    Text("Scopri di più")
                }
            }
        }
    }
}