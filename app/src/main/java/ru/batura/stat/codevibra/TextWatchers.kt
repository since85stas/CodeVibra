package ru.batura.stat.codevibra

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.lifecycle.ViewModel
import ru.batura.stat.codevibra.ui.main.MainViewModel
import java.lang.Exception

/*
    класс для получения текста из EditText простейший вариант
    без особых наваротов, для нормального использования надо доработать
 */
class DecimalTextWatcher (val model : MainViewModel) : TextWatcher {

    var _number:Int = 0

    fun setNumber( string: Int) {
        _number = string
    }

    val number:Int
        get() = _number

    override fun afterTextChanged(s: Editable?) {
        model.uptadeDecimLive()
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        if ( s!!.isNotEmpty()) {
            try {
                _number = s.toString().toInt()
            } catch (e: Exception) {
                Log.d("TextW", " Wrong number")
            }
        } else {
            _number = 0
        }
    }

}


/*
    класс для получения текста из EditText простейший вариант
    без особых наваротов, для нормального использования надо доработать
 */
class BinaryTextWatcher (val model : MainViewModel) : TextWatcher {

    var _number:Int = 0

    fun setNumber( string: Int) {
        _number = string
    }

    val number:Int
        get() = _number

    override fun afterTextChanged(s: Editable?) {
        model.uptadeBinarLive()
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        if ( s!!.isNotEmpty()) {
            try {
                _number = s.toString().toInt(2)
            } catch (e: Exception) {
                Log.d("TextW", " Wrong number")
            }
        } else {
            _number = 0
        }
    }

}