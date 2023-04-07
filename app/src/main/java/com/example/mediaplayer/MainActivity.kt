package com.example.mediaplayer

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mediaplayer.databinding.ActivityMainBinding

const val TAG = "logs"

class MainActivity : AppCompatActivity() {
    lateinit var binding : ActivityMainBinding

    lateinit var adapter: AudioListAdapter
    lateinit var manager: LinearLayoutManager
    private val audioList = mutableListOf<AudioData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        checkPermissions()
        listennersInit()
        recyclerInit()
        initAudioFromCash()
    }

    private fun initAudioFromCash() {
        this.cacheDir.listFiles()?.map {
            val tmpList = it.path.substringAfterLast("/").split(".")
            audioList.add(AudioData(tmpList[1].toInt(), tmpList[2], tmpList[3], tmpList[4] ))
        }

        if(!audioList.isEmpty()) {
            Log.d(TAG, "${audioList.reversed() as MutableList<AudioData> }")
            val reversedlist: MutableList<AudioData> = audioList.reversed() as MutableList<AudioData>
            adapter.setupAudio( reversedlist )
            binding.recycler.addItemDecoration(DividerItemDecoration(this, manager.orientation))
        }
    }

    private fun listennersInit() {
        binding.startWriteBtn.setOnClickListener {
            adapter.writeAudio()
        }
        binding.startWriteBtn2.setOnClickListener {
            adapter.stopWrite()
        }
    }

    private fun checkPermissions() {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.RECORD_AUDIO),
                REQUEST_CODE
            )
        }
    }


    private fun recyclerInit() {
        manager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        adapter = AudioListAdapter(this)
        binding.recycler.adapter = adapter
        binding.recycler.layoutManager = manager
    }

    companion object {
        const val REQUEST_CODE = 111
    }
}