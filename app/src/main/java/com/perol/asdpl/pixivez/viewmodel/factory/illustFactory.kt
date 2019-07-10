package com.perol.asdpl.pixivez.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.perol.asdpl.pixivez.viewmodel.IllustfragmentViewModel

class illustFactory: ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return IllustfragmentViewModel() as T
    }
}

