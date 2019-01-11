package package_profile

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
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
import android.text.TextUtils
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.inputmethod.InputMethodManager
import android.widget.*
import com.android.volley.*
import com.android.volley.toolbox.JsonObjectRequest
import com.exarcplus.exarctivity.Activity_Main
import com.exarcplus.exarctivity.R
import com.rengwuxian.materialedittext.MaterialEditText
import com.squareup.picasso.Picasso
import com.valdesekamdem.library.mdtoast.MDToast
import de.hdodenhof.circleimageview.CircleImageView
import imageCropPackage.Activity_Crop
import org.json.JSONException
import org.json.JSONObject
import setting_package.AppController
import setting_package.SessionManager
import setting_package.Static_Domine_Name
import typeface.CustomFontTextView
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.*


class Activity_Edit_Profile :AppCompatActivity(){


    lateinit var back_lay: LinearLayout
    lateinit var camera_lay: LinearLayout
    lateinit var submit_tv: CustomFontTextView
    lateinit var click_all: LinearLayout
    lateinit var click_full: RelativeLayout
    lateinit var rel_img: RelativeLayout

    lateinit var edt_phone: MaterialEditText
    lateinit var edt_name: MaterialEditText
    lateinit var profile_iv: CircleImageView

    lateinit var date_tv: CustomFontTextView
    lateinit var filter_lay: RelativeLayout


    lateinit var session: SessionManager;
    var token_st = ""; var user_id_st = "";
    var name_st="";  var pro_img_st="";  var email_st="";  var phone_st="";  var dob_st="";

    //======= Image select from gallery and camera=========//

    internal lateinit var mFileTemp: File
    companion object {
        private val REQUEST_WRITE_STORAGE = 112
        private val REQUEST_ID_MULTIPLE_PERMISSIONS = 112
    }
    private val IMAGE_REQUEST_CODE = 7433
    private val CAPTURE_IMAGE_FILE_PROVIDER = "com.exarcplus.exarctivity.fileprovider"
    lateinit var GalIntent: Intent
    internal var mImageUri: Uri? = null
    var crop_activity_st="";

    //======= Image select from gallery and camera=========//

    lateinit var simpleProgressBar: ProgressBar;
    var show_date_st = ""; var send_date_st = "";
    var mYear: Int = 0; var mMonth: Int = 0; var mDay: Int = 0;



    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        back_lay = findViewById(R.id.back_lay);
        camera_lay = findViewById(R.id.camera_lay);
        submit_tv = findViewById(R.id.submit_tv);
        edt_phone = findViewById(R.id.edt_phone);
        edt_name = findViewById(R.id.edt_name);
        click_full = findViewById(R.id.click_full);
        click_all = findViewById(R.id.click_all);
        profile_iv = findViewById(R.id.profile_iv);
        rel_img = findViewById(R.id.rel_img);
        simpleProgressBar = findViewById(R.id.simpleProgressBar);
        date_tv = findViewById(R.id.date_tv);
        filter_lay = findViewById(R.id.filter_lay);

        session = SessionManager(this)

        val user = session.userDetails;
        user_id_st = user.get(SessionManager.KEY_User_id)!!;



        if(user.get(SessionManager.KEY_NAME)!=null && !user.get(SessionManager.KEY_NAME).equals("")){
            edt_name.setText(user.get(SessionManager.KEY_NAME)!!)
            name_st = user.get(SessionManager.KEY_NAME)!!
        }else{
            edt_name.setText("")
            name_st="";
        }


        if(user.get(SessionManager.KEY_EMAIL)!=null && !user.get(SessionManager.KEY_EMAIL).equals("")){
            email_st = user.get(SessionManager.KEY_EMAIL)!!
        }else{
            email_st="";
        }


        if(user.get(SessionManager.KEY_Mobile)!=null && !user.get(SessionManager.KEY_Mobile).equals("")){
            edt_phone.setText(user.get(SessionManager.KEY_Mobile)!!)
            phone_st = user.get(SessionManager.KEY_Mobile)!!
        }else{
            edt_phone.setText("")
            phone_st="";
        }


