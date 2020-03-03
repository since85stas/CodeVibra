package ru.batura.stat.codevibra

import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.RadioGroup
import android.widget.SeekBar
import ru.batura.stat.codevibra.ui.main.MainViewModel

class TempSeekBarListner (val model: MainViewModel): SeekBar.OnSeekBarChangeListener {

    var value =0

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        value = progress
        model.updateSeekVal()
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {
    }

    override fun onStopTrackingTouch(seekBar: SeekBar?) {
    }
}

class LongSeekBarListner (val model: MainViewModel): SeekBar.OnSeekBarChangeListener {

    var value =0

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        value = progress
        model.updateSeekVal()
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {
    }

    override fun onStopTrackingTouch(seekBar: SeekBar?) {
    }
}

class CheckListner (val model: MainViewModel) : CompoundButton.OnCheckedChangeListener {

    override fun onCheckedChanged( buttonView: CompoundButton?, isChecked: Boolean) {
        val id = buttonView!!.id
        when (id) {
            R.id.cycle_check -> {
                if ( isChecked ) {
                    model.isCycleOn = 1
                } else {
                    model.isCycleOn = -1
                }
            }
            R.id.sound_check -> {
                if ( isChecked ) {
                    model.isSoundOn = true
                } else {
                    model.isSoundOn = false
                }
            }
        }
    }
}