package com.perol.asdpl.pixivez.dialog

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.perol.asdpl.pixivez.networks.SharedPreferencesServices

class FirstInfoDialog : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val normalDialog = MaterialAlertDialogBuilder(it)
            normalDialog.setMessage(
                "0.请务必确保在google play或者项目地址内安装与更新,第三方提供的安装包可能存在问题且不是最新\n1.在图片详情页长按可以保存选定图片，长按头像快速关注作者，请提供应用权限\n2.浏览动图时，点击中间0%进度条开始下载,完毕播放后长按进行合成保存，合成过程内存开销相当之大,偶发崩溃不可避免\n3.若播放动图无法播放,请退出页面或者清除缓存后重试,这一般会起作用\n" +
                        "4.这是一个个人开发的应用,反馈请发邮件到设置页标注的邮箱,个人精力和能力是有限的,请不要使用极端方式进行反馈,体谅开发者,也欢迎共同开发设计\n5.遇到更新后闪退的问题,请尝试清除应用数据并更新到最新版,将提示错误信息或日志反馈给开发者,多数情况下这是有效的\n6.限制总开关在官网里，遇到无权限访问的插画，自行至网页开启，开发者不提供帮助服务，开发者并不是老好人"
            )
            normalDialog.setTitle("请务必读完")
            normalDialog.setPositiveButton(
                "我已知晓"
            ) { _, _ ->
                SharedPreferencesServices.getInstance().setBoolean("firstinfo", true)
            }
            normalDialog.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}