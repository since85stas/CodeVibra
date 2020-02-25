package ru.batura.stat.codevibra

import android.view.View
import ru.batura.stat.codevibra.ui.main.MainViewModel

class FocusListner (val model:MainViewModel) : View.OnFocusChangeListener {

    var focus : Int = -1

    override fun onFocusChange(v: View?, hasFocus: Boolean) {
        print ("on focus changed")
        if (hasFocus) {
            when (v!!.id) {
                R.id.edit_binary -> {
                    print("binary")
                    model.changeFocus(v.id)
                    focus = v.id
                }

                R.id.edit_decimal -> {
                    print("decimal")
                    model.changeFocus(v.id)
                    focus = v.id
                }
            }
        }
    }


}