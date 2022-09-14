package com.example.pickinglist;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import android.provider.MediaStore;
import android.util.Log;
import android.util.SparseLongArray;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

public class DrawerLayoutActivity extends AppCompatActivity{
    public static int CAMERA_PERMISSION_CODE = 1;
    public static int CAMERA = 2;

    private Context context;
    private Activity activity;
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
    FloatingActionButton scanBarCode;

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
        scanBarCode = findViewById(R.id.fabScan);
        scanBarCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanCode();
            }
        });

        t = new Toast(context);

        //todo: inserire plant e userId nel costruttore
        api = new DataApi();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        articles = new ArrayList<Article>();
        if(api.loadPickingLists())
        {
            String[] pickingListTitles = api.getPickingListTitles();

            Integer[] positions = new Integer[1];
            ArrayList<Section> pickingListsGroupedBySection = api.getPickingListsGroupedBySection();
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
                drawerList.setChoiceMode(2);

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

        refreshAdapter(position);
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

    void updateArticles(String articleId, String qta)
    {
        ArrayList<DataApi.PickingList> pickingListList =  api.getPickingLists();

        for (DataApi.PickingList pickingList: pickingListList){
            for (Article article : pickingList.getArticles()){
                if(article.Getid().compareTo(articleId) == 0)
                    article.SetQta(Integer.valueOf(qta));
            }
        }

        api.setPickingList(pickingListList);
        adapterReparti.setSections(api.getPickingListsGroupedBySection());
    }

    void refreshAdapter(Integer pickingListToVisualize)
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

    private void scanCode(){
        ScanOptions options = new ScanOptions();
        options.setPrompt("Volume up to flash on");
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);
        options.setCaptureActivity(CaptureAct.class);
        barLuncher.launch(options);
    }

    ActivityResultLauncher<ScanOptions> barLuncher = registerForActivityResult(new ScanContract(), result ->
    {
        if(result.getContents() != null)
        {
            final EditText builderInput = new EditText(DrawerLayoutActivity.this);
            AlertDialog.Builder builder = new AlertDialog.Builder(DrawerLayoutActivity.this);

            String articleId = result.getContents();
            if(articleId != null)
            {
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    50,
                    LinearLayout.LayoutParams.MATCH_PARENT);

                builder.setTitle("Scarica articolo/i");
                builder.setMessage( "Inserisci quantità per articolo " + articleId);
                builderInput.setLayoutParams(lp);
                builder.setView(builderInput);
                builder.setPositiveButton("Aggiungi", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        updateArticles(articleId, builderInput.getText().toString());

                        dialog.dismiss();
                    }
                }).show();
            }
            else
                Toast.makeText(context, "Nessun codice scannerizzato", Toast.LENGTH_SHORT).show();
        }
        else
            Toast.makeText(context, "Nessun risultato", Toast.LENGTH_SHORT).show();
    });
}
