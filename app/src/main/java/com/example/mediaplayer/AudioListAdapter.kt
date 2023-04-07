package com.example.mediaplayer

import android.content.Context
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.recycler_item.view.*
import java.io.File

class AudioListAdapter(private val context: Context) : RecyclerView.Adapter<AudioListAdapter.AudioListHolder>() {

    private val audioList = mutableListOf<AudioData>()
    private lateinit var recorder: MediaRecorder
    private lateinit var audioFile: File
    private var mediaPlayer: MediaPlayer = MediaPlayer()

    inner class AudioListHolder(item : View) : RecyclerView.ViewHolder(item){
        var nameOfAudio: TextView = item.findViewById(R.id.nameOfAudio)
        var dateOfAudio: TextView = item.findViewById(R.id.dateOfAudio)
        var allTimeOfAudio: TextView = item.findViewById(R.id.allTime)
        var playBtn: ImageButton = item.findViewById(R.id.playBtn)
        var stopBtn: ImageButton = item.findViewById(R.id.stopBtn)



    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AudioListHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_item, parent, false)
        Log.d(TAG, "new ${view.tag}")
        return AudioListHolder(view)
    }

    override fun onBindViewHolder(holder: AudioListHolder, position: Int) {
        holder.nameOfAudio.text = audioList[position].name
        holder.dateOfAudio.text = audioList[position].date
        holder.allTimeOfAudio.text = audioList[position].alltime
       // holder.currentTimeOfAudio.text = "1:32"
        holder.playBtn.setOnClickListener {
            startPlay(audioList[position], holder.itemView)
        }
        holder.stopBtn.setOnClickListener {
            holder.stopBtn.visibility = View.INVISIBLE
            holder.playBtn.visibility = View.VISIBLE
            pause()
        }
    }

    override fun getItemCount() = audioList.size

    fun setupAudio(audiolist: MutableList<AudioData> ){
        audioList.addAll(audiolist)
        notifyDataSetChanged()
    }

    fun writeAudio() {
        if(!mediaPlayer.isPlaying)  {
            recorder = MediaRecorder()
            recorder.setAudioSource(MediaRecorder.AudioSource.MIC)
            recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            recorder.setOutputFile(getAudioPath().absolutePath)
            try {
                recorder.prepare()
                recorder.start()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } else {
            Toast.makeText(context, "нельзя одновременно слушать и записывать аудио", Toast.LENGTH_LONG).show()
        }
    }

    fun stopWrite() {
        recorder.stop()
    }

    private fun startPlay(audio: AudioData, view: View) {

        Log.d(TAG, "start play :    ${mediaPlayer.isPlaying}")

        if ( !mediaPlayer.isPlaying ) {
            mediaPlayer.reset()
            view.playBtn.visibility = View.INVISIBLE
            view.stopBtn.visibility = View.VISIBLE
            val file = File(context.cacheDir, "audio.${audio.number}.${audio.name}.${audio.date}.${audio.alltime}.mp3")
            mediaPlayer.setDataSource(file.absolutePath)
            mediaPlayer.prepare()
            mediaPlayer.start()
            mediaPlayer.setOnCompletionListener {
                view.stopBtn.visibility = View.INVISIBLE
                view.playBtn.visibility = View.VISIBLE
            }
        } else {
            Toast.makeText(context, "нельзя включать сразу два аудио", Toast.LENGTH_LONG).show()
        }
    }

    fun pause() {
        mediaPlayer.stop()
    }

    private fun getAudioPath(): File {
        audioFile = File(context.cacheDir.absolutePath + File.separator + "audio.9.name9.19september.119.mp3")
        audioList.add(0, AudioData(9, "name9", "19september", "119"))
        notifyItemInserted(0)
        return audioFile
    }




    fun clear(){
        mediaPlayer.release()
    }
}
