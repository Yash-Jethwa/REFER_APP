package com.example.mini_project;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;


public class HomeActivity extends AppCompatActivity {
    TextView referralCodeTextView;
    private static final String SHARED_PREF_NAME = "your_app_shared_prefs";
    private static final String KEY_REFERRAL_CODE = "referral_code";
    private String referralCode;
    Boolean flag = false;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        referralCodeTextView = findViewById(R.id.textView);

        // Generate a referral code

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        referralCode = sharedPreferences.getString(KEY_REFERRAL_CODE, null);

        if (referralCode == null) {
            flag = true;
            referralCode = generateReferralCode();
            sendReferralCodeToServer(referralCode);
            sharedPreferences.edit().putString(KEY_REFERRAL_CODE, referralCode).apply();
        }

//                sendReferralCodeToServer(referralCode);
        // Set the generated referral code to the TextView
        referralCodeTextView.setText(referralCode);
    }

    private void sendReferralCodeToServer(String code) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://192.168.142.8/referapp/test.php"; // Replace with your server URL
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("response", response);
                Toast.makeText(HomeActivity.this, "Referral code sent successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(HomeActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("error", "" + error);
                Toast.makeText(HomeActivity.this, "Failed to send referral code: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("code", code);
                return map;
            }
        };
        queue.add(stringRequest);
    }

    // Method to generate a random alphanumeric code
    private String generateReferralCode() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder code = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 6; i++) { // Adjust 6 as per your requirement for length of code
            code.append(characters.charAt(random.nextInt(characters.length())));
        }
        return code.toString();
    }

    // Method to handle click event of the share button
    // Method to handle click event of the share button
    public void shareReferralCode(View view) {
        String referralCode = referralCodeTextView.getText().toString();
        if (!referralCode.isEmpty()) {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, "My referral code is: " + referralCode);
            sendIntent.setType("text/plain");
            sendIntent.setPackage("com.whatsapp");
            try {
                startActivity(sendIntent);
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(HomeActivity.this, "WhatsApp not installed", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(HomeActivity.this, "Referral code is empty", Toast.LENGTH_SHORT).show();
        }
    }
}