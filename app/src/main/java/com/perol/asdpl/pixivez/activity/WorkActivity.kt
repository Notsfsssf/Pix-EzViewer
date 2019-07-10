package com.perol.asdpl.pixivez.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.work.*
import com.google.android.material.snackbar.Snackbar
import com.perol.asdpl.pixivez.R
import com.perol.asdpl.pixivez.repository.RetrofitRespository
import com.perol.asdpl.pixivez.services.Works
import com.perol.asdpl.pixivez.worker.DownLoadWorker
import kotlinx.android.synthetic.main.activity_work.*
import org.jetbrains.anko.toast

class WorkActivity : AppCompatActivity() {
    var nextUrl: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_work)
        setSupportActionBar(toolbar)
        val id = intent.getLongExtra("id", 1L)
        if (id == 1L) {
            finish()
            return
        }

        fab.setOnClickListener { view ->
            val retrofit = RetrofitRespository.getInstance()
            retrofit.getLikeIllust(id, "public", null).subscribe({
                nextUrl = it.next_url
                it.illusts.forEach { illust ->
                    if (illust.meta_pages.isNotEmpty()) {
                        for (i in illust.meta_pages.indices) {
                            val imageurl = illust.meta_pages[i].image_urlsX.original
                            Works.ImageDownloadBack(imageurl, illust.user.id.toString(), illust.id.toString(), i.toString(),
                                    if (imageurl.contains("png")) {
                                        ".png"
                                    } else {
                                        ".jpg"
                                    })
                        }
                    } else {
                        Works.ImageDownloadBack(illust.meta_single_page.original_image_url, illust.user.id.toString(), illust.id.toString(), null, if (illust.meta_single_page.original_image_url.contains("png")) {
                            ".png"
                        } else {
                            ".jpg"
                        })
                    }
                }

            }, {}, {
                prometheus(this)
            })

        }
        Log.d("first", "noblocking")


    }

    fun prometheus(lifecycleOwner: LifecycleOwner) {
        if (nextUrl != null) {
            val inputData = Data.Builder()
                    .putString("nexturl", nextUrl)
                    .build()
            val constraints = Constraints.Builder().build()
            val request = OneTimeWorkRequestBuilder<DownLoadWorker>().setConstraints(constraints)
                    .setInputData(inputData).build()
            WorkManager.getInstance(this).enqueue(request)
            WorkManager.getInstance(this).getWorkInfoByIdLiveData(request.id)
                    .observe(lifecycleOwner, Observer { workInfo ->
                        if (workInfo != null && workInfo.state == WorkInfo.State.SUCCEEDED) {
                            val outputData = workInfo.outputData
                            nextUrl = outputData.getString("nexturl")
                            prometheus(lifecycleOwner)
                        }
                    })
        } else {
            toast("www").show()
        }
    }

}
