package com.softitbd.diuquestionbank;

public class Constants {
    // Replace with your actual base URL
    public static final String BASE_URL = "https://diuqb.chatrachaya.com";
    public static final String API_URL = BASE_URL + "/api/";
    public static final String STORAGE_URL = BASE_URL + "/uploads/";

    // API endpoints
    public static final String LOGIN_ENDPOINT = API_URL + "login";
    public static final String REGISTER_ENDPOINT = API_URL + "register";
    public static final String ME_ENDPOINT = API_URL + "me";
    public static final String SEMESTER_LIST_ENDPOINT = API_URL + "semester-list";
    public static final String DEPARTMENT_LIST_ENDPOINT = API_URL + "department-list";
    public static final String YEAR_LIST_ENDPOINT = API_URL + "year-list";
    public static final String GET_DOCUMENT_ENDPOINT = API_URL + "get-document";
    public static final String SAVE_DOCUMENT_ENDPOINT = API_URL + "save-document";
    public static final String REMOVE_DOCUMENT_ENDPOINT = API_URL + "remove-document";
    public static final String GET_SAVE_DOCUMENT_ENDPOINT = API_URL + "get-save-document";
    public static final String GET_SUBSCRIPTION_PLANS = API_URL + "subscription-plans";
    public static final String ADD_SUBSCRIPTION_PLANS = API_URL + "add-subscription";
    // Add more endpoints as needed
}
