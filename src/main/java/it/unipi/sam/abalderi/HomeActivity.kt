package it.unipi.sam.abalderi

import android.content.Intent
import android.content.pm.PackageManager
import androidx.compose.foundation.Image
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource

import it.unipi.sam.abalderi.components.general.MainStructure
import it.unipi.sam.abalderi.components.home_activity.OptionCard

class HomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MainStructure(
                topBarText = stringResource(R.string.title_activity_report)
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.card_space_between)),
                    modifier = Modifier
                        .padding(dimensionResource(R.dimen.fab_margin))
                ) {
                    Image(
                        painterResource(R.drawable.playground),
                        "Immagine Parco Giochi",
                        modifier = Modifier
                            .align(alignment = Alignment.CenterHorizontally)
                            .fillMaxWidth()
                    )
                    OptionCard(
                        stringResource(R.string.equipments_list),
                        stringResource(id = R.string.equipments_list_description),
                        Intent(this@HomeActivity, EquipmentsListActivity::class.java)
                    );
                    OptionCard(
                        stringResource(R.string.qr_code),
                        stringResource(R.string.qr_code_description),
                        Intent(this@HomeActivity, EquipmentsListActivity::class.java)
                    );
                    OptionCard(
                        stringResource(R.string.nfc_tag),
                        stringResource(R.string.nfc_tag_description),
                        Intent(this@HomeActivity, EquipmentsListActivity::class.java)
                    );
                }
            }
        }
    }
}



