package com.websarva.wings.mapsapplication

import android.net.Uri
import androidx.lifecycle.ViewModel

// This is the ViewModel class of the Main Activity.
class MainVM: ViewModel() {
    // 取得した画像のURIを格納する変数
    var uri: Uri? = null

    // マーカーのタップ状態を格納する変数
    var markerTapped: Boolean = false
}