        if(user.get(SessionManager.KEY_DOB)!=null && !user.get(SessionManager.KEY_DOB).equals("")){
           // date_tv.setText(user.get(SessionManager.KEY_DOB)!!)

            val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
            val show_date_Format = SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH)
            val conv_date:Date = simpleDateFormat.parse(user.get(SessionManager.KEY_DOB)!!)
            date_tv.setText(show_date_Format.format(conv_date))
            send_date_st = user.get(SessionManager.KEY_DOB)!!

        }else{
            date_tv.setText("")
            send_date_st="";
        }


        if(user.get(SessionManager.KEY_Image)!=null && !user.get(SessionManager.KEY_Image).equals("")){
            pro_img_st = user.get(SessionManager.KEY_Image)!!
            Picasso.with(this).load(pro_img_st).placeholder(R.mipmap.profile_gray).into(profile_iv)
        }else{
            profile_iv.setImageResource(R.mipmap.profile_gray)
            pro_img_st="";
        }



        back_lay.setOnClickListener(View.OnClickListener {
            val intent = Intent(this,Activity_Main::class.java)
            intent.putExtra("sub_fragment","profile")
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        })


        camera_lay.setOnClickListener(View.OnClickListener {
            //requestPermission();
            checkAndRequestPermissions()
        })

        rel_img.setOnClickListener(View.OnClickListener {
            //requestPermission();
            checkAndRequestPermissions()
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




        submit_tv.setOnClickListener(View.OnClickListener {

            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(submit_tv.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS)

            if (edt_name.getText().toString().trim({ it <= ' ' }).isEmpty()) {
                edt_name.setError("Enter your name")
                edt_name.setErrorColor(resources.getColor(R.color.red))

            }else if (edt_phone.getText().toString().trim({ it <= ' ' }).isEmpty()) {
                edt_phone.setError("Enter your mobile number")
                edt_phone.setErrorColor(resources.getColor(R.color.red))
            }else if (TextUtils.isEmpty(edt_phone.getText().toString()) || edt_phone.getText().toString().length < 10) {
                edt_phone.setError("Enter valid 10 digit mobile no.")
                edt_phone.setErrorColor(resources.getColor(R.color.red))
            } else {
                update_profile_link()
            }

        })




        filter_lay.setOnClickListener {

            val c = Calendar.getInstance()
            mYear = c.get(Calendar.YEAR)
            mMonth = c.get(Calendar.MONTH)
            mDay = c.get(Calendar.DAY_OF_MONTH)
            val date_format = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
            val simple_df = SimpleDateFormat("yyyy/M/dd", Locale.ENGLISH)

            val datePickerDialog = DatePickerDialog(this,R.style.datepicker, object : DatePickerDialog.OnDateSetListener {
                override fun onDateSet(view: DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int) {

                    val date_string = (year.toString()+"/"+(monthOfYear + 1)+ "/" +dayOfMonth.toString());

                    val slot_time:Date = simple_df.parse(date_string)

                    val date_11 = date_format.format(slot_time)

                    send_date_st = date_11;

                    if(!date_string.equals("")) {
                        val show_date_format = SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH)
                        show_date_st = show_date_format.format(slot_time)
                    }

                    Log.e("selected_date_calender", " date_sending_2 ==> " + date_11+ ", date_showing ==> " + show_date_st)

                    if(!show_date_st.equals("")){
                        date_tv.setText(show_date_st);
                    }else{
                        date_tv.setText("Please select date");
                        Toast.makeText(this@Activity_Edit_Profile, "Please select date", Toast.LENGTH_SHORT).show()
                    }

                }
            }, mYear, mMonth, mDay)
            // datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
            datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
            datePickerDialog.show()

        }










      //  profile_details_link();

        setup(click_all);

        setup(click_full);


    }//onCreate

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this,Activity_Main::class.java)
        intent.putExtra("sub_fragment","profile")
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()
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
                    val imageUri = FileProvider.getUriForFile(this@Activity_Edit_Profile, CAPTURE_IMAGE_FILE_PROVIDER, image)
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
        Toast.makeText(this@Activity_Edit_Profile, "Try again", Toast.LENGTH_SHORT).show()
    }

    private fun startCrop(imageUri: Uri) {
        Log.e("imageUri", "" + imageUri)
        val myIntent = Intent(this@Activity_Edit_Profile, Activity_Crop::class.java)
        val typeId = imageUri.toString()
        myIntent.putExtra("pic", typeId)
        myIntent.putExtra("back", "edit_profile")
        myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(myIntent)

    }



    private fun update_profile_link() {

        //http://www.exarcplus.com/exarctivity/json/update_profile.php?user_id=&name=&mobile_no=&dob=&address=

        val url = Static_Domine_Name.domine_name+"json/update_profile.php?user_id="+user_id_st+"&name="+edt_name.text.toString().trim().replace(" ","%20")+"&mobile_no="+edt_phone.text.toString().trim()+"&dob="+send_date_st.replace(" ","%20")+"&address=Bangalore"

        simpleProgressBar.setVisibility(View.VISIBLE);
        Log.e("Activity_Edit_Profile", " url_update_profile ==>" + url)

        val jsonObject = @RequiresApi(Build.VERSION_CODES.O)
        object : JsonObjectRequest(Method.GET, url, null, Response.Listener<JSONObject> { response ->

            Log.e("Activity_Edit_Profile", " response_update_profile ==>" + response)
            try {
                val json = JSONObject(response.toString())// convert String to JSONObject

                val ja = json.getJSONArray("result")

                for(i in 0 until ja.length()){
                    val jsonObject = ja.getJSONObject(i);

                    val message = jsonObject.getString("message")

                    if(message.equals("success")) {
                        val mdToast: MDToast = MDToast.makeText(this, "Successfully updated", MDToast.LENGTH_SHORT, MDToast.TYPE_SUCCESS);
                        mdToast.setGravity(Gravity.CENTER_VERTICAL or Gravity.CENTER_HORIZONTAL,0,0);
                        mdToast.show();

                        session.update_birth_date(send_date_st)
                        session.update_name(edt_name.text.toString().trim())
                        session.update_mobile_no(edt_phone.text.toString().trim())


                        val intent = Intent(this,Activity_Main::class.java)
                        intent.putExtra("sub_fragment","profile")
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                        finish()

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







    fun setup(view: View)
    {
        // Set up touch listener for non-text box views to hide keyboard.
        if (view !is EditText) {
            view.setOnTouchListener { v, event ->

                edt_name.isFocusable = false
                edt_name.isCursorVisible = false

                edt_phone.isFocusable = false
                edt_phone.isCursorVisible = false

                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS)

                false
            }
        }
    }








}//Activity_Profile