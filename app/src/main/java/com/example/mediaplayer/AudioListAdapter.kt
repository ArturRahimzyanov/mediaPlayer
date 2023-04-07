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
        var deleteBtn: ImageButton = item.findViewById(R.id.deleteBtn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AudioListHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_item, parent, false)
        return AudioListHolder(view)
    }

    override fun onBindViewHolder(holder: AudioListHolder, position: Int) {
        holder.nameOfAudio.text = audioList[position].name
        holder.dateOfAudio.text = audioList[position].date
        holder.allTimeOfAudio.text = audioList[position].alltime
        holder.playBtn.setOnClickListener {
            startPlay(audioList[position], holder.itemView)
        }
        holder.stopBtn.setOnClickListener {
            holder.stopBtn.visibility = View.INVISIBLE
            holder.playBtn.visibility = View.VISIBLE
            pause()
        }
        holder.deleteBtn.setOnClickListener {
            deleteAudio(position, holder.itemView)
        }
    }

        override fun getItemCount() = audioList.size
//
//    override fun getItemId(position: Int): Long {
//        return position.toLong()
//    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

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

    private fun getAudioPath(): File {
        val number = 20
        audioFile = File(context.cacheDir.absolutePath + File.separator + "audio.${number}.name${number}.${number}${number}september.${number}${number}${number}.mp3")
        return audioFile
    }

    fun stopWrite() {
        recorder.stop()
        val number = 20
        audioList.add(0, AudioData(number, "name${number}", "${number}${number}september", "${number}${number}${number}"))
        notifyDataSetChanged()
    }

    private fun startPlay(audio: AudioData, view: View) {
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

    fun deleteAudio(position: Int, view: View){
        val number = view.nameOfAudio.text.substring(4)
        if( mediaPlayer.isPlaying   )   {   mediaPlayer.reset()   }
        context.cacheDir.listFiles()?.map {
            if(it.path == "/data/user/0/com.example.mediaPlayer/cache/audio.${number}.name$number.$number${number}september.$number$number$number.mp3"){
                it.delete()
                Log.d(TAG, "in if $number")
            }
        }
        audioList.removeAt(position)
        notifyItemRemoved(position)
    }
}
