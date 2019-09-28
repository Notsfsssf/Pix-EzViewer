package com.perol.asdpl.pixivez.viewmodel

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

open class BaseViewModel : ViewModel(), LifecycleObserver {
    private val viewModelJob = SupervisorJob()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
     val disposables = CompositeDisposable()
    fun launchUI( block: suspend CoroutineScope.() -> Unit) {
        try {
            uiScope.launch(Dispatchers.Main) {
                block()
            }
        }catch (e:Exception){
            e.printStackTrace()
        }
    }
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
        disposables.clear()
    }
}