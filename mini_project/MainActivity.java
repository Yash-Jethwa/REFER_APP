package com.example.mini_project;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private EditText etReferralCode;
    private Button btnJoin;
    private Button btnSkip;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etReferralCode = findViewById(R.id.etReferralCode);
        btnJoin = findViewById(R.id.btnJoin);
        btnSkip = findViewById(R.id.btnSkip);

        requestQueue = Volley.newRequestQueue(this);

        btnJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data = etReferralCode.getText().toString().trim(); // Get trimmed user input

                // Basic input validation (optional, consider more robust validation)
                if (data.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please enter some data", Toast.LENGTH_SHORT).show();
                } else {
                    submitDataToServer(data);
                }
            }
        });

        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the "Skip" button click
                // Navigate to the homepage (replace HomeActivity.class with your actual homepage activity)
                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });
    }

    private void submitDataToServer(String data) {
        String url = "http://192.168.142.8/referapp/test.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("response", response);
                Toast.makeText(MainActivity.this, "Referral code sent successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("error", "" + error);
                Toast.makeText(MainActivity.this, "Failed to send referral code: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("data", data);
                return map;
            }
        };
        requestQueue.add(stringRequest);
    }
}
