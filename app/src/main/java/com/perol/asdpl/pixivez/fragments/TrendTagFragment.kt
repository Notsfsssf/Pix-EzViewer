package com.perol.asdpl.pixivez.fragments

import android.content.Intent
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.marginTop
import androidx.core.view.setPadding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.chip.Chip
import com.perol.asdpl.pixivez.R
import com.perol.asdpl.pixivez.activity.SearchResultActivity
import com.perol.asdpl.pixivez.adapters.TrendingTagAdapter
import com.perol.asdpl.pixivez.sql.SearchHistoryEntity
import kotlinx.android.synthetic.main.trend_tag_fragment.*


class TrendTagFragment : Fragment() {

    companion object {
        fun newInstance() = TrendTagFragment()
    }

    private lateinit var viewModel: TrendTagViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.trend_tag_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        textView_clearn.setOnClickListener {

            viewModel.sethis()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(TrendTagViewModel::class.java)
        viewModel.getIllustTrendTags().subscribe({
            if (it != null) {
                recyclerview_searhm.layoutManager = StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
                val trendingtagAdapter = TrendingTagAdapter(R.layout.view_trendingtag_item, it.trend_tags)
                recyclerview_searhm.adapter = trendingtagAdapter
                recyclerview_searhm.isNestedScrollingEnabled = false
                trendingtagAdapter.setOnItemClickListener { adapter, view, position ->
                    val searchword = it.trend_tags[position].tag
                    upToPage(searchword)
                    viewModel.addhistory(searchword)
                }
            }
        }, {})
        viewModel.searchhistroy.observe(this, Observer { it ->

            val arrayList = ArrayList<String>()
            chipgroup.removeAllViews()
            it.map {
                arrayList.add(it.word)
                chipgroup.addView(getChip(it))
            }

            if (arrayList.isNotEmpty()) textView_clearn.visibility = View.VISIBLE
            else textView_clearn.visibility = View.GONE


        })
    }

    private fun getChip(searchHistoryEntity: SearchHistoryEntity): Chip {
        val chip = Chip(activity)
        val paddingDp = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 5f,
                resources.displayMetrics
        ).toInt()
        chip.text = searchHistoryEntity.word
        chip.setOnClickListener {
            upToPage(searchHistoryEntity.word)
        }
        chip.setOnLongClickListener {
            viewModel.deleteHistoryEntity(searchHistoryEntity).subscribe({
                viewModel.resethistory()
            },{

            },{

            })
            true
        }
        return chip
    }

    private fun upToPage(query: String) {
        val bundle = Bundle()
        bundle.putString("searchword", query)
        val intent = Intent(activity!!, SearchResultActivity::class.java)
        intent.putExtras(bundle)
        startActivityForResult(intent, 775)
    }
}
