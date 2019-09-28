package com.perol.asdpl.pixivez.activity

import android.content.Intent
import android.nfc.Tag
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.perol.asdpl.pixivez.R
import com.perol.asdpl.pixivez.adapters.TagsAdapter
import com.perol.asdpl.pixivez.adapters.TagsTextAdapter
import com.perol.asdpl.pixivez.responses.Tags
import com.perol.asdpl.pixivez.viewmodel.TagsTextViewModel
import kotlinx.android.synthetic.main.fragment_search_r.*

/**
 * A placeholder fragment containing a simple view.
 */
class SearchRActivityFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_search_r, container, false)
    }

    lateinit var tagsTextViewModel: TagsTextViewModel
    lateinit var tagsTextAdapter: TagsTextAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tagsTextAdapter = TagsTextAdapter(R.layout.tagstext_item)
        recyclerview.layoutManager = LinearLayoutManager(activity)
        recyclerview.adapter = tagsTextAdapter
        tagsTextAdapter.setOnItemClickListener { adapter, view, position ->
            tagsTextViewModel.addhistory(tags[position].name)
            val bundle = Bundle()
            bundle.putString("searchword", tags[position].name)
            val intent = Intent(activity!!, SearchResultActivity::class.java)
            intent.putExtras(bundle)
            startActivityForResult(intent, 775)
        }
    }

    val tags = ArrayList<Tags>()
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        tagsTextViewModel = ViewModelProviders.of(activity!!).get(TagsTextViewModel::class.java)
        tagsTextViewModel.tags.observe(this, Observer {
            tagsTextAdapter.setNewData(it)
            tags.clear()
            tags.addAll(it)
        })

    }
}
