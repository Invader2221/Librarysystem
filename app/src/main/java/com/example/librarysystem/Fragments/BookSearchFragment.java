package com.example.librarysystem.Fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.librarysystem.BookList;
import com.example.librarysystem.BookListAdapter;
import com.example.librarysystem.R;
import com.example.librarysystem.Utils.ResponseHandler;
import com.example.librarysystem.Utils.WebService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.example.librarysystem.MainActivity.searchView;

public class BookSearchFragment extends Fragment implements ResponseHandler, BookListAdapter.ItemClickListener {


    String authorId, availableCopies, bookCategory, bookEdition, bookTitle, boolPublisher, isbn, numOfCopies;
    RecyclerView listView;
    BookListAdapter adapter;
    ResponseHandler responseHandler = this;
    private ProgressDialog progressDialog;
    ArrayList<BookList> arrayList;
    RadioButton radioButton;
    private RadioGroup radioGroup;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        View view = inflater.inflate(R.layout.second_fragment, container, false);
        listView = (RecyclerView) view.findViewById(R.id.list_view_one);
        radioGroup = (RadioGroup) view.findViewById(R.id.radioGroup_role);
        setHasOptionsMenu(true);

        progressDialog = ProgressDialog.show(getContext(), "Please wait...", "Retrieving data ...", true);
        WebService.book(responseHandler, authorId, availableCopies, bookCategory, bookEdition, bookTitle, boolPublisher, isbn, numOfCopies, progressDialog, getContext());

        return view;
    }

    @Override

    public void serviceResponse(final JSONObject response, JSONArray jsonArray, String tag) throws JSONException {

        arrayList = new ArrayList<BookList>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject objects = jsonArray.getJSONObject(i);
            String bookTitle = objects.getString("bookTitle");
            String category = objects.getString("bookCategory");
            String autor = objects.getString("authorId");
            String image = objects.getString("bookCover");
            arrayList.add(new BookList(bookTitle, category, autor, image));
        }
        getActivity().runOnUiThread(() -> {

            listView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
            adapter = new BookListAdapter(getActivity(), arrayList);
            adapter.setClickListener(this);
            listView.setAdapter(adapter);


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

                if (arrayList != null) {

                    int selectedId = radioGroup.getCheckedRadioButtonId();
                    radioButton = (RadioButton) getView().findViewById(selectedId);

                    if (String.valueOf(radioButton.getText()).equals("Book Title")) {
                        ArrayList<BookList> tempArry = new ArrayList<BookList>();
                        for (int i = 0; i < arrayList.size(); i++) {
                            if (arrayList.get(i).getTitle().contains(newText)) {
                                tempArry.add(arrayList.get(i));
                            }
                        }
                        adapter = new BookListAdapter(getActivity(), tempArry);
                        adapter.setClickListener(BookSearchFragment.this);
                        listView.setAdapter(adapter);

                    } else if (String.valueOf(radioButton.getText()).equals("Book Category")) {
                        ArrayList<BookList> tempArry = new ArrayList<BookList>();
                        for (int i = 0; i < arrayList.size(); i++) {
                            if (arrayList.get(i).getCategory().contains(newText)) {
                                tempArry.add(arrayList.get(i));
                            }
                        }
                        adapter = new BookListAdapter(getActivity(), tempArry);
                        adapter.setClickListener(BookSearchFragment.this);
                        listView.setAdapter(adapter);
                    } else {
                        ArrayList<BookList> tempArry = new ArrayList<BookList>();
                        for (int i = 0; i < arrayList.size(); i++) {
                            if (arrayList.get(i).getAuthor().contains(newText)) {
                                tempArry.add(arrayList.get(i));
                            }
                        }
                        adapter = new BookListAdapter(getActivity(), tempArry);
                        adapter.setClickListener(BookSearchFragment.this);
                        listView.setAdapter(adapter);
                    }

                }

                return false;
            }
        });


    }

    @Override
    public void onItemClickBookList(View view, int position) {
        Toast.makeText(getActivity(), "You clicked " + adapter.getItem(position).getTitle() + " on row number " + position, Toast.LENGTH_SHORT).show();
    }
}
