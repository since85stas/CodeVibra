package ru.batura.stat.codevibra.ui.main

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import ru.batura.stat.codevibra.*
import ru.batura.stat.codevibra.ChessClockRx.ChessClockRx
import ru.batura.stat.codevibra.ChessClockRx.ChessStateChageListner
import java.util.*

class MainViewModel : ViewModel() , ChessStateChageListner {

    /**
     * viewModelJob allows us to cancel all coroutines started by this ViewModel.
     */
    private var viewModelJob = Job()

    /**
     * A [CoroutineScope] keeps track of all coroutines started by this ViewModel.
     *
     * Because we pass it [viewModelJob], any coroutine started in this uiScope can be cancelled
     * by calling `viewModelJob.cancel()`
     *
     * By default, all coroutines started in uiScope will launch in [Dispatchers.Main] which is
     * the main thread on Android. This is a sensible default because most coroutines started by
     * a [ViewModel] update the UI after performing some processing.
     */
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

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
    var checkListner = CheckListner(this)

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

    private var _stopV : MutableLiveData<Boolean> = MutableLiveData()
    val stopV : LiveData<Boolean>
        get() = _stopV

    private var _isStartPlay : MutableLiveData<Boolean> = MutableLiveData(true)
    val isStartPlay : LiveData<Boolean>
        get() = _isStartPlay

    var soundClock : ChessClockRx? = null

    public var isSoundOn = false

    public var isCycleOn = false

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

    /**
     * при нажатии на кнопку стоп
     */
    fun stopButtonClicked() {
        _stopV.value = true
        print("stop clicked")
    }

    /**
     * запускается после окончания вибрации тут же отключаем звук
     */
    fun vibrateFinish() {
        _startV.value = -99
        _stopV.value = false
        soundClock!!.stopTimer()
        _isStartPlay.value = true
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

    fun startClock( pattern : LongArray) {
        soundClock = ChessClockRx(this,pattern)
    }

    override fun timeChange(time: Long) {
        print("time=$time")
    }

    override fun timeFinish() {
        print("time finish")
    }

    override fun nextInterval() {
        uiScope.launch {
            if (!_isStartPlay.value!!) {
                _isStartPlay.value = true
            } else {
                _isStartPlay.value = false
            }
        }
    }
}


