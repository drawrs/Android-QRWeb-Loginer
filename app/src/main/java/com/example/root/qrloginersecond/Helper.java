package com.example.root.qrloginersecond;

import android.widget.EditText;

import java.math.BigInteger;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by root on 01/05/17.
 * FB: www.fb.me/rizal.ofdraw
 * IG: www.instagram.com/rz.khilman
 * LINE : rizalkhilman312
 * SMS/WA/TELEGRAM : 083824708398
 * EMAIL : rizal.drawrs@gmail.com
 */

public class Helper {
    public static boolean isEmailValid(EditText email){
        // default false
        boolean isValid = false;
        String expression = "^[\\w\\.]+@([\\w\\-]+\\.)+[A-Z] {2,4}$";
        CharSequence inputStr = email.getText().toString();
        Pattern pattern  = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);

        // jika not false
        if (matcher.matches()){
            /*jika valid maka tidak akan dikembalikan*/
            /*ingat, penerapanya harus menggunakan "not"*/
            isValid = true;
        }
        return isValid;
    }
    // cek null input
    public static boolean isEmpty(EditText text){
        // jika teks tidak kosong
        if (text.getText().toString().trim().length() > 0){
            return false;
        } else {
            return true;
        }
    }
    public static String md5(String s){
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("MD5");
            digest.update(s.getBytes(Charset.forName("US-ASCII")), 0, s.length());
            byte[] magnitude = digest.digest();
            BigInteger bi = new BigInteger(1, magnitude);
            String hash = String.format("%0" + (magnitude.length << 1) + "x", bi);
            return hash;
        } catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        }

        return "";
    }
}
