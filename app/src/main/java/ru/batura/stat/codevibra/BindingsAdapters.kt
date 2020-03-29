package ru.batura.stat.codevibra

import android.text.Html
import android.widget.CheckBox
import android.widget.EditText
import android.widget.SeekBar
import android.widget.TextView
import androidx.databinding.BindingAdapter
import ru.batura.stat.codevibra.ui.main.MainViewModel

/**
  *  добавляем  начальное значение из  EditText в слушатель
 */
@BindingAdapter("addEditTextWatcherDec")
fun bindEditTextDec(editText: EditText, model : MainViewModel) {
    val string = editText.text.toString()
//    model.decimalTextWatch.setNumber(editText.text.toString().toLong())
//    editText.addTextChangedListener(model.decimalTextWatch)
}

/**
 *   добавляем  начальное значение из  EditText в слушатель
 */
@BindingAdapter("addEditTextWatcherBin")
fun bindEditTextBinary(editText: EditText, model : MainViewModel) {
    val string = editText.text.toString()
//    model.binaryTextWatch.setNumber(editText.text.toString().toLong(2))
//    editText.addTextChangedListener(model.binaryTextWatch)
}

/**
 *   добавляем слушатель на фокус
 */
@BindingAdapter("addFocusListner")
fun bindFocusListner(editText: EditText, model : MainViewModel) {
    print("focus")
    editText.setOnFocusChangeListener(model.focusListner)
}

/**
 *   добавляем слушатель на seekbar
 */
@BindingAdapter("addSeekListner")
fun bindSeekListner(seekBar: SeekBar, model : MainViewModel) {
    print("seek")
    when (seekBar.id) {

        R.id.temp_seek -> {
            model.tempListner.value = seekBar.progress
            model.updateSeekVal()
            seekBar.setOnSeekBarChangeListener(model.tempListner)
        }

        R.id.longitude_seek -> {
            model.longListner.value = seekBar.progress
            model.updateSeekVal()
            seekBar.setOnSeekBarChangeListener(model.longListner)
        }
    }
}

/**
 *   добавляем слушатель на фокус
 */
@BindingAdapter("addCheckListner")
fun bindCheckListner(checkBox: CheckBox, model : MainViewModel) {
    print("focus")
    checkBox.setOnCheckedChangeListener(model.checkListner)
    checkBox.isChecked = true
}



