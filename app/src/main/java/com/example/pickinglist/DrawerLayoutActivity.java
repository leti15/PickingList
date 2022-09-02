package com.example.pickinglist;
/*
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;


import com.google.android.material.navigation.NavigationView;

public class DrawerLayoutActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;
    private ListView drawerList;
    private String[] navigationDrawerItemTitles;

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_layout);
        context = getApplicationContext();


       // drawerList = (ListView) findViewById(R.id.left_drawer);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        //#region DataSet
        navigationDrawerItemTitles = getResources().getStringArray(R.array.navigation_drawer_items_array);
        DataModel[] drawerItem = new DataModel[navigationDrawerItemTitles.length];
        for (int i = 0; i < navigationDrawerItemTitles.length; i++)
        {
            DataModel d = new DataModel(androidx.appcompat.R.id.search_mag_icon, navigationDrawerItemTitles[i]);
            drawerItem[i] = d;
        }
        Log.wtf("TAG", "onCreate: " + drawerItem.toString());
        //#endregion

      /*  DrawerItemCustomAdapter adapter = new DrawerItemCustomAdapter(this, R.layout.list_view_item_row, drawerItem);
        drawerList.setAdapter(adapter);
        drawerList.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.wtf("2", "onItemClick: i= "+ i+ " l= "+l);
            }
        });
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);

        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        drawerLayout.setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
                Log.wtf("2", "onDrawerSlide: " );
            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {
                Log.wtf("2", "onDrawerOpened: " );

            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {
                Log.wtf("2", "onDrawerClosed: " );

            }

            @Override
            public void onDrawerStateChanged(int newState) {
                Log.wtf("2", "onDrawerStateChanged: " );

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Toast.makeText(context, "item", Toast.LENGTH_SHORT).show();
        return true;
    }

    public void handleClick(View v) {
        // does something very interesting

        Log.wtf("2", " HANDLECLICK " );
    }

}
*/


import android.content.Context;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.sql.Array;
import java.util.ArrayList;

public class DrawerLayoutActivity extends AppCompatActivity{

    private Context context;
    private String[] mNavigationDrawerItemTitles;
    private DrawerLayout drawerLayout;
    private ListView drawerList;
    Toolbar toolbar;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private DataModel[] drawerItems;
    ActionBarDrawerToggle mDrawerToggle;


    RecyclerView rvObject;
    TextView emptyPickingList;

    private SectionAdapter adapterReparti;
    private ArrayList<Article> articles;

    DataApi api;
    Toast t;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_layout);
        context = getApplicationContext();

        mTitle = mDrawerTitle = getTitle();

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerList = (ListView) findViewById(R.id.left_drawer);
        rvObject = findViewById(R.id.rvCompartmentList2);
        emptyPickingList = findViewById(R.id.tvEmpty);
        t = new Toast(context);

        //todo: inserire plant e userId nel costruttore
        api = new DataApi();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        articles = new ArrayList<Article>();
        if(api.loadPickingLists())
        {
            String[] pickingListTitles = api.getPickingListTitles();

            Integer[] positions = new Integer[pickingListTitles.length];
            for (int i = 0; i < pickingListTitles.length; i++) { positions[i] = i; }

            ArrayList<Section> pickingListsGroupedBySection = api.getPickingListsGroupedBySection(positions);
            String[] sections = api.getPickingListTitlesBySection();

            if(sections == null)
            {
                emptyPickingList.setVisibility(View.VISIBLE);
                rvObject.setVisibility(View.GONE);
            }
            else
            {
                emptyPickingList.setVisibility(View.GONE);
                rvObject.setVisibility(View.VISIBLE);

                drawerItems = new DataModel[pickingListTitles.length];
                for (int i = 0; i < pickingListTitles.length; i++) {
                    drawerItems[i] = new DataModel(pickingListTitles[i]);
                }

                DrawerItemCustomAdapter adapter = new DrawerItemCustomAdapter(this, R.layout.list_view_item_row, drawerItems);
                drawerList.setAdapter(adapter);

                adapterReparti = new SectionAdapter(DrawerLayoutActivity.this, pickingListsGroupedBySection);
                rvObject.setAdapter(adapterReparti);
            }
        }
        else
        {
            t.setText("Errore nel caricamento delle pickingList.");
            t.show();
        }

        drawerList.setOnItemClickListener(new DrawerItemClickListener());
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerLayout.setDrawerListener(mDrawerToggle);

        setupDrawerToggle();

        rvObject.setLayoutManager(new LinearLayoutManager(this));

    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }

    }

    private void selectItem(int position) {

        Toast t = new Toast(context);
        t.setText("premuto "+ drawerItems[position].name.toString());
        t.show();

        Integer[] pos = new Integer[]{2,4};
        refreshAdapter(pos);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (mDrawerToggle.onOptionsItemSelected(item)) {

            Log.e("onOptionsItemSelected", item.toString());
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    void setupToolbar(){
        Log.wtf("2", "setupToolbar: inizio");

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    void setupDrawerToggle(){
        mDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout,toolbar,R.string.app_name, R.string.app_name);
        //This is necessary to change the icon of the Drawer Toggle upon state change.
        mDrawerToggle.syncState();
    }

    void refreshAdapter(Integer[] pickingListToVisualize)
    {
        ArrayList<Section> pickingListsGroupedBySection = api.getPickingListsGroupedBySection(pickingListToVisualize);
        String[] sections = api.getPickingListTitlesBySection();

        if(sections == null)
        {
            emptyPickingList.setVisibility(View.VISIBLE);
            rvObject.setVisibility(View.GONE);
        }
        else {
            emptyPickingList.setVisibility(View.GONE);
            rvObject.setVisibility(View.VISIBLE);

            adapterReparti.setSections(pickingListsGroupedBySection);
        }
    }
}
