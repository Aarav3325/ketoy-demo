package com.aarav.ketoydemo

import android.app.Application
import dev.ketoy.adapters.material3.materialDrawableResolver
import dev.ketoy.adapters.material3.materialFontFamilyResolver
import dev.ketoy.adapters.material3.materialIconsResolver
import dev.ketoy.adapters.material3.registerGeneratedAdapters
import dev.ketoy.adapters.material3.registerGeneratedConstructors
import dev.ketoy.capabilities.core.registerCoreCapabilities
import dev.ketoy.runtime.KetoyConfig
import dev.ketoy.runtime.KetoyRuntime
import dev.ketoy.runtime.bundle.KetoyBundleLoader
import dev.ketoy.runtime.capability.CapabilityRegistry

/**
 * Ketoy application bootstrap.
 *
 * Owns the [KetoyRuntime] singleton and a [KetoyBundleLoader] keyed to it.
 * Both are exposed to `MainActivity` and published into the composition via
 * `LocalKetoyRuntime` / `LocalKetoyBundleLoader` so `KetoyScreen` can pick
 * them up without manual wiring.
 *
 * Register host-side capabilities (network / storage / app-specific) on the
 * [CapabilityRegistry] returned by `buildCapabilityRegistry()`. Register
 * Compose adapters + constructor adapters on the registries hanging off
 * [KetoyRuntime] — NOT on the capability registry.
 */
class MyApplication : Application() {

    lateinit var ketoyRuntime: KetoyRuntime
        private set

    lateinit var ketoyBundleLoader: KetoyBundleLoader
        private set

    override fun onCreate() {
        super.onCreate()

        val capabilityRegistry =
            CapabilityRegistry().apply {
                registerCoreCapabilities(context = this@MyApplication)
                // App-specific capabilities (0x4000+) go here:
                // registerSuspend(0x4000) { args -> ... }
            }

        // Resource resolvers — the KBC compiler plugin atomic-collapses
        // `FontFamily(Font(R.font.X))` and `painterResource(R.drawable.X)`
        // into `KBCValue.StringLiteral("X")` at compile time; at render time
        // `KBCParamSet.getFontFamily` / `getPainter` dispatches the bare
        // resource name through these host-supplied resolvers. R8 is safe
        // because we reference the `R.font.X` / `R.drawable.X` fields as
        // compile-time reads here — the shrinker sees them and keeps the
        // resources alive in release builds. No `-keep` rules required.
        val fontResolver = materialFontFamilyResolver {
            // register("courgette_regular", FontFamily(Font(R.font.courgette_regular)))
        }
        val drawableResolver = materialDrawableResolver {
            // register("logo", R.drawable.logo)
        }
        val iconsResolver = materialIconsResolver {
            // registerFilled("Settings", androidx.compose.material.icons.Icons.Filled.Settings)
        }

        val config = KetoyConfig(
            // Production-safe default: signature verification ON. Until you
            // generate a keypair (`openssl genpkey -algorithm Ed25519 ...`)
            // and ship the matching public key at
            // `assets/ketoy/keys/release-public.key`, flip this to false OR
            // pass `publicKey = KetoyKeystore.loadFromAsset(this, "...")`.
            enableSignatureVerification = false,
            fontFamilyResolver = fontResolver,
            drawableResolver = drawableResolver,
            imageVectorResolver = iconsResolver,
        )

        ketoyRuntime = KetoyRuntime(
            capabilityRegistry = capabilityRegistry,
            config = config,
        )

        // Material3 composable + constructor adapters go on the dedicated
        // registries hanging off the runtime — not on CapabilityRegistry.
        registerGeneratedAdapters(ketoyRuntime.adapterRegistry)
        registerGeneratedConstructors(ketoyRuntime.constructorRegistry)

        ketoyBundleLoader = KetoyBundleLoader(ketoyRuntime, this)
    }
}
