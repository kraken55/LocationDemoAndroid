package com.example.locationdemo

import android.os.Bundle
import android.Manifest
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.locationdemo.ui.theme.LocationDemoTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.location.LocationServices
import com.example.locationdemo.ui.theme.MaterialGreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LocationDemoTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialGreen
                ) {
                    LocationDisplayScreen()
                }
            }
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun RequestLocationPermission(onPermissionGranted: () -> Unit) {
    val permissionState = rememberPermissionState(permission = Manifest.permission.ACCESS_FINE_LOCATION)
    if (permissionState.status.isGranted) {
        onPermissionGranted()
    }
    else {
        LaunchedEffect(Unit) {
            permissionState.launchPermissionRequest()
        }
    }
}

@Composable
fun LocationDisplayScreen() {
    val context = LocalContext.current
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    var carDangerImage = painterResource(id = R.drawable.car_danger)
    var location by remember { mutableStateOf<Pair<Double, Double>?>(null) }

    RequestLocationPermission {
        fusedLocationClient.lastLocation.addOnSuccessListener { loc ->
            if (loc != null) {
                location = Pair(loc.latitude, loc.longitude)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        /*
        Text(
            modifier = Modifier.padding(bottom = 20.dp),
            text = "Device Info",
            fontSize = 46.sp,
            fontWeight = FontWeight.Bold
        )
        if (location != null) {
            Text("Latitude: ${location?.first}", fontSize = 28.sp)
            Spacer(modifier = Modifier.padding(8.dp))
            Text("Longitude: ${location?.second}", fontSize = 28.sp)
        } else {
            Text("Cannot fetch location!", fontSize = 24.sp)
        }
        */
        DeviceInfobox(deviceName = "Your device", location = location)

        Image(
            modifier = Modifier.padding(top = 32.dp, bottom = 32.dp),
            painter = carDangerImage,
            contentDescription = null
        )

        DeviceInfobox(deviceName = "Partner device", location = Pair(0.0, 0.0))
        Text(
            modifier = Modifier.padding(bottom = 20.dp, top = 20.dp),
            text = "Distance: TODO",
            fontSize = 28.sp
        )
    }
}

@Composable
fun DeviceInfobox(deviceName: String, location: Pair<Double, Double>?) {
    Text(
        modifier = Modifier.padding(bottom = 20.dp),
        text = deviceName,
        fontSize = 46.sp,
        fontWeight = FontWeight.Bold
    )
    if (location != null) {
        Text("Latitude: ${location.first}", fontSize = 28.sp)
        Spacer(modifier = Modifier.padding(8.dp))
        Text("Longitude: ${location.second}", fontSize = 28.sp)
    } else {
        Text("Cannot fetch location", fontSize = 24.sp)
    }
}