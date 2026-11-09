package com.example.ads

/**
 * AdMob Configuration for Daily Smart Tools.
 *
 * TO REPLACE WITH PRODUCTION ADMOB IDS BEFORE RELEASE:
 * 1. Set [IS_TEST_MODE] = false
 * 2. Replace [ADMOB_APP_ID] in AndroidManifest.xml and below with your real AdMob App ID.
 * 3. Replace [BANNER_AD_UNIT_ID] with your production Banner Ad Unit ID.
 * 4. Replace [INTERSTITIAL_AD_UNIT_ID] with your production Interstitial Ad Unit ID.
 */
object AdConfig {
    // Flag to enable/disable test devices or ad logging
    const val IS_TEST_MODE: Boolean = true

    // Official Google Mobile Ads Test App ID
    const val ADMOB_APP_ID: String = "ca-app-pub-3940256099942544~3347511713"

    // Official Google Mobile Ads Test Banner Ad Unit ID
    const val BANNER_AD_UNIT_ID: String = "ca-app-pub-3940256099942544/6300978111"

    // Official Google Mobile Ads Test Interstitial Ad Unit ID
    const val INTERSTITIAL_AD_UNIT_ID: String = "ca-app-pub-3940256099942544/1033173712"

    // Minimum cooldown in milliseconds between interstitial displays (60 seconds)
    const val INTERSTITIAL_COOLDOWN_MS: Long = 60_000L

    // Minimum completed tool actions before showing an interstitial
    const val ACTIONS_BETWEEN_INTERSTITIALS: Int = 3
}
