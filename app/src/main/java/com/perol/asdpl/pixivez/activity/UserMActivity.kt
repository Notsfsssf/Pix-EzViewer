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

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.perol.asdpl.pixivez.R
import com.perol.asdpl.pixivez.adapters.viewpager.UserMPagerAdapter
import com.perol.asdpl.pixivez.databinding.ActivityUserMBinding
import com.perol.asdpl.pixivez.fragments.UserMessageFragment
import com.perol.asdpl.pixivez.objects.ThemeUtil
import com.perol.asdpl.pixivez.objects.Toasty
import com.perol.asdpl.pixivez.services.GlideApp
import com.perol.asdpl.pixivez.services.PxEZApp
import com.perol.asdpl.pixivez.viewmodel.UserMViewModel
import kotlinx.android.synthetic.main.activity_user_m.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.io.File

class UserMActivity : RinkActivity() {
    var id: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        ThemeUtil.themeInit(this)
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityUserMBinding>(this, R.layout.activity_user_m).apply {
            lifecycleOwner = this@UserMActivity
        }


        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = Color.TRANSPARENT

        id = intent.getLongExtra("data", 1)
        val viewModel = ViewModelProviders.of(this).get(UserMViewModel::class.java)
        viewModel.getdata(id)

        viewModel.userDetail.observe(this, Observer {
            if (it != null) {
                fab.show()
                viewModel.isuser(id).subscribe({
                    if (it) {
                        fab.hide()
                        mviewpager.currentItem = 2
                    }
                }, {})

                binding.user = it
                /*     GlideApp.with(this).asDrawable().load(viewmodel.userDetail.value!!.profile.background_image_url).into(object : SimpleTarget<Drawable>() {
                         override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                             app_bar.background=resource
                         }
                     })*/
                val userMPagerAdapter = UserMPagerAdapter(this, supportFragmentManager, id.toLong(), UserMessageFragment.newInstance(it))
                mviewpager.adapter = userMPagerAdapter
                mtablayout.setupWithViewPager(mviewpager)
            }
        })
        viewModel.isfollow.observe(this, Observer {
            if (it != null) {
                if (it) {
                    fab.setImageResource(R.drawable.ic_check_white_24dp)
                } else
                    fab.setImageResource(R.drawable.ic_add_white_24dp)
            }


        })
        setSupportActionBar(toolbar)
        supportActionBar!!.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowTitleEnabled(false)
        }

        fab.setOnClickListener {
            viewModel.onFabclick(id.toLong())
        }
        fab.setOnLongClickListener {
            Toasty.info(applicationContext, "Private....", Toast.LENGTH_SHORT).show()
            viewModel.onFabLongClick(id.toLong())
            true
        }
        val shareLink = "https://www.pixiv.net/member.php?id=$id"
        imageview_useruserimage.setOnClickListener {
            val array = arrayOf("Link", "User Image")
            MaterialAlertDialogBuilder(this).setTitle("Link").setItems(array) { i, which ->
                when (which) {
                    0 -> {
                        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        val clip: ClipData = ClipData.newPlainText("simple text", shareLink)
                        clipboard.setPrimaryClip(clip)
                        Toasty.info(this@UserMActivity, "copied", Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                        runBlocking {
                            var file: File? = null
                            withContext(Dispatchers.IO) {
                                val f = GlideApp.with(this@UserMActivity).asFile().load(viewModel.userDetail.value!!.user.profile_image_urls.medium).submit()
                                file = f.get()
                                val target = File(PxEZApp.storepath, "user_${viewModel.userDetail.value!!.user.id}.png")
                                file?.copyTo(target, overwrite = true)
                            }
                            PxEZApp.instance.sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)))
                            Toasty.info(this@UserMActivity, "Saved", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }.setTitle("Save").create().show()

        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_userx, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finishAfterTransition()
            R.id.action_share -> share()
            R.id.action_download -> {
//                val intent =Intent(this,WorkActivity::class.java)
//                intent.putExtra("id",id)
//                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun share() {
        val textIntent = Intent(Intent.ACTION_SEND)
        textIntent.type = "text/plain"
        textIntent.putExtra(Intent.EXTRA_TEXT, "https://www.pixiv.net/member.php?id=$id")
        startActivity(Intent.createChooser(textIntent, "分享"))
    }
}
