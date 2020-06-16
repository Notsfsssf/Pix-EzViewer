package com.perol.asdpl.pixivez.manager

import android.content.res.Configuration
import android.os.Bundle
import androidx.preference.ListPreference
import androidx.preference.PreferenceFragmentCompat
import com.arialyy.aria.core.Aria
import com.perol.asdpl.pixivez.R
import com.perol.asdpl.pixivez.activity.RinkActivity
import kotlinx.android.synthetic.main.settings_activity.*

class ManagerSettingsActivity : RinkActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
        setSupportActionBar(toolbar)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.settings, SettingsFragment())
            .commit()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        toolbar.setNavigationOnClickListener { finish() }

    }


    class SettingsFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)
            findPreference<ListPreference>("max_task_num")!!.setOnPreferenceChangeListener { preference, newValue ->
                Aria.get(requireActivity()).downloadConfig.apply {
                    maxTaskNum = (newValue as String).toInt()
                }
                true
            }
            findPreference<ListPreference>("thread_num")!!.setOnPreferenceChangeListener { preference, newValue ->
                Aria.get(requireActivity()).downloadConfig.apply {
                    threadNum  = (newValue as String).toInt()
                }
                true
            }
        }

        override fun onConfigurationChanged(newConfig: Configuration) {
            super.onConfigurationChanged(newConfig)
        }
    }
}