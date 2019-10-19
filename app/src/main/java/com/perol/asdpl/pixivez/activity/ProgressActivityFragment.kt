package com.perol.asdpl.pixivez.activity

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.perol.asdpl.pixivez.R
import kotlinx.android.synthetic.main.fragment_progress.*


/**
 * A placeholder fragment containing a simple view.
 */
class ProgressActivityFragment : Fragment() {
    class ProgressAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        lateinit var mContext: Context;
        private var data = ArrayList<WorkInfo>()
        public fun setNewData(works: ArrayList<WorkInfo>) {
            data.clear()
            data.addAll(works)
            notifyDataSetChanged()
        }

        fun addData(workInfo: WorkInfo) {
            data.add(workInfo)
            notifyDataSetChanged()
        }

        class ProgressViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            mContext = parent.context;
            val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.view_progress_item, parent, false)

            return ProgressViewHolder(view)
        }

        override fun getItemCount() = data.size


        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            if (holder is ProgressViewHolder) {
                val workinfo = data[position]
                val max = data[position].progress.getLong("max", 100)
                val now = data[position].progress.getLong("now", 0)
                val file = data[position].progress.getString("file")
                holder.itemView.setOnClickListener {
                    val bundle = Bundle()
                    val arrayList = LongArray(1)

                    arrayList[0] = (file!!.split(".")[0].toLong())
                    bundle.putLongArray("illustlist", arrayList)
                    bundle.putLong("illustid", file.split(".")[0].toLong())
                    val intent2 = Intent(mContext, PictureActivity::class.java)
                    intent2.putExtras(bundle)
                    mContext.startActivity(intent2)
                }

                holder.itemView.setOnLongClickListener {
                    val choice = arrayOf("RETRY", "CANCEL")
                    AlertDialog.Builder(mContext).setItems(choice
                    ) { _, which ->
                        when (which) {
                            1 -> {
                                WorkManager.getInstance(mContext).cancelWorkById(workinfo.id)
                            }
                            else -> {

                            }
                        }
                    }.create().show()
                    true
                }
                val progressBar = holder.itemView.findViewById<ProgressBar>(R.id.progress)
                val title = holder.itemView.findViewById<TextView>(R.id.title)
                val progressNum = holder.itemView.findViewById<TextView>(R.id.progress_num)
                val progressState = holder.itemView.findViewById<TextView>(R.id.progress_state)
                Log.d("progress!", now.toString())
                progressBar.max = max.toInt()
                progressBar.progress = now.toInt()
                title.text = file
                val progressText = "$now/$max"
                progressNum.text = progressText
                progressState.text = workinfo.state.toString()
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
        val allData = WorkManager.getInstance(activity!!).getWorkInfosByTagLiveData("image")

        allData.observe(this, Observer {
            if (it != null && it.size > 0) {
                progressAdapter.setNewData(ArrayList(it))
            }
        })

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_progress, container, false)
    }

}

