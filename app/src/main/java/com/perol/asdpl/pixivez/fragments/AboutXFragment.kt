package com.perol.asdpl.pixivez.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.core.net.toUri
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.perol.asdpl.pixivez.R



class AboutXFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.pre_about)
        // findPreference<Preference>
    }

    override fun onPreferenceTreeClick(preference: Preference?): Boolean {
        when (preference?.key) {
            "about_feedback_email" -> {
                val emailurl = "PxEzFeedBack@outlook.com"
                val email = Intent(Intent.ACTION_SEND)
                email.type = "message/rfc822"
                email.putExtra(Intent.EXTRA_EMAIL, arrayOf(emailurl))
                startActivity(Intent.createChooser(email, "Choose an Email Client"))
            }
            "about_feedback_telegram" -> {
                val url = "https://t.me/PixEzViewer"
                val uri = Uri.parse(url)
                val intent = Intent(Intent.ACTION_VIEW, uri)
                startActivity(intent)

            }
            "about_feedback_qq" -> {
                val url = "https://jq.qq.com/?_wv=1027&k=524mCLy"
                val uri = Uri.parse(url)
                val intent = Intent(Intent.ACTION_VIEW, uri)
                startActivity(intent)
            }
            "about_rate_github" -> {
                val url = "https://github.com/Notsfsssf"
                val uri = Uri.parse(url)
                val intent = Intent(Intent.ACTION_VIEW, uri)
                startActivity(intent)
            }
        }
        return super.onPreferenceTreeClick(preference)
    }
}