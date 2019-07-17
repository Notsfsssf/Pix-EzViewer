package com.perol.asdpl.pixivez.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.perol.asdpl.pixivez.R
import com.perol.asdpl.pixivez.networks.RestClient
import com.perol.asdpl.pixivez.networks.SharedPreferencesServices
import com.perol.asdpl.pixivez.objects.ThemeUtil
import com.perol.asdpl.pixivez.repository.AppDataRepository
import com.perol.asdpl.pixivez.responses.PixivOAuthResponse
import com.perol.asdpl.pixivez.services.OAuthSecureService
import com.perol.asdpl.pixivez.sql.AppDatabase
import com.perol.asdpl.pixivez.sql.UserEntity
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.jetbrains.anko.selector
import org.jetbrains.anko.toast
import java.util.*

class LoginActivity : RinkActivity() {
    private var username: String? = null
    private var password: String? = null
    private var sharedPreferencesServices: SharedPreferencesServices? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ThemeUtil.Themeinit(this)
        setContentView(R.layout.activity_login)
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        initbind()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_login, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                val intent = Intent(this, AboutActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.language -> {
                val languages = listOf("zh", "en", "zh-rHK")
                selector("language", languages) { dialogInterface, i ->
                    if (sharedPreferencesServices != null) {
                        sharedPreferencesServices!!.setInt("language", i)
                        toast("Please restart app")
                    }
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun initbind() {
        sharedPreferencesServices = SharedPreferencesServices.getInstance()
        try {

            if (sharedPreferencesServices!!.getString("password") != null) {
                edit_password!!.setText(sharedPreferencesServices!!.getString("password"))
                edit_username!!.setText(sharedPreferencesServices!!.getString("username"))
            }
            if (!sharedPreferencesServices!!.getBoolean("firstinfo")) {
                val normalDialog = AlertDialog.Builder(this)
                normalDialog.setMessage("1.在图片详情页长按可以保存选定图片，长按头像快速关注作者，请提供应用权限\n2.浏览动图时，点击播放按钮等待合成动图，完毕即可播放,合成过程内存开销相当之大,偶发崩溃不可避免\n3.若播放动图页面长时间白屏,请退出页面或者清除缓存后重试,这一般会起作用\n" +
                        "4.这是一个个人开发的应用,反馈请发邮件到设置页标注的邮箱,个人精力和能力是有限的,请不要使用极端方式进行反馈,体谅开发者,也欢迎共同开发设计\n5.限制总开关在官网里，遇到无权限访问的插画，自行至网页开启，开发者不提供帮助服务，开发者并不是老好人"
                )
                normalDialog.setTitle("请务必读完")
                normalDialog.setPositiveButton("不再提示"
                ) { dialog, which ->
                    sharedPreferencesServices!!.setBoolean("firstinfo", true)
                }
                normalDialog.show()
            }
//            if (!sharedPreferencesServices!!.getBoolean("readproxy")) {
//                val normalDialog = AlertDialog.Builder(this, R.style.AlertDialogCustom)
//                normalDialog.setMessage("1.应用可以在大陆地区直连获得数据，与其他应用不同，不借助任何第三方服务器或DNS中继，通过改写通讯底层实现，请放心使用\n2.应用是完全免费的，没有进行过任何出售贩卖APP的行为\n3.应用本身因为各种原因不是且不希望开源\n4.应用集成了Bugly来推送更新和收集错误信息，如果十分介意请卸载本应用")
//                normalDialog.setTitle("!使用前")
//                normalDialog.setPositiveButton("不再提示"
//                ) { dialog, which ->
//                    sharedPreferencesServices!!.setBoolean("readproxy", true)
//                }
//                normalDialog.show()
//            }
        } catch (e: Exception) {

        }
        val restClient = RestClient()
        val oAuthSecureService = restClient.getretrofit_OAuthSecure().create(OAuthSecureService::class.java)
        loginBtn!!.setOnClickListener {
            loginBtn.isClickable = false
            username = edit_username!!.text.toString()
            password = edit_password!!.text.toString()
            if (username == null || password == null) {
                return@setOnClickListener
            }
            val map = HashMap<String, Any>()
            map["client_id"] = "MOBrBDS8blbauoSck0ZfDbtuzpyT"
            map["client_secret"] = "lsACyCD94FhDUtGTXi3QzcFE2uU1hqtDaKeqrdwj"
            map["grant_type"] = "password"
            map["username"] = username!!
            map["password"] = password!!
            if (sharedPreferencesServices!!.getString("Device_token") != null) {
                map["device_token"] = sharedPreferencesServices!!.getString("Device_token")
            }
            Toast.makeText(applicationContext, this.getString(R.string.try_to_login), Toast.LENGTH_SHORT).show()
            oAuthSecureService.postAuthToken(map).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe(object : Observer<PixivOAuthResponse> {
                        override fun onSubscribe(d: Disposable) {

                        }

                        override fun onNext(pixivOAuthResponse: PixivOAuthResponse) {
                            val user = pixivOAuthResponse.response.user
                            GlobalScope.launch {
                                AppDataRepository.InsertUser(UserEntity(user.profile_image_urls.px_170x170, user.id.toLong(), user.name, user.mail_address, user.isIs_premium,
                                        pixivOAuthResponse.response.device_token, pixivOAuthResponse.response.refresh_token, "Bearer " + pixivOAuthResponse.response.access_token
                                ))

                                sharedPreferencesServices!!.setBoolean("islogin", true)
                                sharedPreferencesServices!!.setBoolean("isnone", false)
                                sharedPreferencesServices!!.setString("username", username)
                                sharedPreferencesServices!!.setString("password", password)

                            }

                        }

                        override fun onError(e: Throwable) {
                            loginBtn.isClickable = true
                            if (e.message!!.contains("400") && !e.message!!.contains("oauth")) {
                                Toast.makeText(applicationContext, "${e.message}", Toast.LENGTH_LONG).show()
                            } else {
                                Toast.makeText(applicationContext, "${e.message}", Toast.LENGTH_LONG).show()
                            }

                        }

                        override fun onComplete() {
                            loginBtn.isClickable = true
                            Toast.makeText(applicationContext, "登录成功", Toast.LENGTH_LONG).show()
                            val intent = Intent(this@LoginActivity, HelloMActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                    })
        }
    }


    fun s(view: View) {
//        val intent = Intent(this@LoginActivity, NewUserActivity::class.java)
//        startActivity(intent)
        toast(this.resources.getString(R.string.registerclose))
    }
}
