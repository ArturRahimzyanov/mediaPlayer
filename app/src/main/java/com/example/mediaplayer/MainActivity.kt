package com.example.mediaplayer

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mediaplayer.databinding.ActivityMainBinding

const val TAG: String = "logs"

class MainActivity : AppCompatActivity() {
    lateinit var binding : ActivityMainBinding


    lateinit var adapter: AudioListAdapter
    lateinit var manager: LinearLayoutManager
    private val audioList = mutableListOf<AudioData>()
    private var isRecording = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        checkPermissions()
        initEnterfragment()
    }


    private inline fun FragmentManager.inTransaction(func: FragmentTransaction.() -> Unit ){
        val fragmentTransaction = beginTransaction()
        fragmentTransaction.func()
        fragmentTransaction.commit()
    }
    private fun initEnterfragment() {
        supportFragmentManager.inTransaction {
            //add(R.id.fragment_container, EnterFragment())
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


    companion object {
        const val REQUEST_CODE = 111
    }
}