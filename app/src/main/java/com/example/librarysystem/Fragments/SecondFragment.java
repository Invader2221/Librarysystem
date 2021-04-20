package com.example.librarysystem.Fragments;

import android.app.ProgressDialog;
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

import androidx.fragment.app.Fragment;

import com.example.librarysystem.BookList;
import com.example.librarysystem.CustomAdapter;
import com.example.librarysystem.R;
import com.example.librarysystem.Utils.ResponseHandler;
import com.example.librarysystem.Utils.WebService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.example.librarysystem.MainActivity.searchView;

public class SecondFragment extends Fragment implements ResponseHandler {


    String authorId, availableCopies, bookCategory, bookEdition, bookTitle, boolPublisher, isbn, numOfCopies;
    ListView listView;
    ArrayList<String> stringArrayList = new ArrayList<>();
    CustomAdapter customAdapter;
    ResponseHandler responseHandler = this;
    private ProgressDialog progressDialog;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        View view = inflater.inflate(R.layout.second_fragment, container, false);
        listView = (ListView) view.findViewById(R.id.list_view_one);
        setHasOptionsMenu(true);

        progressDialog = ProgressDialog.show(getContext(), "Please wait...", "Retrieving data ...", true);
        WebService.book(responseHandler, authorId, availableCopies, bookCategory, bookEdition, bookTitle, boolPublisher, isbn, numOfCopies, progressDialog, getContext());


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                customAdapter.getItem(position);
                Toast.makeText(getActivity(), "csdsd", Toast.LENGTH_SHORT).show();


            }
        });

        return view;
    }

    @Override

    public void serviceResponse(final JSONObject response, JSONArray jsonArray, String tag) throws JSONException {

        //  String bookTitle = "";
        ArrayList<BookList> arrayList = new ArrayList<BookList>();
        for (int i = 0; i <= jsonArray.length() - 1; i++) {
            JSONObject objects = jsonArray.getJSONObject(i);
            String bookTitle = objects.getString("bookTitle");
            arrayList.add(new BookList(bookTitle, "sdfdf", "https://www.clker.com/cliparts/l/u/5/P/D/A/arrow-50x50-md.png"));
        }
        getActivity().runOnUiThread(() -> {

             customAdapter = new CustomAdapter(getActivity(), arrayList);
            //MyListAdapter adapter = new MyListAdapter(this, stringArrayList, "subtitle","https://www.clker.com/cliparts/l/u/5/P/D/A/arrow-50x50-md.png");

            //adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, stringArrayList);
            listView.setAdapter(customAdapter);
            if (progressDialog != null) {
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
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

               // if (adapter != null) {
                  //  adapter.getFilter().filter(newText);
               // }

                return false;
            }
        });


    }
}
