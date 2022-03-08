package com.example.alert.reminderapp

import android.content.Context

import android.media.MediaPlayer

import android.content.Intent

import android.content.BroadcastReceiver
import android.os.Build
import androidx.annotation.RequiresApi


class MyBroadcastReceiver() : BroadcastReceiver() {
    var mp: MediaPlayer? = null
    @RequiresApi(Build.VERSION_CODES.O)


    override fun onReceive(context: Context, intent: Intent) {
        val str = intent.getStringExtra("a")
        val timestr = intent.getStringExtra("b")
        val i = Intent(context, notifpopup::class.java)
        i.putExtra("a",str)
        i.putExtra("b",timestr)
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(i)
    }
}