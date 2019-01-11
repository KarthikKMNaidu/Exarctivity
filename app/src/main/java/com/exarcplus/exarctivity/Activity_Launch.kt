package com.exarcplus.exarctivity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.Uri
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.text.Html
import android.util.Log
import com.afollestad.materialdialogs.DialogAction
import com.afollestad.materialdialogs.MaterialDialog
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog
import com.github.javiersantos.materialstyleddialogs.enums.Style
import org.jsoup.Jsoup
import setting_package.SessionManager
import typeface.CustomFontTextView
import java.io.IOException


class Activity_Launch : AppCompatActivity() {

    lateinit var bottom_tv:CustomFontTextView;
    lateinit var sessionManager: SessionManager;
    var currentVersion = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launch)

        bottom_tv = findViewById(R.id.bottom_tv);
        sessionManager = SessionManager(this)



        try {
            currentVersion = packageManager.getPackageInfo(packageName, 0).versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }




        isInternetOn();

    }//onCreate





    @Suppress("DEPRECATION")
    fun isInternetOn(): Boolean {

        // get Connectivity Manager object to check connection
        val connec = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        // Check for network connections
        if (connec.getNetworkInfo(0).state == android.net.NetworkInfo.State.CONNECTED ||
                connec.getNetworkInfo(0).state == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).state == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).state == android.net.NetworkInfo.State.CONNECTED) {

            GetVersionCode().execute();

            return true

        } else if (connec.getNetworkInfo(0).state == android.net.NetworkInfo.State.DISCONNECTED || connec.getNetworkInfo(1).state == android.net.NetworkInfo.State.DISCONNECTED) {

            MaterialStyledDialog.Builder(this)
                    .setTitle("Exarctivity!")
                    .withDialogAnimation(false)
                    .setStyle(Style.HEADER_WITH_TITLE)
                    .setDescription("Please Check the internet connection.")
                    .setPositiveText("Done!")
                    .onPositive(MaterialDialog.SingleButtonCallback { dialog, which ->
                        finishAffinity()
                    })
                    .show()


            return false
        }



        return false
    }






    @SuppressLint("StaticFieldLeak")
    private inner class GetVersionCode : AsyncTask<Void, String, String>() {
        override fun doInBackground(vararg voids: Void): String? {

            var newVersion: String? = null

            try {
                 val document = Jsoup.connect("https://play.google.com/store/apps/details?id=" + this@Activity_Launch.getPackageName() + "&hl=en")
                    .timeout(30000)
                    .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                    .referrer("http://www.google.com")
                    .get()
                if (document != null) {
                    val element = document!!.getElementsContainingOwnText("Current Version")
                    for (ele in element) {
                        if (ele.siblingElements() != null) {
                            val sibElemets = ele.siblingElements()
                            for (sibElemet in sibElemets) {
                                newVersion = sibElemet.text()
                            }
                        }
                    }
                }

            } catch (e: IOException) {
                e.printStackTrace()

                if (newVersion == null) {

                    val secondsDelayed = 1
                    Handler().postDelayed({

                        if (sessionManager.isLoggedIn) {
                            val myIntent = Intent(this@Activity_Launch, Activity_Main::class.java)
                            myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(myIntent)
                            finish()
                        } else {
                            val myIntent = Intent(this@Activity_Launch, Activity_Login::class.java)
                            myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(myIntent)
                            finish()
                        }

                    }, (secondsDelayed * 2000).toLong())

                }

            }

            return newVersion

        }


        override fun onPostExecute(onlineVersion: String?) {
            super.onPostExecute(onlineVersion)
            if (onlineVersion != null && !onlineVersion.isEmpty()) {

                if (java.lang.Float.valueOf(currentVersion) < java.lang.Float.valueOf(onlineVersion)) {

                    MaterialStyledDialog.Builder(this@Activity_Launch)
                        .setTitle("Exarctivity!!")
                        .setDescription("New version is available, Please update application...")
                        .setStyle(Style.HEADER_WITH_TITLE)
                        .setIcon(R.mipmap.app_icon)
                        .setHeaderColor(R.color.main_color)
                        .setPositiveText("Update")
                        .setNegativeText("Later")
                        .onNegative(object : MaterialDialog.SingleButtonCallback{
                            override fun onClick(dialog: MaterialDialog, which: DialogAction) {

                                if (sessionManager.isLoggedIn) {
                                    val myIntent = Intent(this@Activity_Launch, Activity_Main::class.java)
                                    myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                                    startActivity(myIntent)
                                    finish()
                                } else {
                                    val myIntent = Intent(this@Activity_Launch, Activity_Login::class.java)
                                    myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                                    startActivity(myIntent)
                                    finish()
                                }

                            }
                        })
                        .onPositive(object : MaterialDialog.SingleButtonCallback {
                            override fun onClick(dialog: MaterialDialog, which: DialogAction) {
                                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.exarcplus.exarctivity")))
                            }
                        })
                        .show()

                } else {

                    val secondsDelayed = 1
                    Handler().postDelayed({

                        if (sessionManager.isLoggedIn) {
                            val myIntent = Intent(this@Activity_Launch, Activity_Main::class.java)
                            myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(myIntent)
                            finish()
                        } else {
                            val myIntent = Intent(this@Activity_Launch, Activity_Login::class.java)
                            myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(myIntent)
                            finish()
                        }

                    }, (secondsDelayed * 2000).toLong())

                }

            }

            Log.e("Launching_Activity", "Current version ==>: "+currentVersion+" Playstore version ==>: "+onlineVersion)


        }


    }

















}
