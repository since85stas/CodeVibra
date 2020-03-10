package ru.batura.stat.codevibra.ui.main

import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.text.bold
import androidx.core.text.set
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.main_fragment.*
import kotlinx.coroutines.*
import ru.batura.stat.codevibra.R
import ru.batura.stat.codevibra.createVibrationPattern
import ru.batura.stat.codevibra.databinding.MainFragmentBinding
import ru.batura.stat.codevibra.getGetTempValue
import ru.batura.stat.codevibra.getIntervalLenght
import java.time.Duration
import java.util.*


class MainFragment : Fragment(), SoundPool.OnLoadCompleteListener {

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
    private val ioScope = CoroutineScope(Dispatchers.IO + viewModelJob)

    private var soundPool: SoundPool? = null

    private var soundId: Int = 0

    var timerObj : Timer? = null

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        createSoundPool()

        val bindings : MainFragmentBinding= DataBindingUtil.inflate(
            inflater,
            R.layout.main_fragment,
            container,
            false
        )

        val factory = MainFragmentViewModelFactory(requireActivity().application)

        viewModel = ViewModelProviders.of(this, factory).get(MainViewModel::class.java)

        bindings.viewModel = viewModel

        return bindings.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        // наблюдаем за изменением целого значения
        viewModel.decimalLive.observe(viewLifecycleOwner, Observer {
            edit_decimal.text = SpannableStringBuilder(it.toString())
//            viewModel.uptadeBinarLive()
        })

        // наблюдаем за изменением бинарного значения
        viewModel.binaryLive.observe(viewLifecycleOwner, Observer {
            edit_binary.text = SpannableStringBuilder(it.toString(2))
//            viewModel.uptadeDecimLive()
        })

        // наблюдаем за изменением фокуса
        viewModel.focusChanged.observe(viewLifecycleOwner, Observer {
            changeTextWatchers(it)
        })

        // наблюдаем за включением вибрации
        viewModel.startV.observe(viewLifecycleOwner, Observer {
            if (it == "STOP") {

            }
            else if (it !=  "NO") {
                createVibrate( it.toLong(2),
                    viewModel.seekTempLive.value!!,
                    viewModel.seekLongLive.value!!
                    )
//                viewModel.createAnimationText(it)
            } else {
                val toast = Toast.makeText(activity ,"Wrong binary format number",Toast.LENGTH_LONG)
                toast.show()
            }
        })

        // наблюдаем за отключение вибрации
        viewModel.stopV.observe(viewLifecycleOwner, Observer {
            if (it) {
                    cancelVibrate()
            }
        })

        // наблюдаем за ползунками
        viewModel.seekTempLive.observe(viewLifecycleOwner, Observer {
            temp_title.text = getTitleString(R.id.temp_title, it)
        })
        viewModel.seekLongLive.observe(viewLifecycleOwner, Observer {
            long_title.text = getTitleString(R.id.long_title, it)
        })

        //
        viewModel.isStartPlay.observe(viewLifecycleOwner, Observer {
            if (viewModel.isSoundOn) {
                uiScope.launch {
                    withContext(Dispatchers.IO) {
                        if (it) {
                            playSound()
                        } else {
                            stopSound()
                        }
                    }
                }
            }
        })

        viewModel.textAnimation.observe(viewLifecycleOwner, Observer {
            text_view_binary.setText(it, TextView.BufferType.SPANNABLE)
        }
        )

        super.onActivityCreated(savedInstanceState)
    }

    /**
     *  в зависимости от того на какой editText нажал пользователь
     *  добавляет слушатель в нажатый и убирает из другого
     */
    private fun changeTextWatchers(viewId : Int) {
        when (viewId) {
            R.id.edit_binary -> {
                print("binary")
                edit_binary.addTextChangedListener(viewModel.binaryTextWatch)
                viewModel.binaryTextWatch.setNumber(viewModel.decimalTextWatch.number)

                edit_decimal.removeTextChangedListener(viewModel.decimalTextWatch)
            }

            R.id.edit_decimal -> {
                print("decimal")
                edit_decimal.addTextChangedListener(viewModel.decimalTextWatch)
                viewModel.decimalTextWatch.setNumber(viewModel.binaryTextWatch.number)

                edit_binary.removeTextChangedListener(viewModel.binaryTextWatch)
            }
        }
    }

    /**
     * запускаем вибрацию с нужой каритной
     */
    private fun createVibrate(num : Long, tempProgress : Int, longProgress : Int) {
        val vibrator = this.activity!!.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        val canVibrate: Boolean = vibrator.hasVibrator()

        // если вибрация доступна
        if (canVibrate) {
            // создаем рисунок вибрации
            val pattern = createVibrationPattern(num, tempProgress, longProgress)

            viewModel.startClockBold(pattern, getIntervalLenght(num, tempProgress, longProgress))

            val longitude = pattern.sum()
            timerObj = Timer()
            val timerTaskObj: TimerTask = object : TimerTask() {
                override fun run() {
                    uiScope.launch {
//                        stopVibrating()
                        cancelVibrate()
                    }
                }
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                // API 26
                vibrator.vibrate(
                    VibrationEffect.createWaveform(
                        pattern,
                        0
                    )
                )

                ioScope.launch {
                    timerObj!!.schedule(timerTaskObj, longitude)
                }
            } else {
                // This method was deprecated in API level 26
                vibrator.vibrate(pattern,0)
                ioScope.launch {
                    timerObj!!.schedule(timerTaskObj, longitude)
                }
            }
        }

        // changing buttons
        isVibrating()
    }

    /**
     * функция для остановки вибрации
     */
    private fun cancelVibrate ()  {
        val vibrator = this.activity!!.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        val canVibrate: Boolean = vibrator.hasVibrator()
        if (canVibrate) vibrator.cancel()
        if (timerObj != null) timerObj!!.cancel()
        uiScope.launch {
            stopVibrating()
        }
    }

    /**
     * создаем саунд пул для звуков
     */
    fun createSoundPool() {
        val attributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_GAME)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()
        soundPool = SoundPool.Builder()
            .setAudioAttributes(attributes)
            .build()

        soundId = soundPool!!.load(this.context!!,R.raw.drum1,1)
    }

    override fun onLoadComplete(soundPool: SoundPool?, sampleId: Int, status: Int) {
        print ("sound loader ID=$sampleId staus=$status")
    }

    /**
     *
     */
    suspend fun playSound() {
        soundPool!!.play(
            soundId, 1f,1f,0,0,1f
        )
    }

    suspend fun stopSound() {
        soundPool!!.stop(soundId)
    }

    /**
     * создает строку для записи
     */
    private fun getTitleString (id : Int, progress : Int) : String{
        when (id) {
            R.id.temp_title -> {
                val res = getGetTempValue( progress , longitude_seek.progress)
                return "Temp $res"
            }
            R.id.long_title -> return "Long $progress"
        }
        return "Title"
    }

    /**
     * при включении вибрации
     */
    private fun isVibrating () {
        start_button.visibility = View.GONE
        stop_button.visibility  = View.VISIBLE

        edit_binary.visibility = View.GONE
        text_view_binary.visibility = View.VISIBLE
    }

    private fun stopVibrating() {
        start_button.visibility = View.VISIBLE
        stop_button.visibility  = View.GONE

        edit_binary.visibility = View.VISIBLE
        text_view_binary.visibility = View.GONE

        viewModel.vibrateFinish()
    }
}
