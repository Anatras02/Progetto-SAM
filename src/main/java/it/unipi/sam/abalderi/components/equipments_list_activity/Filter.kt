package it.unipi.sam.abalderi.components.equipments_list_activity

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.SocialDistance
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import it.unipi.sam.abalderi.R
import it.unipi.sam.abalderi.view_models.EquipmentsListViewModel

@Composable
fun Filter(label: String, viewModel: EquipmentsListViewModel) {
    val text by viewModel.filterText.observeAsState()

    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
        ,
        value = text ?: "",
        onValueChange = { viewModel.onFilterTextChange(it) },
        label = { Text(label) },
    )
}