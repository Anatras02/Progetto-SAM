package it.unipi.sam.abalderi.components.equipments_list_activity

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import it.unipi.sam.abalderi.R
import it.unipi.sam.abalderi.view_models.EquipmentsListViewModel

@Composable
fun FilterLayout(
    modifier: Modifier = Modifier,
    verticalArrangement: Arrangement.Vertical,
    viewModel: EquipmentsListViewModel,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.card_space_between)),
        modifier = Modifier
            .padding(dimensionResource(R.dimen.fab_margin))
    ) {
        Filter(stringResource(R.string.filter_equipments), viewModel)
        Column(modifier = modifier, verticalArrangement = verticalArrangement, content = content)
    }
}