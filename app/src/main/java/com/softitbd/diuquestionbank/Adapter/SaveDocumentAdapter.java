package com.softitbd.diuquestionbank.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.softitbd.diuquestionbank.Constants;
import com.softitbd.diuquestionbank.Model.DocumentModel;
import com.softitbd.diuquestionbank.PreferencesUtils;
import com.softitbd.diuquestionbank.R;
import com.softitbd.diuquestionbank.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SaveDocumentAdapter extends RecyclerView.Adapter<SaveDocumentAdapter.DocumentViewHolder>{

    private List<DocumentModel> documents;
    private Context context;
    private ProgressDialog progressDialog;
    public SaveDocumentAdapter(Context context, List<DocumentModel> documents) {
        this.context = context;
        this.documents = documents;
    }
    @NonNull
    @Override
    public DocumentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_save_document, parent, false);
        return new DocumentViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull DocumentViewHolder holder, int position) {
        DocumentModel document = documents.get(position);
        holder.bind(document);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.openDocument(document.getFile());
            }
        });

    }


    @Override
    public int getItemCount() {
        return documents.size();
    }
    public class DocumentViewHolder extends RecyclerView.ViewHolder {
        private final TextView nameTextView;
        private final TextView semesterTextView;
        private final TextView yearTextView;
        private final TextView departmentTextView;
        private final TextView updatedAtTextView;
        private final Button removeButton;


        public DocumentViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            semesterTextView = itemView.findViewById(R.id.semesterTextView);
            yearTextView = itemView.findViewById(R.id.yearTextView);
            departmentTextView = itemView.findViewById(R.id.departmentTextView);
            updatedAtTextView = itemView.findViewById(R.id.updatedAtTextView);
            removeButton = itemView.findViewById(R.id.removeButton);

        }

        public void bind(DocumentModel document) {
            nameTextView.setText("Name: " + document.getName());
            semesterTextView.setText("Semester: " + document.getSemester());
            yearTextView.setText("Year: " + document.getYear());
            departmentTextView.setText("Department: " + document.getDepartment());
            updatedAtTextView.setText("Updated At: " + document.getUpdatedAt());

            // Handle button clicks here
            removeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    removeDocument(document.getId());
                }
            });

//            openButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    // Handle open button click
//                    openDocument(document.getFile());
//                }
//            });
        }

        private void openDocument(String filename) {
            // Example: open a PDF file using an Intent
            Intent intent = new Intent(Intent.ACTION_VIEW);
            Uri uri = Uri.parse(filename);
            intent.setDataAndType(uri, "application/pdf");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(intent);
        }
    }

    private void removeDocument(int id) {
        showProgressDialog();
        RequestQueue requestQueue;
        requestQueue = Volley.newRequestQueue(context);
        String authToken = "Bearer "+ PreferencesUtils.getAccessToken(context);
        String apiUrl = Constants.REMOVE_DOCUMENT_ENDPOINT;
        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("document_id", id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, apiUrl, requestBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                dismissProgressDialog();
                try {
                    Utils.showToast(context,response.getString("message"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                dismissProgressDialog();
                Utils.showToast(context, "An error occurred. Please try again later.");
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
        };

        requestQueue.add(jsonObjectRequest);
    }
    private void showProgressDialog() {
        progressDialog = new ProgressDialog(context);
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
