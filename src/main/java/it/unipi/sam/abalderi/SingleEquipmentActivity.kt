package it.unipi.sam.abalderi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.rememberCameraPositionState
import it.unipi.sam.abalderi.components.general.CenteredCircularProgressIndicator
import it.unipi.sam.abalderi.components.general.MainStructure
import it.unipi.sam.abalderi.view_models.SingleEquipmentViewModel

class SingleEquipmentActivity : ComponentActivity() {
    private val singleEquipmentViewModel: SingleEquipmentViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        setContent {
            val equipment = singleEquipmentViewModel.equipment.observeAsState(null).value
            val singapore = LatLng(1.35, 103.87)
            val cameraPositionState = rememberCameraPositionState {
                position = CameraPosition.fromLatLngZoom(singapore, 10f)
            }

            MainStructure(
                topBarText = equipment?.name
                    ?: stringResource(R.string.title_activity_single_equipment),
            ) {
                if (equipment !== null) {
                    Column(
                        modifier = Modifier
                            .padding(dimensionResource(R.dimen.fab_margin))
                            .verticalScroll(rememberScrollState())
                    ) {
                        Text(equipment.name, style=MaterialTheme.typography.h4)
                        Text(equipment.description)
                        GoogleMap(
                            modifier = Modifier.fillMaxSize(),
                            cameraPositionState = cameraPositionState
                        )
                    }
                } else {
                    CenteredCircularProgressIndicator()
                }
            }
        }
    }

    override fun onStart() {
        val extras = intent.extras
        val id: Int = extras?.getInt("id") ?: 0

        singleEquipmentViewModel.getEquipment(id)

        super.onStart()
    }
}