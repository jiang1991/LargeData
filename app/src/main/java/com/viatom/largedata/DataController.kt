package com.viatom.largedata

import kotlin.math.ceil

object DataController {

    // for list
    var viewData: FloatArray? = null
    const val step: Int = 125*60*2
    @kotlin.jvm.JvmStatic
    fun getPages(): Int {
        if (viewData == null) {
            return 0
        }
        return ceil(viewData!!.size / step.toDouble()).toInt()
    }

    @kotlin.jvm.JvmStatic
    fun getViewDataByIndex(index: Int) : FloatArray? {
        var from: Int = index*step
        var to: Int = (index + 1)*step

        if (viewData == null) {
            return null
        }

        if (from > viewData!!.size) {
            return null
        }

        if (to > viewData!!.size) {
            to = viewData!!.size
        }

        return viewData!!.copyOfRange(from, to)
    }



    // for ecg view
    const val ECG_VIEW_ROW = 6
    const val ECG_VIEW_LINE_MAX = 10

}