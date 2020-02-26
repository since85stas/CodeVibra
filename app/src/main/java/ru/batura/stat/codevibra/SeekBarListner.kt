package ru.batura.stat.codevibra

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