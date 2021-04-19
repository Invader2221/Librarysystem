package com.example.librarysystem;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.librarysystem.Utils.ResponseHandler;
import com.example.librarysystem.Utils.Util;
import com.example.librarysystem.Utils.WebService;

import org.json.JSONException;
import org.json.JSONObject;

import static com.example.librarysystem.Utils.Util.PREFS_NAME;

public class Login extends AppCompatActivity implements ResponseHandler {

    String userName, password;
    TextView registerText;
    EditText userField, passField, passField1;
    Button btn_login;
    private ProgressDialog progressDialog;

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
                } else {
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
            Intent intent = new Intent(Login.this, HomePage.class);
            startActivity(intent);
            Login.this.finish();
        }

    }


    @Override
    public void serviceResponse(final JSONObject response, String tag) throws JSONException {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                passField1 = (EditText) findViewById(R.id.login_password_input);

                String userName = null;
                String password1 = null;
                try {
                    password1 = response.get("password").toString();
                    userName = response.get("userName").toString();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

//                String enteredPassword = passField.getEditableText().toString();


//        btn_login1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                a = passField1.getText().toString();
//
//            }
//        });

//        passField.setOnContextClickListener(new View.OnContextClickListener() {
//            @Override
//            public boolean onContextClick(View view) {
//                a = passField.getText().toString();
//                return false;
//            }
//        });

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

                    Intent intent = new Intent(Login.this, HomePage.class);
                    SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
                    editor.putBoolean("USER_LOGGED", true);
                    editor.apply();
                    startActivity(intent);
                    Login.this.finish();

                }

            }
        });
    }
}
