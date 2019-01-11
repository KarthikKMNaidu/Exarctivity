package package_qr

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.Toast
import com.android.volley.*
import com.android.volley.toolbox.JsonObjectRequest
import com.exarcplus.exarctivity.R
import com.valdesekamdem.library.mdtoast.MDToast
import org.json.JSONException
import org.json.JSONObject
import setting_package.AppController
import setting_package.SessionManager
import setting_package.Static_Domine_Name
import typeface.CustomFontTextView
import java.text.SimpleDateFormat
import java.util.*


class Fragment_QR_Scan: Fragment() {

    lateinit var out_time_tv:CustomFontTextView;
    lateinit var in_time_tv:CustomFontTextView;
    lateinit var difference_time_tv:CustomFontTextView;
    lateinit var date_tv:CustomFontTextView;
    lateinit var check_in_lay:LinearLayout;
    lateinit var check_out_lay:LinearLayout;
    lateinit var reference_iv: ImageView;

    lateinit var dt:Date;
    var click_status="";
    companion object {
        private val REQUEST_WRITE_STORAGE = 112
        private val REQUEST_ID_MULTIPLE_PERMISSIONS = 112
    }
    lateinit var simpleProgressBar: ProgressBar;
    lateinit var session: SessionManager;
    var token_st = ""; var user_id_st = ""; var be_in_time = ""; var be_out_time=""; var show_date_st = ""; var send_date_st="";

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view1 = inflater!!.inflate(R.layout.fragment_qr_scan,container,false)

        out_time_tv = view1.findViewById(R.id.out_time_tv);
        in_time_tv = view1.findViewById(R.id.in_time_tv);
        date_tv = view1.findViewById(R.id.date_tv);
        check_in_lay = view1.findViewById(R.id.check_in_lay);
        check_out_lay = view1.findViewById(R.id.check_out_lay);
        simpleProgressBar = view1.findViewById(R.id.simpleProgressBar);
        difference_time_tv = view1.findViewById(R.id.difference_time_tv);
        reference_iv = view1.findViewById(R.id.reference_iv);


        session = SessionManager(activity)

        val user = session.userDetails;
        user_id_st = user.get(SessionManager.KEY_User_id)!!;


