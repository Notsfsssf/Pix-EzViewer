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

import android.content.Context
import android.content.Intent
import android.media.MediaScannerConnection
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.work.*
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.perol.asdpl.pixivez.R
import com.perol.asdpl.pixivez.objects.Toasty
import com.perol.asdpl.pixivez.services.PxEZApp
import com.perol.asdpl.pixivez.works.ImgDownLoadWorker
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.fragment_progress.*
import kotlinx.android.synthetic.main.view_progress_item.*
import java.io.File

/**
 * A placeholder fragment containing a simple view.
 */
class ProgressActivityFragment : Fragment() {
    inner class ProgressAdapter() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        lateinit var mContext: Context;
        private var data = ArrayList<WorkInfo>()
        fun setNewData(works: ArrayList<WorkInfo>) {
            data = works
            notifyDataSetChanged()
        }

        fun addData(workInfo: WorkInfo) {
            data.add(workInfo)
            notifyDataSetChanged()
        }


        inner class ProgressViewHolder(override val containerView: View) :
            RecyclerView.ViewHolder(containerView), LayoutContainer {


        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            mContext = parent.context;
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.view_progress_item, parent, false)

            return ProgressViewHolder(view)
        }

        override fun getItemCount() = data.size

        override fun onBindViewHolder(
            holder: RecyclerView.ViewHolder,
            position: Int,
            payloads: MutableList<Any>
        ) {

            if (payloads.isEmpty()) {
                onBindViewHolder(holder, position)
            } else {
                if (holder is ProgressViewHolder) {
                    val workinfo = data[position]
                    val max = data[position].progress.getLong("max", 100)
                    val now = data[position].progress.getLong("now", 0)
                    val id = data[position].progress.getLong("id", 0)
                    val title1 = data[position].progress.getString("title")
                    holder.apply {
                        progress.max = max.toInt()
                        progress.progress = now.toInt()
                        title.text = title1
                        val progressText = if (workinfo.state == WorkInfo.State.SUCCEEDED) {
                            "completed"
                        } else {
                            "$now/$max"
                        }
                        progress_num.text = progressText
                        progress_state.text = workinfo.state.toString()

                    }


                }
            }
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

            if (holder is ProgressViewHolder) {
                val workInfo = data[position]
                val max = data[position].progress.getLong("max", 100)
                val now = data[position].progress.getLong("now", 0)
                val id = data[position].progress.getLong("id", 0)
                val title1 = data[position].progress.getString("title")
                val url = data[position].progress.getString("url")
                val fileName = data[position].progress.getString("file")
                val userId = data[position].progress.getString("user_id")
                val needCreateFold = data[position].progress.getBoolean("needcreatefold", false)
                val userName = data[position].progress.getString("username")
                holder.apply {
                    itemView.setOnClickListener {
                        val bundle = Bundle()
                        val arrayList = LongArray(1)
                        arrayList[0] = id
                        bundle.putLongArray("illustlist", arrayList)
                        bundle.putLong("illustid", id)
                        val intent2 = Intent(mContext, PictureActivity::class.java)
                        intent2.putExtras(bundle)
                        mContext.startActivity(intent2)
                    }
                    itemView.setOnLongClickListener {
                        val choice = arrayOf("RETRY", "CANCEL")
                        MaterialAlertDialogBuilder(mContext).setItems(
                            choice
                        ) { _, which ->
                            when (which) {
                                1 -> {
                                    WorkManager.getInstance(mContext).cancelWorkById(workInfo.id)
                                }
                                else -> {

                                    val inputData = workDataOf(
                                        "file" to fileName,
                                        "url" to url,
                                        "title" to title1,
                                        "id" to id,
                                        "username" to userName,
                                        "needcreatefold" to needCreateFold,
                                        "user_id" to userId
                                    )
                                    val oneTimeWorkRequest =
                                        OneTimeWorkRequestBuilder<ImgDownLoadWorker>()
                                            .setInputData(inputData)
                                            .addTag("image")
                                            .build()
                                    WorkManager.getInstance(PxEZApp.instance).enqueueUniqueWork(
                                        url!!,
                                        ExistingWorkPolicy.REPLACE,
                                        oneTimeWorkRequest
                                    )


                                    WorkManager.getInstance(PxEZApp.instance)
                                        .getWorkInfoByIdLiveData(oneTimeWorkRequest.id)
                                        .observeForever(object : Observer<WorkInfo> {
                                            override fun onChanged(workInfo: WorkInfo?) {
                                                if (workInfo == null) {
                                                    return
                                                }
                                                if (workInfo.state == WorkInfo.State.SUCCEEDED) {
                                                    if (workInfo.outputData.getBoolean(
                                                            "exist",
                                                            false
                                                        )
                                                    ) {
                                                        Toasty.success(
                                                            PxEZApp.instance,
                                                            PxEZApp.instance.resources.getString(R.string.alreadysaved),
                                                            Toast.LENGTH_SHORT
                                                        ).show()
                                                        WorkManager.getInstance(PxEZApp.instance)
                                                            .getWorkInfoByIdLiveData(
                                                                oneTimeWorkRequest.id
                                                            ).removeObserver(this)
                                                        return
                                                    }
                                                    Toasty.success(
                                                        PxEZApp.instance,
                                                        PxEZApp.instance.resources.getString(R.string.savesuccess),
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                    val path = workInfo.outputData.getString("path")
                                                    if (path != null)
                                                        MediaScannerConnection.scanFile(
                                                            PxEZApp.instance,
                                                            arrayOf(path),
                                                            arrayOf(
                                                                MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                                                                    File(path).extension
                                                                )
                                                            )
                                                        ) { _, _ ->
                                                        }
                                                } else if (workInfo.state == WorkInfo.State.FAILED) {

                                                } else if (workInfo.state == WorkInfo.State.CANCELLED) {

                                                    val file = File(PxEZApp.storepath, fileName)
                                                    file.delete()
                                                }
                                                WorkManager.getInstance(PxEZApp.instance)
                                                    .getWorkInfoByIdLiveData(oneTimeWorkRequest.id)
                                                    .removeObserver(this)
                                            }
                                        })
                                }
                            }
                        }.create().show()
                        true
                    }
                    Log.d("progress!", now.toString())
                    progress.max = max.toInt()
                    progress.progress = now.toInt()
                    title.text = title1
                    val progressText = if (workInfo.state == WorkInfo.State.SUCCEEDED) {
                        "completed"
                    } else {
                        "$now/$max"
                    }
                    progress_num.text = progressText
                    progress_state.text = workInfo.state.toString()

                }

//                WorkManager.getInstance(mContext).getWorkInfoByIdLiveData(workinfo.id).observe(this@ProgressActivityFragment, Observer {
//                    this.itemChange(position, it)
//                })
            }
        }

    }

    val progressAdapter = ProgressAdapter()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerview.apply {
            layoutManager = LinearLayoutManager(activity!!)
            adapter = progressAdapter
        }
        WorkManager.getInstance(activity!!).getWorkInfosByTagLiveData("image")
            .observe(viewLifecycleOwner, Observer {
                it.sortByDescending {
                    it.progress.getString("title")
                }
                progressAdapter.setNewData(ArrayList(it))
            })
        fab.setOnClickListener {
            val builder = MaterialAlertDialogBuilder(activity)
            builder.setTitle("Clean all?")
                .setPositiveButton(android.R.string.ok) { i, v ->
                    WorkManager.getInstance(PxEZApp.instance).pruneWork()
                }
                .setNegativeButton(android.R.string.cancel) { i, v ->

                }
            val dialog = builder.create()
            dialog.show()

        }

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_progress, container, false)
    }

}

