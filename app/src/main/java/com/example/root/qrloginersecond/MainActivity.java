package com.example.root.qrloginersecond;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.HashMap;

/**
 * Created by Rizal Khilman on 01/05/17.
 * FB: www.fb.me/rizal.ofdraw
 * IG: www.instagram.com/rz.khilman
 * LINE : rizalkhilman312
 * SMS/WA/TELEGRAM : 083824708398
 * EMAIL : rizal.drawrs@gmail.com
 */
public class MainActivity extends AppCompatActivity {
    SessionManager sessionManager;

    Button btnQRLogin, btnLogout;
    TextView vInfo;
    String email, password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // permisson check
        int permissionCheck = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA);


        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            // should we show an explanation
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.CAMERA)){
                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA}, permissionCheck);
            }
        }
        // cek session
        sessionManager = new SessionManager(getApplicationContext());

        sessionManager.checkLogin();

        setView();
    }
    private void setView(){
        btnQRLogin = (Button) findViewById(R.id.btnQRLogin);
        btnLogout = (Button) findViewById(R.id.btnLogout);

        vInfo = (TextView) findViewById(R.id.vInfo);
        // ambil data
        HashMap<String, String> user = sessionManager.getUserDetails();
        String email = user.get(SessionManager.kunci_email);
        String password = user.get(SessionManager.kunci_pwd);
        vInfo.setText(Html.fromHtml("Welcome, <b>" + email + "</b>"));

    }
    public void startQRScanner(View view) {
        Intent intent = new Intent(this, QRScanner.class);
        HashMap<String, String> user = sessionManager.getUserDetails();
        String usrEmail = user.get(SessionManager.kunci_email);
        String usrPwd = user.get(SessionManager.kunci_pwd);
        intent.putExtra("email", usrEmail);
        intent.putExtra("password", usrPwd);
        startActivity(intent);

    }
    public void logout(View view){
        sessionManager.logout();
        finish();
    }


}
