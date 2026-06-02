package com.aarav.ketoydemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.aarav.ketoydemo.ui.theme.KetoyDemoTheme

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import dev.ketoy.runtime.bundle.KetoyBundleSource
import dev.ketoy.runtime.compose.KetoyScreen
import dev.ketoy.runtime.compose.LocalKetoyBundleLoader
import dev.ketoy.runtime.compose.LocalKetoyRuntime
import com.aarav.ketoydemo.MyApplication
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val app = application as MyApplication
        enableEdgeToEdge()
        setContent {
            KetoyDemoTheme {
                CompositionLocalProvider(
                    LocalKetoyRuntime provides app.ketoyRuntime,
                    LocalKetoyBundleLoader provides app.ketoyBundleLoader,
                ) {
                    KetoyScreen(
                        entryPoint = "HelloKetoyScreen",
                        bundleSource = KetoyBundleSource.Asset("ketoy/main.ktx"),
                    ) {
                        // Native fallback — rendered when the .ktx bundle is absent,
                        // incompatible, or corrupt. Replace with your own native screen.
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            Text("Hello Android")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    KetoyDemoTheme {
        Greeting("Android")
    }
}