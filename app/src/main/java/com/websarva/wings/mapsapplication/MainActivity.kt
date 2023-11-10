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
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.ImagePainter
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

        // ViewModelのインスタンスを生成
        val vm = MainVM()

        val context: Context = applicationContext

        /*val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if(uri != null) {
                vm.uri = uri

                *//*val bitmap = getBitmapFromUri(uri, context)
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
                }*//*
            } else {
                Log.d("hoge", "uri: null")
            }
        }
//        pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        launchPickMedia(pickMedia)//写真選択画面の起動*/

        setContent {
            MapsApplicationTheme {
                Surface {
                    MyScreen()
                }
            }
        }
    }
}

@Composable
fun Map(originalBitmap: Bitmap, vm: MainVM) {

    /*LaunchedEffect(vm.markerTapped) {
        val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if(uri != null) {
                vm.uri = uri
            } else {
                Log.d("hoge", "uri: null")
            }
        }
    }*/

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
        Marker(
            state = MarkerState(position = singapore),
            title = "シンガポール",
            snippet = "ここはシンガポール",
            icon = resizedIcon,
            onInfoWindowClick = { vm.markerTapped = true } //ウィンドウクリック時の動作。ここで写真選択画面を起動したい
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


// ChatGPT
@Composable
fun MyScreen() {
    // 画像のUriを保持するためのState
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    // ActivityResultLauncherを取得
    val getContent = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        // 画像が選択されたときの処理
        if (uri != null) {
            imageUri = uri
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // 画像が選択された場合、その画像を表示
        imageUri?.let {
            /*Image(
                painter = ImagePainter(LocalContext.current, it),
                contentDescription = null,
                modifier = Modifier
                    .size(200.dp)
                    .padding(16.dp)
                    .clip(MaterialTheme.shapes.medium)
            )*/
        }

        // 画像を選択するボタン
        Button(
            onClick = {
                getContent.launch("image/*")
            },
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = "Select Image")
        }
    }
}

@Composable
fun rememberLauncherForActivityResult(
    contract: ActivityResultContract<String, Uri?>,
    onResult: (result: Uri?) -> Unit
): ActivityResultLauncher<String> {
    val launcher = rememberLauncher(contract, onResult)
    DisposableEffect(launcher) {
        onDispose {
            launcher.unregister()
        }
    }
    return launcher
}

@Composable
fun rememberLauncher(
    contract: ActivityResultContract<String, Uri?>,
    onResult: (result: Uri?) -> Unit
): ActivityResultLauncher<String> {
    val launcher = rememberUpdatedState(onResult)
    val currentOnResult by rememberUpdatedState(launcher.value)

    val activity = LocalContext.current as? ComponentActivity

    var launcher2: ActivityResultLauncher<String>? = null

    DisposableEffect(activity) {


        if (activity != null) {
            launcher2 = activity.registerForActivityResult(contract) { result ->
                currentOnResult(result)
            }
        }

        onDispose {
            launcher2?.unregister()
        }
    }

    return launcher2!!
}
