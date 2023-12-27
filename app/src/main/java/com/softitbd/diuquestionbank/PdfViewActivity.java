package com.softitbd.diuquestionbank;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.softitbd.diuquestionbank.Adapter.DocumentAdapter;
import com.softitbd.diuquestionbank.AuthUI.LoginActivity;
import com.softitbd.diuquestionbank.Model.DocumentModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PdfViewActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private DocumentAdapter documentAdapter;
    private ProgressDialog progressDialog;
    private RequestQueue requestQueue;
    private TextView notFound;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_pdf_view);

        requestQueue = Volley.newRequestQueue(this);
        notFound = findViewById(R.id.notFound);
        notFound.setVisibility(View.GONE);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            int semesterId = extras.getInt("SEMESTER_ID", 0);
            int departmentId = extras.getInt("DEPARTMENT_ID", 0);
            int yearId = extras.getInt("YEAR_ID", 0);

            String type = extras.getString("type", "");
            String title = extras.getString("title", "");

            getSupportActionBar().setTitle(title);
            // Add your logic to handle the data and display PDF content


            recyclerView = findViewById(R.id.recyclerView);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            showProgressDialog();
            getData(semesterId,departmentId,yearId,type);

        }
    }

    private void getData(int semesterId, int departmentId, int yearId, String type) {
        // Obtain the Bearer token from your authentication process
        String authToken = "Bearer "+PreferencesUtils.getAccessToken(PdfViewActivity.this);
        String apiUrl = Constants.GET_DOCUMENT_ENDPOINT;

        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("semester_id", semesterId);
            requestBody.put("department_id", departmentId);
            requestBody.put("year_id", yearId);
            requestBody.put("type", type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, apiUrl, requestBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                dismissProgressDialog();

                try {
                    JSONArray documentArray = response.getJSONArray("documents");
                    List<DocumentModel> documents = parseJsonArray(documentArray);

                    // Initialize and set the RecyclerView adapter
                    documentAdapter = new DocumentAdapter(PdfViewActivity.this, documents);
                    recyclerView.setAdapter(documentAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                dismissProgressDialog();
                Utils.showToast(PdfViewActivity.this, "An error occurred. Please try again later.");
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


    private List<DocumentModel> parseJsonArray(JSONArray jsonArray) {
        List<DocumentModel> documents = new ArrayList<>();
        if (jsonArray.length()==0){
            recyclerView.setVisibility(View.GONE);
            notFound.setVisibility(View.VISIBLE);
            notFound.setText("No data found!");
        }else {
            notFound.setVisibility(View.GONE);
            for (int i = 0; i < jsonArray.length(); i++) {
                try {
                    JSONObject jsonDocument = jsonArray.getJSONObject(i);

                    DocumentModel document = new DocumentModel();
                    document.setId(jsonDocument.getInt("id"));
                    document.setName(jsonDocument.getString("name"));
                    document.setType(jsonDocument.getString("type"));
                    document.setFile(jsonDocument.getString("file"));
                    document.setSemester(jsonDocument.getString("semester"));
                    document.setDepartment(jsonDocument.getString("department"));
                    document.setYear(jsonDocument.getString("year"));
                    document.setUpdatedAt(jsonDocument.getString("updated_at"));

                    documents.add(document);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }


        return documents;
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
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}