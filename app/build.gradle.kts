plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.android)
    id("dev.ketoy.compiler") version "0.3.4-alpha"
}

android {
    namespace = "com.aarav.ketoydemo"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.aarav.ketoydemo"
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    debugImplementation(libs.androidx.compose.ui.tooling)
    // Ketoy 0.3.4-alpha
    implementation(platform("dev.ketoy.vm:ketoy-bom:0.3.4-alpha"))
    implementation("dev.ketoy.vm:ketoy-runtime")
    implementation("dev.ketoy.vm:ketoy-annotations")
    implementation("dev.ketoy.vm:ketoy-capabilities-core")
    implementation("dev.ketoy.vm:ketoy-capabilities-navigation")
    implementation("dev.ketoy.vm:ketoy-adapters-material3")
}

ketoy {
    // ADR-0003 inline-source app bundle: compile the @KetoyComposable
    // closure inside this module into ONE signed .ktx at compileReleaseKotlin.
    exportFromAppModule.set(true)
    bundleId.set("main")
    bundleVariant.set("release")
    capabilityRegistryFile.set(file("ketoy-capabilities.json"))
    // minimum host APK versionCode required to activate this bundle.
    // 0 = universally compatible (default).
    minAppVersion.set(0)
    // Emit source line numbers for the dev overlay.
    debugMode.set(true)

    // Optional: sign the bundle with Ed25519. Generate via:
    //   openssl genpkey -algorithm Ed25519 -outform DER -out key.der
    //   tail -c 32 key.der > app/keys/release-private.key
    // Without a key the plugin emits an unsigned bundle gracefully.
    val signingKey = file("keys/release-private.key")
    if (signingKey.exists()) {
        signingKeyFile.set(signingKey)
    }
}

kotlin {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
    }
}
