package com.exarcplus.exarctivity

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.annotation.RequiresApi
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.content.FileProvider
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
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
import com.squareup.picasso.Picasso
import com.valdesekamdem.library.mdtoast.MDToast
import de.hdodenhof.circleimageview.CircleImageView
import imageCropPackage.Activity_Crop
import org.json.JSONException
import org.json.JSONObject
import setting_package.AppController
import setting_package.Static_Domine_Name
import typeface.CustomFontTextView
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.util.*


class Activity_SignUp : AppCompatActivity(){


    lateinit var sign_in_tv: CustomFontTextView;
    lateinit var submit_tv: CustomFontTextView;

    lateinit var male_lay: LinearLayout;
    lateinit var female_lay: LinearLayout;
    lateinit var click_full: LinearLayout;
    lateinit var full_click: LinearLayout;
    lateinit var rel_lay: RelativeLayout;

    lateinit var male_iv: ImageView;
    lateinit var female_iv: ImageView;


    lateinit var edt_name: MaterialEditText;
    lateinit var edt_email: MaterialEditText;
    lateinit var edt_pswd: MaterialEditText;
    lateinit var edt_confirm_pswd: MaterialEditText;
    lateinit var edt_phone: MaterialEditText;

    lateinit var profile_image: CircleImageView;

    var gender_st="";

    //======= Image select from gallery and camera=========//

    internal lateinit var mFileTemp: File
    var mImagePath_send = "";
    var edited_image = "";
    var url_user = "";
    companion object {
        private val REQUEST_ID_MULTIPLE_PERMISSIONS = 112

        var static_name="";var static_email=""; var static_pswd="";var static_confirm_pswd="";
        var static_number=""; var static_gender=""; var static_signup_img="";

    }
    private val IMAGE_REQUEST_CODE = 7433
    private val CAPTURE_IMAGE_FILE_PROVIDER = "com.exarcplus.exarctivity.fileprovider"
    lateinit var GalIntent: Intent
    internal var mImageUri: Uri? = null
    var crop_activity_st="";

    //======= Image select from gallery and camera=========//

    lateinit var simpleProgressBar: ProgressBar;


    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        sign_in_tv = findViewById(R.id.sign_in_tv)
        submit_tv = findViewById(R.id.submit_tv)

        male_lay = findViewById(R.id.male_lay)
        female_lay = findViewById(R.id.female_lay)
        full_click = findViewById(R.id.full_click)
        click_full = findViewById(R.id.click_full)
        rel_lay = findViewById(R.id.rel_lay)

        male_iv = findViewById(R.id.male_iv)
        female_iv = findViewById(R.id.female_iv)

        edt_name = findViewById(R.id.edt_name)
        edt_email = findViewById(R.id.edt_email)
        edt_pswd = findViewById(R.id.edt_pswd)
        edt_confirm_pswd = findViewById(R.id.edt_confirm_pswd)
        edt_phone = findViewById(R.id.edt_phone)
        profile_image = findViewById(R.id.profile_image)
        simpleProgressBar = findViewById(R.id.simpleProgressBar);

        val text = "<font color=#5E5F61>Already have an account?</font> <font color=#303F9F> LOGIN</font>"
        sign_in_tv.setText(Html.fromHtml(text))



        male_lay.setOnClickListener(View.OnClickListener {
            male_iv.setImageResource(R.mipmap.male_color)
            female_iv.setImageResource(R.mipmap.female_gray)
            gender_st="Male";
        })


        female_lay.setOnClickListener(View.OnClickListener {
            male_iv.setImageResource(R.mipmap.male_gray)
            female_iv.setImageResource(R.mipmap.female_color)
            gender_st="Female";
        })




