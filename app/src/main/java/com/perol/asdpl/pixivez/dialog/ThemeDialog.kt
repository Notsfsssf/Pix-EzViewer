package com.perol.asdpl.pixivez.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.google.android.material.switchmaterial.SwitchMaterial
import com.perol.asdpl.pixivez.R
import com.perol.asdpl.pixivez.adapters.ColorfulAdapter
import com.perol.asdpl.pixivez.networks.SharedPreferencesServices
import io.multimoon.colorful.Colorful
import io.multimoon.colorful.CustomThemeColor
import io.multimoon.colorful.ThemeColor
import io.multimoon.colorful.ThemeColorInterface

class ThemeDialog : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val list = ArrayList<ThemeColorInterface>().also {
                val myCustomColor1 = CustomThemeColor(
                        requireActivity(),
                        R.style.bili_primary_color,
                        R.style.bili_primary_dark_color,
                        R.color.pink, // <= use the color you defined in my_custom_primary_color
                        R.color.pink // <= use the color you defined in my_custom_primary_dark_color
                )
                val myCustomColor2 = CustomThemeColor(
                        requireActivity(),
                        R.style.blue_primary_color,
                        R.style.blue_primary_dark_color,
                        R.color.blue, // <= use the color you defined in my_custom_primary_color
                        R.color.blue // <= use the color you defined in my_custom_primary_dark_color
                )
                it += ThemeColor.BLUE
                it += ThemeColor.AMBER
                it += ThemeColor.GREEN
                it += ThemeColor.PINK
                it += ThemeColor.PURPLE
                it += ThemeColor.BLUE_GREY
                it += ThemeColor.ORANGE
                it += ThemeColor.RED
                it += ThemeColor.TEAL
                it += ThemeColor.LIGHT_BLUE
                it += ThemeColor.LIGHT_GREEN
                it += myCustomColor1
                it += myCustomColor2
            }
            val inflater = requireActivity().layoutInflater;
            val view = inflater.inflate(R.layout.dialog_theme, null)
            val switchMaterial = view.findViewById<SwitchMaterial>(R.id.switchmaterial)
            switchMaterial.apply {
                isChecked = Colorful().getDarkTheme()
                setOnCheckedChangeListener { compoundButton, b ->
                    Colorful().edit().setDarkTheme(isChecked).apply(requireActivity()).apply {
                        requireActivity().recreate()
                    }
                }
            }
            val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerview)
            recyclerView.apply {
                layoutManager = GridLayoutManager(requireActivity(), 4)
                adapter = ColorfulAdapter(R.layout.view_colorfulitem, list).apply {
                    onItemClickListener = BaseQuickAdapter.OnItemClickListener { adapter, view, position ->
                        SharedPreferencesServices.getInstance().setInt("colornum", position)
                        Colorful().edit().setPrimaryColor(list[position]).apply(requireActivity()) {
                            requireActivity().recreate()
                        }
                    }
                }
            }
            val builder = AlertDialog.Builder(it)
                    .setView(view)
            builder.setMessage("Theme")

            // Create the AlertDialog object and return it
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

}