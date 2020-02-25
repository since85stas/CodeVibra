package ru.batura.stat.codevibra

import android.widget.EditText
import androidx.databinding.BindingAdapter
import ru.batura.stat.codevibra.ui.main.MainViewModel

/*
    добавляем слушателя дял получения текста из EditText
 */
@BindingAdapter("addEditTextWatcherDec")
fun bindEditTextDec(editText: EditText, model : MainViewModel) {
    val string = editText.text.toString()
    model.decimalTextWatch.setNumber(editText.text.toString().toInt())
    editText.addTextChangedListener(model.decimalTextWatch)
}

/*
    добавляем слушателя дял получения текста из EditText
 */
@BindingAdapter("addEditTextWatcherBin")
fun bindEditTextBinary(editText: EditText, model : MainViewModel) {
    val string = editText.text.toString()
    model.binaryTextWatch.setNumber(editText.text.toString().toInt())
    editText.addTextChangedListener(model.binaryTextWatch)
}