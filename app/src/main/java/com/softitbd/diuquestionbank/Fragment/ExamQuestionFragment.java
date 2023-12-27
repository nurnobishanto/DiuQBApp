package com.softitbd.diuquestionbank.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.softitbd.diuquestionbank.Adapter.ListAdapter;
import com.softitbd.diuquestionbank.Constants;
import com.softitbd.diuquestionbank.MainActivity;
import com.softitbd.diuquestionbank.Model.ListModel;
import com.softitbd.diuquestionbank.PdfViewActivity;
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


public class ExamQuestionFragment extends Fragment {

    private Spinner spinnerSemester;
    private Spinner spinnerDepartment;
    private Spinner spinnerYear;
    private List<ListModel> semesterList;
    private List<ListModel> departmentList;
    private List<ListModel> yearList;
    private Button btnQuestion;
    private Button btnSolutions;
    private Button btnHandNotes;
    private Button btnSlides;

    public ExamQuestionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_exam_question, container, false);

        spinnerSemester = view.findViewById(R.id.spinnerSemester);
        spinnerDepartment = view.findViewById(R.id.spinnerDepartment);
        spinnerYear = view.findViewById(R.id.spinnerYear);
        btnQuestion = view.findViewById(R.id.btnQuestion);
        btnSolutions = view.findViewById(R.id.btnSolutions);
        btnHandNotes = view.findViewById(R.id.btnHandNotes);
        btnSlides = view.findViewById(R.id.btnSlides);


        semesterList = new ArrayList<>();
        departmentList = new ArrayList<>();
        yearList = new ArrayList<>();
        // Load semester list from API
        // Load semester, department, and year lists from API
        loadList(Constants.SEMESTER_LIST_ENDPOINT, semesterList, spinnerSemester,"Select Semester");
        loadList(Constants.DEPARTMENT_LIST_ENDPOINT, departmentList, spinnerDepartment,"Select Department");
        loadList(Constants.YEAR_LIST_ENDPOINT, yearList, spinnerYear, "Select Year");


        btnQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get selected items from spinners
                int selectedSemesterId = semesterList.get(spinnerSemester.getSelectedItemPosition()).getId();
                int selectedDepartmentId = departmentList.get(spinnerDepartment.getSelectedItemPosition()).getId();
                int selectedYearId = yearList.get(spinnerYear.getSelectedItemPosition()).getId();

                if (selectedSemesterId != 0 && selectedDepartmentId != 0 && selectedYearId != 0){
                    // Create an Intent to start the new activity
                    Intent intent = new Intent(requireContext(), PdfViewActivity.class);

                    // Pass the selected values to the new activity
                    intent.putExtra("SEMESTER_ID", selectedSemesterId);
                    intent.putExtra("DEPARTMENT_ID", selectedDepartmentId);
                    intent.putExtra("YEAR_ID", selectedYearId);
                    intent.putExtra("type", "question");
                    intent.putExtra("title", "All PDF Question");

                    // Start the new activity
                    startActivity(intent);
                }else {
                    Utils.showToast(getContext(),"Select all field");
                }

            }
        });
        btnSolutions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (PreferencesUtils.getUserStatus(getContext()).equals("active")){
                    // Get selected items from spinners
                    int selectedSemesterId = semesterList.get(spinnerSemester.getSelectedItemPosition()).getId();
                    int selectedDepartmentId = departmentList.get(spinnerDepartment.getSelectedItemPosition()).getId();
                    int selectedYearId = yearList.get(spinnerYear.getSelectedItemPosition()).getId();

                    if (selectedSemesterId != 0 && selectedDepartmentId != 0 && selectedYearId != 0){
                        // Create an Intent to start the new activity
                        Intent intent = new Intent(requireContext(), PdfViewActivity.class);

                        // Pass the selected values to the new activity
                        intent.putExtra("SEMESTER_ID", selectedSemesterId);
                        intent.putExtra("DEPARTMENT_ID", selectedDepartmentId);
                        intent.putExtra("YEAR_ID", selectedYearId);
                        intent.putExtra("type", "solution");
                        intent.putExtra("title", "All Solutions");

                        // Start the new activity
                        startActivity(intent);
                    }else {
                        Utils.showToast(getContext(),"Select all field");
                    }
                }else {
                    Utils.showSubscriptionDialog(getContext(),"Subscribe our monthly package (Tk. 20)  to access our premium features", new Runnable() {
                        @Override
                        public void run() {
                            // Call your subscription function here
                            Utils.Subscribe(getContext(),2);
                        }
                    });
                }


            }
        });
        btnHandNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (PreferencesUtils.getUserStatus(getContext()).equals("active")){
                    // Get selected items from spinners
                    int selectedSemesterId = semesterList.get(spinnerSemester.getSelectedItemPosition()).getId();
                    int selectedDepartmentId = departmentList.get(spinnerDepartment.getSelectedItemPosition()).getId();
                    int selectedYearId = yearList.get(spinnerYear.getSelectedItemPosition()).getId();

                    if (selectedSemesterId != 0 && selectedDepartmentId != 0 && selectedYearId != 0){
                        // Create an Intent to start the new activity
                        Intent intent = new Intent(requireContext(), PdfViewActivity.class);

                        // Pass the selected values to the new activity
                        intent.putExtra("SEMESTER_ID", selectedSemesterId);
                        intent.putExtra("DEPARTMENT_ID", selectedDepartmentId);
                        intent.putExtra("YEAR_ID", selectedYearId);
                        intent.putExtra("type", "hand_note");
                        intent.putExtra("title", "All Hand Notes");

                        // Start the new activity
                        startActivity(intent);
                    }else {
                        Utils.showToast(getContext(),"Select all field");
                    }
                }else {
                    Utils.showSubscriptionDialog(getContext(),"Subscribe our monthly package (Tk. 20)  to access our premium features", new Runnable() {
                        @Override
                        public void run() {
                            // Call your subscription function here
                            Utils.Subscribe(getContext(),2);
                        }
                    });
                }


            }
        });
        btnSlides.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (PreferencesUtils.getUserStatus(getContext()).equals("active")){
                    // Get selected items from spinners
                    int selectedSemesterId = semesterList.get(spinnerSemester.getSelectedItemPosition()).getId();
                    int selectedDepartmentId = departmentList.get(spinnerDepartment.getSelectedItemPosition()).getId();
                    int selectedYearId = yearList.get(spinnerYear.getSelectedItemPosition()).getId();

                    if (selectedSemesterId != 0 && selectedDepartmentId != 0 && selectedYearId != 0){
                        // Create an Intent to start the new activity
                        Intent intent = new Intent(requireContext(), PdfViewActivity.class);

                        // Pass the selected values to the new activity
                        intent.putExtra("SEMESTER_ID", selectedSemesterId);
                        intent.putExtra("DEPARTMENT_ID", selectedDepartmentId);
                        intent.putExtra("YEAR_ID", selectedYearId);
                        intent.putExtra("type", "slide");
                        intent.putExtra("title", "All Slides");

                        // Start the new activity
                        startActivity(intent);
                    }else {
                        Utils.showToast(getContext(),"Select all field");
                    }
                }else {
                    Utils.showSubscriptionDialog(getContext(),"Subscribe our monthly package (Tk. 20)  to access our premium features", new Runnable() {
                        @Override
                        public void run() {
                            // Call your subscription function here
                            Utils.Subscribe(getContext(),2);
                        }
                    });
                }


            }
        });
        return view;
    }
    private void loadList(String apiUrl, List<ListModel> list, Spinner spinner, String select_option) {
        // Replace the following URL with your API endpoint

        // Use Volley to make a POST request with Bearer token
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, apiUrl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // Assuming the semester data is an array named "semester_list" in the JSON response
                            JSONArray semestersArray = response.getJSONArray("list");
                            ListModel lm =  new ListModel(0,select_option);
                            list.add(lm);
                            // Populate the list with semester objects
                            for (int i = 0; i < semestersArray.length(); i++) {
                                JSONObject semesterObject = semestersArray.getJSONObject(i);
                                int id = semesterObject.getInt("id");
                                String name = semesterObject.getString("name");
                                ListModel listModel =  new ListModel(id,name);
                                list.add(listModel);
                            }

                            ListAdapter adapter = new ListAdapter(getContext(),list);


                            // Apply the adapter to the spinner
                            spinner.setAdapter(adapter);

                            // Set a listener to handle item selection
                            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                                    // Retrieve the selected semester ID
                                    int selectedSemesterId = list.get(position).getId();
                                    // Do something with the selected ID
                                    // For example, showToast("Selected Semester ID: " + selectedSemesterId);
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parentView) {
                                    // Do nothing here
                                }
                            });

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                // Include the Bearer token in the headers
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer "+ PreferencesUtils.getAccessToken(getContext()));
                headers.put("Content-Type", "application/json"); // Adjust content type based on your needs
                return headers;
            }
        };

        // Add the request to the RequestQueue
        Volley.newRequestQueue(requireContext()).add(jsonObjectRequest);
    }

}