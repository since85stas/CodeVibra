package ru.batura.stat.codevibra.ui.main

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.text.SpannableStringBuilder
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.main_fragment.*
import ru.batura.stat.codevibra.R
import ru.batura.stat.codevibra.createVibrationPattern
import ru.batura.stat.codevibra.databinding.MainFragmentBinding

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

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
                createVibrate( it )
            }
        })
        super.onActivityCreated(savedInstanceState)
    }

    /**
     *  в зависимости от того на какой editText нажал пользователь
     *  добавляет слушатель в нажатый и убирает из другого
     */
    fun changeTextWatchers(viewId : Int) {
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
    fun createVibrate(num : Int) {
        val vibrator = this.activity!!.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        val canVibrate: Boolean = vibrator.hasVibrator()

        // если вибрация доступна
        if (canVibrate) {
            // создаем рисунок вибрации
            val pattern = createVibrationPattern(num)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                // API 26
                vibrator.vibrate(
                    VibrationEffect.createWaveform(
                        pattern,
                        VibrationEffect.DEFAULT_AMPLITUDE
                    )
                )
            } else {
                // This method was deprecated in API level 26
                vibrator.vibrate(pattern,0)
            }
        }
        viewModel.vibrateFinish()
    }



}
