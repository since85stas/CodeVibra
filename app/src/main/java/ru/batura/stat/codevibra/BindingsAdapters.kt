package ru.batura.stat.codevibra

import android.widget.EditText
import androidx.databinding.BindingAdapter
import ru.batura.stat.codevibra.ui.main.MainViewModel

/**
  *  добавляем  начальное значение из  EditText в слушатель
 */
@BindingAdapter("addEditTextWatcherDec")
fun bindEditTextDec(editText: EditText, model : MainViewModel) {
    val string = editText.text.toString()
    model.decimalTextWatch.setNumber(editText.text.toString().toInt())
//    editText.addTextChangedListener(model.decimalTextWatch)
}

/**
 *   добавляем  начальное значение из  EditText в слушатель
 */
@BindingAdapter("addEditTextWatcherBin")
fun bindEditTextBinary(editText: EditText, model : MainViewModel) {
    val string = editText.text.toString()
    model.binaryTextWatch.setNumber(editText.text.toString().toInt())
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