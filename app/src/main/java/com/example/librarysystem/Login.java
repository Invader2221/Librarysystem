package com.example.librarysystem;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.librarysystem.Utils.ResponseHandler;
import com.example.librarysystem.Utils.Util;
import com.example.librarysystem.Utils.WebService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.example.librarysystem.Utils.Util.PREFS_NAME;

public class Login extends AppCompatActivity implements ResponseHandler {

    String userName, password, getUserName;
    TextView registerText;
    EditText userField, passField, passField1;
    Button btn_login;
    private ProgressDialog progressDialog;
    public static final String MY_PREFS_NAME = "UserLogin";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().setTitle("Login");

        userField = (EditText) findViewById(R.id.login_user);
        passField = (EditText) findViewById(R.id.login_password_input);
        btn_login = findViewById(R.id.login_btn);
        registerText = (TextView) findViewById(R.id.registerTextView);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                userName = userField.getText().toString();
                password = passField.getText().toString();
                if (userName.equals("")) {
                    Util.showDialog(Login.this, "Error!", "Please enter username!");
                } else if (password.equals("")) {
                    Util.showDialog(Login.this, "Error!", "Please enter password!");
                } else if (password.length() < 8) {
                    passField.setError("Minimum length of password is 8 characters!");
                    passField.requestFocus();

                }  else {
                    progressDialog = ProgressDialog.show(Login.this, "Please wait...", "Retrieving data ...", true);
                    WebService.login(Login.this, userName, password, progressDialog, Login.this);

                }

            }
        });


        registerText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this, Signup.class));
            }
        });

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        if (settings.getBoolean("USER_LOGGED", false)) {
            Intent intent = new Intent(Login.this, MainActivity.class);
            startActivity(intent);
            Login.this.finish();
        }

    }


    @Override
    public void serviceResponse(final JSONObject response,  JSONArray jsonArray, String tag) throws JSONException {

        passField1 = (EditText) findViewById(R.id.login_password_input);
        String password1 = response.get("password").toString();
        String userName = response.get("userName").toString();

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if (progressDialog != null) {
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                }

                Log.e("userName", userName);
                if (userName.equals("null")) {
                    Util.showDialog(Login.this, "Error!", "Username not found!");
                } else if (!password1.equals(passField.getEditableText().toString())) {
                    Util.showDialog(Login.this, "Error!", "Password is not matched!");
                } else if (password1.equals(passField.getEditableText().toString())) {
                    Toast.makeText(Login.this, "Successful login", Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(Login.this, MainActivity.class);
                    SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
                    editor.putBoolean("USER_LOGGED", true);
//                    getUserName = userName;
                    editor.putString("loggedUserName", userName);
//                    intent.putExtra("userName",userName);
                    editor.apply();
                    startActivity(intent);
                    Login.this.finish();

                }

            }
        });
    }
}
