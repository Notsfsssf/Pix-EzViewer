package com.perol.asdpl.pixivez.objects

import androidx.fragment.app.Fragment

abstract class LazyFragment : Fragment() {
    private var isLoaded = false
    override fun onResume() {
        super.onResume()
        if (!isLoaded) {
            isLoaded = true
            loadData()
        }
    }

    protected abstract fun loadData()
}