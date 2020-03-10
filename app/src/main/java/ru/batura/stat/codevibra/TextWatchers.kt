package ru.batura.stat.codevibra

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.lifecycle.ViewModel
import ru.batura.stat.codevibra.ui.main.MainViewModel
import java.lang.Exception

public val DECIMAL_ID = 1123

public val BINARY_ID = 1122

/*
    класс для получения текста из EditText простейший вариант
    без особых наваротов, для нормального использования надо доработать
 */
class DecimalTextWatcher (val model : MainViewModel) : TextWatcher {



    var _number: Long = 0

    fun setNumber( string: Long) {
        _number = string
    }

    val number: Long
        get() = _number

    override fun afterTextChanged(s: Editable?) {
//        model.uptadeDecimLive()
        model.binaryTextWatch.setNumber(model.decimalTextWatch.number)
        model.createNumbRespresentation(DECIMAL_ID)
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        if ( s!!.isNotEmpty()) {
            try {
                _number = s.toString().toLong()
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



    var _number: Long = 0

    fun setNumber( string: Long) {
        _number = string
    }

    val number: Long
        get() = _number

    override fun afterTextChanged(s: Editable?) {
//        model.uptadeBinarLive()
        if (_number != -111L) {
            model.decimalTextWatch.setNumber(model.binaryTextWatch.number)
        }
        model.createNumbRespresentation(BINARY_ID)
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        if ( s!!.isNotEmpty()) {
            try {
                _number = s.toString().toLong(2)
            } catch (e: Exception) {
                Log.d("TextW", " Wrong number")
                _number = -111;
            }
        } else {
            _number = 0
        }
    }

}