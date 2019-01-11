package package_holidays

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import com.android.volley.*
import com.android.volley.toolbox.JsonObjectRequest
import com.exarcplus.exarctivity.R
import com.valdesekamdem.library.mdtoast.MDToast
import org.json.JSONException
import org.json.JSONObject
import package_holidays.adapter_package.Adapter_Holiday_List
import package_holidays.array_package.Array_Holiday_list
import setting_package.AppController
import setting_package.SessionManager
import setting_package.Static_Domine_Name


class Fragment_Holidays : Fragment() {

    lateinit var holiday_listView:RecyclerView
    lateinit var simpleProgressBar: ProgressBar;
    lateinit var session: SessionManager;
    var token_st = ""; var user_id_st = "";
    var holiday_list = ArrayList<Array_Holiday_list>()
    lateinit var adapter_Holiday_List:Adapter_Holiday_List;

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view1 = inflater!!.inflate(R.layout.fragment_holiday,container,false)

        holiday_listView = view1.findViewById(R.id.holiday_listView)
        simpleProgressBar = view1.findViewById(R.id.simpleProgressBar)



        session = SessionManager(activity)

        val user = session.userDetails;
        user_id_st = user.get(SessionManager.KEY_User_id)!!;




        holiday_list_link()

        return view1;


    } //return view1;




    private fun holiday_list_link() {

        val url = Static_Domine_Name.domine_name+"json/holidays.php?user_id="+user_id_st
        Log.e("Fragment_Holidays", " url_category ==>" + url)

        simpleProgressBar.setVisibility(View.VISIBLE)

        val jsonObject = @RequiresApi(Build.VERSION_CODES.O)
        object : JsonObjectRequest(Method.GET, url, null, Response.Listener<JSONObject> { response ->

            Log.e("Fragment_Holidays", " response_category ==>" + response)

            holiday_list.clear();

            try {
                val json = JSONObject(response.toString())// convert String to JSONObject

                val ja = json.getJSONArray("result")

                for(i in 0 until ja.length()){
                    
                    val jsonObject = ja.getJSONObject(i);
                    
                       val title = jsonObject.getString("title")
                       val from_date = jsonObject.getString("from_date")
                       val to_date = jsonObject.getString("to_date")


                    val m = Array_Holiday_list(i.toString(),title,from_date,to_date,"0");
                    holiday_list.add(m)

                }
                Log.e("Fragment_Holidays", "holiday_list ==>" + holiday_list.size);

            } catch (e: JSONException) {
                e.printStackTrace()
            }

                simpleProgressBar.setVisibility(View.GONE)

                if(holiday_list.size>0){

                    adapter_Holiday_List = Adapter_Holiday_List(activity,holiday_list);
                    holiday_listView.setLayoutManager(LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false))
                    holiday_listView.setAdapter(adapter_Holiday_List)
                    adapter_Holiday_List.notifyDataSetChanged()

                    adapter_Holiday_List.SetOnItemClickListener(object : Adapter_Holiday_List.OnItemClickListener {
                        override fun onItemClick(view: View, position: Int, status: String, viewPosition: Int) {
                            Log.e("Fragment_Holidays", "adapter_Holiday_List ==>" + holiday_list.get(viewPosition).name);
                        }
                    });

                }else{
                    val mdToast: MDToast = MDToast.makeText(activity, "No Holiday List Found", MDToast.LENGTH_SHORT, MDToast.TYPE_INFO);
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

        })
        {}
        AppController.getInstance().addToRequestQueue(jsonObject)

        jsonObject.retryPolicy = DefaultRetryPolicy(20 * 30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)

    }









}