/*
 * MIT License
 *
 * Copyright (c) 2019 Perol_Notsfsssf
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE
 */

package com.perol.asdpl.pixivez.activity

import android.app.Activity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.preference.DropDownPreference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.perol.asdpl.pixivez.R
import com.perol.asdpl.pixivez.adapters.ColorData
import com.perol.asdpl.pixivez.adapters.ColorfulAdapter
import com.perol.asdpl.pixivez.objects.ThemeUtil
import com.perol.asdpl.pixivez.services.PxEZApp
import kotlinx.android.synthetic.main.activity_theme.*

class ThemeActivity : AppCompatActivity() {
    class ThemeFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            addPreferencesFromResource(R.xml.pre_theme)
            findPreference<DropDownPreference>("dark_mode")!!.setOnPreferenceChangeListener { preference, newValue ->
                AppCompatDelegate.setDefaultNightMode(newValue.toString().toInt())
                PxEZApp.instance.setTheme(R.style.AppThemeBase_pink)
                true
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ThemeUtil.themeInit(this)
        setContentView(R.layout.activity_theme)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportFragmentManager.beginTransaction().replace(R.id.fragment_theme, ThemeFragment()).commit()
        val list = arrayListOf(
                ColorData(ContextCompat.getColor(this, R.color.colorPrimary), "Primary"),
                ColorData(ContextCompat.getColor(this, R.color.md_blue_300), "Blue"),
                ColorData(ContextCompat.getColor(this, R.color.pink), "Pink"),
                ColorData(ContextCompat.getColor(this, R.color.miku), "Miku"),
            ColorData(ContextCompat.getColor(this, R.color.md_purple_500), "Purple"),
                ColorData(ContextCompat.getColor(this, R.color.md_cyan_300), "Cyan"),
                ColorData(ContextCompat.getColor(this, R.color.md_green_300), "Green"),
                ColorData(ContextCompat.getColor(this, R.color.md_indigo_300), "Indigo"),
                ColorData(ContextCompat.getColor(this, R.color.md_red_500), "Red"),
                ColorData(ContextCompat.getColor(this, R.color.now), "Pale green"))
        recyclerview.apply {
            layoutManager = LinearLayoutManager(this@ThemeActivity)
            adapter = ColorfulAdapter(R.layout.view_colorfulitem, list).apply {
                onItemClickListener = BaseQuickAdapter.OnItemClickListener { adapter, view, position ->
                    PreferenceManager.getDefaultSharedPreferences(this@ThemeActivity).edit().putInt("colorint", position).apply()
                    setResult(Activity.RESULT_OK)
                    finish()
                }
            }
            isNestedScrollingEnabled = false
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()

        }
        return super.onOptionsItemSelected(item)
    }
}
