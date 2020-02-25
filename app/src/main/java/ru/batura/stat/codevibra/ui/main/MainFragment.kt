package ru.batura.stat.codevibra.ui.main

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.text.SpannableStringBuilder
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.main_fragment.*
import ru.batura.stat.codevibra.R
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

}
