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
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.main_fragment.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import ru.batura.stat.codevibra.R
import ru.batura.stat.codevibra.createVibrationPattern
import ru.batura.stat.codevibra.databinding.MainFragmentBinding
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

    private var soundPool: SoundPool? = null

    private var soundId: Int = 0

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

        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)

        bindings.viewModel = viewModel

        return bindings.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        // наблюдаем за изменением целого значения
        viewModel.decimalLive.observe(viewLifecycleOwner, Observer {
            edit_binary.text = SpannableStringBuilder(it.toString(2))
        })

        // наблюдаем за изменением бинарного значения
        viewModel.binaryLive.observe(viewLifecycleOwner, Observer {
            edit_decimal.text = SpannableStringBuilder(it.toString())
        })

        // наблюдаем за изменением фокуса
        viewModel.focusChanged.observe(viewLifecycleOwner, Observer {
            changeTextWatchers(it)
        })

        // наблюдаем за включением вибрации
        viewModel.startV.observe(viewLifecycleOwner, Observer {
            if (it > 0) {
                createVibrate( it,
                    viewModel.seekTempLive.value!!,
                    viewModel.seekLongLive.value!!
                    )
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
                edit_decimal.removeTextChangedListener(viewModel.decimalTextWatch)
            }

            R.id.edit_decimal -> {
                print("decimal")
                edit_decimal.addTextChangedListener(viewModel.decimalTextWatch)
                edit_binary.removeTextChangedListener(viewModel.binaryTextWatch)
            }
        }
    }

    /**
     * запускаем вибрацию с нужой каритной
     */
    private fun createVibrate(num : Int, tempProgress : Int, longProgress : Int) {
        val vibrator = this.activity!!.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        val canVibrate: Boolean = vibrator.hasVibrator()



        playSound()

        // если вибрация доступна
        if (canVibrate) {
            // создаем рисунок вибрации
            val pattern = createVibrationPattern(num, tempProgress, longProgress)
            val longitude = pattern.sum()
            val timerObj = Timer()
            val timerTaskObj: TimerTask = object : TimerTask() {
                override fun run() {
                    uiScope.launch {
                        stopVibrating()
                    }
                }
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                // API 26
                vibrator.vibrate(
                    VibrationEffect.createWaveform(
                        pattern,
                        VibrationEffect.DEFAULT_AMPLITUDE
                    )
                )
                timerObj.schedule(timerTaskObj, longitude)
            } else {
                // This method was deprecated in API level 26
                vibrator.vibrate(pattern,0)
                timerObj.schedule(timerTaskObj, longitude)
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
    fun playSound() {
        soundPool!!.play(
            soundId, 1f,1f,0,0,1f
        )

    }

    /**
     * создает строку для записи
     */
    private fun getTitleString (id : Int, progress : Int) : String{
        when (id) {
            R.id.temp_title -> return "Temp $progress"
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
    }

    private fun stopVibrating() {
        start_button.visibility = View.VISIBLE
        stop_button.visibility  = View.GONE
        viewModel.vibrateFinish()
    }
}
