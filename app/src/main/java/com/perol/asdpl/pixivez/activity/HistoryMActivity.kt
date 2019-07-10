package com.perol.asdpl.pixivez.activity

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.perol.asdpl.pixivez.R
import com.perol.asdpl.pixivez.adapters.HistoryAdapter
import com.perol.asdpl.pixivez.databinding.ActivityHistoryMBinding
import com.perol.asdpl.pixivez.networks.SharedPreferencesServices
import com.perol.asdpl.pixivez.objects.ThemeUtil
import com.perol.asdpl.pixivez.sql.IllustBeanEntity
import com.perol.asdpl.pixivez.viewmodel.HistoryMViewModel
import kotlinx.android.synthetic.main.activity_history_m.*


class HistoryMActivity : RinkActivity() {
    lateinit var activityHistoryMBinding: ActivityHistoryMBinding
    var historyMViewModel: HistoryMViewModel? = null
    lateinit var sharedPreferencesServices: SharedPreferencesServices
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ThemeUtil.Themeinit(this)
        activityHistoryMBinding = DataBindingUtil.setContentView(this, R.layout.activity_history_m)
        sharedPreferencesServices = SharedPreferencesServices.getInstance()
        initview()
        initdata()
        initbind()
    }

    private fun initbind() {
        fab.setOnClickListener {
            historyMViewModel!!.fabOnClick()
        }
        historyAdapter.setOnItemChildClickListener { adapter, view, position ->
            val bundle = Bundle()
            val arrayList = LongArray(1)
            arrayList[0]=(historyMViewModel!!.illustBeans.value!![position].illustid)
            bundle.putLongArray("illustlist", arrayList)
            bundle.putLong("illustid",historyMViewModel!!.illustBeans.value!![position].illustid)
            val intent2 = Intent(applicationContext, PictureActivity::class.java)
            intent2.putExtras(bundle)
            startActivity(intent2)
        }
    }

    private fun initdata() {
        historyMViewModel = ViewModelProviders.of(this).get(HistoryMViewModel::class.java)

        historyMViewModel!!.illustBeans.observe(this, Observer {
            illustBeans(it)
        })
        historyMViewModel!!.first()

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()

        }
        return super.onOptionsItemSelected(item)
    }
    val historyAdapter = HistoryAdapter(R.layout.view_recommand_itemh)
    private fun illustBeans(it: ArrayList<IllustBeanEntity>?) {
        historyAdapter.setNewData(it)
    }

    private fun initview() {
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        recyclerview_historym.layoutManager =     GridLayoutManager(this,2,RecyclerView.VERTICAL,false)
        recyclerview_historym.adapter = historyAdapter
        recyclerview_historym.smoothScrollToPosition(historyAdapter.data.size)

    }
}
