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

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.gson.Gson
import com.perol.asdpl.pixivez.R
import com.perol.asdpl.pixivez.networks.RestClient
import com.perol.asdpl.pixivez.networks.SharedPreferencesServices
import com.perol.asdpl.pixivez.objects.ThemeUtil
import com.perol.asdpl.pixivez.objects.Toasty
import com.perol.asdpl.pixivez.repository.AppDataRepository
import com.perol.asdpl.pixivez.responses.ErrorResponse
import com.perol.asdpl.pixivez.responses.PixivOAuthResponse
import com.perol.asdpl.pixivez.services.OAuthSecureService
import com.perol.asdpl.pixivez.services.Works
import com.perol.asdpl.pixivez.sql.UserEntity
import io.noties.markwon.Markwon
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import java.util.*

class LoginActivity : RinkActivity() {
    private var username: String? = null
    private var password: String? = null
    private var sharedPreferencesServices: SharedPreferencesServices? = null
    val markdownShot = "# TroubleShoot帮助\n" +
            "如果你登录时提示\n" +
            "```shell\n" +
            "    http 400 bad request\n" +
            "```\n" +
            "请尝试以下步骤  \n" +
            "1.确保应用是最新的，到google play或者github获取最新的版本  \n" +
            "2.已是最新版，则用户名或者密码有错(104:)，需要核对账号密码  \n" +
            "3.账号未完成验证或者用户账号无效(103:)，需要到官网进行验证完善  \n" +
            "ps:账号密码指的是pixiv的账号密码，而不是github的\n" +
            "\n" +
            "\n" +
            "如果登录后提示\n" +
            "```shell\n" +
            "    程序出错，即将退出,xx resourece not found\n" +
            "```\n" +
            "1.确保应用是最新的，到google play或者github获取最新的版本  \n" +
            "2.确保应用是google play或者github获取的，而不是别人传的甚至某某dalao卖的  \n" +
            "3.到以上渠道安装完最新版本后，清除应用数据并重新尝试登录\n" +
            "\n" +
            "## 若都无帮助\n" +
            "请通过反馈邮箱，详细反馈版本号，系统信息，错误信息截图  \n" +
            "反馈之前,你需要确保  \n" +
            "1.应用是google play或者github获取的  \n" +
            "2.不是使用尝鲜系统或者使用xposed或者magisk模块进行过魔改  \n" +
            "3.已仔细阅读完毕\"请务必读完\""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ThemeUtil.themeInit(this)
        setContentView(R.layout.activity_login)
        val window = window
//        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
//        window.decorView.systemUiVisibility =
//            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS or WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
//        window.statusBarColor = Color.TRANSPARENT

        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        initbind()

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_login, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                val intent = Intent(this, SettingActivity::class.java)
                startActivity(intent)
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
                val normalDialog = MaterialAlertDialogBuilder(this)
                normalDialog.setMessage(
                    "0.请务必确保在google play或者项目地址内安装与更新,第三方提供的安装包可能存在问题且不是最新\n1.在图片详情页长按可以保存选定图片，长按头像快速关注作者，请提供应用权限\n2.浏览动图时，点击中间0%进度条开始下载,完毕播放后长按进行合成保存，合成过程内存开销相当之大,偶发崩溃不可避免\n3.若播放动图无法播放,请退出页面或者清除缓存后重试,这一般会起作用\n" +
                            "4.这是一个个人开发的应用,反馈请发邮件到设置页标注的邮箱,个人精力和能力是有限的,请不要使用极端方式进行反馈,体谅开发者,也欢迎共同开发设计\n5.遇到更新后闪退的问题,请尝试清除应用数据并更新到最新版,将提示错误信息或日志反馈给开发者,多数情况下这是有效的\n6.限制总开关在官网里，遇到无权限访问的插画，自行至网页开启，开发者不提供帮助服务，开发者并不是老好人"
                )
                normalDialog.setTitle("请务必读完")
                normalDialog.setPositiveButton(
                    "我已知晓"
                ) { dialog, which ->
                    sharedPreferencesServices!!.setBoolean("firstinfo", true)
                }
                normalDialog.show()
            }
        } catch (e: Exception) {

        }
        val restClient = RestClient()
        val oAuthSecureService =
            restClient.getretrofit_OAuthSecure().create(OAuthSecureService::class.java)
        textview_help.setOnClickListener {
            val builder = MaterialAlertDialogBuilder(this)
            val view = layoutInflater.inflate(R.layout.new_dialog_user_help, null)
            val webView = view.findViewById(R.id.web_user_help) as TextView
            // obtain an instance of Markwon
            val markwon = Markwon.create(this);

            val node = markwon.parse(markdownShot);

            val markdown = markwon.render(node);

            // use it on a TextView
            markwon.setParsedMarkdown(webView, markdown);
            builder.setPositiveButton(android.R.string.ok) { _, _ ->

            }
            builder.setView(view)
            builder.create().show()
        }

        edit_username.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                // Ignore.
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Ignore.
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                accountTextInputLayout.isErrorEnabled = false
            }
        })
        edit_password.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                // Ignore.
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Ignore.
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                passwordTextInputLayout.isErrorEnabled = false
            }
        })

        loginBtn!!.setOnClickListener {
            //            loginBtn.isClickable = false

            username = edit_username!!.text.toString().trim()
            password = edit_password!!.text.toString()

            if (username.isNullOrBlank()) accountTextInputLayout.error =
                getString(R.string.error_blank_account)

            if (password.isNullOrBlank()) passwordTextInputLayout.error =
                getString(R.string.error_blank_password)

            if (username.isNullOrBlank() || password.isNullOrBlank()) {
                return@setOnClickListener
            }

            loginBtn.isEnabled = false

            sharedPreferencesServices!!.setString("username", username)
            sharedPreferencesServices!!.setString("password", password)
            val map = HashMap<String, Any>()
            map["client_id"] = "MOBrBDS8blbauoSck0ZfDbtuzpyT"
            map["client_secret"] = "lsACyCD94FhDUtGTXi3QzcFE2uU1hqtDaKeqrdwj"
            map["grant_type"] = "password"
            map["username"] = username!!
            map["password"] = password!!
            if (sharedPreferencesServices!!.getString("Device_token") != null) {
                map["device_token"] = sharedPreferencesServices!!.getString("Device_token")
            } else map["device_token"] = "pixiv"

            map["get_secure_url"] = true
            map["include_policy"] = true

            oAuthSecureService.postAuthToken(map).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<PixivOAuthResponse> {
                    override fun onSubscribe(d: Disposable) {
                        Toast.makeText(
                            applicationContext,
                            getString(R.string.try_to_login),
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    override fun onNext(pixivOAuthResponse: PixivOAuthResponse) {
                        val user = pixivOAuthResponse.response.user
                        GlobalScope.launch {
                            AppDataRepository.insertUser(
                                UserEntity(
                                    user.profile_image_urls.px_170x170,
                                    user.id.toLong(),
                                    user.name,
                                    user.mail_address,
                                    user.isIs_premium,
                                    pixivOAuthResponse.response.device_token,
                                    pixivOAuthResponse.response.refresh_token,
                                    "Bearer " + pixivOAuthResponse.response.access_token
                                )
                            )

                            sharedPreferencesServices!!.setBoolean("isnone", false)
                            sharedPreferencesServices!!.setString("username", username)
                            sharedPreferencesServices!!.setString("password", password)

                        }
                    }

                    override fun onError(e: Throwable) {
//                            loginBtn.isClickable = true
                        loginBtn.isEnabled = true

                        textview_help.visibility = View.VISIBLE
                        if (e is HttpException) {
                            try {
                                val errorBody = e.response()?.errorBody()?.string()
                                val gson = Gson()
                                val errorResponse = gson.fromJson<ErrorResponse>(
                                    errorBody,
                                    ErrorResponse::class.java
                                )

                                var errMsg = "${e.message}\n${errorResponse.errors.system.message}"

                                if (errorResponse.has_error && errorResponse.errors.system.message.contains(
                                        Regex(""".*103:.*""")
                                    )
                                ) {
                                    errMsg = getString(R.string.error_invalid_account_password)
                                }

                                Toast.makeText(applicationContext, errMsg, Toast.LENGTH_LONG).show()
                            } catch (e1: IOException) {
                                Toast.makeText(
                                    applicationContext,
                                    "${e.message}",
                                    Toast.LENGTH_LONG
                                ).show()
                            }

                        } else {
                            Toast.makeText(applicationContext, "${e.message}", Toast.LENGTH_LONG)
                                .show()
                        }
                    }

                    override fun onComplete() {
//                            loginBtn.isClickable = true
//                            loginBtn.isEnabled = true // Avoid double logins.

                        Toast.makeText(applicationContext, "登录成功", Toast.LENGTH_LONG).show()
                        val intent = Intent(this@LoginActivity, HelloMActivity::class.java).apply {
                            // 避免循环添加账号导致相同页面嵌套。或者在添加账号（登录）成功时回到账号列表页面而不是导航至新的主页
                            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK // Or launchMode = "singleTop|singleTask"
                        }
                        startActivity(intent)
//                        finish()
                    }
                })
        }
        Works.checkUpdate(this)
    }

    fun showHelp(view: View) {
//        val intent = Intent(this@LoginActivity, NewUserActivity::class.java)
//        startActivity(intent)
        Toasty.info(this, this.resources.getString(R.string.registerclose), Toast.LENGTH_LONG)
            .show()
    }
}