        val calendar = Calendar.getInstance()
        val show_date_Format = SimpleDateFormat("dd MMM yyyy, EEEE", Locale.ENGLISH)
        val send_date_Format = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)//remove


        calendar.add(Calendar.DATE, 0)
        dt = calendar.time

        Log.e("Fragment_QR_Scan", " calendar_2 ==> " + calendar.time)

         show_date_st = show_date_Format.format(dt)
         send_date_st = send_date_Format.format(dt)

        if(!show_date_st.equals("")){
            date_tv.setText(show_date_st);
            attendance_details_link(send_date_st)
        }else{
            date_tv.setText("");
        }


        check_in_lay.setOnClickListener(View.OnClickListener {

            if(in_time_tv.text.toString().trim().equals("")){
                click_status="IN"
                checkAndRequestPermissions()
            }else{
                click_status=""
                val mdToast: MDToast = MDToast.makeText(activity, "Already Checked In", MDToast.LENGTH_SHORT, MDToast.TYPE_INFO);
                mdToast.setGravity(Gravity.CENTER, 0, 0)
                mdToast.show();
            }

        })


        check_out_lay.setOnClickListener(View.OnClickListener {

            if(in_time_tv.text.toString().trim().equals("")){
                val mdToast: MDToast = MDToast.makeText(activity, "Still Not Checked In", MDToast.LENGTH_SHORT, MDToast.TYPE_INFO);
                mdToast.setGravity(Gravity.CENTER, 0, 0)
                mdToast.show();
            }else if(out_time_tv.text.toString().trim().equals("")){
                click_status="OUT"
                checkAndRequestPermissions()
            }else{
                click_status=""
                val mdToast: MDToast = MDToast.makeText(activity, "Already Checked OUT", MDToast.LENGTH_SHORT, MDToast.TYPE_INFO);
                mdToast.setGravity(Gravity.CENTER, 0, 0)
                mdToast.show();
            }
        })




        return view1;

    } //return view1;


    override fun onResume() {
        super.onResume()
        Log.e("Fragment_QR_Scan", "resume ")
        Log.e("Fragment_QR_Scan", "scan_result ==> "+ScanActivity.scan_result)

        if(!ScanActivity.scan_result.equals("")){

            if(click_status.equals("IN")){
                Log.e("Fragment_QR_Scan", "check_in")
                 dt = Date();

                val calendar = Calendar.getInstance()
                val show_time_Format = SimpleDateFormat("hh:mm a", Locale.ENGLISH)
                val send_time_Format = SimpleDateFormat("HH:mm", Locale.ENGLISH)//remove

                calendar.add(Calendar.DATE, 0)
                dt = calendar.time

                Log.e("Fragment_QR_Scan", " calendar_2 ==> " + calendar.time)

                val show_time_st = show_time_Format.format(dt)
                val send_time_st = send_time_Format.format(dt)

                if(!show_time_st.equals("")){
                    in_time_tv.setText(show_time_st);
                    check_in_lay.setBackgroundResource(R.color.view)
                    send_attendance_link(send_date_st,"Checked In")
                }else{
                    in_time_tv.setText("");
                }

            }else if(click_status.equals("OUT")){
                Log.e("Fragment_QR_Scan", "check_out")

                val calendar = Calendar.getInstance()
                val show_time_Format = SimpleDateFormat("hh:mm a", Locale.ENGLISH)
                val send_time_Format = SimpleDateFormat("HH:mm", Locale.ENGLISH)//remove

                calendar.add(Calendar.DATE, 0)
                dt = calendar.time

                Log.e("Fragment_QR_Scan", " calendar_2 ==> " + calendar.time)

                val show_time_st = show_time_Format.format(dt)
                val send_time_st = send_time_Format.format(dt)

                if(!show_time_st.equals("")){
                    out_time_tv.setText(show_time_st);
                    check_out_lay.setBackgroundResource(R.color.view)
                    send_attendance_link(send_date_st,"Checked OUT")
                }else{
                    out_time_tv.setText("");
                }


            }else{

            }

        }else{

        }
        
    }





    private fun checkAndRequestPermissions(): Boolean {
        val camera = ContextCompat.checkSelfPermission(activity!!, Manifest.permission.CAMERA)
        val wtite = ContextCompat.checkSelfPermission(activity!!, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        val read = ContextCompat.checkSelfPermission(activity!!, Manifest.permission.READ_EXTERNAL_STORAGE)
        val listPermissionsNeeded = ArrayList<String>()
        if (wtite != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
        if (camera != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA)
        }
        if (read != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
        if (!listPermissionsNeeded.isEmpty()) {

            ActivityCompat.requestPermissions(activity!!, listPermissionsNeeded.toArray(arrayOfNulls<String>(listPermissionsNeeded.size)), REQUEST_ID_MULTIPLE_PERMISSIONS)
            return false
        }else{
            //selectImage()
            val i = Intent(activity, ScanActivity::class.java)
            startActivity(i)
        }
        return true
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        Log.e("in fragment on request", "Permission callback called-------")
        when (requestCode) {
            REQUEST_ID_MULTIPLE_PERMISSIONS -> {

                val perms = HashMap<String, Int>()
                // Initialize the map with both permissions
                perms[Manifest.permission.WRITE_EXTERNAL_STORAGE] = PackageManager.PERMISSION_GRANTED
                perms[Manifest.permission.CAMERA] = PackageManager.PERMISSION_GRANTED
                perms[Manifest.permission.READ_EXTERNAL_STORAGE] = PackageManager.PERMISSION_GRANTED
                // Fill with actual results from user
                if (grantResults.size > 0) {
                    for (i in permissions.indices)
                        perms[permissions[i]] = grantResults[i]
                    if (perms[Manifest.permission.WRITE_EXTERNAL_STORAGE] == PackageManager.PERMISSION_GRANTED && perms[Manifest.permission.CAMERA] == PackageManager.PERMISSION_GRANTED && perms[Manifest.permission.READ_EXTERNAL_STORAGE] == PackageManager.PERMISSION_GRANTED) {
                        Log.e("in fragment on request", "CAMERA & WRITE_EXTERNAL_STORAGE READ_EXTERNAL_STORAGE permission granted")
                        //  selectImage()
                        val i = Intent(activity, ScanActivity::class.java)
                        startActivity(i)

                    } else {
                        Log.e("in fragment on request", "Some permissions are not granted ask again ")
                        if (ActivityCompat.shouldShowRequestPermissionRationale(activity!!, Manifest.permission.WRITE_EXTERNAL_STORAGE) || ActivityCompat.shouldShowRequestPermissionRationale(activity!!, Manifest.permission.CAMERA) || ActivityCompat.shouldShowRequestPermissionRationale(activity!!, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                            showDialogOK("Camera and Storage Permission required for this app",
                                    object : DialogInterface.OnClickListener{
                                        override fun onClick(dialog: DialogInterface, which: Int) {
                                            when (which) {
                                                DialogInterface.BUTTON_POSITIVE -> checkAndRequestPermissions()
                                                DialogInterface.BUTTON_NEGATIVE -> {
                                                }
                                            }// proceed with logic by disabling the related features or quit the app.
                                        }
                                    })
                        } else {
                            Toast.makeText(activity!!, "Go to settings and enable permissions", Toast.LENGTH_LONG).show()
                            //                            //proceed with logic by disabling the related features or quit the app.
                        }
                    }
                }
            }
        }

    }



    private fun showDialogOK(message: String, okListener: DialogInterface.OnClickListener) {
        AlertDialog.Builder(activity!!)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", okListener)
                .create()
                .show()
    }



    private fun attendance_details_link(date_st:String) {

        //http://www.exarcplus.com/exarctivity/json/attendance_details.php?user_id=1&date=2018-12-06

        val url = Static_Domine_Name.domine_name+"json/attendance_details.php?user_id="+user_id_st+"&date="+date_st

        simpleProgressBar.setVisibility(View.VISIBLE);

        Log.e("Fragment_QR_Scan", " url_details ==>" + url)

        val jsonObject = @RequiresApi(Build.VERSION_CODES.O)
        object : JsonObjectRequest(Method.GET, url, null, Response.Listener<JSONObject> { response ->

            Log.e("Fragment_QR_Scan", " response_details ==>" + response)
            try {
                val json = JSONObject(response.toString())// convert String to JSONObject

                val ja = json.getJSONArray("result")

                for(i in 0 until ja.length()){

                    val jsonObject = ja.getJSONObject(i);
                    val message = jsonObject.getString("message")

                    if(message.equals("success")) {

                        be_in_time = jsonObject.getString("in_time")
                        be_out_time = jsonObject.getString("out_time")
                        val admin_status = jsonObject.getString("admin_status")
                        session.update_admin_status(admin_status);

                    }else {
                        val mdToast: MDToast = MDToast.makeText(activity, "" +message, MDToast.LENGTH_SHORT, MDToast.TYPE_WARNING);
                        mdToast.setGravity(Gravity.CENTER, 0, 0)
                        mdToast.show();
                    }
                }

            } catch (e: JSONException) {
                e.printStackTrace()
            }

            simpleProgressBar.setVisibility(View.GONE);


            if(be_in_time!=null && !be_in_time.equals("")){
                in_time_tv.setText(be_in_time)
                check_in_lay.setBackgroundResource(R.color.view)
            }else {
                in_time_tv.setText("")
                check_in_lay.setBackgroundResource(R.color.button_bg_color)
            }


            if(be_out_time!=null && !be_out_time.equals("")){
                out_time_tv.setText(be_out_time)
                check_out_lay.setBackgroundResource(R.color.view)
            }else{
                out_time_tv.setText("")
                check_out_lay.setBackgroundResource(R.color.button_bg_color)
            }


            if(!be_in_time.equals("") && !be_out_time.equals("")){
                difference_time_tv.setVisibility(View.VISIBLE)
                reference_iv.setImageResource(R.drawable.qr_screen_logo)

                val simpleDateFormat = SimpleDateFormat("hh:mm a", Locale.ENGLISH)
               
                val date_in = simpleDateFormat.parse(be_in_time)
                val date_out = simpleDateFormat.parse(be_out_time)

               val difference = date_out.getTime() - date_in.getTime()
               val days = (difference / (1000 * 60 * 60 * 24)).toInt()
               var hours = ((difference - 1000 * 60 * 60 * 24 * days) / (1000 * 60 * 60)).toInt()
               val min = (difference - 1000 * 60 * 60 * 24 * days - 1000 * 60 * 60 * hours).toInt()/(1000 * 60)
               // hours = if(hours < 0) -hours else hours

                if(hours < 0){
                    hours = -hours
                }else{
                }

                difference_time_tv.setText("Today Working hrs : "+hours.toString()+" Hrs "+min.toString()+" Min")
                Log.e("Fragment_QR_Scan", " difference_hours ==> "+hours)
            }else{
                difference_time_tv.setVisibility(View.GONE)
                difference_time_tv.setText("")
                reference_iv.setImageResource(R.drawable.qr_icon)
            }



        }, Response.ErrorListener { error ->
            simpleProgressBar.setVisibility(View.GONE);
            if (error is TimeoutError) {
                Toast.makeText(activity, "Oops! Time out Error", Toast.LENGTH_SHORT).show()
            } else if (error is ServerError) {
                Toast.makeText(activity, "Oops! Server Error", Toast.LENGTH_SHORT).show()
            } else if (error is AuthFailureError) {
                Toast.makeText(activity, "Oops! Authentication Failure Error", Toast.LENGTH_SHORT).show()
            } else if (error is NetworkError) {
                Toast.makeText(activity, "Oops! Network Error", Toast.LENGTH_SHORT).show()
            } else if (error is ParseError) {
                Toast.makeText(activity, "Oops! Parse Error", Toast.LENGTH_SHORT).show()
            }

        })
        {}
        AppController.getInstance().addToRequestQueue(jsonObject)

        jsonObject.retryPolicy = DefaultRetryPolicy(20 * 30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
    }



    private fun send_attendance_link(date_st:String,status:String) {
        //http://www.exarcplus.com/exarctivity/json/attendance.php?user_id=&date=2018-12-06&in_time=&out_time=
        val url = Static_Domine_Name.domine_name+"json/attendance.php?user_id="+user_id_st+"&date="+date_st+"&in_time="+in_time_tv.text.toString().trim().replace(" ","%20")+"&out_time="+out_time_tv.text.toString().trim().replace(" ","%20")

        simpleProgressBar.setVisibility(View.VISIBLE);
        Log.e("Fragment_QR_Scan", " url_send_attendance ==>" + url)

        val jsonObject = @RequiresApi(Build.VERSION_CODES.O)
        object : JsonObjectRequest(Method.GET, url, null, Response.Listener<JSONObject> { response ->

            Log.e("Fragment_QR_Scan", " response_send_attendance ==>" + response)
            try {
                val json = JSONObject(response.toString())// convert String to JSONObject

                val ja = json.getJSONArray("result")

                for(i in 0 until ja.length()){

                    val jsonObject = ja.getJSONObject(i);
                    val message = jsonObject.getString("message")

                    if(message.equals("success")) {
                        val mdToast: MDToast = MDToast.makeText(activity, "Successfully "+status, MDToast.LENGTH_SHORT, MDToast.TYPE_SUCCESS);
                        mdToast.setGravity(Gravity.CENTER, 0, 0)
                        mdToast.show();
                    }else {
                        val mdToast: MDToast = MDToast.makeText(activity, "" +message, MDToast.LENGTH_SHORT, MDToast.TYPE_WARNING);
                        mdToast.setGravity(Gravity.CENTER, 0, 0)
                        mdToast.show();
                    }
                }

            } catch (e: JSONException) {
                e.printStackTrace()
            }
            simpleProgressBar.setVisibility(View.GONE);

        }, Response.ErrorListener { error ->
            simpleProgressBar.setVisibility(View.GONE);
            if (error is TimeoutError) {
                Toast.makeText(activity, "Oops! Time out Error", Toast.LENGTH_SHORT).show()
            } else if (error is ServerError) {
                Toast.makeText(activity, "Oops! Server Error", Toast.LENGTH_SHORT).show()
            } else if (error is AuthFailureError) {
                Toast.makeText(activity, "Oops! Authentication Failure Error", Toast.LENGTH_SHORT).show()
            } else if (error is NetworkError) {
                Toast.makeText(activity, "Oops! Network Error", Toast.LENGTH_SHORT).show()
            } else if (error is ParseError) {
                Toast.makeText(activity, "Oops! Parse Error", Toast.LENGTH_SHORT).show()
            }

        })
        {}
        AppController.getInstance().addToRequestQueue(jsonObject)

        jsonObject.retryPolicy = DefaultRetryPolicy(20 * 30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
    }




}