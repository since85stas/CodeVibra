package ru.batura.stat.codevibra.ui.main

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.batura.stat.codevibra.*
import java.util.*

class MainViewModel : ViewModel() {

    // листнеры для отслеживания изменения вводимого текста
    val binaryTextWatch = BinaryTextWatcher(this)
    val decimalTextWatch = DecimalTextWatcher(this)

    // liveData для числовых значений
    var decimalLive : MutableLiveData<Int> = MutableLiveData()
    var binaryLive : MutableLiveData<Int> = MutableLiveData()

    // листнеры для отслеживания ползунков
    var tempListner = TempSeekBarListner(this)
    var longListner = LongSeekBarListner(this)

    //
    var seekTempLive : MutableLiveData<Int> = MutableLiveData()
    var seekLongLive : MutableLiveData<Int> = MutableLiveData()

    // добавляем листнер на Focus
    var focusListner: FocusListner = FocusListner(this)

    private var _focusChanged : MutableLiveData<Int> = MutableLiveData()
    val focusChanged : LiveData<Int>
        get() = _focusChanged

    private var _startV : MutableLiveData<Int> = MutableLiveData()
    val startV : LiveData<Int>
        get() = _startV

    init {
        print("init view model")
    }

    fun startButtonClicked() {
        print("start clicked")
        when (focusListner.focus) {
            R.id.edit_binary -> {
                _startV.value = binaryLive.value
            }

            R.id.edit_decimal -> {
                _startV.value = decimalLive.value
            }
        }
    }

    fun vibrateFinish() {
        _startV.value = -99
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

    fun updateSeekVal() {
        seekTempLive.value = tempListner.value
        seekLongLive.value = longListner.value
    }
}


