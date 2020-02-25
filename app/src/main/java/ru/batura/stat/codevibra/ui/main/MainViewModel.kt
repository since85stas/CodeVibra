package ru.batura.stat.codevibra.ui.main

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.batura.stat.codevibra.BinaryTextWatcher
import ru.batura.stat.codevibra.DecimalTextWatcher
import ru.batura.stat.codevibra.FocusListner
import ru.batura.stat.codevibra.R

class MainViewModel : ViewModel() {

    // листнеры для отслеживания изменения вводимого текста
    val binaryTextWatch = BinaryTextWatcher(this)
    val decimalTextWatch = DecimalTextWatcher(this)

    // liveData для числовых значений
    var decimalLive : MutableLiveData<Int> = MutableLiveData()
    var binaryLive : MutableLiveData<Int> = MutableLiveData()

    var focusListner: FocusListner = FocusListner(this)

    private var _focusChanged : MutableLiveData<Int> = MutableLiveData()
    val focusChanged : LiveData<Int>
        get() = _focusChanged

    init {
        print("init view model")
    }

    fun startButtonClicked() {
        print("start clicked")
    }


    /**
     * изменяет значение целого поля
     */
    fun uptadeDecimLive() {
        decimalLive.value = decimalTextWatch.number
    }

    /**
     * изменяет значение бинарного поля
     */
    fun uptadeBinarLive() {
        binaryLive.value  = binaryTextWatch.number
    }

    /**
     * изменяет значения фокуса
     */
    fun changeFocus(id: Int) {
        _focusChanged.value = id
    }
}


