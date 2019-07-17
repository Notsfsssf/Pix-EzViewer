package com.perol.asdpl.pixivez.activity

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.perol.asdpl.pixivez.R
import com.perol.asdpl.pixivez.adapters.viewpager.UserMPagerAdapter
import com.perol.asdpl.pixivez.databinding.ActivityUserMBinding
import com.perol.asdpl.pixivez.fragments.UserMessageFragment
import com.perol.asdpl.pixivez.objects.ThemeUtil
import com.perol.asdpl.pixivez.viewmodel.UserMViewModel
import kotlinx.android.synthetic.main.activity_user_m.*
import org.jetbrains.anko.*

class UserMActivity : RinkActivity() {
    var id: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        ThemeUtil.Themeinit(this)
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityUserMBinding>(this, R.layout.activity_user_m)
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        id = intent.getLongExtra("data", 1)
        val viewmodel = ViewModelProviders.of(this).get(UserMViewModel::class.java)
        viewmodel.getdata(id)

        viewmodel.userDetail.observe(this, Observer {
            if (it != null) {
                fab.show()
                viewmodel.isuser(id).doOnSuccess {
                    if (it) {
                        fab.hide()
                        mviewpager.setCurrentItem(2)
                    }
                }.subscribe()

                binding.user = it
                /*     GlideApp.with(this).asDrawable().load(viewmodel.userDetail.value!!.profile.background_image_url).into(object : SimpleTarget<Drawable>() {
                         override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                             app_bar.background=resource
                         }
                     })*/
                val userMPagerAdapter = UserMPagerAdapter(this,supportFragmentManager, id.toLong(), UserMessageFragment.newInstance(it))
                mviewpager.adapter = userMPagerAdapter
                mtablayout.setupWithViewPager(mviewpager)
            }
        })
        viewmodel.isfollow.observe(this, Observer {
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
            viewmodel.onFabclick(id.toLong())
        }
        fab.setOnLongClickListener {
            viewmodel.onFabLongClick(id.toLong())
            true
        }
        val shareLink = "https://www.pixiv.net/member.php?id=$id";
        imageview_useruserimage.setOnClickListener {
            alert("", "Link") {
                customView {
                    relativeLayout {
                        textView(shareLink) {
                            setTextIsSelectable(true)
                            isFocusable = true
                            textSize = 16f
                            singleLine = false
                            isFocusableInTouchMode = true
                        }.lparams(width = wrapContent, height = wrapContent) {
                            gravity = Gravity.CENTER
                            centerHorizontally()
                            centerVertically()
                        }
                    }
                }
            }.show()

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
