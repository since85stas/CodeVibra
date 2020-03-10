package ru.batura.stat.codevibra.ui.main

import android.app.Application
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextWatcher
import android.text.style.ForegroundColorSpan
import androidx.core.text.bold
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
import kotlin.collections.ArrayList

class MainViewModel(val application: Application) : ViewModel(), ChessStateChageListner {

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
    var decimalLive: MutableLiveData<Long> = MutableLiveData()
    var binaryLive: MutableLiveData<Long> = MutableLiveData()

    var numberRepresentation: String = ""

    // листнеры для отслеживания ползунков
    var tempListner = TempSeekBarListner(this)
    var longListner = LongSeekBarListner(this)

    //
    var checkListner = CheckListner(this)

    //
    var seekTempLive: MutableLiveData<Int> = MutableLiveData()
    var seekLongLive: MutableLiveData<Int> = MutableLiveData()

    // добавляем листнер на Focus
    var focusListner: FocusListner = FocusListner(this)

    // добавляем focusChanged
    private var _focusChanged: MutableLiveData<Int> = MutableLiveData()
    val focusChanged: LiveData<Int>
        get() = _focusChanged

    //
    private var _startV: MutableLiveData<String> = MutableLiveData()
    val startV: LiveData<String>
        get() = _startV

    private var _stopV: MutableLiveData<Boolean> = MutableLiveData()
    val stopV: LiveData<Boolean>
        get() = _stopV

    private var _isStartPlay: MutableLiveData<Boolean> = MutableLiveData(false)
    val isStartPlay: LiveData<Boolean>
        get() = _isStartPlay

    private var _textAnimation: MutableLiveData<SpannableStringBuilder> = MutableLiveData()
    val textAnimation: LiveData<SpannableStringBuilder>
        get() = _textAnimation

    var soundClock: ChessClockRx? = null

    var isSoundOn = false

    var isCycleOn = -1
    var isCycleOnTemp = -1

    init {
        print("init view model")
//        createVibrate(1,1,1)
    }

    fun startButtonClicked() {
        print("start clicked")
//        when (focusListner.focus) {
//            R.id.edit_binary -> {
//                _startV.value = binaryLive.value
//            }
//            R.id.edit_decimal -> {
//                _startV.value = decimalLive.value
//            }
//        }
        _startV.value = numberRepresentation
//        createAnimationText(2)
    }

    /**
     *
     */
    fun createNumbRespresentation(textWatchId : Int) {

        if (binaryTextWatch.number == decimalTextWatch.number) {
            numberRepresentation = decimalTextWatch.number.toString(2)
        }  else if ( binaryTextWatch.number  == (-111L) || decimalTextWatch.number == (-111L)) {
            numberRepresentation = "NO"
            print("minus representation")
        }  else {
            print("wrong representation")
        }
        when (textWatchId) {
            BINARY_ID -> {
                if (numberRepresentation != "NO") {
                    if (decimalLive.value != decimalTextWatch.number) {
                        decimalLive.value = decimalTextWatch.number
                    }
                } else {
                    decimalLive.value = 0
                }
            }
            DECIMAL_ID -> {
                if ( binaryLive.value != binaryTextWatch.number ) {
                    binaryLive.value = binaryTextWatch.number
                }
            }
        }


    }

    /**
     * при нажатии на кнопку стоп
     */
    fun stopButtonClicked() {
        _stopV.value = true
        isCycleOnTemp = isCycleOn
        isCycleOn = 0
        print("stop clicked")
    }

    /**
     * запускается после окончания вибрации тут же отключаем звук
     */
    fun vibrateFinish() {
        if (isCycleOn == -1) {
            _startV.value = "STOP"
            _stopV.value = false
        }
        if (isCycleOn == 0) {
            _startV.value = "STOP"
            _stopV.value = false

//            soundClock!!.stopTimer()
            isCycleOn = isCycleOnTemp
        } else if (isCycleOn == 1) {
            startButtonClicked()
        }
//        _isStartPlay.value = false
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
        binaryLive.value = binaryTextWatch.number
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

    fun startClock(pattern: LongArray, interval: Long) {
        soundClock = ChessClockRx(this, pattern, interval)
    }

    fun startClockBold(pattern: LongArray, interval: Long) {
        soundClock = ChessClockRx(this, pattern, interval)
    }

    override fun timeChange(time: Long) {
        print("time=$time")
    }

    override fun timeFinish() {
        print("time finish")
    }

    override fun nextInterval(value: Boolean) {
        uiScope.launch {
            _isStartPlay.value = value
//            createAnimationText()
        }
    }

    override fun setBold(position: Int) {
//        if (position == 2)
        uiScope.launch {
            createAnimationText(position)
        }
    }

    fun createAnimationText(boldPos: Int) {
        val string = decimalTextWatch.number.toString(2)
        val stringBuilder = SpannableStringBuilder()
        if (string.length > 1) {
            if (boldPos == 0) {
                stringBuilder.bold { append(string.elementAt(boldPos).toString()) }
                    .append(string.substring(boldPos + 1, string.length))
            } else if (boldPos == string.length - 1) {
                stringBuilder
                    .append(string.substring(0, boldPos))
                    .bold { append(string.elementAt(boldPos)) }
            } else {
                stringBuilder.append((string.substring(0, boldPos).toString()))
                stringBuilder.bold { append(string.elementAt(boldPos).toString()) }
                stringBuilder.append(string.substring(boldPos + 1, string.length).toString())
            }
        }
//        val spannable : Spannable = stringBuilder
//                    .setSpan(ForegroundColorSpan(Color.GREEN),
//                0,
//                2,
//                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        _textAnimation.value = stringBuilder
    }

}


