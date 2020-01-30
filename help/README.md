# PixEz 阅读器 常见问题（FAQ）

下面是我们收集的一些 ~~99% 加群的有问题的普通用户会提出的~~ 常见问题，如果你在使用时遇到了问题，我们建议你先仔细查看下方是否有你面临的问题，并采取对应的解决方案。

## 1. 为什么第一次一打开 / 刚登录就白屏 + “程序出错，即将退出”？

应用使用了 Android App Bundle 来分发（具体原理看[这里](https://developer.android.com/guide/app-bundle)）；简单的来说，对于不同的设备，Google Play 会在下载时分发一个主 APK + 若干个资源 APK，以减少下载体积。

你（你的朋友）可能属于以下任一情形：

- 你从 Play 商店下载的本应用，又使用（包括但不限于）快传、QQ 应用分享、安装包提取、应用还原等各种方式得到了一个 APK，分享给了别人
- 你在 X 宝 / X 鱼 / X 安 上通过某种方式下载到了一个 APK
- 你用某个备份软件 / 系统自带的备份对本应用进行了备份还原

在以上的操作中，你得到的只有一个 Google Play 分发的主 APK，而缺少了资源 APK，所以软件自然不能正常运行了。

**解决方法：**

去 [GitHub 本项目 Release 页面](https://github.com/Notsfsssf/Pix-EzViewer/releases) 下载完整 APK。你也可以卸载残缺应用之后从 Google Play 重新安装一次。**本应用是免费应用，如果您付费购买，请立即请求退款并打差评！**

如果你是分享者 且 你的朋友没有访问 Google Play 的能力，请分享 Release 页面的 APK，或者让你的朋友到这里来自己下载。

## 2. 登录时提示 `http 400 bad request`

请尝试以下步骤：

1. 确保应用是最新的，去 [GitHub 本项目主页](https://github.com/Notsfsssf/Pix-EzViewer/) 下载最新版本。
2. 已是最新版，则用户名或者密码必定有错，请仔细核对

PS：用户名、密码指的是 pixiv 的账号密码，而不是 GitHub 的

## 3. 为什么有的图看不了？

一般有这些情况：

1. 原画师把图删了 / 设为隐藏了；

2. 图是 R18 的，但是你账号没开查看 R18。

   例如这样：

   ![未解锁](https://raw.githubusercontent.com/Notsfsssf/Pix-EzViewer/master/help/Not-Unlocked.jpg)

**解决方法：**

对于情况 1：我们表示爱莫能助… 建议利用搜索引擎的快照等尝试恢复。

对于情况 2：在 [网页端](https://pixiv.net)（电脑或手机）登录 pixiv，找到「用户设置」-「浏览限制」，将「限制浏览的作品（R-18）」改为「显示作品」，保存设置之后在本应用内再试。

## 没有帮助？

如果你的问题并不在这篇 FAQ 里，你可以提 Issue / 发邮件 / 加群联系开发者寻求帮助。

反馈前请确认：

- 你从 Google Play 安装本应用，或使用 GitHub Release 页上的 APK（关联本页问题 2）

- 你并未使用尝鲜版系统（如 MIUI 内测版等），且你并未对系统进行大幅度魔改

  （包括但不限于 Xposed 模块、Magisk 模块等等）

- 你已仔细阅读了应用内的“请务必读完”

联系方式具体，请参见 [README](/README.md) 页的「交流反馈」部分。

