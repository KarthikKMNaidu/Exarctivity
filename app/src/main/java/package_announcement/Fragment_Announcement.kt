package package_announcement

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.support.annotation.RequiresApi
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
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
import test_package.MenuInScrollViewActivity
import typeface.CustomFontTextView
import java.text.SimpleDateFormat
import java.util.*


class Fragment_Announcement : Fragment() {


    lateinit var simpleProgressBar: ProgressBar;
    lateinit var session: SessionManager;
    var admin_status_st = "";
    var user_id_st = "";

    lateinit var side_menu_lay:LinearLayout
    lateinit var date_tv: CustomFontTextView
    lateinit var pages_tv: CustomFontTextView


//******************* Below for viewPager *************************
    lateinit var viewPager: ViewPager
    var announcement_list = ArrayList<Announcement_Array>()
    internal lateinit var ll_dots: LinearLayout
    internal lateinit var adapter_pager: Adapter_Announcement
    internal var currentPage = 0
    internal var current_pos = 0
    internal var timer: Timer? = null
    internal val DELAY_MS: Long = 500//delay in milliseconds before task is to be executed
    internal val PERIOD_MS: Long = 2000 // time in milliseconds between successive task executions.

    lateinit var dots: Array<TextView?>


//**********************  Above for viewPager  ****************************






    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view1 = inflater!!.inflate(R.layout.fragment_announcement, container, false)

        viewPager = view1.findViewById(R.id.viewPager)
        simpleProgressBar = view1.findViewById(R.id.simpleProgressBar)
        side_menu_lay = view1.findViewById(R.id.side_menu_lay)
        date_tv = view1.findViewById(R.id.date_tv)
        pages_tv = view1.findViewById(R.id.pages_tv)



        side_menu_lay.setOnClickListener(View.OnClickListener {
            val intent = Intent(activity,Activity_Add_Announcement()::class.java)
            startActivity(intent);
        })

/*
        side_menu_lay.setOnClickListener(View.OnClickListener {
            val intent = Intent(activity, MenuInScrollViewActivity()::class.java)
            startActivity(intent);
        })*/


        session = SessionManager(activity)

        val user = session.userDetails;
        user_id_st = user.get(SessionManager.KEY_User_id)!!;
        admin_status_st = user.get(SessionManager.KEY_Admin_Status)!!;

        //admin_status_st="1";

        if(admin_status_st!=null && admin_status_st.equals("1")){
            side_menu_lay.setVisibility(View.VISIBLE);
        }else{
            side_menu_lay.setVisibility(View.GONE);
        }


        viewPager.setOnPageChangeListener(object : ViewPager.OnPageChangeListener {

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

                  Log.e("Fragment_Announcement", "onPageScrolled ==> "+position)

                  val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.ENGLISH)
                  val showing_day = SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH)

                  val getting_formate = simpleDateFormat.parse(announcement_list.get(position).getDate());
                  val formated_date = showing_day.format(getting_formate);
                  date_tv.setText(formated_date);

                 pages_tv.setText((position+1).toString()+" of "+announcement_list.size)

            }

            override fun onPageSelected(position: Int) {
                currentPage = position
                current_pos = position

            }

            override fun onPageScrollStateChanged(state: Int) {
            }
        })


//After setting the adapter use the timer

      /*  val handler = Handler()
        val Update = Runnable {
            if (currentPage == announcement_list.size) {
                currentPage = 0
            } else {
                viewPager.setCurrentItem(currentPage++, true)
            }
        }

        timer = Timer() // This will create a new Thread
        timer!!.schedule(object : TimerTask() { // task to be scheduled

            override fun run() {
                handler.post(Update)
            }
        }, DELAY_MS, PERIOD_MS)*/



        return view1;


    } //return view1;


    override fun onResume() {
        super.onResume()
        announcement_list_link()
    }



    private fun announcement_list_link() {

        val url = Static_Domine_Name.domine_name+"json/announment_list.php?user_id="+user_id_st

        Log.e("Fragment_Announcement", " url_category ==>" + url)

        simpleProgressBar.setVisibility(View.VISIBLE)

        val jsonObject = @RequiresApi(Build.VERSION_CODES.O)
        object : JsonObjectRequest(Method.GET, url, null, Response.Listener<JSONObject> { response ->

            Log.e("Fragment_Announcement", " response_category ==>" + response)

            announcement_list.clear();

            try {
                val json = JSONObject(response.toString())// convert String to JSONObject

                val ja = json.getJSONArray("result")

                for (i in 0 until ja.length()) {

                    val jsonObject = ja.getJSONObject(i);

                    val message = jsonObject.getString("message")
                    val name = jsonObject.getString("name")
                    val title = jsonObject.getString("title")
                    val description = jsonObject.getString("description")
                    val created_date = jsonObject.getString("created")

                    if(message.equals("success")){
                        val m = Announcement_Array(i.toString(),name,title,description,created_date,"0");
                        announcement_list.add(m)
                    }else{

                    }

                }
                Log.e("Fragment_Announcement", "announcement_list ==>" + announcement_list.size);

            } catch (e: JSONException) {
                e.printStackTrace()
            }

            simpleProgressBar.setVisibility(View.GONE)

            if (announcement_list.size > 0) {

                if (getActivity()!=null){
                    adapter_pager = Adapter_Announcement(activity, announcement_list)
                    viewPager.adapter = adapter_pager
                    viewPager.setClipToPadding(false)
                    viewPager.setPadding(40, 0, 40, 0)
                    viewPager.offscreenPageLimit = 3

                }else{
                    // Toast.makeText(activity, "Activity Empty", Toast.LENGTH_SHORT).show()
                }

            } else {
                val mdToast: MDToast = MDToast.makeText(activity, "No Announcement Found", MDToast.LENGTH_SHORT, MDToast.TYPE_INFO);
                mdToast.setGravity(Gravity.CENTER, 0, 0)
                mdToast.show();
            }


        }, Response.ErrorListener { error ->

            simpleProgressBar.setVisibility(View.GONE)

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

        }) {}
        AppController.getInstance().addToRequestQueue(jsonObject)

        jsonObject.retryPolicy = DefaultRetryPolicy(20 * 30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)

    }


}

