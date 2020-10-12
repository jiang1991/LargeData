package com.viatom.largedata

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.hardware.usb.UsbManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.os.storage.StorageManager
import android.util.Log
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.widget.AppCompatSeekBar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.File
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.min

class MainActivity : AppCompatActivity() {

    private val TAG = "large data"

    private val ACTION_USB_PERMISSION = "ACTION_USB_PERMISSION"

    private lateinit var dataSet: ByteArray
    private lateinit var recycleView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private var viewManager: RecyclerView.LayoutManager =
        LinearLayoutManager(this)

    lateinit var seekBar: AppCompatSeekBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        loadData()

        loadUsb()
    }

    private fun loadData() {
        val assetManager = resources.assets
        val data = assetManager.open("14m.BIN")
        dataSet = data.readBytes()

        // 一小时数据 60*60*128/2*3
        val hourSize: Int = 60*60*128/2*3
        val part : Int = ceil(dataSet.size / hourSize.toDouble()).toInt()
        Log.d("zhixin","readBytes: ${dataSet.size}  part: $part")

        for (i in 0 until part) {
            val fs = ZhixinUtils.byte2Float(dataSet.copyOfRange(i*hourSize, min((i+1)*hourSize, dataSet.size)))
            Log.d("fs", Arrays.toString(fs))
        }

        Log.d("zhixin", "convent finished")
    }

    private fun loadUsb() {

        try {
            val manager: StorageManager = this.getSystemService(Context.STORAGE_SERVICE) as StorageManager
            val mc : Class<StorageManager> = StorageManager::class.java
            val path : Array<String>  = mc.getMethod("getVolumePaths").invoke(manager) as Array<String>

            for (p in path) {
                Log.d(TAG, p)
                val volState = mc.getMethod("getVolumeState", String::class.java).invoke(manager, p)
                if (Environment.MEDIA_MOUNTED == volState) {
                    val fileRoot : File = File(p)
                    for (file in fileRoot.listFiles()) {
                        Log.d(TAG, file.path)

                        if (file.path == "/storage/5227-C03C/1.BIN") {
                            val bytes: ByteArray = file.readBytes()
                            Log.d(TAG, bytes.size.toString())
                        }
                    }
                }
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
}
