package com.example.root.qrloginersecond;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.google.zxing.Result;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

/**
 * Created by Rizal Khilman on 01/05/17.
 * FB: www.fb.me/rizal.ofdraw
 * IG: www.instagram.com/rz.khilman
 * LINE : rizalkhilman312
 * SMS/WA/TELEGRAM : 083824708398
 * EMAIL : rizal.drawrs@gmail.com
 */
public class QRScanner extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    private ZXingScannerView qrScanner;
    private String remoteUrl;
    AQuery aQuery;
    String getEmail, getPwd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrscanner);
        // get data from previous activity
        Intent intent = getIntent();
        getEmail = intent.getStringExtra("email");
        getPwd = intent.getStringExtra("password");
        // start the scanner
        qrScanner = new ZXingScannerView(this);
        setContentView(qrScanner);
        qrScanner.setResultHandler(this);
        qrScanner.startCamera();
    }

    @Override
    protected void onPause() {
        super.onPause();
        qrScanner.stopCamera();
    }

    @Override
    public void handleResult(Result result) {
        // mengirim data ke web
        String newUri = result.getText();
        sendData(newUri);
        // remove double slash, if u wanna resume camera scanner after qr was scanned
        //qrScanner.resumeCameraPreview(this);
    }
    public void sendData(String uri){
        // Create data variable for sent values to server
        remoteUrl = uri;
        aQuery = new AQuery(QRScanner.this);

        // bikin array
        HashMap<String, String> param = new HashMap<String, String>();
        // bawa nilai kunci
        param.put("email", getEmail);
        param.put("password", getPwd);

        aQuery.ajax(remoteUrl, param, String.class, new AjaxCallback<String>(){
            @Override
            public void callback(String url, String object, AjaxStatus status) {
                super.callback(url, object, status);
                if(object != null){
                    try {
                        // buat objek json
                        JSONObject json = new JSONObject(object);

                        String session_status, session_msg;
                        String log_status, log_msg;

                        log_status = json.getString("log_status");
                        log_msg = json.getString("log_msg");

                        session_msg = json.getString("session_msg");
                        session_status = json.getString("session_status");
                        AlertDialog.Builder bl = new AlertDialog.Builder(QRScanner.this);
                        if (session_status.equalsIgnoreCase("true")){

                            if (log_status.equalsIgnoreCase("true")){
                                bl.setTitle("Info");
                            } else {
                                bl.setTitle("Error");
                            }
                            bl.setMessage(log_msg);
                        } else if (session_status.equalsIgnoreCase("false")){
                            bl.setTitle("Error");
                            bl.setMessage(session_msg);
                        } else {
                            bl.setTitle("Error");
                            bl.setMessage("Terjadi kesalahan!");
                        }
                        bl.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // pindah ke activity main
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            }
                        });
                        AlertDialog alertDialog = bl.create();
                        alertDialog.show();


                    } catch (JSONException e){
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}
