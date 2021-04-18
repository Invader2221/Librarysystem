package com.example.librarysystem.Utils;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

public class WebService {

    public static final String BASE_URL = "https://cerberus-library-user.herokuapp.com/";
    public static final String LIBRARY_URL = "https://cerberus-library-book.herokuapp.com/";


    private static final String serviceError = "System Error - Please Try Again Later";
    private static final String networkError = "Network Error - Please Try Again Later";


    /**
     * User Login Method
     **/
    public static void login(final ResponseHandler res, String username, String password, final ProgressDialog progressDialog, final Context context) {


        OkHttpClient client = new OkHttpClient();

        final MediaType JSON
                = MediaType.get("application/json; charset=utf-8");

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userName", username);
            jsonObject.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(JSON, jsonObject.toString());

        final okhttp3.Request request = new okhttp3.Request.Builder()
                .url(BASE_URL + "mobile/login")
                .post(body)
                .build();


        Call call = client.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("HttpService", "onFailure() Request was: " + request);
                e.printStackTrace();
                progressDialog.dismiss();
                Util.showDialog(context, "Error!", networkError);
            }

            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {
                try {
                    String jsonData = response.body().string();
                    JSONObject Jobject = new JSONObject(jsonData);
                    res.serviceResponse(Jobject, "");
                } catch (Exception e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                    Util.showDialog(context, "Error!", serviceError);
                }
            }
        });

    }


    public static void register(final ResponseHandler res, String email, String password, String role, String userName, final ProgressDialog progressDialog, final Context context) {


        OkHttpClient client = new OkHttpClient();

        final MediaType JSON
                = MediaType.get("application/json; charset=utf-8");

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("email", email);
            jsonObject.put("password", password);
            jsonObject.put("role", role);
            jsonObject.put("userName", userName);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(JSON, jsonObject.toString());

        final okhttp3.Request request = new okhttp3.Request.Builder()
                .url(BASE_URL + "signup")
                .post(body)
                .build();


        Call call = client.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("HttpService", "onFailure() Request was: " + request);
                e.printStackTrace();
                progressDialog.dismiss();
                Util.showDialog(context, "Error!", networkError);
            }

            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {
                try {
                    String jsonData = response.body().string();
                    JSONObject Jobject = new JSONObject(jsonData);
                    res.serviceResponse(Jobject, jsonData);
                } catch (Exception e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                    Util.showDialog(context, "Error!", serviceError);
                }
            }
        });

    }

}
