package com.aarav.ketoydemo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.ketoy.annotations.KetoyComposable
import dev.ketoy.annotations.KetoyEntryPoint

/**
 * Default KBC entry-point shipped by `ketoy init`.
 *
 * The KetoyBC compiler plugin compiles this function (and every other
 * `@KetoyComposable` / `@KetoyEntryPoint` reachable from it) into the
 * `main.ktx` bundle written to `assets/ketoy/main.ktx` at release build
 * time. At runtime, `KetoyScreen(entryPoint = "HelloKetoyScreen", ...)`
 * loads + executes it.
 */
@KetoyEntryPoint
@KetoyComposable
@Composable
fun HelloKetoyScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "Hello, GeoWav!",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF4F378B),
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = "This screen ships as KBC bytecode inside the APK.",
            fontSize = 14.sp,
            color = Color.DarkGray,
        )
    }
}
