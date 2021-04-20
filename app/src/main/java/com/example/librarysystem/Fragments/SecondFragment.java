package com.example.librarysystem.Fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
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
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.librarysystem.R;
import com.example.librarysystem.Utils.ResponseHandler;
import com.example.librarysystem.Utils.WebService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.example.librarysystem.MainActivity.searchView;
import static java.nio.file.Paths.get;

public class SecondFragment extends Fragment implements ResponseHandler {


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
        return inflater.inflate(R.layout.second_fragment, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        progressDialog = ProgressDialog.show(getContext(), "Please wait...", "Retrieving data ...", true);
        WebService.book(responseHandler, authorId, availableCopies, bookCategory, bookEdition, bookTitle, boolPublisher, isbn, numOfCopies, progressDialog, getContext());

//        String[] allbooks = {bookTitle.toString()};

        // String authorId = get("authorId").toString();


    }

    @Override

    public void serviceResponse(final JSONObject response, String tag) throws JSONException {

        String bookTitle = response.get("title 01").toString();
        String authorId = response.get("").toString();
        String isbn = response.get("").toString();


        Activity view = null;
        listView = (ListView) view.findViewById(R.id.list_view_one);
        setHasOptionsMenu(true);
        for (int i = 0; i <= 100; i++) {
            stringArrayList.add(isbn);
        }

        adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, stringArrayList);


        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Toast.makeText(getActivity(), adapter.getItem(position), Toast.LENGTH_SHORT).show();

                progressDialog = ProgressDialog.show(getContext(), "Please wait...", "Retrieving data ...", true);
                WebService.book(responseHandler, authorId, availableCopies, bookCategory, bookEdition, bookTitle, boolPublisher, isbn, numOfCopies, progressDialog, getContext());

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
