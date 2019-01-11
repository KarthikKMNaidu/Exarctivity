package com.exarcplus.exarctivity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v7.app.AppCompatActivity
import android.text.Html
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.view.Gravity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import com.android.volley.*
import com.android.volley.toolbox.JsonObjectRequest
import com.rengwuxian.materialedittext.MaterialEditText
import com.valdesekamdem.library.mdtoast.MDToast
import org.json.JSONException
import org.json.JSONObject
import setting_package.AppController
import setting_package.SessionManager
import setting_package.Static_Domine_Name
import typeface.CustomFontTextView


class Activity_Login : AppCompatActivity(){

    lateinit var register_tv: CustomFontTextView;
    lateinit var submit_tv: CustomFontTextView;

    lateinit var click_full: LinearLayout;
    lateinit var full_click: RelativeLayout;

    lateinit var edt_pswd: MaterialEditText;
    lateinit var edt_email: MaterialEditText;
    lateinit var simpleProgressBar:ProgressBar
    lateinit var sessionManager: SessionManager




    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        register_tv = findViewById(R.id.register_tv);
        edt_pswd = findViewById(R.id.edt_pswd)
        edt_email = findViewById(R.id.edt_email)
        submit_tv = findViewById(R.id.submit_tv)
        full_click = findViewById(R.id.full_click)
        click_full = findViewById(R.id.click_full)
        simpleProgressBar = findViewById(R.id.simpleProgressBar);


        sessionManager = SessionManager(this);


        val text = "<font color=#5E5F61>I’m New to Exarcplus?</font> <font color=#303F9F>REGISTER</font>"
        register_tv.setText(Html.fromHtml(text))


        register_tv.setOnClickListener(View.OnClickListener {
            val myIntent = Intent(this@Activity_Login, Activity_SignUp::class.java)
            startActivity(myIntent)
        })



        edt_email.setOnTouchListener { view, motionEvent ->

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                view.setImportantForAutofill(View.IMPORTANT_FOR_AUTOFILL_NO);
                edt_email.isCursorVisible = true
                edt_email.isFocusableInTouchMode = true
            }else{
                edt_email.isCursorVisible = true
                edt_email.isFocusableInTouchMode = true
            }

            false
        }




        edt_pswd.setOnTouchListener { view, motionEvent ->

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                view.setImportantForAutofill(View.IMPORTANT_FOR_AUTOFILL_NO);
                edt_pswd.isCursorVisible = true
                edt_pswd.isFocusableInTouchMode = true
            }else{
                edt_pswd.isCursorVisible = true
                edt_pswd.isFocusableInTouchMode = true
            }

            false
        }


        submit_tv.setOnClickListener(View.OnClickListener {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(submit_tv.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS)

            if (edt_email.getText().toString().trim({ it <= ' ' }).isEmpty() && edt_pswd.getText().toString().trim({ it <= ' ' }).isEmpty()) {
                edt_email.setError("Enter your email id")
                edt_email.setErrorColor(resources.getColor(R.color.red))
                edt_pswd.setError("Enter your password")
                edt_pswd.setErrorColor(resources.getColor(R.color.red))

            }else if (!Patterns.EMAIL_ADDRESS.matcher(edt_email.text.toString()).matches()) {
                edt_email.setError("Enter valid email id")
                edt_email.setErrorColor(resources.getColor(R.color.red))
            }else if (TextUtils.isEmpty(edt_pswd.text.toString())) {
                edt_pswd.setError("Enter your passwod")
                edt_pswd.setErrorColor(resources.getColor(R.color.red))
            }else {
                login_link()
            }

        })
        //

        setup(full_click)

        setup(click_full)

    }//onCreate




    private fun login_link() {

        //http://www.exarcplus.com/exarctivity/json/login.php?email=&password=

        val url = Static_Domine_Name.domine_name+"json/login.php?email="+edt_email.text.toString().trim()+"&password="+edt_pswd.text.toString().trim();
        simpleProgressBar.setVisibility(View.VISIBLE);
        Log.e("Fragment_Event", " url_ongoing ==>" + url)

        val jsonObject = @RequiresApi(Build.VERSION_CODES.O)
        object : JsonObjectRequest(Method.GET, url, null, Response.Listener<JSONObject> { response ->

            Log.e("Fragment_Event", " response_ongoing ==>" + response)
            try {
                val json = JSONObject(response.toString())// convert String to JSONObject

                val ja = json.getJSONArray("result")

                for(i in 0 until ja.length()){
                    val jsonObject = ja.getJSONObject(i);

                    val message = jsonObject.getString("message")

                    if(message.equals("success")) {

                        val id = jsonObject.getString("id")
                        val name = jsonObject.getString("name")
                        val mobile_no = jsonObject.getString("mobile_no")
                        val email = jsonObject.getString("email")
                        val profile_image = jsonObject.getString("profile_image")
                        val dob = jsonObject.getString("dob")
                        val gender = jsonObject.getString("gender")

                        sessionManager.createLoginSession(name,email,mobile_no,profile_image,"0","0",id,"0",gender,dob)

                        val myIntent = Intent(this@Activity_Login, Activity_Main::class.java)
                        myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(myIntent)
                        finish()

                    }/*else if(message.equals("Check your password")) {
                        val mdToast: MDToast = MDToast.makeText(this, "Check your password", MDToast.LENGTH_SHORT, MDToast.TYPE_ERROR);
                        mdToast.setGravity(Gravity.CENTER_VERTICAL or Gravity.CENTER_HORIZONTAL,0,0);
                        mdToast.show();
                    }else if(message.equals("Check your email")) {
                        val mdToast: MDToast = MDToast.makeText(this, "Check your email", MDToast.LENGTH_SHORT, MDToast.TYPE_ERROR);
                        mdToast.setGravity(Gravity.CENTER_VERTICAL or Gravity.CENTER_HORIZONTAL,0,0);
                        mdToast.show();
                    }*/else if(message.equals("Wait for Admin Approval")) {
                        val mdToast: MDToast = MDToast.makeText(this, "Please Wait For Admin Approval", MDToast.LENGTH_SHORT, MDToast.TYPE_INFO);
                        mdToast.setGravity(Gravity.CENTER_VERTICAL or Gravity.CENTER_HORIZONTAL,0,0);
                        mdToast.show();
                    }else {
                        val mdToast: MDToast = MDToast.makeText(this, "" +message, MDToast.LENGTH_SHORT, MDToast.TYPE_ERROR);
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

        })
        {}
        AppController.getInstance().addToRequestQueue(jsonObject)

        jsonObject.retryPolicy = DefaultRetryPolicy(20 * 30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
    }









    fun setup(view: View)
    {
        // Set up touch listener for non-text box views to hide keyboard.
        if (view !is EditText) {
            view.setOnTouchListener { v, event ->
                edt_email.isFocusable = false
                edt_email.isCursorVisible = false

                edt_pswd.isFocusable = false
                edt_pswd.isCursorVisible = false

                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS)

                false
            }
        }
    }

}