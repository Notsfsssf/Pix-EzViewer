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

package com.perol.asdpl.pixivez.fragments

import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.preference.Preference
import androidx.preference.PreferenceCategory
import androidx.preference.PreferenceFragmentCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.perol.asdpl.pixivez.BuildConfig
import com.perol.asdpl.pixivez.R
import com.perol.asdpl.pixivez.dialog.ThanksDialog

class ThanksFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.pre_thanks)
        findPreference<PreferenceCategory>("huonaicai")?.isVisible = !BuildConfig.ISGOOGLEPLAY

        findPreference<Preference>("xuemo")?.apply {
            loadDrawableByUrl(
                "https://avatars.githubusercontent.com/LuckXuemo",
                R.drawable.xuemo,
                onLoadCleared = { it?.let { icon = it } },
                onResourceReady = { icon = it })
            onPreferenceClickListener = Preference.OnPreferenceClickListener {
                startActivityByUri("https://github.com/LuckXuemo")
                true
            }
        }

        findPreference<Preference>("ultranity")?.apply {
            loadDrawableByUrl(
                "https://avatars.githubusercontent.com/ultranity",
                R.drawable.xuemo,
                onLoadCleared = { it?.let { icon = it } },
                onResourceReady = { icon = it })
            onPreferenceClickListener = Preference.OnPreferenceClickListener {
                startActivityByUri("https://github.com/ultranity")
                true
            }
        }
        findPreference<Preference>("hunterx9")?.apply {
            loadDrawableByUrl(
                "https://avatars.githubusercontent.com/hunterx9",
                R.drawable.hunterx9,
                onLoadCleared = { it?.let { icon = it } },
                onResourceReady = { icon = it })
            onPreferenceClickListener = Preference.OnPreferenceClickListener {
                startActivityByUri("https://github.com/hunterx9")
                true
            }
        }
        findPreference<Preference>("Skimige")?.apply {
            loadDrawableByUrl(
                "https://avatars.githubusercontent.com/Skimige",
                R.drawable.skimige,
                onLoadCleared = { it?.let { icon = it } },
                onResourceReady = { icon = it })
            onPreferenceClickListener = Preference.OnPreferenceClickListener {
                startActivityByUri("https://github.com/Skimige")
                true
            }
        }

        findPreference<Preference>("TragicLife")?.apply {
            loadDrawableByUrl(
                "https://avatars.githubusercontent.com/TragicLifeHu",
                R.drawable.skimige,
                onLoadCleared = { it?.let { icon = it } },
                onResourceReady = { icon = it })
            onPreferenceClickListener = Preference.OnPreferenceClickListener {
                startActivityByUri("https://github.com/TragicLifeHu")
                true
            }
        }
        findPreference<Preference>("Misoni")?.apply {
            loadDrawableByUrl(
                "https://avatars.githubusercontent.com/MISONLN41",
                R.drawable.skimige,
                onLoadCleared = { it?.let { icon = it } },
                onResourceReady = { icon = it })
            onPreferenceClickListener = Preference.OnPreferenceClickListener {
                startActivityByUri("https://github.com/MISONLN41")
                true
            }
        }
//        listOf(
//            mapOf(
//                "Preference" to findPreference<Preference?>("xuemo"),
//                "AvatarUrl" to "https://avatars.githubusercontent.com/LuckXuemo",
//                "Placeholder" to R.drawable.xuemo,
//                "GithubProfile" to "https://github.com/LuckXuemo"
//            ),
//            mapOf(
//                "Preference" to findPreference<Preference?>("hunterx9"),
//                "AvatarUrl" to "https://avatars.githubusercontent.com/hunterx9",
//                "Placeholder" to R.drawable.hunterx9,
//                "GithubProfile" to "https://github.com/hunterx9"
//            ),
//            mapOf(
//                "Preference" to findPreference<Preference?>("Skimige"),
//                "AvatarUrl" to "https://avatars.githubusercontent.com/Skimige",
//                "Placeholder" to R.drawable.skimige,
//                "GithubProfile" to "https://github.com/Skimige"
//            )
//        ).forEach {
//            (it["Preference"] as Preference?)?.apply {
//                loadDrawableByUrl(
//                    it["AvatarUrl"] as String,
//                    it["Placeholder"] as Int,
//                    onLoadCleared = { d -> d?.let { icon = d } },
//                    onResourceReady = { d -> icon = d })
//                onPreferenceClickListener = Preference.OnPreferenceClickListener { _ ->
//                    startActivityByUri(it["GithubProfile"] as String)
//                    true
//                }
//            }
//        }
    }

    override fun onPreferenceTreeClick(preference: Preference?): Boolean {
        when (preference?.key) {
            "support" -> startActivityByUri("https://play.google.com/store/apps/details?id=com.perol.asdpl.play.pixivez")
            /*  "pr" -> startActivityByUri("https://github.com/Notsfsssf/Pix-EzViewer/pulls")*/
            "thanks" -> {
                val thanksDialog = ThanksDialog()
                thanksDialog.show(childFragmentManager)

//                MaterialDialog(requireContext()).show {
//                    customView(R.layout.dialog_thanks, scrollable = true, horizontalPadding = false)
//                    cornerRadius(2.0F)
//                    lifecycleOwner(this@ThanksFragment)
//                }.getCustomView().apply {
//                    list.apply {
//                        adapter = ThanksAdapter(R.layout.simple_list_item, ThanksDialog().array).apply {
//                            setHeaderView(LayoutInflater.from(context).inflate(R.layout.dialog_thanks_header, null, false))
//                        }
//                        layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
//                    }
//                }

//                MaterialDialog(requireContext()).show {
////                    title(text = "")
//                    message(R.string.summary)
//                    listItems(items = ThanksDialog().array)
//                    cornerRadius(2.0F)
//                    lifecycleOwner(this@ThanksFragment)
//                }
            }
            "wepay" -> {
                val view = requireActivity().layoutInflater.inflate(R.layout.wepayimage, null)
                view.findViewById<ImageView>(R.id.imageview).setImageResource(R.drawable.weixinqr)
                MaterialAlertDialogBuilder(activity).setView(view).setPositiveButton(android.R.string.ok) { _, _ ->
                }.show()

            }

        }
        return super.onPreferenceTreeClick(preference)
    }

    private fun startActivityByUri(uri: String) {
        Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(uri)
        }.also {
            it.resolveActivity(requireContext().packageManager)?.run {
                startActivity(it)
            }
        }
    }

    private fun loadDrawableByUrl(
        url: String, @DrawableRes placeholder: Int,
        onLoadCleared: (Drawable?) -> Unit,
        onResourceReady: (Drawable) -> Unit
    ) {
        Glide.with(this@ThanksFragment)
            .load(url)
            .apply(RequestOptions.bitmapTransform(CircleCrop()))
            .placeholder(placeholder)
            .into(object : CustomTarget<Drawable>() {
                override fun onLoadCleared(placeholder: Drawable?) {
                    onLoadCleared(placeholder)
                }

                override fun onResourceReady(
                    resource: Drawable,
                    transition: Transition<in Drawable>?
                ) {
                    onResourceReady(resource)
                }
            })
    }
}
