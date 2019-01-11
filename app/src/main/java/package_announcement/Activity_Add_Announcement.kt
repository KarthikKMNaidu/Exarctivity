package package_announcement

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.*
import com.android.volley.*
import com.android.volley.toolbox.JsonObjectRequest
import com.exarcplus.exarctivity.R
import com.rengwuxian.materialedittext.MaterialEditText
import com.valdesekamdem.library.mdtoast.MDToast
import org.json.JSONException
import org.json.JSONObject
import setting_package.AppController
import setting_package.SessionManager
import setting_package.Static_Domine_Name
import typeface.CustomFontTextView
import java.net.URLEncoder


class Activity_Add_Announcement:AppCompatActivity() {



    lateinit var simpleProgressBar: ProgressBar;
    lateinit var back_lay: LinearLayout
    lateinit var sessionManager: SessionManager
    var token_st = "";  var user_id_st = "";

    lateinit var full_click: RelativeLayout
    lateinit var click_full: LinearLayout
    lateinit var edt_subject: MaterialEditText
    lateinit var edt_message: MaterialEditText
    lateinit var submit_tv: CustomFontTextView


    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_announcement);



        simpleProgressBar = findViewById(R.id.simpleProgressBar)
        full_click = findViewById(R.id.full_click)
        click_full = findViewById(R.id.click_full)
        submit_tv = findViewById(R.id.submit_tv)
        edt_subject = findViewById(R.id.edt_subject)
        edt_message = findViewById(R.id.edt_message)
        back_lay = findViewById(R.id.back_lay)


        back_lay.setOnClickListener {
            onBackPressed()
        }

        edt_message.cancelLongPress();
        edt_subject.cancelLongPress();
        edt_message.setLongClickable(false);
        edt_subject.setLongClickable(false);

        edt_subject.setOnTouchListener { view, motionEvent ->

            edt_subject.isCursorVisible = true
            edt_subject.isFocusableInTouchMode = true

            false
        }



        edt_message.setOnTouchListener { view, motionEvent ->

            edt_message.isCursorVisible = true
            edt_message.isFocusableInTouchMode = true

            false
        }


        edt_subject.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->

            if (actionId == EditorInfo.IME_ACTION_DONE) {
                edt_subject.isFocusable = false
                edt_subject.isCursorVisible = false

                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS)

                return@OnEditorActionListener false
            }
            false
        })



        sessionManager = SessionManager(this);

        val user = sessionManager.userDetails;

        token_st = user.get(SessionManager.KEY_Token)!!;
        user_id_st = user.get(SessionManager.KEY_User_id)!!;



        submit_tv.setOnClickListener(View.OnClickListener {

            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(submit_tv.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS)

            if (edt_subject.text.toString().trim().equals("")) {

                edt_subject.setError("Title field should not be empty")
                edt_subject.setErrorColor(resources.getColor(R.color.red))

            } else if (edt_message.text.toString().trim().equals("")) {
                edt_message.setError("Description field should not be empty")
                edt_message.setErrorColor(resources.getColor(R.color.red))
            }else{
                val encodedString = URLEncoder.encode(edt_message.text.toString(), "UTF-8")
                System.out.format("'%s'\n", encodedString)

                Log.e("Add_Announcement", " encodedString ==> " + encodedString)

                add_announcement_link(encodedString)
            }

        })





        setup(click_full);

        setup(full_click);





    }//onCreate()




    private fun add_announcement_link(description:String) {

        val url = Static_Domine_Name.domine_name+"json/add_announcement.php?user_id="+user_id_st+"&title="+edt_subject.text.toString().replace(" ","%20").trim()+"&description="+description

        // val url = "http://www.exarcplus.com/exarctivity/json/add_announcement.php?user_id=&title=&description="
        Log.e("Add_Announcement", " url_add_announce ==>" + url)

        simpleProgressBar.setVisibility(View.VISIBLE)

        val jsonObject = @RequiresApi(Build.VERSION_CODES.O)
        object : JsonObjectRequest(Method.GET, url, null, Response.Listener<JSONObject> { response ->

            Log.e("Add_Announcement", " response_add_announce ==>" + response)

            try {
                val json = JSONObject(response.toString())// convert String to JSONObject

                val ja = json.getJSONArray("result")

                for (i in 0 until ja.length()) {

                    val jsonObject = ja.getJSONObject(i);

                    val message = jsonObject.getString("message")

                    if(message.equals("success")){
                        val mdToast: MDToast = MDToast.makeText(this, "Successfully Submitted", MDToast.LENGTH_SHORT, MDToast.TYPE_SUCCESS);
                        mdToast.setGravity(Gravity.CENTER_VERTICAL or Gravity.CENTER_HORIZONTAL,0,0);
                        mdToast.show();
                        onBackPressed();
                    }else{
                        val mdToast: MDToast = MDToast.makeText(this, ""+message, MDToast.LENGTH_SHORT, MDToast.TYPE_INFO);
                        mdToast.setGravity(Gravity.CENTER_VERTICAL or Gravity.CENTER_HORIZONTAL,0,0);
                        mdToast.show();
                    }



                }

            } catch (e: JSONException) {
                e.printStackTrace()
            }

            simpleProgressBar.setVisibility(View.GONE)



        }, Response.ErrorListener { error ->

            simpleProgressBar.setVisibility(View.GONE)

            if (error is TimeoutError) {
                Toast.makeText(this, "Oops! Time out Error", Toast.LENGTH_SHORT).show()
            } else if (error is ServerError) {
                Toast.makeText(this, "Oops! Server Error", Toast.LENGTH_SHORT).show()
            } else if (error is AuthFailureError) {
                Toast.makeText(this, "Oops! Authentication Failure Error", Toast.LENGTH_SHORT).show()
            } else if (error is NetworkError) {
                Toast.makeText(this, "Oops! Network Error", Toast.LENGTH_SHORT).show()
            } else if (error is ParseError) {
                Toast.makeText(this, "Oops! Parse Error", Toast.LENGTH_SHORT).show()
            }

        }) {}
        AppController.getInstance().addToRequestQueue(jsonObject)

        jsonObject.retryPolicy = DefaultRetryPolicy(20 * 30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)

    }

















    fun setup(view: View)
    {
        // Set up touch listener for non-text box views to hide keyboard.
        if (view !is EditText) {
            view.setOnTouchListener { v, event ->

                edt_subject.isFocusable = false
                edt_subject.isCursorVisible = false

                edt_message.isFocusable = false
                edt_message.isCursorVisible = false

                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS)

                false
            }
        }
    }





}