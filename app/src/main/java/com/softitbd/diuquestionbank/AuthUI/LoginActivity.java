package com.softitbd.diuquestionbank.AuthUI;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.toolbox.Volley;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.softitbd.diuquestionbank.Constants;
import com.softitbd.diuquestionbank.MainActivity;
import com.softitbd.diuquestionbank.PreferencesUtils;
import com.softitbd.diuquestionbank.R;
import com.softitbd.diuquestionbank.Utils;

import org.json.JSONException;
import org.json.JSONObject;



public class LoginActivity extends AppCompatActivity {
    private EditText editTextUserId, editTextPassword;
    private Button buttonLogin,buttonSignup;
    private ProgressDialog progressDialog;
    private RequestQueue requestQueue;
    private boolean doubleBackToExitPressedOnce = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Login");
        setContentView(R.layout.activity_login);
        // Initialize the RequestQueue
        requestQueue = Volley.newRequestQueue(this);

        initViews();
        setupListeners();
    }

    private void initViews() {
        editTextUserId = findViewById(R.id.editTextUserId);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);
        buttonSignup = findViewById(R.id.buttonSignup);

    }

    private void setupListeners() {
        buttonLogin.setOnClickListener(v -> loginUser());
        buttonSignup.setOnClickListener(v -> navigateToRegisterActivity());
    }

    private void loginUser() {
        String userId = editTextUserId.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (TextUtils.isEmpty(userId) || TextUtils.isEmpty(password)) {
            Utils.showToast(this, "Please enter both user ID and password");
            return;
        }
        showProgressDialog();

        String loginUrl = Constants.LOGIN_ENDPOINT;
        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("user_id", userId);
            requestBody.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, loginUrl, requestBody, new Response.Listener<JSONObject>() {
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
                        Utils.showToast(LoginActivity.this, "Login failed. Please check your credentials.");
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
                Utils.showToast(LoginActivity.this, "An error occurred. Please try again later.");
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
    private void navigateToRegisterActivity() {
        Intent intent = new Intent(this, RegistrationActivity.class);
        startActivity(intent);
        finish();
    }
    private void showProgressDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Logging in...");
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
        Utils.showToast(LoginActivity.this,"Press back again to exit");
        new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 2000);
    }
}
