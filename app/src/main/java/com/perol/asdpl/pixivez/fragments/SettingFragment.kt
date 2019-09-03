package com.perol.asdpl.pixivez.fragments

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.perol.asdpl.pixivez.R

class SettingFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.pref_settings)
    }
}