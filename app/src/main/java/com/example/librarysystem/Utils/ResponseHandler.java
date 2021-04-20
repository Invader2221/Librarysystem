package com.example.librarysystem.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public interface ResponseHandler extends Serializable {
    public void serviceResponse(JSONObject response, JSONArray jsonArray, String tag) throws JSONException;

}