        edt_name.setOnTouchListener { view, motionEvent ->

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                view.setImportantForAutofill(View.IMPORTANT_FOR_AUTOFILL_NO);
                edt_name.isCursorVisible = true
                edt_name.isFocusableInTouchMode = true
            }else{
                edt_name.isCursorVisible = true
                edt_name.isFocusableInTouchMode = true
            }
            false
        }


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




        edt_confirm_pswd.setOnTouchListener { view, motionEvent ->

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                view.setImportantForAutofill(View.IMPORTANT_FOR_AUTOFILL_NO);
                edt_confirm_pswd.isCursorVisible = true
                edt_confirm_pswd.isFocusableInTouchMode = true
            }else{
                edt_confirm_pswd.isCursorVisible = true
                edt_confirm_pswd.isFocusableInTouchMode = true
            }

            false
        }




        edt_phone.setOnTouchListener { view, motionEvent ->

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                view.setImportantForAutofill(View.IMPORTANT_FOR_AUTOFILL_NO);
                edt_phone.isCursorVisible = true
                edt_phone.isFocusableInTouchMode = true
            }else{
                edt_phone.isCursorVisible = true
                edt_phone.isFocusableInTouchMode = true
            }

            false
        }


        sign_in_tv.setOnClickListener(View.OnClickListener {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(sign_in_tv.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS)

            onBackPressed();
        })


        submit_tv.setOnClickListener(View.OnClickListener {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(submit_tv.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS)


            if (edt_name.getText().toString().trim({ it <= ' ' }).isEmpty()) {

                edt_name.setError("Enter your name")
                edt_name.setErrorColor(resources.getColor(R.color.red))

            } else if (!Patterns.EMAIL_ADDRESS.matcher(edt_email.text.toString()).matches()) {

                edt_email.setError("Enter your mail id")
                edt_email.setErrorColor(resources.getColor(R.color.red))

            }else if(!edt_email.text.toString().contains("exarcplus.com")) {

                edt_email.setError("Enter only exarcplus mail id")
                edt_email.setErrorColor(resources.getColor(R.color.red))

            }else if (TextUtils.isEmpty(edt_pswd.text.toString()) || edt_pswd.text.toString().length < 6) {

                edt_pswd.setError("Password must be at least 6 character")
                edt_pswd.setErrorColor(resources.getColor(R.color.red))

            } else if (edt_confirm_pswd.getText().toString().isEmpty()) {

                edt_confirm_pswd.setError("Re enter your password")
                edt_confirm_pswd.setErrorColor(resources.getColor(R.color.red))

            } else if (edt_pswd.text.toString() != edt_confirm_pswd.getText().toString()) {

                edt_confirm_pswd.setError("Confirm password does not match")
                edt_confirm_pswd.setErrorColor(resources.getColor(R.color.red))

            }else if (TextUtils.isEmpty(edt_phone.getText().toString()) || edt_phone.getText().toString().length < 10) {

                edt_phone.setError("Enter valid 10 digit mobile no.")
                edt_phone.setErrorColor(resources.getColor(R.color.red))

            } else if (gender_st.equals("")) {
                val mdToast: MDToast = MDToast.makeText(this, "please select gender", MDToast.LENGTH_SHORT, MDToast.TYPE_ERROR);
                mdToast.setGravity(Gravity.CENTER_VERTICAL or Gravity.CENTER_HORIZONTAL,0,0);
                mdToast.show();
            } else {
                register_link()
            }

        })



        rel_lay.setOnClickListener(View.OnClickListener {
            make_static_value()
            checkAndRequestPermissions()
        })


        val bundle = intent.extras;
        if(bundle!=null){
            if(bundle.getString("intent_value")!=null && bundle.getString("intent_value").equals("activity_crop")){
                get_static_value();
                static_signup_img = bundle.getString("response_path_show")!!
                mImagePath_send = bundle.getString("response_path")!!
            }else{
                static_name=""; static_email=""; static_pswd=""; static_confirm_pswd="";
                static_number=""; static_gender=""; static_signup_img="";mImagePath_send="";
            }
        }else{
            static_name=""; static_email=""; static_pswd=""; static_confirm_pswd="";
            static_number=""; static_gender=""; static_signup_img="";mImagePath_send="";
        }


        if(static_signup_img!=null && !static_signup_img.equals("")){
            Log.e("Activity_SignUp", " static_signup_img111 => " + static_signup_img)
            Picasso.with(this).load(static_signup_img).placeholder(R.mipmap.profile_bg_icon).into(profile_image)
        }else{
            profile_image.setImageResource(R.mipmap.profile_bg_icon)
            static_signup_img="";mImagePath_send="";
        }

        Log.e("Activity_SignUp", " static_signup_img => " + static_signup_img)



        setup(full_click);

        setup(click_full);





    }//onCreate




    fun make_static_value(){

        static_name = edt_name.text.toString()
        static_email = edt_email.text.toString()
        static_gender = gender_st;
        static_pswd = edt_pswd.text.toString()
        static_confirm_pswd = edt_confirm_pswd.text.toString()
        static_number = edt_phone.text.toString()
    }

    fun get_static_value(){

        edt_name.setText(static_name)
        edt_email.setText(static_email)
        edt_pswd.setText(static_pswd)
        edt_confirm_pswd.setText(static_confirm_pswd)
        edt_phone.setText(static_number)
        gender_st = static_gender;


        if(gender_st.equals("Male")){
            male_iv.setImageResource(R.mipmap.male_color)
            female_iv.setImageResource(R.mipmap.female_gray)
        }else if(gender_st.equals("Female")){
            male_iv.setImageResource(R.mipmap.male_gray)
            female_iv.setImageResource(R.mipmap.female_color)
        }else{
            male_iv.setImageResource(R.mipmap.male_gray)
            female_iv.setImageResource(R.mipmap.female_gray)
            gender_st="";
        }

    }



    private fun register_link() {

        val url = Static_Domine_Name.domine_name+"json/register.php?name="+edt_name.text.toString().trim().replace(" ","%20")+"&mobile_no="+edt_phone.text.toString().trim()+"&gender="+gender_st+"&profile_image="+mImagePath_send+"&email="+edt_email.text.toString().trim()+"&password="+edt_pswd.text.toString().trim();
        simpleProgressBar.setVisibility(View.VISIBLE);
        Log.e("Activity_SignUp", " url_register ==>" + url)

        val jsonObject = @RequiresApi(Build.VERSION_CODES.O)
        object : JsonObjectRequest(Method.GET, url, null, Response.Listener<JSONObject> { response ->

            Log.e("Activity_SignUp", " response_register ==>" + response)
            try {
                val json = JSONObject(response.toString())// convert String to JSONObject

                val ja = json.getJSONArray("result")

                for(i in 0 until ja.length()){
                    val jsonObject = ja.getJSONObject(i);

                    val message = jsonObject.getString("message")

                    if(message.equals("success")) {
                        val mdToast: MDToast = MDToast.makeText(this, "Please wait for admin to approve", MDToast.LENGTH_SHORT, MDToast.TYPE_SUCCESS);
                        mdToast.setGravity(Gravity.CENTER_VERTICAL or Gravity.CENTER_HORIZONTAL,0,0);
                        mdToast.show();
                        onBackPressed();
                    }else if(message.equals("Email already exists")) {
                        val mdToast: MDToast = MDToast.makeText(this, "Email already exists", MDToast.LENGTH_SHORT, MDToast.TYPE_WARNING);
                        mdToast.setGravity(Gravity.CENTER_VERTICAL or Gravity.CENTER_HORIZONTAL,0,0);
                        mdToast.show();
                    }else {
                        val mdToast: MDToast = MDToast.makeText(this, "" +message, MDToast.LENGTH_SHORT, MDToast.TYPE_WARNING);
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







    private fun checkAndRequestPermissions(): Boolean {
        val camera = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
        val wtite = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        val read = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
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
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(arrayOfNulls<String>(listPermissionsNeeded.size)), REQUEST_ID_MULTIPLE_PERMISSIONS)
            return false
        }else{
            selectImage()
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
                    // Check for both permissions
                    if (perms[Manifest.permission.WRITE_EXTERNAL_STORAGE] === PackageManager.PERMISSION_GRANTED
                            && perms[Manifest.permission.CAMERA] === PackageManager.PERMISSION_GRANTED &&
                            perms[Manifest.permission.READ_EXTERNAL_STORAGE] === PackageManager.PERMISSION_GRANTED) {
                        Log.e("in fragment on request", "CAMERA & WRITE_EXTERNAL_STORAGE READ_EXTERNAL_STORAGE permission granted")
                        // process the normal flow
                        //else any one or both the permissions are not granted
                        selectImage()
                    } else {
                        Log.e("in fragment on request", "Some permissions are not granted ask again ")
                        //permission is denied (this is the first time, when "never ask again" is not checked) so ask again explaining the usage of permission
                        //                        // shouldShowRequestPermissionRationale will return true
                        //show the dialog or snackbar saying its necessary and try again otherwise proceed with setup.
                        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA) || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
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
                            Toast.makeText(this, "Go to settings and enable permissions", Toast.LENGTH_LONG).show()
                            //                            //proceed with logic by disabling the related features or quit the app.
                        }//permission is denied (and never ask again is  checked)
                        //shouldShowRequestPermissionRationale will return false
                    }
                }
            }
        }

    }

    private fun showDialogOK(message: String, okListener: DialogInterface.OnClickListener) {
        AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", okListener)
                .create()
                .show()
    }


    private fun selectImage() {
        val options = arrayOf<CharSequence>("Take Photo", "Choose from Gallery", "Cancel")

        createTempFile()

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Add Photo!")
        builder.setItems(options, object : DialogInterface.OnClickListener {
            override fun onClick(dialog: DialogInterface, item: Int) {

                if (options[item] == "Take Photo") {

                    val path = File(getFilesDir(), "your/path")
                    if (!path.exists()) path.mkdirs()
                    val image = File(path, "image.jpg")
                    val imageUri = FileProvider.getUriForFile(this@Activity_SignUp, CAPTURE_IMAGE_FILE_PROVIDER, image)
                    val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
                    startActivityForResult(intent, IMAGE_REQUEST_CODE)

                } else if (options[item] == "Choose from Gallery") {
                    GalIntent = Intent(Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    startActivityForResult(Intent.createChooser(GalIntent, "Select Image From Gallery"), 2)
                } else if (options[item] == "Cancel") {
                    dialog.dismiss()
                }
            }
        })

        builder.show()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == IMAGE_REQUEST_CODE && resultCode == AppCompatActivity.RESULT_OK) {


            val path = File(getFilesDir(), "your/path")
            if (!path.exists()) path.mkdirs()
            val imageFile = File(path, "image.jpg")

            val mImagePath = imageFile.getPath()
            val mImageUri = Uri.fromFile(File(mImagePath))

            Log.e("ImageUri", "-->$mImageUri")


            startCrop(mImageUri)


        } else if (requestCode == 2 && resultCode == AppCompatActivity.RESULT_OK) {
            if (data != null) {
                mImageUri = data.data
                startCrop(mImageUri!!)

            }
        }
    }

    private fun createTempFile() {
        val state = Environment.getExternalStorageState()
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            mFileTemp = File(Environment.getExternalStorageDirectory(), "exarc.jpg")
        } else {
            mFileTemp = File(getFilesDir(), "exarc.jpg")
        }
    }

    @Throws(IOException::class)
    private fun copyStream(input: InputStream, output: OutputStream) {
        val buffer = ByteArray(1024)
        val bytesRead = input.read(buffer);
        while ((input.read(buffer)) != -1) {
            output.write(buffer, 0, bytesRead)
        }
    }


    fun errored() {
        Toast.makeText(this@Activity_SignUp, "Try again", Toast.LENGTH_SHORT).show()
    }

    private fun startCrop(imageUri: Uri) {
        Log.e("imageUri", "" + imageUri)
        val myIntent = Intent(this@Activity_SignUp, Activity_Crop::class.java)
        val typeId = imageUri.toString()
        myIntent.putExtra("pic", typeId)
        myIntent.putExtra("back", "profile")
        myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(myIntent)

    }








    fun setup(view: View)
    {
        // Set up touch listener for non-text box views to hide keyboard.
        if (view !is EditText) {
            view.setOnTouchListener { v, event ->
                edt_name.isFocusable = false
                edt_name.isCursorVisible = false

                edt_email.isFocusable = false
                edt_email.isCursorVisible = false

                edt_pswd.isFocusable = false
                edt_pswd.isCursorVisible = false

                edt_confirm_pswd.isFocusable = false
                edt_confirm_pswd.isCursorVisible = false

                edt_phone.isFocusable = false
                edt_phone.isCursorVisible = false

                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS)

                false
            }
        }
    }






}