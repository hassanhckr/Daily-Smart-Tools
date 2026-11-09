package com.example.ads

import android.app.Activity
import android.content.Context
import android.util.Log
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback

class AdManager(private val context: Context) {

    private var interstitialAd: InterstitialAd? = null
    private var isLoadingInterstitial: Boolean = false
    private var lastAdShownTimestamp: Long = 0L
    private var completedActionCount: Int = 0

    init {
        try {
            MobileAds.initialize(context) { status ->
                Log.d("AdManager", "Mobile Ads initialized: $status")
                preloadInterstitial()
            }
        } catch (e: Exception) {
            Log.e("AdManager", "Mobile Ads init failed", e)
        }
    }

    fun preloadInterstitial() {
        if (interstitialAd != null || isLoadingInterstitial) return

        isLoadingInterstitial = true
        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(
            context,
            AdConfig.INTERSTITIAL_AD_UNIT_ID,
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(ad: InterstitialAd) {
                    interstitialAd = ad
                    isLoadingInterstitial = false
                    Log.d("AdManager", "Interstitial Ad loaded")

                    ad.fullScreenContentCallback = object : FullScreenContentCallback() {
                        override fun onAdDismissedFullScreenContent() {
                            interstitialAd = null
                            lastAdShownTimestamp = System.currentTimeMillis()
                            preloadInterstitial()
                        }

                        override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                            interstitialAd = null
                            isLoadingInterstitial = false
                            preloadInterstitial()
                        }
                    }
                }

                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    interstitialAd = null
                    isLoadingInterstitial = false
                    Log.w("AdManager", "Interstitial Ad failed to load: ${loadAdError.message}")
                }
            }
        )
    }

    /**
     * Record a user action (e.g. calculation completed, QR generated, note saved).
     * Only displays an interstitial if cooldown time has passed and action threshold is met.
     */
    fun notifyActionCompleted(activity: Activity?) {
        completedActionCount++
        val now = System.currentTimeMillis()
        val cooldownPassed = (now - lastAdShownTimestamp) >= AdConfig.INTERSTITIAL_COOLDOWN_MS

        if (completedActionCount >= AdConfig.ACTIONS_BETWEEN_INTERSTITIALS && cooldownPassed) {
            if (activity != null && interstitialAd != null) {
                completedActionCount = 0
                interstitialAd?.show(activity)
            } else {
                preloadInterstitial()
            }
        }
    }
}
