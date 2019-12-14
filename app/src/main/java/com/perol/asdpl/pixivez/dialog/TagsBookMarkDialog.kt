import android.app.Dialog
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.perol.asdpl.pixivez.R
import com.perol.asdpl.pixivez.adapters.TagsAdapter
import com.perol.asdpl.pixivez.responses.BookMarkDetailResponse
import com.perol.asdpl.pixivez.viewmodel.PictureXViewModel

class TagsBookMarkDialog : DialogFragment() {
    companion object {

    }

    lateinit var recyclerView: RecyclerView
    lateinit var editText: EditText
    lateinit var pictureXViewModel: PictureXViewModel
    lateinit var imageButton: ImageButton

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = MaterialAlertDialogBuilder(it)
            val inflater = requireActivity().layoutInflater
            val view = inflater.inflate(R.layout.dialog_bookmark, null)
            val tagsAdapter = TagsAdapter(R.layout.view_tags_item, null)
            recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView).apply {
                layoutManager = LinearLayoutManager(activity)
                adapter = tagsAdapter
            }
            editText = view.findViewById(R.id.edittext)
            imageButton = view.findViewById(R.id.add)

            imageButton.setOnClickListener {
                if (editText.text.isNotBlank() && pictureXViewModel.tags.value != null) {
                    tagsAdapter.addData(
                        0,
                        BookMarkDetailResponse.BookmarkDetailBean.TagsBean().apply {
                        isIs_registered = true
                        name = editText.text.toString()
                            editText.text.clear()
                    })
                    recyclerView.smoothScrollToPosition(0)
                }
            }
            pictureXViewModel =
                ViewModelProviders.of(requireParentFragment()).get(PictureXViewModel::class.java)
            pictureXViewModel.tags.observe(this, Observer {
                tagsAdapter.setNewData(it.tags)
            })
            pictureXViewModel.fabOnLongClick()
            builder
                .setView(view)
                .setNegativeButton(
                    android.R.string.cancel
                ) { dialog, id ->

                }
                .setPositiveButton(R.string.bookmark_public) { _, _ ->
                    if (pictureXViewModel.tags.value != null)
                        pictureXViewModel.onDialogClick(false)
                }

                .setNeutralButton(R.string.bookmark_private) { _, _ ->
                    if (pictureXViewModel.tags.value != null)
                        pictureXViewModel.onDialogClick(true)
                }
            // Create the AlertDialog object and return it
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}