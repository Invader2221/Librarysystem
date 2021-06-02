package com.example.librarysystem.Fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.librarysystem.R;
import com.example.librarysystem.Utils.ResponseHandler;
import com.example.librarysystem.Utils.WebService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ProfileFragment extends Fragment implements ResponseHandler {

    String User,Role,Email, password;
    TextView userName, userEmail, userRole;
    private ProgressDialog progressDialog;
    ResponseHandler responseHandler = this;
    String Username;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        userName = (TextView) view.findViewById(R.id.prfile_name);
        userEmail = (TextView) view.findViewById(R.id.prfile_email);
        userRole = (TextView) view.findViewById(R.id.prfile_user_role);
        setHasOptionsMenu(false);

        progressDialog = ProgressDialog.show(getContext(), "Please wait...", "Retrieving data ...", true);
        WebService.user(responseHandler, User, Role, Email, password, progressDialog, getContext());

        return view;
    }

    @Override
    public void serviceResponse(JSONObject response, JSONArray jsonArray, String tag) throws JSONException {

        String password1= response.get("password").toString();
        String userNamE = response.getString("userName");
        String EmaiL = response.get("email").toString();
        String RolE = response.get("role").toString();

        if (progressDialog != null) {
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        }
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {

                userName.setText(userNamE);
                userEmail.setText(EmaiL);
                userRole.setText(RolE);

            }
        });
    }
}