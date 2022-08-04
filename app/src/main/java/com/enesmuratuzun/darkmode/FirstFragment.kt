package com.enesmuratuzun.darkmode

import android.app.UiModeManager
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.enesmuratuzun.darkmode.databinding.FragmentFirstBinding

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {
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

        super.onViewCreated(view, savedInstanceState)
        var darkMode = activity?.getDrawable(R.drawable.ic_dark_mode)
        var lightMode = activity?.getDrawable(R.drawable.ic_light_mode)

        when(uiModeManager?.nightMode){
            UiModeManager.MODE_NIGHT_NO -> {
                binding.imageView.setImageDrawable(lightMode)
                binding.toggleButton.check(binding.button2.id)
            }
            UiModeManager.MODE_NIGHT_YES -> {
                binding.imageView.setImageDrawable(darkMode)
                binding.toggleButton.check(binding.button1.id)
            }
        }

        binding.toggleButton.addOnButtonCheckedListener { _, checkedId, _ ->
            when (checkedId) {
                binding.button1.id -> {
                    setDarkMode(1)
                }
                else -> {
                    setDarkMode(0)
                }
            }
        }
    }
    private fun setDarkMode(value: Int) {
        when (value){
            0 -> {
                uiModeManager?.nightMode = UiModeManager.MODE_NIGHT_NO
            }
            else -> {
                uiModeManager?.nightMode = UiModeManager.MODE_NIGHT_YES
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
