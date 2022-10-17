package it.unipi.sam.abalderi.components.equipments_list_activity

import android.content.Intent
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import it.unipi.sam.abalderi.R
import it.unipi.sam.abalderi.SingleEquipmentActivity


@Composable
fun EquipmentCard(id: Int, name: String, distance: Float?) {
    val context = LocalContext.current;

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
            if (distance != null) {
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
                TextButton(onClick = {
                    val i = Intent(context, SingleEquipmentActivity::class.java)
                    i.putExtra("id", id)
                    context.startActivity(i)
                }) {
                    Text("Scopri di più")
                }
            }
        }
    }
}