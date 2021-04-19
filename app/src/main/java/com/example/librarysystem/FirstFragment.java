package com.example.librarysystem;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.librarysystem.Utils.ResponseHandler;
import com.example.librarysystem.Utils.Util;
import com.example.librarysystem.Utils.WebService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.example.librarysystem.MainActivity.searchView;

public class FirstFragment extends Fragment implements ResponseHandler {


    String authorId, availableCopies, bookCategory, bookEdition, bookTitle, boolPublisher, isbn, numOfCopies;
    ListView listView;
    ArrayList<String> stringArrayList = new ArrayList<>();
    ArrayAdapter<String> adapter;
    ResponseHandler responseHandler = this;
    private ProgressDialog progressDialog;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        listView = (ListView) view.findViewById(R.id.list_view_one);
        setHasOptionsMenu(true);
        for (int i = 0; i <= 100; i++) {
            stringArrayList.add("Item " + i);
        }

        adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, stringArrayList);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Toast.makeText(getActivity()
                        , adapter.getItem(position), Toast.LENGTH_SHORT).show();

                progressDialog = ProgressDialog.show(getContext(), "Please wait...", "Retrieving data ...", true);
                WebService.book(responseHandler, authorId, availableCopies, bookCategory, bookEdition, bookTitle, boolPublisher, isbn, numOfCopies, progressDialog, getContext());
            }
        });
    }

    @Override

    public void serviceResponse(final JSONObject response, String tag) throws JSONException {

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {


                String userName = null;
                String password1 = null;
                try {
                    password1 = response.get("password").toString();
                    userName = response.get("userName").toString();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (progressDialog != null) {
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                }
                if (userName != null) {
                    Util.showDialog(getContext(), "Error!", "Username not found!");
                } else {
                    Toast.makeText(getContext(), "Successful login", Toast.LENGTH_LONG).show();


                }

            }
        });
    }

    @Override
    public void onCreateOptionsMenu(final Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String quary) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                adapter.getFilter().filter(newText);

                return false;
            }
        });


    }
}
