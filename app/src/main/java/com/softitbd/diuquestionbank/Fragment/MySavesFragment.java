package com.softitbd.diuquestionbank.Fragment;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.softitbd.diuquestionbank.Adapter.SaveDocumentAdapter;
import com.softitbd.diuquestionbank.Constants;
import com.softitbd.diuquestionbank.Model.DocumentModel;
import com.softitbd.diuquestionbank.PreferencesUtils;
import com.softitbd.diuquestionbank.R;
import com.softitbd.diuquestionbank.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MySavesFragment extends Fragment {

    private RecyclerView recyclerView;
    private SaveDocumentAdapter documentAdapter;
    private ProgressDialog progressDialog;
    private RequestQueue requestQueue;
    private TextView notFound;
    public MySavesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_saves, container, false);
        requestQueue = Volley.newRequestQueue(getContext());

        notFound = view.findViewById(R.id.notFound);
        notFound.setVisibility(View.GONE);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        showProgressDialog();
        getSaveData();
        return view;
    }
    private void getSaveData(){
        String authToken = "Bearer "+ PreferencesUtils.getAccessToken(getContext());
        String apiUrl = Constants.GET_SAVE_DOCUMENT_ENDPOINT;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, apiUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                dismissProgressDialog();
                try {
                    JSONArray documentArray = response.getJSONArray("documents");
                    List<DocumentModel> documents = parseJsonArray(documentArray);

                    // Initialize and set the RecyclerView adapter
                    documentAdapter = new SaveDocumentAdapter(getContext(), documents);
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
                Utils.showToast(getContext(), "An error occurred. Please try again later.");
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
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    private void dismissProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
}