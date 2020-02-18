package com.perol.asdpl.pixivez.fragments


import android.annotation.SuppressLint
import android.os.Bundle
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.chip.Chip
import com.perol.asdpl.pixivez.R
import com.perol.asdpl.pixivez.objects.AdapterRefreshEvent
import com.perol.asdpl.pixivez.sql.entity.BlockTagEntity
import com.perol.asdpl.pixivez.viewmodel.BlockViewModel
import kotlinx.android.synthetic.main.activity_block.*
import kotlinx.android.synthetic.main.fragment_block_tag.*
import kotlinx.coroutines.runBlocking
import org.greenrobot.eventbus.EventBus

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [BlockTagFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class BlockTagFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var viewModel: BlockViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        viewModel = ViewModelProvider(this).get(BlockViewModel::class.java)
    }

    @SuppressLint("SetTextI18n")
    private fun getChip(blockTagEntity: BlockTagEntity): Chip {
        val chip = Chip(requireContext())
        val paddingDp = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, 5f,
            resources.displayMetrics
        ).toInt()
        chip.text = "${blockTagEntity.name} ${blockTagEntity.translateName}"

        chip.setOnLongClickListener {
            runBlocking {
                viewModel.deleteSingleTag(blockTagEntity)
                getTagList()
            }
            EventBus.getDefault().post(AdapterRefreshEvent())
            true
        }
        return chip
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_block_tag, container, false)
    }

    override fun onStart() {
        super.onStart()
        getTagList()
    }

    private fun getTagList() {
        runBlocking {
            val it = viewModel.getAllTags()
            chipgroup.removeAllViews()
            it.forEach { v ->
                chipgroup.addView(getChip(v))
            }
        }

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment BlockTagFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            BlockTagFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
