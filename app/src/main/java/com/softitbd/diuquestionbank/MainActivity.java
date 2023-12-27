package com.softitbd.diuquestionbank;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;
import com.softitbd.diuquestionbank.Adapter.DocumentAdapter;
import com.softitbd.diuquestionbank.AuthUI.LoginActivity;
import com.softitbd.diuquestionbank.Fragment.AboutFragment;
import com.softitbd.diuquestionbank.Fragment.ContactUsFragment;
import com.softitbd.diuquestionbank.Fragment.HomeFragment;
import com.softitbd.diuquestionbank.Fragment.MySavesFragment;
import com.softitbd.diuquestionbank.Fragment.howToUseFragment;
import com.softitbd.diuquestionbank.Model.DocumentModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity {
    private DrawerLayout mDrawer;
    private Toolbar toolbar;
    private NavigationView nvDrawer;
    private ActionBarDrawerToggle drawerToggle;
    private TextView navHeaderUserName;
    private TextView navHeaderUserId;
    private TextView navHeaderEmail;
    private TextView navHeaderStatus;
    private ProgressDialog progressDialog;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestQueue = Volley.newRequestQueue(this);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.menu);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        mDrawer = findViewById(R.id.drawer_layout);
        nvDrawer = findViewById(R.id.nvView);
        // Setup navigation header
        View headerView = nvDrawer.getHeaderView(0);
        navHeaderUserName = headerView.findViewById(R.id.navHeaderUserName);
        navHeaderUserId = headerView.findViewById(R.id.navHeaderUserId);
        navHeaderEmail = headerView.findViewById(R.id.navHeaderEmail);
        navHeaderStatus = headerView.findViewById(R.id.navHeaderStatus);

        // Set user data in navigation header
        setNavHeaderUserData();
        setupDrawerContent(nvDrawer);

        drawerToggle = setupDrawerToggle();
        mDrawer.addDrawerListener(drawerToggle);

        setFragment(new HomeFragment());
        getSupportActionBar().setTitle("Home");
        reloadUserData();
    }

    private void setNavHeaderUserData() {
        // Get user data (replace with your logic to get user data)
        String userName = PreferencesUtils.getUserName(this);
        String userId = PreferencesUtils.getUserId(this);

        // Set user data in navigation header
        navHeaderUserName.setText(userName);
        navHeaderUserId.setText(userId);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private ActionBarDrawerToggle setupDrawerToggle() {
        return new ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.nav_open, R.string.nav_close);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });
    }

    public void selectDrawerItem(MenuItem menuItem) {
        Fragment fragment = new HomeFragment();
        switch (menuItem.getItemId()) {
            case R.id.navHowToUse:
                getSupportActionBar().setTitle("How to use");
                fragment = new howToUseFragment();
                break;
             case R.id.navHome:
                 getSupportActionBar().setTitle("Home");
                fragment = new HomeFragment();
                break;
            case R.id.navSaves:
                getSupportActionBar().setTitle("My Saves");
                fragment = new MySavesFragment();
                break;
            case R.id.navAbout:
                getSupportActionBar().setTitle("About");
                fragment = new AboutFragment();
                break;
            case R.id.navContactUs:
                getSupportActionBar().setTitle("Contact US");
                fragment = new ContactUsFragment();
                break;
            case R.id.navReload:
                reloadUserData();
                break;
            case R.id.navSubscribe:
                Utils.showSubscriptionDialog(MainActivity.this,"Subscribe our monthly package (Tk. 20) to access our premium features", new Runnable() {
                    @Override
                    public void run() {
                        // Call your subscription function here
                        Utils.Subscribe(MainActivity.this,2);
                    }
                });
                break;
            case R.id.navLogout:
                showLogoutConfirmationDialog();
                break;
            default:
                showToast("Default action");
        }

        setFragment(fragment);
        menuItem.setChecked(true);
        // Close the navigation drawer
        mDrawer.closeDrawers();
    }

    private void reloadUserData() {
        setFragment(new HomeFragment());
        getSupportActionBar().setTitle("Home");
        showProgressDialog();
        String authToken = "Bearer "+PreferencesUtils.getAccessToken(MainActivity.this);
        String apiUrl = Constants.ME_ENDPOINT;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, apiUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                dismissProgressDialog();
                try {
                    navHeaderStatus.setText("Free Plan");
                    String status = response.getString("status");
                    PreferencesUtils.saveUserStatus(MainActivity.this,status);
                    if (status.equals("active")){
                        int remaining_days = response.getInt("remaining_days");
                        String end_date = response.getString("end_date");
                        PreferencesUtils.saveUserRemainingDay(MainActivity.this,remaining_days);
                        PreferencesUtils.saveUserEndDate(MainActivity.this,end_date);
                        navHeaderStatus.setText("Active Subscription, Remaining : "+remaining_days+" Days");
                    }
                    JSONObject userObject = response.getJSONObject("user");
                    PreferencesUtils.saveUserId(MainActivity.this, userObject.optString("user_id"));
                    PreferencesUtils.saveUserName(MainActivity.this, userObject.optString("name"));
                    navHeaderUserName.setText(userObject.optString("name"));
                    navHeaderUserId.setText(userObject.optString("user_id"));
                    navHeaderEmail.setText(userObject.optString("email"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                dismissProgressDialog();
                Utils.showToast(MainActivity.this, "An error occurred. Please try again later.");
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                // Include the Bearer token in the headers
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", authToken);
                headers.put("Content-Type", "application/json"); // Adjust content type based on your needs
                return headers;
            }
        };;
        requestQueue.add(jsonObjectRequest);
    }
    private void showProgressDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    private void dismissProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
    private void showLogoutConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Logout")
                .setMessage("Are you sure you want to logout?")
                .setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        PreferencesUtils.saveAccessToken(getApplicationContext(),"");
                        navigateToLogin();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void navigateToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish(); // Close the current activity
    }
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        if (mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    private void setFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.content, fragment);
        fragmentTransaction.commit();
    }
}
