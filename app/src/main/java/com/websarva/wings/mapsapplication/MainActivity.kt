package com.websarva.wings.mapsapplication

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import coil.compose.AsyncImage
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.websarva.wings.mapsapplication.ui.theme.MapsApplicationTheme
import java.io.InputStream


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MapsInitializer.initialize(this)

//        val bitmappedPicture = BitmapDescriptorFactory.fromResource(R.drawable.singapore)
//        val originalBitmap = BitmapFactory.decodeResource(resources, R.drawable.singapore)
        val context: Context = applicationContext

        val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if(uri != null) {
                val bitmap = getBitmapFromUri(uri, context)
                setContent {
                    MapsApplicationTheme {
                        // A surface container using the 'background' color from the theme
                        Surface(
                            modifier = Modifier.fillMaxSize(),
                            color = MaterialTheme.colorScheme.background
                        ) {
                            if (bitmap != null) {
                                Greeting(bitmap)//
                            }
                        }
                    }
                    // Compose画面に切り替えて画像を表示
//                    DisplayImageFromUri(uri = uri.toString())
                }
            } else {
                Log.d("hoge", "uri: null")
            }
        }
//        pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        launchPickMedia(pickMedia)//写真選択画面の起動
    }
}

@Composable
fun Greeting(originalBitmap: Bitmap) {
    val singapore = LatLng(1.35, 103.87)

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(singapore, 10f)
    }
    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState
    ) {
        var zoom by remember { mutableStateOf(cameraPositionState.position.zoom.toInt()) }
        zoom = cameraPositionState.position.zoom.toInt()
        Log.d("zoom", zoom.toString())
        val newWidth = 10*zoom// 新しい幅
        val newHeight = 10*zoom // 新しい高さ
        Log.d("width", newWidth.toString())
        val resizedBitmap = Bitmap.createScaledBitmap(originalBitmap, newWidth, newHeight, false)
        val resizedIcon = BitmapDescriptorFactory.fromBitmap(resizedBitmap)
        val launchPickmedia = false
        Marker(
            state = MarkerState(position = singapore),
            title = "シンガポール",
            snippet = "ここはシンガポール",
            icon = resizedIcon,
//            onInfoWindowClick = launchPickmedia ウィンドウクリック時の動作。ここで写真選択画面を起動したい
        )


    }
}

@Composable
fun DisplayImageFromUri(uri: String) {
    AsyncImage(model = uri, contentDescription = null, modifier = Modifier.fillMaxSize())
}

// URIからビットマップを取得するメソッド
private fun getBitmapFromUri(uri: Uri, context: Context): Bitmap? {
    val inputStream: InputStream? =context.contentResolver.openInputStream(uri)
    return BitmapFactory.decodeStream(inputStream)
}

fun launchPickMedia(pickMedia: ActivityResultLauncher<PickVisualMediaRequest>){
    pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
}

