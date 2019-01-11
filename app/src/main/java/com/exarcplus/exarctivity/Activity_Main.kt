package com.exarcplus.exarctivity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.ImageView
import android.widget.LinearLayout
import package_announcement.Fragment_Announcement
import package_attendance.Fragment_Attendance
import package_holidays.Fragment_Holidays
import package_profile.Fragment_Profile
import package_qr.Fragment_QR_Scan
import setting_package.SessionManager
import typeface.CustomFontTextView

class Activity_Main : AppCompatActivity() {

    lateinit var bottom_tv: CustomFontTextView;

    lateinit var b_scanner_click: LinearLayout;
    lateinit var b_profile_click: LinearLayout;
    lateinit var b_holidays_click: LinearLayout;
    lateinit var b_announce_click: LinearLayout;
    lateinit var b_attendance_click: LinearLayout;

    lateinit var b_announce_iv: ImageView;
    lateinit var b_attendance_iv: ImageView;
    lateinit var b_scanner_iv: ImageView;
    lateinit var b_holidays_iv: ImageView;
    lateinit var b_profile_iv: ImageView;

    lateinit var session: SessionManager;
    var token_st = ""; var admin_status = "0";
    var back_note="";

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        b_scanner_click = findViewById(R.id.b_scanner_click);
        b_profile_click = findViewById(R.id.b_profile_click);
        b_holidays_click = findViewById(R.id.b_holidays_click);
        b_announce_click = findViewById(R.id.b_announce_click);
        b_attendance_click = findViewById(R.id.b_attendance_click);

        b_announce_iv = findViewById(R.id.b_announce_iv);
        b_attendance_iv = findViewById(R.id.b_attendance_iv);
        b_scanner_iv = findViewById(R.id.b_scanner_iv);
        b_holidays_iv = findViewById(R.id.b_holidays_iv);
        b_profile_iv = findViewById(R.id.b_profile_iv);



        session = SessionManager(this)

        val user = session.userDetails;

        if(user.get(SessionManager.KEY_Admin_Status)!=null && !user.get(SessionManager.KEY_Admin_Status).equals("")){
            admin_status = user.get(SessionManager.KEY_Admin_Status)!!;
        }else{
            admin_status = "0"
        }


        val bundle = intent.extras
        if (bundle!= null) {

            if(bundle.getString("sub_fragment")!=null && !bundle.getString("sub_fragment").equals("")){
                back_note = bundle.getString("sub_fragment")
            }else{
                back_note = ""
            }
            Log.e("Activity_Main"," back_note ==> "+back_note);

        }else{
            back_note = ""
        }




        if(back_note!=null && back_note.equals("profile")) {

            val frag = Fragment_Profile()
            val manager = supportFragmentManager
            val transaction = manager.beginTransaction()
            transaction.replace(R.id.content_frame, frag)
            transaction.commit()

            profile_select();

        }else{
            val frag = Fragment_QR_Scan()
            val manager = supportFragmentManager
            val transaction = manager.beginTransaction()
            transaction.replace(R.id.content_frame, frag)
            transaction.commit()

            qr_scan_select();
        }


        b_attendance_click.setOnClickListener {

            attendance_select()

            val frag = Fragment_Attendance()
            val manager = supportFragmentManager
            val transaction = manager.beginTransaction()
            transaction.replace(R.id.content_frame, frag)
            transaction.commit()
        }


        b_announce_click.setOnClickListener {

            announcement_select()

            val frag = Fragment_Announcement()
            val manager = supportFragmentManager
            val transaction = manager.beginTransaction()
            transaction.replace(R.id.content_frame, frag)
            transaction.commit()
        }

        b_scanner_click.setOnClickListener {

            qr_scan_select()

            val frag1 = Fragment_QR_Scan()
            supportFragmentManager.beginTransaction().replace(R.id.content_frame, frag1).commit()
        }


        b_holidays_click.setOnClickListener {

            holiday_select()

            val frag = Fragment_Holidays()
            val manager = supportFragmentManager
            val transaction = manager.beginTransaction()
            transaction.replace(R.id.content_frame, frag)
            transaction.commit()
        }


        b_profile_click.setOnClickListener {

            profile_select()

            val frag = Fragment_Profile()
            val manager = supportFragmentManager
            val transaction = manager.beginTransaction()
            transaction.replace(R.id.content_frame, frag)
            transaction.commit()
        }





    }//onCreate





    fun attendance_select()
    {
        b_attendance_iv.setImageResource(R.mipmap.calendar_color)
        b_announce_iv.setImageResource(R.mipmap.announcement_gray)
        b_scanner_iv.setImageResource(R.mipmap.qr_gray)
        b_holidays_iv.setImageResource(R.mipmap.holiday_gray)
        b_profile_iv.setImageResource(R.mipmap.profile_gray)
    }

    fun announcement_select()
    {
        b_attendance_iv.setImageResource(R.mipmap.calendar_gray)
        b_announce_iv.setImageResource(R.mipmap.announce_color)
        b_scanner_iv.setImageResource(R.mipmap.qr_gray)
        b_holidays_iv.setImageResource(R.mipmap.holiday_gray)
        b_profile_iv.setImageResource(R.mipmap.profile_gray)
    }

    fun qr_scan_select()
    {
        b_attendance_iv.setImageResource(R.mipmap.calendar_gray)
        b_announce_iv.setImageResource(R.mipmap.announcement_gray)
        b_scanner_iv.setImageResource(R.mipmap.qr_color)
        b_holidays_iv.setImageResource(R.mipmap.holiday_gray)
        b_profile_iv.setImageResource(R.mipmap.profile_gray)
    }

    fun holiday_select()
    {
        b_attendance_iv.setImageResource(R.mipmap.calendar_gray)
        b_announce_iv.setImageResource(R.mipmap.announcement_gray)
        b_scanner_iv.setImageResource(R.mipmap.qr_gray)
        b_holidays_iv.setImageResource(R.mipmap.holiday_color)
        b_profile_iv.setImageResource(R.mipmap.profile_gray)
    }

    fun profile_select()
    {
        b_attendance_iv.setImageResource(R.mipmap.calendar_gray)
        b_announce_iv.setImageResource(R.mipmap.announcement_gray)
        b_scanner_iv.setImageResource(R.mipmap.qr_gray)
        b_holidays_iv.setImageResource(R.mipmap.holiday_gray)
        b_profile_iv.setImageResource(R.mipmap.profile_color)
    }



}