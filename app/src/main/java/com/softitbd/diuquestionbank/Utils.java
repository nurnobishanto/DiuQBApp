package com.softitbd.diuquestionbank;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.softitbd.diuquestionbank.Adapter.DocumentAdapter;
import com.softitbd.diuquestionbank.Model.DocumentModel;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Utils {

    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void Subscribe(Context context, int subscription_plan_id){
        RequestQueue requestQueue;
        requestQueue = Volley.newRequestQueue(context);
        String authToken = "Bearer "+PreferencesUtils.getAccessToken(context);
        String apiUrl = Constants.ADD_SUBSCRIPTION_PLANS;

        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("subscription_plan_id", subscription_plan_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, apiUrl, requestBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    showToast(context, response.getString("message"));
                    context.startActivity(new Intent(context,MainActivity.class));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();

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
        };;

        requestQueue.add(jsonObjectRequest);
    }
    public static void showSubscriptionDialog(Context context, String message, final Runnable positiveAction) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Subscribe");
        builder.setMessage(message);

        builder.setPositiveButton("Subscribe", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Call the positive action (subscribe function)
                if (positiveAction != null) {
                    positiveAction.run();
                }
                dialog.dismiss(); // Dismiss the dialog after the action is performed
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss(); // Dismiss the dialog if the user clicks cancel
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
