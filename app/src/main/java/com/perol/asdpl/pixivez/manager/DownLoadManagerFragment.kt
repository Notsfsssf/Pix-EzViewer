package com.perol.asdpl.pixivez.manager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.arialyy.aria.core.Aria
import com.arialyy.aria.core.common.AbsEntity
import com.facebook.litho.*
import com.facebook.litho.annotations.*
import com.facebook.litho.annotations.State
import com.facebook.litho.sections.Children
import com.facebook.litho.sections.SectionContext
import com.facebook.litho.sections.annotations.GroupSectionSpec
import com.facebook.litho.sections.annotations.OnCreateChildren
import com.facebook.litho.sections.common.SingleComponentSection
import com.facebook.litho.sections.widget.ListRecyclerConfiguration
import com.facebook.litho.sections.widget.RecyclerCollectionComponent
import com.facebook.litho.widget.Text
import com.google.gson.Gson
import com.perol.asdpl.pixivez.responses.Illust

class DownLoadManagerFragment : Fragment() {

    companion object {
        fun newInstance() = DownLoadManagerFragment()
    }

    private lateinit var viewModel: DownLoadManagerViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val data = Aria.download(this).taskList
        val c = ComponentContext(requireContext())
        return LithoView.create(
            requireContext(), RecyclerCollectionComponent.create(c)
                .section(AriaItemSection.create(SectionContext(c)).d(data).build())
                .recyclerConfiguration(ListRecyclerConfiguration.create().build()).build()
        )
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(DownLoadManagerViewModel::class.java)
    }


}
