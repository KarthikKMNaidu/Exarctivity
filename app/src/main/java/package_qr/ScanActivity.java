package package_qr;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import com.google.zxing.Result;
import com.google.zxing.client.android.camera.CameraConfigurationUtils;
import com.valdesekamdem.library.mdtoast.MDToast;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import me.dm7.barcodescanner.zxing.ZXingScannerView;


public class ScanActivity extends Activity implements ZXingScannerView.ResultHandler {

    private ZXingScannerView mScannerView;
    public static String scan_result="";



    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        mScannerView = new ZXingScannerView(this);   // Programmatically initialize the scanner view
        setContentView(mScannerView);// Set the scanner view as the content view
        scan_result="";


    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera();

        Log.e("ScanActivity","hasFlash() ==> "+hasFlash());
        if(hasFlash()){
            mScannerView.setFlash(true);
        }else{
        }
        // Start camera on resume
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
        // Stop camera on pause
    }




    @Override
    public void handleResult(Result rawResult) {
        // Do something with the result here
        Log.e("ScanActivity","rawResult.getText() ==> "+rawResult.getText()); // Prints scan results
        Log.e("ScanActivity", "getBarcodeFormat() ==> "+rawResult.getBarcodeFormat().toString()); // Prints the scan format (qrcode, pdf417 etc.)

        SimpleDateFormat send_date_Format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);//remove
        Calendar calendar1 = Calendar.getInstance();

        calendar1.add(Calendar.DATE, 0);
        Date d = calendar1.getTime();

       String show_date_st = send_date_Format.format(d);

        scan_result = rawResult.getText();

        if(scan_result.equals(show_date_st)){
            onBackPressed();
        }else{
            scan_result="";
            MDToast mdToast = MDToast.makeText(this, "Invalid QR Code!!!", MDToast.LENGTH_SHORT, MDToast.TYPE_ERROR);
            mdToast.setGravity(Gravity.CENTER, 0, 0);
            mdToast.show();
        }

        // If you would like to resume scanning, call this method below:
       // mScannerView.resumeCameraPreview(this);
    }

    private boolean hasFlash() {
        return getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
    }



}