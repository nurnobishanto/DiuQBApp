package com.softitbd.diuquestionbank.AuthUI;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;

import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.softitbd.diuquestionbank.Constants;
import com.softitbd.diuquestionbank.MainActivity;
import com.softitbd.diuquestionbank.PreferencesUtils;
import com.softitbd.diuquestionbank.R;
import com.softitbd.diuquestionbank.Utils;

import org.json.JSONException;
import org.json.JSONObject;

public class RegistrationActivity extends AppCompatActivity {
    private EditText editTextName, editTextUserId, editTextPassword,editTextEmail;
    private Button buttonLogin,buttonSignup;
    private ProgressDialog progressDialog;
    private RequestQueue requestQueue;
    private boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Registration");
        setContentView(R.layout.activity_registration);
        // Initialize the RequestQueue
        requestQueue = Volley.newRequestQueue(this);

        initViews();
        setupListeners();
    }
    private void initViews() {
        editTextName = findViewById(R.id.editTextName);
        editTextUserId = findViewById(R.id.editTextUserId);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextEmail = findViewById(R.id.editTextEmail);
        buttonLogin = findViewById(R.id.buttonLogin);
        buttonSignup = findViewById(R.id.buttonSignup);
    }

    private void setupListeners() {
        buttonSignup.setOnClickListener(v -> registerUser());
        buttonLogin.setOnClickListener(v -> navigateToLoginActivity());
    }

    private void registerUser() {
        String name = editTextName.getText().toString().trim();
        String userId = editTextUserId.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(userId) || TextUtils.isEmpty(password) || TextUtils.isEmpty(email)) {
            Utils.showToast(this, "Please enter al fields");
            return;
        }
        showProgressDialog();
        String registerUrl = Constants.REGISTER_ENDPOINT;

        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("name", name);
            requestBody.put("user_id", userId);
            requestBody.put("password", password);
            requestBody.put("email", email);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, registerUrl, requestBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                dismissProgressDialog();

                try {
                    int status = response.getInt("status");
                    if (status == 1) {
                        JSONObject userObject = response.getJSONObject("user");
                        String accessToken = response.getString("access_token");
                        saveUserData(userObject, accessToken);
                        navigateToHomeActivity();
                    } else {
                        Utils.showToast(RegistrationActivity.this, "Login failed. Please check your credentials.");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                dismissProgressDialog();
                Utils.showToast(RegistrationActivity.this, "An error occurred. Please try again later.");
            }
        });

        requestQueue.add(jsonObjectRequest);
    }

    private void saveUserData(JSONObject userObject, String accessToken) {
        PreferencesUtils.saveUserId(this, userObject.optString("user_id"));
        PreferencesUtils.saveUserName(this, userObject.optString("name"));
        PreferencesUtils.saveAccessToken(this, accessToken);
    }

    private void navigateToHomeActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
    private void navigateToLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
    private void showProgressDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Registration in progress...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    private void dismissProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Utils.showToast(RegistrationActivity.this,"Press back again to exit");
        new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 2000);
    }
}