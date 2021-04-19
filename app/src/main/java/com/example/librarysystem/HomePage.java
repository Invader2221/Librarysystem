package com.example.librarysystem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.librarysystem.Utils.Util;
import com.example.librarysystem.Utils.WebService;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;

import static com.example.librarysystem.Utils.Util.PREFS_NAME;

public class HomePage extends AppCompatActivity {

    String authorId, availableCopies, bookCategory, bookEdition, bookTitle, boolPublisher, isbn, numOfCopies;
    Button logOutButton;
    ListView listView;
    ArrayList<String> stringArrayList = new ArrayList<>();
    ArrayAdapter<String> adapter;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        listView = (ListView) findViewById(R.id.list_view);

        for (int i = 0; i <= 100; i++) {
            stringArrayList.add("Item " + i);
        }

        adapter = new ArrayAdapter<>(HomePage.this, android.R.layout.simple_list_item_1, stringArrayList);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Toast.makeText(getApplicationContext()
                        , adapter.getItem(position), Toast.LENGTH_SHORT).show();

                progressDialog = ProgressDialog.show(HomePage.this, "Please wait...", "Retrieving data ...", true);
                WebService.book(HomePage.this, authorId, availableCopies, bookCategory, progressDialog, bookEdition, bookTitle, boolPublisher, isbn, numOfCopies, HomePage.this);
            }
        });
    }

    @Override
    public void serviceResponse(final JSONObject response, String tag) throws JSONException {

        runOnUiThread(new Runnable() {
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

                Log.e("userName", userName);
                if (userName.equals("null")) {
                    Util.showDialog(HomePage.this, "Error!", "Username not found!");
                } else {
                    Toast.makeText(HomePage.this, "Successful login", Toast.LENGTH_LONG).show();

//                    Intent intent = new Intent(Login.this, HomePage.class);
//                    SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
//                    editor.putBoolean("USER_LOGGED", true);
//                    editor.apply();
//                    startActivity(intent);
//                    Login.this.finish();

                }

            }
        });
    }


//        logOutButton = (Button) findViewById(R.id.home_logout_btn);
//
//        logOutButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(HomePage.this, Login.class);
//                SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
//                editor.putBoolean("USER_LOGGED", false);
//                editor.apply();
//                startActivity(intent);
//                HomePage.this.finish();
//
//            }
//        });


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_search, menu);

        MenuItem menuItem = menu.findItem(R.id.menu_search_view);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);

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

        return super.onCreateOptionsMenu(menu);
    }


}
