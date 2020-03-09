package ru.batura.stat.codevibra

import java.util.*
import kotlin.math.pow

fun createVibrationPattern ( num:Int, tempProgress : Int , longProgress : Int) : LongArray {

    // временные величины
    val xTemp = tempProgress /100.0
    val xDl = longProgress /100.0

    val TEN : Double = 10.toDouble()

    val tPause : Long = ((0.125) * TEN.pow( 4 - xTemp)).toLong()

    val tVibr: Long = (tPause * xDl).toLong()

    val tAftVibrPause : Long = (tPause*(1-xDl)).toLong()

    val longList  = LinkedList<Long> ()
    val strings  = num.toString(2).toCharArray()
//    val strings  = "1011".toCharArray()


    for (i  in strings.indices) {
        if ( i == 0) {
            if (strings[i] == '0') {
                longList.add(tPause)
            } else {
                longList.add(1)
                longList.add(tVibr)
                longList.add(tAftVibrPause)
            }
        } else {
            if ( strings[i] == '0' ) {
                    val last = longList.last()
                    val newVal = last + tPause
                    longList.removeLast()
                    longList.add(newVal)
            } else {
                longList.add(tVibr)
                longList.add(tAftVibrPause)
            }
        }
    }

    class TempUtils () {

    }

    return longList.toLongArray()
}


fun getIntervalLenght(num:Int, tempProgress : Int , longProgress : Int) : Long{
    val xTemp = tempProgress /100.0
    val xDl = longProgress /100.0
    val TEN : Double = 10.toDouble()
    val tPause : Long = ((0.125) * TEN.pow( 4 - xTemp)).toLong()
    return tPause
}


fun getGetTempValue(tempProgress : Int , longProgress : Int) : Int{
    // временные величины
    val xTemp = tempProgress /100.0
    val xDl = longProgress /100.0

    val TEN : Double = 10.toDouble()

    val tPause : Long = ((0.125) * TEN.pow( 4 - xTemp)).toLong()
    return 60000/tPause.toInt()
}