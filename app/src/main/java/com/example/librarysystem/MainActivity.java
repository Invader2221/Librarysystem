package com.example.librarysystem;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.core.view.MenuItemCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.librarysystem.Fragments.FirstFragment;
import com.example.librarysystem.Fragments.SecondFragment;
import com.example.librarysystem.Fragments.ThirdFragment;
import com.google.android.material.navigation.NavigationView;

import static com.example.librarysystem.Utils.Util.PREFS_NAME;

public class MainActivity extends AppCompatActivity {

    public static SearchView searchView;
    private DrawerLayout dl;
    private ActionBarDrawerToggle t;
    private NavigationView nv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dl = (DrawerLayout) findViewById(R.id.drawer_layout);
        t = new ActionBarDrawerToggle(this, dl, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        dl.addDrawerListener(t);
        t.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        nv = (NavigationView) findViewById(R.id.nv);
        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id) {
                    case R.id.itemOne:
                        Fragment fragment = new FirstFragment();
                        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.frame_container, fragment);
                        ft.commitAllowingStateLoss();
                        setTitle("Library System");
                        dl.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.itemTwo:
                        Fragment fragmentSecond = new SecondFragment();
                        FragmentTransaction ftSecond = getSupportFragmentManager().beginTransaction();
                        ftSecond.replace(R.id.frame_container, fragmentSecond);
                        ftSecond.commitAllowingStateLoss();
                        setTitle("Book search");
                        dl.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.itemThree:
                        Fragment fragmentThird = new ThirdFragment();
                        FragmentTransaction ftThird = getSupportFragmentManager().beginTransaction();
                        ftThird.replace(R.id.frame_container, fragmentThird);
                        ftThird.commitAllowingStateLoss();
                        setTitle("Book");
                        dl.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.nav_logout:
                        logout();
                        break;
                    default:
                        return true;
                }


                return true;

            }
        });

        Fragment fragment = new FirstFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frame_container, fragment);
        ft.commitAllowingStateLoss();


    }

    private void logout() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);

        alertDialogBuilder.setTitle("Logout");
        alertDialogBuilder
                .setMessage("Are you sure?")
                .setCancelable(true)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(MainActivity.this, Login.class);
                        SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
                        editor.putBoolean("USER_LOGGED", false);
                        editor.apply();
                        startActivity(intent);
                        MainActivity.this.finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_search, menu);

        MenuItem menuItem = menu.findItem(R.id.menu_search_view);
        searchView = (SearchView) MenuItemCompat.getActionView(menuItem);


        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (t.onOptionsItemSelected(item))
            return true;

        return super.onOptionsItemSelected(item);
    }
}