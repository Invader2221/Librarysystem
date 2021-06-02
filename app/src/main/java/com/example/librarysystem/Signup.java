package com.example.librarysystem;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.librarysystem.Utils.ResponseHandler;
import com.example.librarysystem.Utils.Util;
import com.example.librarysystem.Utils.WebService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Signup extends AppCompatActivity implements ResponseHandler {

    String userName, password, email;
    EditText uName, emailInput, pass;
    RadioButton radioButton;
    Button createButton;
    private ProgressDialog progressDialog;
    private RadioGroup radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        getSupportActionBar().setTitle("Signup");

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        uName = (EditText) findViewById(R.id.reg_username);
        emailInput = (EditText) findViewById(R.id.reg_email);
        pass = (EditText) findViewById(R.id.reg_password);
        createButton = (Button) findViewById(R.id.register_btn);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup_role);

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int selectedId = radioGroup.getCheckedRadioButtonId();
                radioButton = (RadioButton) findViewById(selectedId);


                userName = uName.getText().toString();
                email = emailInput.getText().toString();
                password = pass.getText().toString();

                if (userName.equals("")) {
                    Util.showDialog(Signup.this, "Error!", "Please enter username!");
                } else if (email.equals("")) {
                    Util.showDialog(Signup.this, "Error!", "Please enter email!");
                } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    emailInput.setError("Please provide a valid email");
                    emailInput.requestFocus();

                } else if (password.equals("")) {
                    Util.showDialog(Signup.this, "Error!", "Please enter password!");
                } else if (password.length() < 8) {
                    pass.setError("Minimum length of password is 8 characters!");
                    pass.requestFocus();

                } else {
                    progressDialog = ProgressDialog.show(Signup.this, "Please wait...", "Retrieving data ...", true);
                    WebService.sigup(Signup.this, email, password, String.valueOf(radioButton.getText()), userName, progressDialog, Signup.this);

                }

            }
        });

    }

    @Override
    public void serviceResponse(final JSONObject response, JSONArray jsonArray, String tag) throws JSONException {

        boolean result = response.getBoolean("result");

        String message = response.get("message").toString();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if (progressDialog != null) {
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                }

                if (!result) {
                    Util.showDialog(Signup.this, "Error!", message);
                } else {
                    Toast.makeText(Signup.this, message, Toast.LENGTH_LONG).show();
                    startActivity(new Intent(Signup.this, Login.class));
                }

            }
        });

    }


    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }


}