package com.example.alert.reminderapp

import android.annotation.SuppressLint
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.alert.reminderapp.DBHelper as DBHelper
import android.widget.Toast

import android.widget.TextView

import android.widget.AdapterView.OnItemClickListener
import android.app.AlarmManager

import android.app.PendingIntent

import android.widget.EditText
import com.example.alert.reminderapp.MyBroadcastReceiver as MyBroadcastReceiver
import android.provider.MediaStore
import androidx.loader.content.CursorLoader
import java.io.File
import java.lang.Exception


class persistence_notification : AppCompatActivity() {

    val db = DBHelper(this)
    var uriText = ""
    var numid = 0
    var duratime : Float = 1.0F
    lateinit var radiodetect: RadioGroup
    var selectid : Int = 0
    lateinit var radName : RadioButton
    var duratext = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_persistence_notification)
        if (db.GetPersist() != null) {
            val persistList: ArrayList<HashMap<String, String>>? = db.GetPersist()
            val lv: ListView = findViewById<View>(R.id.listView1) as ListView
            val adapter: ListAdapter = SimpleAdapter(
                this@persistence_notification,
                persistList,
                R.layout.itemlists,
                arrayOf("PERSIST_ID","PERSIST_NAME"),
                intArrayOf(R.id.numN, R.id.Notification)
            )
            lv.setAdapter(adapter)
            lv.onItemClickListener =
                OnItemClickListener { arg0, v, arg2, arg3 ->
                    val tv = v.findViewById<View>(R.id.numN) as TextView
                    val s = tv.text.toString()
                    numid = s.toInt()
                    val toast = Toast.makeText(
                        applicationContext,
                        "$numid",
                        Toast.LENGTH_SHORT
                    )
                    toast.show()
                }
        }
        var actB = getSupportActionBar()
        if (actB != null) {
            actB.setDisplayHomeAsUpEnabled(true)
        }
    }


    override fun onContextItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onContextItemSelected(item)
    }

    fun persist_pop(v: View?) {
        val nDialogView = LayoutInflater.from(this).inflate(R.layout.popup, null, false)
        val nBuilder = AlertDialog.Builder(this).setView(nDialogView)
        val mAlertDog = nBuilder.show()
        nDialogView.findViewById<Button>(R.id.button8).setOnClickListener() {
            mAlertDog.dismiss()
        }
        nDialogView.findViewById<Button>(R.id.button7).setOnClickListener() {
            musicPicker()
        }
        nDialogView.findViewById<Button>(R.id.button9).setOnClickListener() {
            var vname = nDialogView.findViewById<EditText>(R.id.persistText).text.toString()
            db.insertTable1(vname, uriText)
            mAlertDog.dismiss()
            startActivity(Intent(this@persistence_notification, persistence_notification::class.java))
        }
    }


    fun musicPicker() {
        val videoIntent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        )
        startActivityForResult(Intent.createChooser(videoIntent, "Select Audio"), 111)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 111 && resultCode == RESULT_OK) {
            val uri: Uri? = data?.data //The uri with the location of the file
            try {
                val uriString = uri.toString()
                val myFile = File(uriString)
                //    String path = myFile.getAbsolutePath();
                val displayName: String? = null
                val path2: String = getAudioPath(uri)
                val f = File(path2)
                val fileSizeInBytes: Long = f.length()
                val fileSizeInKB = fileSizeInBytes / 1024
                val fileSizeInMB = fileSizeInKB / 1024
                uriText = path2
            } catch (e: Exception) {
                //handle exception
                Toast.makeText(
                    this@persistence_notification,
                    "Unable to process,try again",
                    Toast.LENGTH_SHORT
                ).show()
            }
            //val mPlayer = MediaPlayer()
            //mPlayer.setAudioAttributes(
            //    AudioAttributes.Builder()
            //        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
            //        .build()
            //)
            //if (selectedFile != null) {
            //    mPlayer.setDataSource(applicationContext, selectedFile)
            //}
            //mPlayer.prepare()
            //mPlayer.start()
        }
    }

    @SuppressLint("Range")
    fun getNameFromURI(uri: Uri?): String? {
        val c: Cursor? = contentResolver.query(uri!!, null, null, null, null)
        c?.moveToFirst()
        return c?.getString(c.getColumnIndex(OpenableColumns.DISPLAY_NAME))
    }

    fun goDel(v: View?){
        db.deletePersist(numid)
        startActivity(Intent(this@persistence_notification, persistence_notification::class.java))
    }

    fun getRadio(v: View?){
        val radioDialog = LayoutInflater.from(this).inflate(R.layout.radiopick,null,false)
        val radioBuilder = AlertDialog.Builder(this).setView(radioDialog)
        val radiogo = radioBuilder.show()
        radioDialog.findViewById<Button>(R.id.button13).setOnClickListener {
            radiodetect = radioDialog.findViewById(R.id.rG)
            selectid = radiodetect.checkedRadioButtonId
            radName = radiodetect.findViewById(selectid)
            var radVar = radName.text
            when (radVar){
                "30 seconds" -> duratime = 0.5F
                "1 minute" -> duratime = 1.0F
                "2 minutes" -> duratime = 2.0F
                "5 minutes" -> duratime = 5.0F
                "10 minutes" -> duratime = 10.0F
            }
            duratext = radVar.toString()
            Toast.makeText(this, "Duration set in $radVar", Toast.LENGTH_LONG).show()
            radiogo.dismiss()
        }
        radioDialog.findViewById<Button>(R.id.button14).setOnClickListener {
            radiogo.dismiss()
        }
    }

    fun startAlert(v: View?) {
        val arr = db.getPreciseIDPersist()
        val i = (duratime*60)
        var dat = 1
        var reqC = 234324243
        for (iii in arr){
            val intent = Intent(this, MyBroadcastReceiver::class.java)
            intent.putExtra("a",iii)
            intent.putExtra("b",duratime.toString())
            val pendingIntent = PendingIntent.getBroadcast(
                this.applicationContext, reqC, intent, PendingIntent.FLAG_CANCEL_CURRENT
            )
            val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
            alarmManager[AlarmManager.RTC_WAKEUP, (System.currentTimeMillis()
                    + (i * dat) * 1000).toLong()] = pendingIntent
            Toast.makeText(this, "Alarm set in $duratext", Toast.LENGTH_LONG).show()
            reqC += 1
            dat += 1
        }
    }

    private fun getAudioPath(uri: Uri?): String {
        val data = arrayOf(MediaStore.Audio.Media.DATA)
        val loader = uri?.let {
            CursorLoader(
                applicationContext, it, data, null, null, null
            )
        }
        val cursor = loader!!.loadInBackground()
        val column_index = cursor!!.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)
        cursor.moveToFirst()
        return cursor.getString(column_index)
    }

}