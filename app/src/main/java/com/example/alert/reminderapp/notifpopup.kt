package com.example.alert.reminderapp

import android.media.AudioAttributes
import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AlertDialog

import android.view.*


class notifpopup : AppCompatActivity() {

    val db = DBHelper(this)
    lateinit var urires : Uri
    var popUpOpen = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_notifpopup)

        popper()
        val intent = intent
        val str = intent.getStringExtra("a").toString()
        val uristr = db.getPreciseURIPersist(str.toInt())
        urires = Uri.parse(uristr)
        music(urires)
    }

    private fun popper(){
        popUpOpen = true
        val nDialogView = LayoutInflater.from(this).inflate(R.layout.surprise, null, false)
        val nBuilder = AlertDialog.Builder(this).setView(nDialogView)
        nBuilder.setCancelable(false)
        val mAlertDog = nBuilder.show()
        val tv = nDialogView.findViewById<TextView>(R.id.surpriseText)
        val stv = nDialogView.findViewById<TextView>(R.id.surpriseLabel)
        val str = intent.getStringExtra("a").toString()
        val timestr = intent.getStringExtra("b").toString()
        tv.text = db.getPreciseNamePersist(str.toInt())
        stv.text = "U already using the phone for $timestr minutes. Go and take a break"

    }
    private fun music(urires: Uri){
        var mp = MediaPlayer()
        mp.setAudioAttributes(
            AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build()
        )
        mp.setDataSource(this,urires)
        mp.prepare()
        mp.start()
    }

}