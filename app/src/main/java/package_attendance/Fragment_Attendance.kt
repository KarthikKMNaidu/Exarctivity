package package_attendance

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.PendingIntent.getActivity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.support.annotation.RequiresApi
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat.startActivity
import android.support.v4.view.GravityCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import com.android.volley.*
import com.android.volley.toolbox.JsonObjectRequest
import com.exarcplus.exarctivity.R
import com.itextpdf.text.*
import com.itextpdf.text.pdf.PdfWriter
import com.rengwuxian.materialedittext.MaterialEditText
import com.valdesekamdem.library.mdtoast.MDToast
import org.json.JSONException
import org.json.JSONObject
import setting_package.AppController
import setting_package.SessionManager
import setting_package.Static_Domine_Name
import typeface.AnyTextView
import typeface.CustomFontTextView
import java.io.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class Fragment_Attendance : Fragment() {

    internal lateinit var header: CustomFontTextView
    internal lateinit var date_tv: CustomFontTextView
    lateinit var filter_lay: LinearLayout

    lateinit var out_time_tv:CustomFontTextView;
    lateinit var in_time_tv:CustomFontTextView;
    lateinit var difference_time_tv:CustomFontTextView;
    lateinit var check_in_lay:LinearLayout;
    lateinit var check_out_lay:LinearLayout;
    lateinit var not_admin_lay:LinearLayout;
    lateinit var admin_lay:LinearLayout;
    lateinit var employee_listView:RecyclerView;



    internal lateinit var simpleDateFormat: SimpleDateFormat
    internal var d = Date()

    var mYear: Int = 0; var mMonth: Int = 0; var mDay: Int = 0;

    lateinit var simpleProgressBar: ProgressBar;
    lateinit var session: SessionManager;
    var admin_status = ""; var user_id_st = ""; var be_in_time = ""; var be_out_time=""; var show_date_st = ""; var send_date_st="";

    var attendance_Array = ArrayList<Attendance_Array>();
    lateinit var adapter_Attendance: Adapter_Attendance;


    lateinit var calendar:Calendar;
    lateinit var calendarView:CalendarView;






    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view1 = inflater!!.inflate(R.layout.fragment_attendance,container,false)



        header = view1.findViewById(R.id.header)
        date_tv = view1.findViewById(R.id.date_tv)
        filter_lay = view1.findViewById(R.id.filter_lay)

        out_time_tv = view1.findViewById(R.id.out_time_tv);
        in_time_tv = view1.findViewById(R.id.in_time_tv);
        check_in_lay = view1.findViewById(R.id.check_in_lay);
        check_out_lay = view1.findViewById(R.id.check_out_lay);
        simpleProgressBar = view1.findViewById(R.id.simpleProgressBar);
        difference_time_tv = view1.findViewById(R.id.difference_time_tv);
        calendarView = view1.findViewById(R.id.calendarView);
        not_admin_lay = view1.findViewById(R.id.not_admin_lay);
        admin_lay = view1.findViewById(R.id.admin_lay);
        employee_listView = view1.findViewById(R.id.employee_listView);



        session = SessionManager(activity)

        val user = session.userDetails;
        user_id_st = user.get(SessionManager.KEY_User_id)!!;


        if(user.get(SessionManager.KEY_Admin_Status)!=null && !user.get(SessionManager.KEY_Admin_Status).equals("")){
            admin_status = user.get(SessionManager.KEY_Admin_Status)!!;
        }else{
            admin_status = "0";
        }

        //admin_status="1"


        simpleDateFormat = SimpleDateFormat("yyyy/MM/dd", Locale.ENGLISH)

        val calendar1 = Calendar.getInstance()

        val show_date_Format = SimpleDateFormat("dd MMM yyyy, EEEE", Locale.ENGLISH)
        val send_date_Format = SimpleDateFormat("yyyy/MM/dd", Locale.ENGLISH)//remove

        Log.e("selected_date_current", " calendar_1 ==> " + calendar1.time)

        calendar1.add(Calendar.DATE, 0)
        d = calendar1.time

        Log.e("selected_date_current", " calendar_2 ==> " + calendar1.time)

        show_date_st = show_date_Format.format(d)
        send_date_st = send_date_Format.format(d)

        if(!show_date_st.equals("")){
            date_tv.setText(show_date_st);
            attendance_report_link(send_date_st);
        }else{
            date_tv.setText("Please select date");
        }

        Log.e("selected_date_current", " date_sending_1 ==> " + send_date_st+ ", date_showing ==> " + show_date_st)


        calendarView.setWeekSeparatorLineColor(resources.getColor(R.color.view))
        calendarView.setFocusedMonthDateColor(Color.GRAY); // set yellow color for the dates of focused month
        val maxDate = Date();//set your max date here
        calendarView.setMaxDate(maxDate.getTime())



        calendarView.setOnDateChangeListener(object : CalendarView.OnDateChangeListener{

            override fun onSelectedDayChange(view: CalendarView, year: Int, month: Int, dayOfMonth: Int) {
                // TODO Auto-generated method stub

                val simpleDateFormat1 = SimpleDateFormat("yyyy/M/dd", Locale.ENGLISH)

                val date_string = (year.toString()+"/"+(month + 1)+ "/" +dayOfMonth.toString());

                val slot_time:Date = simpleDateFormat1.parse(date_string)

                val date_11 = simpleDateFormat.format(slot_time)

                send_date_st = date_11;

                if(!date_string.equals("")) {
                    val show_d = SimpleDateFormat("dd MMM yyyy, EEEE", Locale.ENGLISH)
                    show_date_st = show_d.format(slot_time)
                }else{
                    show_date_st = "";
                }

                Log.e("selected_date_calender", " date_sending_2 ==> " + date_11+ ", date_showing ==> " + show_date_st)

                if(!show_date_st.equals("")){
                    date_tv.setText(show_date_st);
                    attendance_report_link(send_date_st);
                }else{
                    date_tv.setText("Please select date");
                    Toast.makeText(activity, "Please select date", Toast.LENGTH_SHORT).show()
                }
            }
        })






        filter_lay.setOnClickListener {

            //  call_filter_dialog();

            val c = Calendar.getInstance()
            mYear = c.get(Calendar.YEAR)
            mMonth = c.get(Calendar.MONTH)
            mDay = c.get(Calendar.DAY_OF_MONTH)


            val simpleDateFormat1 = SimpleDateFormat("yyyy/M/dd", Locale.ENGLISH)

            val datePickerDialog = DatePickerDialog(activity,R.style.datepicker, object : DatePickerDialog.OnDateSetListener {
                override fun onDateSet(view: DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int) {

                    val date_string = (year.toString()+"/"+(monthOfYear + 1)+ "/" +dayOfMonth.toString());

                    val slot_time:Date = simpleDateFormat1.parse(date_string)

                    val date_11 = simpleDateFormat.format(slot_time)

                    send_date_st = date_11;

                    if(!date_string.equals("")) {
                        val show_d = SimpleDateFormat("dd MMM yyyy, EEEE", Locale.ENGLISH)
                        show_date_st = show_d.format(slot_time)
                    }else{
                        show_date_st = "";
                    }

                    Log.e("selected_date_calender", " date_sending_2 ==> " + date_11+ ", date_showing ==> " + show_date_st)

                    if(!show_date_st.equals("")){
                        date_tv.setText(show_date_st);
                        attendance_report_link(send_date_st);
                    }else{
                        date_tv.setText("Please select date");
                        Toast.makeText(activity, "Please select date", Toast.LENGTH_SHORT).show()
                    }

                }
            }, mYear, mMonth, mDay)
            // datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
            datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
            datePickerDialog.show()

        }




        return view1;



    } //return view1;




    private fun attendance_report_link(date_st:String) {

        //http://www.exarcplus.com/exarctivity/json/attendance_report.php?user_id=1&date=2018-12-06&admin_status=1

        val url = Static_Domine_Name.domine_name+"json/attendance_report.php?user_id="+user_id_st+"&date="+date_st+"&admin_status="+admin_status

        simpleProgressBar.setVisibility(View.VISIBLE);

        Log.e("Fragment_Attendance", " url_details ==>" + url)

        val jsonObject = @RequiresApi(Build.VERSION_CODES.O)
        object : JsonObjectRequest(Method.GET, url, null, Response.Listener<JSONObject> { response ->

            Log.e("Fragment_Attendance", " response_details ==>" + response)
            attendance_Array.clear()
            
            try {
                val json = JSONObject(response.toString())// convert String to JSONObject

                val ja = json.getJSONArray("result")

                for(i in 0 until ja.length()){

                    val jsonObject = ja.getJSONObject(i);
                    val message = jsonObject.getString("message")
                    
                    if(message.equals("success")) {

                        val admin_status_st = jsonObject.getString("admin_status")
                        
                        if(admin_status.equals("1")){

                            val profile_image = jsonObject.getString("profile_image")
                            val name = jsonObject.getString("name")
                            val date = jsonObject.getString("date")
                            val in_time = jsonObject.getString("in_time")
                            val out_time = jsonObject.getString("out_time")
                            
                            val m = Attendance_Array(i.toString(),admin_status_st,profile_image,name,date,in_time,out_time,"0");
                            attendance_Array.add(m)
                            
                        }else{
                            be_in_time = jsonObject.getString("in_time")
                            be_out_time = jsonObject.getString("out_time")
                        }

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

            
            if(admin_status.equals("1")){

                if(attendance_Array.size>0){

                   // createandDisplayPdf()

                    admin_lay.setVisibility(View.VISIBLE);
                    not_admin_lay.setVisibility(View.GONE);

                    adapter_Attendance = Adapter_Attendance(activity,attendance_Array);
                    employee_listView.setLayoutManager(LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false))
                    employee_listView.setAdapter(adapter_Attendance)
                    adapter_Attendance.notifyDataSetChanged()

                    adapter_Attendance.SetOnItemClickListener(object : Adapter_Attendance.OnItemClickListener {
                        override fun onItemClick(view: View, position: Int, status: String, viewPosition: Int) {
                            Log.e("Fragment_Attendance", "adapter_Attendance ==>" + attendance_Array.get(viewPosition).name);
                        }
                    });


                }else{

                    admin_lay.setVisibility(View.GONE);
                    not_admin_lay.setVisibility(View.VISIBLE);

                    val mdToast: MDToast = MDToast.makeText(activity, "No Employee List Found", MDToast.LENGTH_SHORT, MDToast.TYPE_INFO);
                    mdToast.setGravity(Gravity.CENTER, 0, 0)
                    mdToast.show();
                }
                
            }else{

                admin_lay.setVisibility(View.GONE);
                not_admin_lay.setVisibility(View.VISIBLE);


                if(be_in_time!=null && !be_in_time.equals("")){
                    in_time_tv.setText(be_in_time)
                }else {
                    in_time_tv.setText("")
                }


                if(be_out_time!=null && !be_out_time.equals("")){
                    out_time_tv.setText(be_out_time)
                }else{
                    out_time_tv.setText("")
                }


                if(!be_in_time.equals("") && !be_out_time.equals("")){
                    difference_time_tv.setVisibility(View.VISIBLE)

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

                    difference_time_tv.setText("Total Working hrs : "+hours.toString()+" Hrs "+min.toString()+" Min")
                    Log.e("Fragment_Attendance", " difference_hours ==> "+hours)
                }else{
                    difference_time_tv.setVisibility(View.GONE)
                    difference_time_tv.setText("")
                }
                
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



   /* private fun set_pdf_file() {

        fun main(args: Array<String>) {

            Log.e("Fragment_Attendance", " set_pdf_file ==>" + "In")

            val path = Environment.getExternalStorageDirectory().absolutePath + "/Dir"
            val dir = File(path)
            if (!dir.exists())
                dir.mkdirs()
            val file = File(dir, "newFile.pdf")

            val document = Document(PageSize.A4, 70f, 55f, 100f, 55f)
            val output = FileOutputStream(file)

            try {
                val writer = PdfWriter.getInstance(document, output)
                writer.setEncryption("a".toByteArray(), "b".toByteArray(), PdfWriter.ALLOW_PRINTING, PdfWriter.STANDARD_ENCRYPTION_128)
                writer.createXmpMetadata()
                writer.setBoxSize("art", Rectangle(36f, 54f, 559f, 788f))
                document.open()

                for (j in 0 until attendance_Array.size) {
                    document.add(Paragraph(j.toString() + "," + attendance_Array.get(j).getName() + "," + attendance_Array.get(j).getDate() + "," + attendance_Array.get(j).getIn_time() + "," + attendance_Array.get(j).getOut_time() + "\n"))
                }
                document.addCreationDate()
                document.addTitle(show_date_st+", Attendance report")
                document.close()
                writer.close()
            } catch (e: DocumentException) {
                e.printStackTrace()
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            }

        }

    }*/


    fun createandDisplayPdf() {

        Log.e("Fragment_Attendance", " createandDisplayPdf ==> "+"In")

        val doc = Document()

        try {
            val path = Environment.getExternalStorageDirectory().absolutePath + "/Exarctivity"

            val dir = File(path)
            if (!dir.exists())
                dir.mkdirs()

            val file = File(dir, "newFile.pdf")
            val fOut = FileOutputStream(file)

            PdfWriter.getInstance(doc, fOut)

            //open the document
            doc.open()

            val p1 = Paragraph();
            p1.add(Paragraph(show_date_st+"Attendance_report"))
            for (j in 0 until attendance_Array.size) {
                p1.add(Paragraph((j+1).toString() + "," + attendance_Array.get(j).getName() + "," + attendance_Array.get(j).getDate() + "," + attendance_Array.get(j).getIn_time() + "," + attendance_Array.get(j).getOut_time() + "\n"))
            }

            val paraFont = Font(Font.FontFamily.COURIER)
            p1.alignment = Paragraph.ALIGN_CENTER
            p1.font = paraFont
            doc.addCreationDate()
            doc.addTitle(send_date_st+", Attendance report")
            //add paragraph to document

            doc.add(p1)

        } catch (de: DocumentException) {
            Log.e("PDFCreator", "DocumentException:$de")
        } catch (e: IOException) {
            Log.e("PDFCreator", "ioException:$e")
        } finally {
            doc.close()
        }

       // viewPdf(send_date_st+".pdf", "Exarctivity")
    }

    // Method for opening a pdf file
    private fun viewPdf(file: String, directory: String) {
        try {

            val pdfFile = File(Environment.getExternalStorageDirectory().toString() + "/" + directory + "/" + file)
            val path = Uri.fromFile(pdfFile)

            // Setting the intent for pdf reader
            val pdfIntent = Intent(Intent.ACTION_VIEW)
            pdfIntent.setDataAndType(path, "application/pdf")
            pdfIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            activity!!.startActivity(pdfIntent)

        } catch (e: ActivityNotFoundException) {
            Toast.makeText(activity, "Can't read pdf file", Toast.LENGTH_SHORT).show()
        }

    }


}