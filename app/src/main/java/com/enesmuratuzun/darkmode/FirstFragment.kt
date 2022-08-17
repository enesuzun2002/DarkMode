package com.enesmuratuzun.darkmode

import android.app.UiModeManager
import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.enesmuratuzun.darkmode.databinding.FragmentFirstBinding
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {
    private var mInterstitialAd: InterstitialAd? = null
    private var uiModeManager: UiModeManager? = null
    private var _binding: FragmentFirstBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        uiModeManager = activity?.getSystemService(AppCompatActivity.UI_MODE_SERVICE) as UiModeManager
        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        getInterstitial()

        super.onViewCreated(view, savedInstanceState)
        var darkMode = activity?.getDrawable(R.drawable.ic_dark_mode)
        var lightMode = activity?.getDrawable(R.drawable.ic_light_mode)

        when(uiModeManager?.nightMode){
            UiModeManager.MODE_NIGHT_NO -> {
                binding.imageView.setImageDrawable(lightMode)
                if(!Constants.isStarted){
                    binding.toggleButton.check(binding.button2.id)
                    Constants.isStarted = true
                }

            }
            UiModeManager.MODE_NIGHT_YES -> {
                binding.imageView.setImageDrawable(darkMode)
                if(!Constants.isStarted){
                    binding.toggleButton.check(binding.button1.id)
                    Constants.isStarted = true
                }
            }
        }

        binding.toggleButton.addOnButtonCheckedListener { _, checkedId, _ ->
            if(Constants.isStarted){
                if (checkedId == binding.button1.id && uiModeManager?.nightMode != UiModeManager.MODE_NIGHT_YES) {
                    setDarkMode(1)
                }
                else if(checkedId == binding.button2.id && uiModeManager?.nightMode != UiModeManager.MODE_NIGHT_NO){
                    setDarkMode(0)
                }
            }
        }

        val mAdView = AdView(requireActivity())
        mAdView.setAdSize(AdSize.FULL_BANNER)
        if(BuildConfig.DEBUG){
            mAdView.adUnitId= "ca-app-pub-3940256099942544/6300978111"
        } else {
            mAdView.adUnitId= Constants.bannerAdId
        }
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)
        binding.adViewContainer.addView(mAdView)

    }
    private fun setDarkMode(value: Int) {
        Constants.count++
        when (value){
            0 -> {
                uiModeManager?.nightMode = UiModeManager.MODE_NIGHT_NO
            }
            else -> {
                uiModeManager?.nightMode = UiModeManager.MODE_NIGHT_YES
            }
        }
        if(Constants.count == 5){
            if (mInterstitialAd != null) {
                mInterstitialAd?.show(requireActivity())
            } else {
                Log.d("TAG", "The interstitial ad wasn't ready yet.")
            }
            Constants.count = 0
        }
    }

    fun getInterstitial() {
        var adRequest = AdRequest.Builder().build()
        var adUnitId: String = if(BuildConfig.DEBUG){
            "ca-app-pub-3940256099942544/1033173712"
        } else {
            Constants.interstitialAdId
        }

        InterstitialAd.load(requireActivity(),adUnitId, adRequest, object : InterstitialAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                Log.d(TAG, adError?.toString()!!)
                mInterstitialAd = null
            }

            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                Log.d(TAG, "Ad was loaded.")
                mInterstitialAd = interstitialAd
            }
        })
        mInterstitialAd?.fullScreenContentCallback = object: FullScreenContentCallback() {
            override fun onAdClicked() {
                // Called when a click is recorded for an ad.
                Log.d(TAG, "Ad was clicked.")
            }

            override fun onAdDismissedFullScreenContent() {
                // Called when ad is dismissed.
                Log.d(TAG, "Ad dismissed fullscreen content.")
                mInterstitialAd = null
            }

            override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                // Called when ad fails to show.
                Log.e(TAG, "Ad failed to show fullscreen content.")
                mInterstitialAd = null
            }

            override fun onAdImpression() {
                // Called when an impression is recorded for an ad.
                Log.d(TAG, "Ad recorded an impression.")
            }

            override fun onAdShowedFullScreenContent() {
                // Called when ad is shown.
                Log.d(TAG, "Ad showed fullscreen content.")
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
