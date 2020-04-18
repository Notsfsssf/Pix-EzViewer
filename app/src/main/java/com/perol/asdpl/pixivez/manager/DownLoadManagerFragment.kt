package com.perol.asdpl.pixivez.manager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.list.listItems
import com.arialyy.annotations.Download
import com.arialyy.aria.core.Aria
import com.arialyy.aria.core.download.DownloadEntity
import com.arialyy.aria.core.task.DownloadTask
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.perol.asdpl.pixivez.R
import com.perol.asdpl.pixivez.activity.PictureActivity
import com.perol.asdpl.pixivez.databinding.DownLoadManagerFragmentBinding
import com.perol.asdpl.pixivez.databinding.ItemDownloadTaskBinding
import com.perol.asdpl.pixivez.services.IllustD
import org.jetbrains.annotations.NotNull


class DownloadTaskAdapter() :
    BaseQuickAdapter<DownloadEntity, BaseViewHolder>(R.layout.item_download_task) {
    init {
        this.setOnItemClickListener { adapter, view, position ->
            val item = data[position]
            val illust = objectMapper.readValue(item.str, IllustD::class.java)
            PictureActivity.startSingle(context, id = illust.id)
        }
        this.setOnItemLongClickListener { adapter, view, position ->
            val item = data[position]
            val illust = objectMapper.readValue(item.str, IllustD::class.java)
            MaterialDialog(context).show {
                title(text = illust.title)
                listItems(R.array.download_task_choice) { _, index, string ->
                    when (index) {
                        0 -> {
                            Aria.download(context).load(data[position].id).stop()

                        }
                        1 -> {
                            Aria.download(context).load(data[position].id).resume()
                        }
                        2 -> {
                            Aria.download(context).load(data[position].id).cancel(true)
                        }
                    }
                }
            }
            true
        }

    }


    override fun onItemViewHolderCreated(
        @NotNull viewHolder: BaseViewHolder,
        viewType: Int
    ) { // 绑定 view
        DataBindingUtil.bind<ItemDownloadTaskBinding>(viewHolder.itemView)
    }

    private fun Int.toIEntityString(): String {
        return when (this) {
            0 -> {
                "FAIL"
            }
            1 -> "COMPLETE"
            2 -> "STOP"
            3 -> "WAIT"
            4 -> "RUNNING"
            5 -> {
                "PRE"
            }
            6 -> {
                "POST_PRE"
            }
            7 -> {
                "CANCEL"
            }
            else -> {
                "OTHER"
            }
        }
    }

    val objectMapper = ObjectMapper().registerKotlinModule()
    override fun convert(helper: BaseViewHolder, item: DownloadEntity) {
        val binding = helper.getBinding<ItemDownloadTaskBinding>()!!
        val progress = helper.getView<ProgressBar>(R.id.progress)
        progress.max = item.fileSize.toInt()
        progress.progress = item.currentProgress.toInt()
        binding.progressFont.text = "${item.currentProgress.toInt()}/${item.fileSize.toInt()}"
        try {
            val illust = objectMapper.readValue(item.str, IllustD::class.java)
            helper.setText(R.id.title, illust.title)
            helper.setText(R.id.status, item.state.toIEntityString())
        } catch (e: Exception) {

        }

    }


}

class DownLoadManagerFragment : Fragment() {


    fun refreshSingle(task: DownloadTask) {
        task?.let {
            var index = -1
            for (i in downloadTaskAdapter.data.indices) {
                if (downloadTaskAdapter.data[i].id == task.downloadEntity.id) {
                    index = i
                    break
                }
            }

            if (index != -1) {
                downloadTaskAdapter.setData(index, it.entity)
            }
        }
    }

    @Download.onTaskPre
    fun onTaskPre(task: DownloadTask) {
        refreshSingle(task)
    }

    @Download.onTaskStop
    fun onTaskStop(task: DownloadTask) {
        refreshSingle(task)
    }

    @Download.onTaskCancel
    fun onTaskCancel(task: DownloadTask) {
        task?.let {
            var index = -1
            for (i in downloadTaskAdapter.data.indices) {
                if (downloadTaskAdapter.data[i].id == task.downloadEntity.id) {
                    index = i
                    break
                }
            }

            if (index != -1) {
                downloadTaskAdapter.remove(index)
            }
        }
    }

    @Download.onTaskRunning
    fun onTaskRunning(task: DownloadTask) {
        refreshSingle(task)
    }

    @Download.onTaskComplete
    fun onTaskComplete(task: DownloadTask) {
        refreshSingle(task)
    }

    @Download.onTaskResume
    fun onTaskResume(task: DownloadTask) {
        refreshSingle(task)
    }

    @Download.onTaskStart
    fun onTaskStart(task: DownloadTask) {
        refreshSingle(task)
    }

    @Download.onTaskFail
    fun onTaskFail(task: DownloadTask) {
        refreshSingle(task)
    }
    companion object {
        fun newInstance() = DownLoadManagerFragment()
    }

    override fun onDestroy() {
        Aria.download(this).unRegister()
        super.onDestroy()

    }

    private lateinit var viewModel: DownLoadManagerViewModel
    lateinit var binding: DownLoadManagerFragmentBinding
    lateinit var downloadTaskAdapter: DownloadTaskAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Aria.download(this).register()
        downloadTaskAdapter = DownloadTaskAdapter()
        binding = DownLoadManagerFragmentBinding.inflate(inflater)
        binding.progressList.apply {
            adapter = downloadTaskAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(DownLoadManagerViewModel::class.java)

    }

    override fun onResume() {
        super.onResume()
        val taskList = Aria.download(this).taskList
        downloadTaskAdapter.setNewData(taskList)
    }

}
