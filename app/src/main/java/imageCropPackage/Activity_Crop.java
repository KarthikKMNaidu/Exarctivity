package imageCropPackage;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.exarcplus.exarctivity.Activity_Main;
import com.exarcplus.exarctivity.Activity_SignUp;
import com.exarcplus.exarctivity.R;
import com.valdesekamdem.library.mdtoast.MDToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;

import package_profile.Activity_Edit_Profile;
import setting_package.AppController;
import setting_package.SessionManager;

import static setting_package.Static_Domine_Name.domine_name;


public class Activity_Crop extends FragmentActivity {

    public static final String TAG = "Activity_Crop";
    public static String image_static_list;
    String  userImageName="",gallery_imageUrl="",response_imageUrl="";
    String back="",loo_id="",profile_pic="";
    String sourepath="";

    private ImageCropView imageCropView;
    ProgressDialog progress;
    private int serverResponseCode = 0;
    SessionManager session;
    RequestQueue requestQueue;
     HashMap<String, String> user_session;
    String user_id="", token="", response_path="";

    URL url = null;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop);


       session = new SessionManager(getApplicationContext());

       user_session = session.getUserDetails();

        user_id=user_session.get(SessionManager.KEY_User_id);


        Intent myIntent = getIntent();
         profile_pic=myIntent.getStringExtra("pic");
         back=myIntent.getStringExtra("back");




        if(back==null)
        {
            back="";
        }

        if(loo_id==null)
        {
            loo_id="";
        }

        imageCropView = (ImageCropView)findViewById(R.id.image);

        Uri uri=Uri.parse(profile_pic);

        if(uri!=null)
        {
            Log.e("uri","--->" +uri);
            String filePath = BitmapLoadUtils.getPathFromUri(this, uri);
            if(filePath!=null)
            {
                Log.e("filePath","--->" +filePath);
                Uri filePathUri = Uri.parse(filePath);
                loadAsync(filePathUri);
            }
            else {

               loadAsync(uri);

            }
        }
        else {

           Log.e("uri","--->?" +"null");
            Toast.makeText(this,"Image cannot be processed",Toast.LENGTH_SHORT).show();
        }

        imageCropView.setAspectRatio(3, 2);

        findViewById(R.id.ratio916btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                // onBackPressed();
                Intent intent = new Intent(Activity_Crop.this, Activity_Edit_Profile.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });

        findViewById(R.id.crop_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!imageCropView.isChangingScale()) {

                    Bitmap b = imageCropView.getCroppedImage();
                    bitmapConvertToFile(b);
                    progress = new ProgressDialog(Activity_Crop.this);
                    progress.setMessage("Please wait ...");
                    progress.setCancelable(false);
                    progress.show();

                }
            }
        });

    }//oncreate


    @Override
    public void onBackPressed(){
        super.onBackPressed();
        Intent intent = new Intent(Activity_Crop.this, Activity_Edit_Profile.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }



    private boolean isPossibleCrop(int widthRatio, int heightRatio) {
        int bitmapWidth = imageCropView.getViewBitmap().getWidth();
        int bitmapHeight = imageCropView.getViewBitmap().getHeight();
        if (bitmapWidth < widthRatio && bitmapHeight < heightRatio) {
            return false;
        } else {
            return true;
        }
    }

  public int uploadFile(final String sourceFileUri) {

        int day, month, year;
        int second, minute, hour;
        GregorianCalendar date = new GregorianCalendar();

        day = date.get(Calendar.DAY_OF_MONTH);
        month = date.get(Calendar.MONTH);
        year = date.get(Calendar.YEAR);

        second = date.get(Calendar.SECOND);
        minute = date.get(Calendar.MINUTE);
        hour = date.get(Calendar.HOUR);

        String name=(hour+""+minute+""+second+""+day+""+(month+1)+""+year);
        //String tag=name+".png";
        final String  fileName = name+".png";

        userImageName=fileName;

        Log.e("SourceFile", "path" + sourceFileUri);
        Log.e("FileName", "path" + userImageName);

        HttpURLConnection conn = null;
        DataOutputStream dos = null;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        File sourceFile = new File(sourceFileUri);

        if (!sourceFile.isFile()) {

            progress.dismiss();

            Log.e("uploadFile", "Source File not exist :"+sourceFileUri);

            runOnUiThread(new Runnable() {
                public void run()
                {
                }
            });

            return 0;

        }
        else
        {
            try
            {

                FileInputStream fileInputStream = new FileInputStream(sourceFile);

               if(back.equals("profile")) {
                    String u1 = domine_name+"upload_user_image.php";
                    //String u1 = "http://www.exarcplus.com/exarctivity/upload_user_image.php";
                    url = new URL(u1);
                    Log.e("Activity_Crop ","url ===> "+u1);

                }else if(back.equals("edit_profile")) {
                    String u1 = domine_name+"upload_user_image.php";
                    url = new URL(u1);
                    Log.e("Activity_Crop ","url ===> "+u1);
                }

                // Open a HTTP  connection to  the URL
                conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true); // Allow Inputs
                conn.setDoOutput(true); // Allow Outputs
                conn.setUseCaches(false); // Don't use a Cached Copy
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                conn.setRequestProperty("uploaded_file", fileName);
                dos = new DataOutputStream(conn.getOutputStream());
                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\"" +fileName + "\"" + lineEnd);

                dos.writeBytes(lineEnd);

                // create a buffer of  maximum size
                bytesAvailable = fileInputStream.available();

                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                buffer = new byte[bufferSize];

                // read file and write it into form...
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                while (bytesRead > 0) {

                    dos.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                }
                // send_image multipart form data necesssary after file data...
                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                // Responses from the server (code and message)
                serverResponseCode = conn.getResponseCode();
                String serverResponseMessage = conn.getResponseMessage();

                Scanner result = new Scanner(conn.getInputStream());
                 response_path = result.nextLine();

               /* try {
                    JSONObject jsonObject = new JSONObject(response_path);
                    String result_st = jsonObject.getString("result");

                    if(result_st.equals("success")){

                        if(back.equals("profile")){
                            response_imageUrl = jsonObject.getString("profile_image");
                        }else{
                            response_imageUrl = jsonObject.getString("image");
                        }
                    }else{
                        response_imageUrl="";
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }*/

                Log.e("ImageUploader", "response_path: " + response_path);

                Log.i("uploadFile", "HTTP Response is : " + serverResponseMessage + ": " + serverResponseCode);

                if(serverResponseCode == 200){

                    runOnUiThread(new Runnable() {
                         public void run()
                        {
                            Log.e("Activity_Crop","Back_on_response ==> "+back);

                           if(back.equals("profile")) {

                                String image_url_show=domine_name+response_path+fileName;
                                String image_url_send=response_path+fileName;

                               Log.e("Activity_Crop","profile ==> "+image_url_show);

                               Intent intent = new Intent(Activity_Crop.this, Activity_SignUp.class);
                               intent.putExtra("response_path",image_url_send);
                               intent.putExtra("response_path_show",image_url_show);
                               intent.putExtra("intent_value","activity_crop");
                               intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                               startActivity(intent);
                               finish();

                            }else if(back.equals("edit_profile")){
                               String image_url_show=domine_name+response_path+fileName;
                               String image_url_send=response_path+fileName;
                               Log.e("Activity_Crop","profile ==> "+image_url_show);

                               update_gallery_image_link(image_url_send,image_url_show);
                           }
                        }

                    });
                }

                //close the streams //

                fileInputStream.close();
                dos.flush();
                dos.close();

            } catch (MalformedURLException ex) {


                progress.dismiss();
                ex.printStackTrace();

                runOnUiThread(new Runnable() {
                    public void run()
                    {
                        Toast.makeText(Activity_Crop.this, "MalformedURLException", Toast.LENGTH_SHORT).show();
                    }
                });

                Log.e("Upload file to server", "error: " + ex.getMessage(), ex);
            } catch (Exception e)
            {

                progress.dismiss();
                e.printStackTrace();

                runOnUiThread(new Runnable() {
                    public void run()
                    {
                        Toast.makeText(Activity_Crop.this, "Network error please try after some time ", Toast.LENGTH_SHORT).show();
                    }
                });
                Log.e("Upload file Exception", "Exception : "  + e.getMessage(), e);
            }


            progress.dismiss();
            return serverResponseCode;
        }



    }


    String getStringFromInputStream(HttpURLConnection conn) {
        String strResponse = "";
        try {
            DataInputStream inStream = new DataInputStream(
                    conn.getInputStream());

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    inStream));
            String line;
            while ((line = br.readLine()) != null) {
                strResponse += line;
            }
            br.close();

            inStream.close();
        } catch (IOException ioex) {
            Log.e("Debug", "error: " + ioex.getMessage(), ioex);
        }
        return strResponse;
    }




    public File bitmapConvertToFile(Bitmap bitmap) {

        Log.e("Enter"," ---> ");

        FileOutputStream fileOutputStream = null;
        File bitmapFile = null;
        try {
            File file = new File(Environment.getExternalStoragePublicDirectory("WMG"), "");
            if (!file.exists()) {
                file.mkdir();
            }

            bitmapFile = new File(file, "IMG_" + (new SimpleDateFormat("yyyyMMddHHmmss", Locale.ENGLISH)).format(Calendar.getInstance().getTime()) + ".png");
            fileOutputStream = new FileOutputStream(bitmapFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 75, fileOutputStream);

            final File finalBitmapFile = bitmapFile;

            new Thread(new Runnable() {
                @Override
                public void run() {

                       sourepath=finalBitmapFile.getAbsolutePath();

                    uploadFile(finalBitmapFile.getAbsolutePath());
                }
            }).start();


        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.flush();
                    fileOutputStream.close();
                } catch (Exception e) {
                }
            }
        }

        return bitmapFile;
    }

  private boolean setImageURI(final Uri uri, final Bitmap bitmap) {

        File pdf = new File(uri.getPath());
        imageCropView.setImageFilePath(pdf.toString());
        Log.e("CroppingImageUri", "" + uri);
         //ByteArrayOutputStream bytes = new ByteArrayOutputStream();
         //  bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
         //   imageCropView.setImageFilePath(uri.toString());
         // imageCropView.setImageBitmap(bitmap);

         String path =uri.getPath();
         //decodeFile(path,100,100);

        if (isPossibleCrop(4, 4)) {
            imageCropView.setAspectRatio(3, 2);
        } else {
            Toast.makeText(Activity_Crop.this, "Cannot Crop Try different Image", Toast.LENGTH_SHORT).show();
        }


        return true;
    }



    private void loadAsync( final Uri uri ) {

        DownloadAsync task = new DownloadAsync();
        task.execute(uri );
    }

    class DownloadAsync extends AsyncTask<Uri, Void, Bitmap> implements DialogInterface.OnCancelListener {

        ProgressDialog mProgress;
        private Uri mUri;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            mProgress = new ProgressDialog(Activity_Crop.this);
            mProgress.setIndeterminate( true );
            mProgress.setCancelable( true );
            mProgress.setMessage( "Loading image..." );
            mProgress.setOnCancelListener( this );
            mProgress.show();
        }

        @Override
        protected Bitmap doInBackground( Uri... params ) {
            mUri = params[0];

            Bitmap bitmap = null;

            return bitmap;
        }

        @Override
        protected void onPostExecute( Bitmap result ) {
            super.onPostExecute( result );

            if ( mProgress.getWindow() != null ) {
                mProgress.dismiss();
            }

            setImageURI( mUri, result );

            if ( result != null ) {

                mProgress.dismiss();
            } else {
                mProgress.dismiss();
            }
        }

        @Override
        public void onCancel( DialogInterface dialog ) {
            this.cancel( true );
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();

        }
    }






    public void update_gallery_image_link(String img_send,String img_show) {

        String url = domine_name+"json/update_profile_image.php?user_id="+user_id+"&profile_image="+img_send;

        Log.e("Activity_Crop","url_update_profile ==> "+url);

        StringRequest MyStringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {

                Log.e("Activity_Crop", "response_update_profile ==> " + response);

                try {

                    JSONObject json = new JSONObject(response);// convert String to JSONObject

                    JSONArray ja = json.getJSONArray("result");

                    for (int i=0;i<ja.length();i++){
                        JSONObject jsonObject = ja.getJSONObject(i);

                        String message = jsonObject.getString("message");

                        if (message.equals("success")) {

                            session.update_image(img_show);

                            Intent intent = new Intent(Activity_Crop.this, Activity_Main.class);
                            intent.putExtra("sub_fragment", "profile");
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        } else {
                            MDToast mdToast = MDToast.makeText(Activity_Crop.this, "" + message, MDToast.LENGTH_SHORT, MDToast.TYPE_ERROR);
                            mdToast.setGravity(Gravity.CENTER, 0, 0);
                            mdToast.show();
                        }

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("values", "==>errr");
                VolleyLog.e("Error: " + error);

                    if (error instanceof TimeoutError) {
                        Toast.makeText(Activity_Crop.this, "Oops! Time out Error", Toast.LENGTH_SHORT).show();
                    } else if (error instanceof ServerError) {
                        Toast.makeText(Activity_Crop.this, "Oops! Server Error", Toast.LENGTH_SHORT).show();
                    } else if (error instanceof AuthFailureError) {
                        Toast.makeText(Activity_Crop.this, "Oops! Authentication Failure Error", Toast.LENGTH_SHORT).show();
                    } else if (error instanceof NetworkError) {
                        Toast.makeText(Activity_Crop.this, "Oops! Network Error", Toast.LENGTH_SHORT).show();
                    } else if (error instanceof ParseError) {
                        Toast.makeText(Activity_Crop.this, "Oops! Parse Error", Toast.LENGTH_SHORT).show();
                    }
            }
        });

        AppController.getInstance().addToRequestQueue(MyStringRequest);
        MyStringRequest.setRetryPolicy(new DefaultRetryPolicy(20 * 30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }



}