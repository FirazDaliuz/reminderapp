package com.example.alert.reminderapp

import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.text.format.DateFormat
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TimePicker
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.loader.content.CursorLoader
import java.io.File
import java.lang.Exception
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters.next


class schedulenotif : AppCompatActivity() {

    var daypicked = ""
    var timepicked = ""
    var daystr = arrayListOf<String>("Mon","Tues","Wed","Thur","Fri","Sat","Sun")
    val isDay = arrayListOf<Boolean>(false,false,false,false,false,false,false)
    val isTime = arrayListOf<Boolean>(false,false)
    lateinit var btnblu: Drawable
    lateinit var btnred: Drawable
    lateinit var monBTN: Button
    lateinit var tuesBTN: Button
    lateinit var wedBTN: Button
    lateinit var thurBTN: Button
    lateinit var friBTN: Button
    lateinit var satBTN: Button
    lateinit var sunBTN: Button
    var uriText = ""
    val db = DBHelper(this)
    @RequiresApi(Build.VERSION_CODES.O)
    var day : LocalDate? = LocalDate.now()
    var scheddate = ""
    var timedat = ""

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_schedulenotif)
        monBTN = findViewById<Button>(R.id.monday)
        tuesBTN = findViewById<Button>(R.id.tuesday)
        wedBTN = findViewById<Button>(R.id.wednesday)
        thurBTN = findViewById<Button>(R.id.thursday)
        friBTN = findViewById<Button>(R.id.friday)
        satBTN = findViewById<Button>(R.id.saturday)
        sunBTN = findViewById<Button>(R.id.sunday)
        Toast.makeText(this, day?.with(DayOfWeek.MONDAY).toString(), Toast.LENGTH_LONG).show()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun monClick(v:View?){
        scheddate = day?.with(next(DayOfWeek.MONDAY)).toString()
        Toast.makeText(this, "Monday", Toast.LENGTH_SHORT).show()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun tuesClick(v:View?){
        scheddate = day?.with(next(DayOfWeek.TUESDAY)).toString()
        Toast.makeText(this, "Tuesday", Toast.LENGTH_SHORT).show()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun wedClick(v:View?){
        scheddate = day?.with(next(DayOfWeek.WEDNESDAY)).toString()
        Toast.makeText(this, "Wednesday", Toast.LENGTH_SHORT).show()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun thurClick(v:View?){
        scheddate = day?.with(next(DayOfWeek.THURSDAY)).toString()
        Toast.makeText(this, "Thursday", Toast.LENGTH_SHORT).show()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun friClick(v:View?){
        scheddate = day?.with(next(DayOfWeek.FRIDAY)).toString()
        Toast.makeText(this, "Friday", Toast.LENGTH_SHORT).show()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun satClick(v:View?){
        scheddate = day?.with(next(DayOfWeek.SATURDAY)).toString()
        Toast.makeText(this, "Saturday", Toast.LENGTH_SHORT).show()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun sunClick(v:View?){
        scheddate = day?.with(next(DayOfWeek.SUNDAY)).toString()
        Toast.makeText(this, "Sunday", Toast.LENGTH_SHORT).show()
    }


    fun musicPicker(v: View?) {
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
                    this@schedulenotif,
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

    fun cancelSchedule(v:View?){
        startActivity(Intent(this@schedulenotif, MainActivity::class.java))
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun addSchedule(v:View?){
        var schedname = findViewById<EditText>(R.id.schedulename)
        var g = scheddate

        val timep = findViewById<TimePicker>(R.id.timePicker)
        timep.setIs24HourView(DateFormat.is24HourFormat(this))
        timedat = timep.hour.toString()+":"+timep.minute.toString()

        var scheduri = uriText

        db.insertTable2(schedname.text.toString(),g, timedat, scheduri)

    }
}