package com.example.alert.reminderapp

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ListAdapter
import android.widget.ListView
import android.widget.SimpleAdapter
import androidx.annotation.RequiresApi
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters

class schedule : AppCompatActivity() {

    var db = DBHelper(this)
    var day : LocalDate? = LocalDate.now()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_schedule)
        val lv1 : ListView = findViewById(R.id.firstview) as ListView
        val lv2 : ListView = findViewById(R.id.seconview) as ListView
        val lv3 : ListView = findViewById(R.id.thirdview) as ListView
        val lv4 : ListView = findViewById(R.id.fourtview) as ListView
        val lv5 : ListView = findViewById(R.id.fifthview) as ListView
        val lv6 : ListView = findViewById(R.id.sixthview) as ListView
        val lv7 : ListView = findViewById(R.id.sevenview) as ListView
        listMaker(day?.with(TemporalAdjusters.next(DayOfWeek.MONDAY)).toString(),lv1)
        listMaker(day?.with(TemporalAdjusters.next(DayOfWeek.TUESDAY)).toString(),lv2)
        listMaker(day?.with(TemporalAdjusters.next(DayOfWeek.WEDNESDAY)).toString(),lv3)
        listMaker(day?.with(TemporalAdjusters.next(DayOfWeek.THURSDAY)).toString(),lv4)
        listMaker(day?.with(TemporalAdjusters.next(DayOfWeek.FRIDAY)).toString(),lv5)
        listMaker(day?.with(TemporalAdjusters.next(DayOfWeek.SATURDAY)).toString(),lv6)
        listMaker(day?.with(TemporalAdjusters.next(DayOfWeek.SUNDAY)).toString(),lv7)
        var actB = getSupportActionBar()
        if (actB != null) {
            actB.setDisplayHomeAsUpEnabled(true)
        }
    }

    fun listMaker(a:String,b:ListView){
        if (db.GetSchedule(a) != null){
            val persistList: ArrayList<HashMap<String, String>>? = db.GetSchedule(a)
            val adapter : ListAdapter = SimpleAdapter(
                this@schedule,
                persistList,
                R.layout.viewsched,
                arrayOf("SCHEDULE_TIME","SCHEDULE_NAME"),
                intArrayOf(R.id.timefinal,R.id.namefinal)
                )
            b.setAdapter(adapter)
        }
    }

    fun goaddSCED(v: View?){
        startActivity(Intent(this@schedule, MainActivity::class.java))
    }
}