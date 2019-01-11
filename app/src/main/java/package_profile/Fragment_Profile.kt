package package_profile

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import com.afollestad.materialdialogs.DialogAction
import com.afollestad.materialdialogs.MaterialDialog
import com.exarcplus.exarctivity.Activity_Login
import com.exarcplus.exarctivity.Activity_Main
import com.exarcplus.exarctivity.R
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog
import com.github.javiersantos.materialstyleddialogs.enums.Style
import com.squareup.picasso.Picasso
import setting_package.SessionManager
import typeface.CustomFontTextView
import java.text.SimpleDateFormat
import java.util.*


class Fragment_Profile : Fragment() {


    lateinit var logout_lay:LinearLayout;
    lateinit var edit_lay:LinearLayout;
    lateinit var profile_iv: ImageView;
    lateinit var name_tv: CustomFontTextView;
    lateinit var email_tv:CustomFontTextView;
    lateinit var phone_no_tv:CustomFontTextView;
    lateinit var dob_tv:CustomFontTextView;

    lateinit var sessionManager: SessionManager;

    var name_st="";  var pro_img_st="";  var email_st="";  var phone_st="";  var dob_st="";


    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view1 = inflater!!.inflate(R.layout.fragment_profile,container,false)

        logout_lay = view1.findViewById(R.id.logout_lay);
        edit_lay = view1.findViewById(R.id.edit_lay);
        profile_iv = view1.findViewById(R.id.profile_iv);
        name_tv = view1.findViewById(R.id.name_tv);
        email_tv = view1.findViewById(R.id.email_tv);
        phone_no_tv = view1.findViewById(R.id.phone_no_tv);
        dob_tv = view1.findViewById(R.id.dob_tv);

        sessionManager = SessionManager(activity)

        val user = sessionManager.userDetails;





        if(user.get(SessionManager.KEY_NAME)!=null && !user.get(SessionManager.KEY_NAME).equals("")){
            name_tv.setText(user.get(SessionManager.KEY_NAME)!!)
            name_st = user.get(SessionManager.KEY_NAME)!!
        }else{
            name_tv.setText("")
            name_st="";
        }


        if(user.get(SessionManager.KEY_EMAIL)!=null && !user.get(SessionManager.KEY_EMAIL).equals("")){
            email_tv.setText(user.get(SessionManager.KEY_EMAIL)!!)
            email_st = user.get(SessionManager.KEY_EMAIL)!!
        }else{
            email_tv.setText("")
            email_st="";
        }


        if(user.get(SessionManager.KEY_Mobile)!=null && !user.get(SessionManager.KEY_Mobile).equals("")){
            phone_no_tv.setText(user.get(SessionManager.KEY_Mobile)!!)
            phone_st = user.get(SessionManager.KEY_Mobile)!!
        }else{
            phone_no_tv.setText("")
            phone_st="";
        }


        if(user.get(SessionManager.KEY_DOB)!=null && !user.get(SessionManager.KEY_DOB).equals("")){

           val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
           val show_date_Format = SimpleDateFormat("dd MMM yyyy, EEEE", Locale.ENGLISH)

            val conv_date:Date = simpleDateFormat.parse(user.get(SessionManager.KEY_DOB)!!)
            dob_tv.setText(show_date_Format.format(conv_date))
            //date_tv.setText(user.get(SessionManager.KEY_DOB)!!)
            dob_st = user.get(SessionManager.KEY_DOB)!!
        }else{
            dob_tv.setText("")
            dob_st="";
        }


        if(user.get(SessionManager.KEY_Image)!=null && !user.get(SessionManager.KEY_Image).equals("")){
            pro_img_st = user.get(SessionManager.KEY_Image)!!
          Picasso.with(activity).load(pro_img_st).placeholder(R.mipmap.profile_bg_icon).into(profile_iv)
        }else{
            profile_iv.setImageResource(R.mipmap.profile_bg_icon)
            pro_img_st="";
        }

        //Activity_Edit_Profile

        edit_lay.setOnClickListener(View.OnClickListener {
            val intent = Intent(activity,Activity_Edit_Profile::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            activity!!.finish()
        })





        logout_lay.setOnClickListener(View.OnClickListener {

            Log.e("Fragment_Profile", "logout_click ==> is working")

            MaterialStyledDialog.Builder(activity)
                    .setTitle("Exarctivity!")
                    .setDescription("Do you want to logout?")
                    .setStyle(Style.HEADER_WITH_TITLE)
                    .setIcon(R.mipmap.app_icon)
                    .setHeaderColor(R.color.main_color)
                    .setPositiveText("Yes")
                    .setNegativeText("No")
                    .onNegative(object : MaterialDialog.SingleButtonCallback{
                        override fun onClick(dialog: MaterialDialog, which: DialogAction) {
                            // Toast.makeText(this@Activity_Main,"Negative Click",Toast.LENGTH_SHORT).show()
                        }
                    })
                    .onPositive(object : MaterialDialog.SingleButtonCallback {
                        override fun onClick(dialog: MaterialDialog, which: DialogAction) {

                            if (sessionManager.isLoggedIn()) {
                                sessionManager.logoutUser()

                                val intent = Intent(activity, Activity_Login::class.java)
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                                startActivity(intent)
                                activity!!.finish()

                            }else{

                            }

                        }
                    })
                    .show()

            /*if (session.isLoggedIn()) {
                session.logoutUser()
                  val accessToken = AccessToken.getCurrentAccessToken()
                if (accessToken != null) {
                    LoginManager.getInstance().logOut()
                }*/

        })










        return view1;


    } //return view1;












}