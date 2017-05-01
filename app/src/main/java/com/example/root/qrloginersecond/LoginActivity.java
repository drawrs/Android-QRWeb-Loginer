package com.example.root.qrloginersecond;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigInteger;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

/**
 * Created by Rizal Khilman on 01/05/17.
 * FB: www.fb.me/rizal.ofdraw
 * IG: www.instagram.com/rz.khilman
 * LINE : rizalkhilman312
 * SMS/WA/TELEGRAM : 083824708398
 * EMAIL : rizal.drawrs@gmail.com
 */
public class LoginActivity extends AppCompatActivity {
    Button btnSaveLogin;
    EditText txtEmail, txtPwd;
    private String email, password;
    AQuery aQuery;
    SessionManager sessionManager;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sessionManager = new SessionManager(getApplicationContext());
        setView();
    }
    public void setView(){
        // edit teks
        txtEmail = (EditText) findViewById(R.id.txtEmail);
        txtPwd = (EditText) findViewById(R.id.txtPwd);
        // button
        btnSaveLogin = (Button) findViewById(R.id.btnSaveLogin);

        btnSaveLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get value and set into var
                email = txtEmail.getText().toString();
                password = Helper.md5(txtPwd.getText().toString());

                txtEmail.setError(null);
                txtPwd.setError(null);

                if (Helper.isEmpty(txtEmail)){
                    txtEmail.setError("Email tidak boleh kosong OM!");
                    txtEmail.requestFocus();
                }  else if (Helper.isEmpty(txtPwd)){
                    txtPwd.setError("Apalagi katasandi, tidak boleh kosong OM!");
                    txtPwd.requestFocus();
                } else {
                    aQuery = new AQuery(LoginActivity.this);
                    loginUser();
                }
            }
        });
    }

    private void loginUser() {
        String url = Config.URL_API;

        hideKeyboard();
        // bikin array
        HashMap<String, String> param = new HashMap<String, String>();

        // bawa nilai kunci
        param.put("email", email);
        param.put("password", password);

        ProgressDialog pg = new ProgressDialog(LoginActivity.this);
        pg.setMessage("Login..");
        pg.setCancelable(false);
        //pg.show();
        aQuery.progress(pg).ajax(url, param, String.class, new AjaxCallback<String>(){
            @Override
            public void callback(String url, String object, AjaxStatus status) {
                String log_status = null, msg = null;
                if (object != null){
                    try {
                        // tampung response JSON
                        JSONObject json = new JSONObject(object);
                        // ambil jadikan variable
                        log_status = json.getString("status");
                        msg = json.getString("msg");
                        // cek
                        if (log_status.equalsIgnoreCase("true")){
                            // kondisi berhasil login
                            // disable
                            btnSaveLogin.setEnabled(false);

                            //btnQRLogin.setEnabled(true);
                            // buat session
                            sessionManager.createSession(email, password);
                            startMain();
                            //finish();

                        }
                        // tampilkan pesan
                        Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(LoginActivity.this, "Gagal mengambil data", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });
    }
    public void startMain(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    public void hideKeyboard(){
        View view = this.getCurrentFocus();
        if(view != null){
            // close keyboard
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}
