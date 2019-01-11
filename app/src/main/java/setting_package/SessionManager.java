package setting_package;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import com.exarcplus.exarctivity.Activity_Login;
import java.util.HashMap;


public class SessionManager {

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;

    int PRIVATE_MODE = 0;

    private static final String PREF_NAME = "ExarcPref";
    private static final String IS_LOGIN = "IsLoggedIn";
    public static final String KEY_NAME = "name";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_Mobile = "mobile_number";
    public static final String KEY_Image = "image";
    public static final String KEY_Login_status = "login_status";
    public static final String KEY_Token = "token";
    public static final String KEY_User_id = "user_id";
    public static final String KEY_Admin_Status = "admin_status";
    public static final String KEY_Gender = "user_gender";
    public static final String KEY_DOB = "birth_date";



    public SessionManager(Context context){
        this._context = context;

        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();

    }


    public void createLoginSession(String name, String email, String mobile_num, String image, String login_status, String token,
                                   String user_id, String admin_status, String user_gender, String birth_date)
    {
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_NAME, name);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_Mobile, mobile_num);
        editor.putString(KEY_Image, image);
        editor.putString(KEY_Login_status, login_status);
        editor.putString(KEY_Token, token);
        editor.putString(KEY_User_id, user_id);
        editor.putString(KEY_Admin_Status, admin_status);
        editor.putString(KEY_Gender, user_gender);
        editor.putString(KEY_DOB, birth_date);


        editor.commit();
    }



   public void update_name(String name)
    {
        editor.putString(KEY_NAME, name);
        editor.commit();
    }

   public void update_mobile_no(String profession)
    {
        editor.putString(KEY_Mobile, profession);
        editor.commit();
    }

   public void update_birth_date(String birth_date)
    {
        editor.putString(KEY_DOB, birth_date);
        editor.commit();
    }

   public void update_image(String image)
    {
        editor.putString(KEY_Image, image);
        editor.commit();
    }


   public void update_admin_status(String admin_status)
    {
        editor.putString(KEY_Admin_Status, admin_status);
        editor.commit();
    }



    public void checkLogin(){
        if(!this.isLoggedIn()){
            Intent i = new Intent(_context, Activity_Login.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            _context.startActivity(i);
        }else{
            Intent i = new Intent(_context, Activity_Login.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            _context.startActivity(i);
        }
    }



    public HashMap<String, String> getUserDetails(){

        HashMap<String, String> user = new HashMap<String, String>();
        user.put(KEY_NAME, pref.getString(KEY_NAME, null));
        user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));
        user.put(KEY_Mobile, pref.getString(KEY_Mobile, null));
        user.put(KEY_Image, pref.getString(KEY_Image, null));
        user.put(KEY_Login_status, pref.getString(KEY_Login_status, null));
        user.put(KEY_Token, pref.getString(KEY_Token, null));
        user.put(KEY_User_id, pref.getString(KEY_User_id, null));
        user.put(KEY_Admin_Status, pref.getString(KEY_Admin_Status, null));
        user.put(KEY_Gender, pref.getString(KEY_Gender, null));
        user.put(KEY_DOB, pref.getString(KEY_DOB, null));

        return user;
    }

    public void logoutUser(){

        editor.clear();
        editor.commit();

    }

    public boolean isLoggedIn()
    {
        return pref.getBoolean(IS_LOGIN, false);
    }








}
