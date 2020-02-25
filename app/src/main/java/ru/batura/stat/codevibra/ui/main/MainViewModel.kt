package ru.batura.stat.codevibra.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.batura.stat.codevibra.BinaryTextWatcher
import ru.batura.stat.codevibra.DecimalTextWatcher

class MainViewModel : ViewModel() {

    val binaryTextWatch = BinaryTextWatcher(this)
    val decimalTextWatch = DecimalTextWatcher(this)

    var decimalLive : MutableLiveData<Int> = MutableLiveData()
    var binaryLive : MutableLiveData<Int> = MutableLiveData()

    init {
//        decimalLive.value = decimalTextWatch.number
//        binaryLive.value  = binaryTextWatch.number
        print("init view model")
    }

    fun startButtonClicked() {
        print("start clicked")
    }

    fun fromIntToBinary(num : Int) : String {

        return ""
    }

    fun uptadeLive() {
        decimalLive.value = decimalTextWatch.number
//        binaryLive.value  = binaryTextWatch.number
    }
}


