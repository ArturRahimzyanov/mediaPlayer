package com.example.mediaplayer

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Email
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.graphics.vector.PathData
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mediaplayer.databinding.ActivityMainBinding
import com.vk.api.sdk.VK
import com.vk.api.sdk.auth.VKAccessToken
import com.vk.api.sdk.auth.VKAuthCallback
import com.vk.api.sdk.auth.VKScope



const val TAG: String = "logs"
const val access_token = ""


class MainActivity : AppCompatActivity(), VKAuthCallback {
    lateinit var binding: ActivityMainBinding
    lateinit var adapter: AudioListAdapter
    lateinit var manager: LinearLayoutManager
    private val audioList = mutableListOf<AudioData>()
    private var isRecording = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        VK.initialize(this)
        setContentView(binding.root)
        checkPermissions()
        initEnterfragment()
    }

    fun toast(ms: String) {
        Toast.makeText(this, ms, Toast.LENGTH_LONG).show()
    }

    private inline fun FragmentManager.inTransaction(func: FragmentTransaction.() -> Unit) {
        val fragmentTransaction = beginTransaction()
        fragmentTransaction.func()
        fragmentTransaction.commit()
    }

    private fun initEnterfragment() {
        supportFragmentManager.inTransaction {
            //add(R.id.fragment_container, EnterFragment())
        }

        binding.mainbtn.setOnClickListener {
            val scopes = listOf(VKScope.EMAIL)
            VK.login(this, scopes)
        }
    }

    private fun checkPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) !=
            PackageManager.PERMISSION_GRANTED
        ) {
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

    override fun onLogin(token: VKAccessToken) {
        toast("norm")
    }

    override fun onLoginFailed(errorCode: Int) {
        toast("$errorCode ne poluchilos")
    }

}




