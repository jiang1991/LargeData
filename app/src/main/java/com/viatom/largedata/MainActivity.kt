package com.viatom.largedata

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.widget.AppCompatSeekBar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private val TAG = "large data"

    private lateinit var dataSet: ByteArray
    private lateinit var recycleView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private var viewManager: RecyclerView.LayoutManager =
        LinearLayoutManager(this)

    lateinit var seekBar: AppCompatSeekBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loadData()

//        val indexArray = Array(5) {
//            i -> i
//        }

        viewAdapter = MyAdapter(this)

        recycleView = findViewById<RecyclerView>(R.id.recycle_view).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }

        seekBar = findViewById<AppCompatSeekBar>(R.id.progress_bar)
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val p = (progress / 100.toDouble() * DataController.getPages()).toInt()
                recycleView.scrollToPosition(p)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                Log.d(TAG, "onStartTrackingTouch: {${seekBar?.progress}}")
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                val percent = (seekBar?.progress?.div(100.toDouble())).toString()
                Toast.makeText(this@MainActivity, percent, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun loadData() {
        val assetManager = resources.assets
        val data = assetManager.open("R20200227225237")
        dataSet = data.readBytes()
        dataSet = dataSet.copyOfRange(10, dataSet.size - 20)
        val intArray = DataConvert.unCompressAlgECG(dataSet)

        DataController.viewData = FloatArray(intArray.size)
        for (i in intArray.indices) {
            DataController.viewData!![i] = (intArray[i] * (1.0035 * 1800) / (4096 * 178.74)).toFloat()
        }
        Log.d(TAG, "File R20200227225237 size: " + dataSet.size)
    }
}
