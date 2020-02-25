package ru.batura.stat.codevibra

import kotlin.math.pow

fun createVibrationPattern ( num:Int) : LongArray {
    val pattern = longArrayOf(500, 300, 400, 800)
    val xTemp = 0.5
    val xDl = 0.5

    val TEN : Double = 10.toDouble()

    val tPause : Double = (0.125) * TEN.pow(xTemp)

    val tVibr: Double = tPause * xDl

    val tAftVibrPause : Double = tPause*(1-xDl)

    val longList : MutableList<Long> = ArrayList<Long> ()
    val strings  = num.toString(2).split("")
    for (number in strings) {
        if (number.equals("0")) {
            longList.add(tPause.toLong())
        } else if ( number.equals("1")) {

        } else {
            print("wront timing")
        }
    }
    return pattern
}