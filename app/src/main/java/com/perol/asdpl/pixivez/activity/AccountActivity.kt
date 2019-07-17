package com.perol.asdpl.pixivez.activity

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.perol.asdpl.pixivez.R
import com.perol.asdpl.pixivez.adapters.AccountChoiceAdapter
import com.perol.asdpl.pixivez.networks.SharedPreferencesServices
import com.perol.asdpl.pixivez.objects.ThemeUtil
import com.perol.asdpl.pixivez.repository.AppDataRepository
import kotlinx.android.synthetic.main.activity_account.*
import kotlinx.coroutines.runBlocking
import org.jetbrains.anko.alert
import org.jetbrains.anko.noButton
import org.jetbrains.anko.yesButton

class AccountActivity : AppCompatActivity() {
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
            R.id.nav_add->{
                startActivity(Intent(this,LoginActivity::class.java))
            }
            R.id.nav_logout->{
                alert {
                    title=resources.getString(R.string.logoutallaccount)
                    yesButton {
                        runBlocking {
                            AppDataRepository.deleteAllUser()
                        }
                        startActivity(Intent(this@AccountActivity, LoginActivity::class.java))
                        finish()
                    }
                    noButton {

                    }
                }.show()
            }
        }
        return super.onOptionsItemSelected(item)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ThemeUtil.Themeinit(this)
        setContentView(R.layout.activity_account)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        recyclerview_account.layoutManager=LinearLayoutManager(this)
        runBlocking {
            val users= AppDataRepository.getAllUser()
            recyclerview_account.adapter=AccountChoiceAdapter(R.layout.view_account_item,users,
                    SharedPreferencesServices.getInstance().getInt("usernum"))

        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.account, menu)
        return true
    }

}
