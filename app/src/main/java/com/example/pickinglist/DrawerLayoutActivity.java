package com.example.pickinglist;
import static com.example.pickinglist.MainActivity.CONFIGURED;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DrawerLayoutActivity extends AppCompatActivity{
    public static int CAMERA_PERMISSION_CODE = 1;
    public static int CAMERA = 2;

    private Context context;
    private Activity activity;

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    private String[] mNavigationDrawerItemTitles;
    private DrawerLayout drawerLayout;
    private ListView drawerList;
    Toolbar toolbar;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private DataModel[] drawerItems;
    ActionBarDrawerToggle mDrawerToggle;

    String namePlant;

    RecyclerView rvObject;
    TextView emptyPickingList;
    FloatingActionButton scanBarCode;
    Button btnSave;

    private SectionAdapter adapterReparti;
    private ArrayList<Article> articles;

    DataApi api;
    Toast t;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_layout);
        context = getApplicationContext();
        pref = getSharedPreferences(MainActivity.PREFERENCES_FILE, context.MODE_PRIVATE);
        editor = pref.edit();

        t = new Toast(context);
        mTitle = mDrawerTitle = getTitle();

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerList = (ListView) findViewById(R.id.left_drawer);
        rvObject = findViewById(R.id.rvCompartmentList2);
        emptyPickingList = findViewById(R.id.tvEmpty);
        scanBarCode = findViewById(R.id.fabScan);
        btnSave = findViewById(R.id.btnSave);

        scanBarCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanCode();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(api != null && !api.getPickingLists().isEmpty())
                    api.savePickingLists(context);
                else
                    Toast.makeText(context, "Nulla da salvare.", Toast.LENGTH_SHORT).show();
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        refreshPickingLists();;

        drawerList.setOnItemClickListener(new DrawerItemClickListener());
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerLayout.setDrawerListener(mDrawerToggle);

        setupDrawerToggle();

        rvObject.setLayoutManager(new LinearLayoutManager(this));
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            refreshAdapter(position);
        }

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

    void setupDrawerToggle(){
        mDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout,toolbar,R.string.app_name, R.string.app_name);
        mDrawerToggle.syncState();
    }

    void updateArticles(String articleId, String qta)
    {
        boolean articleFound = false;
        ArrayList<DataApi.PickingList> pickingListList =  api.getPickingLists();

        for (int i = 0; i< pickingListList.size(); i++){
            DataApi.PickingList pickingList = pickingListList.get(i);
            if(api.pickingListsShowed.contains(i) || api.pickingListsShowed.isEmpty())
                for (Article article : pickingList.getArticles())
                    if(article.getRegisterCode().compareTo(articleId) == 0)
                    {
                        articleFound = true;
                        article.setQta(Integer.valueOf(qta));
                    }
        }

        if(!articleFound)
            Toast.makeText(context, "Prodotto con matricola " + articleId + " non trovato.", Toast.LENGTH_LONG).show();

        api.setPickingList(pickingListList);
        adapterReparti.setSections(api.getPickingListsGroupedBySection());
    }

    void refreshPickingLists()
    {
        namePlant = pref.getString(MainActivity.PLANT, MainActivity.NONE);
        if(namePlant.compareTo(MainActivity.NONE) ==0)
            Toast.makeText(context, "Prima esegui la configurazione.", Toast.LENGTH_SHORT).show();

        api = new DataApi(namePlant);

        api.loadPickingLists(context, new VolleyCallback() {
            @Override
            public void onSuccessResponse(String result) {

                ArrayList<DataApi.PickingList> pickingLists = new ArrayList<>(); //array di picking list
                ArrayList<Article> articleList = new ArrayList<>(); //array di article
                JSONArray array = null;
                try {
                    array = new JSONArray(result);
                    JSONObject obj;
                    for (int i = 0; i < array.length(); i++) {
                        obj = new JSONObject(array.getJSONObject(i).toString()); //una pickingList

                        JSONArray articles = new JSONArray(obj.getString("articles")); //array di articoli
                        articleList = new ArrayList<Article>();
                        for (int j = 0; j < articles.length(); j++)
                        {
                            JSONObject objArticle = new JSONObject(articles.getString(j));
                            JSONObject objLocation = new JSONObject(objArticle.getString("location"));

                            ArrayList<Location> path = new ArrayList<Location>();
                            JSONArray objLocationPath = new JSONArray(objLocation.getString("path"));
                            for (int k = 1; k < objLocationPath.length(); k++) {
                                JSONObject tmpLocation = new JSONObject(objLocationPath.get(k).toString()); //singola location in path
                                Location l = new Location(tmpLocation.getLong("id"), tmpLocation.getLong("warehouseId"), tmpLocation.getString("code"), tmpLocation.getString("name"), tmpLocation.getLong("plantId"), tmpLocation.getLong("parentId"), null );
                                path.add(l);
                            }

                            Location location = new Location(objLocation.getLong("id"), objLocation.getLong("warehouseId"), objLocation.getString("code"), objLocation.getString("name"), objLocation.getLong("plantId"), objLocation.getLong("parentId"), path) ;
                            Article tmpArticle = new Article(objArticle.getLong("id"), objArticle.getInt("needingQta"), objArticle.getString("measureUnit"), objArticle.getString("name"), objArticle.getString("registerCode"), location);
                            articleList.add(tmpArticle);
                        }
                        DataApi.PickingList p = new DataApi.PickingList(obj.getLong("id"), obj.getString("name"), articleList);

                        pickingLists.add(p);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                api.setPickingList(pickingLists);
                onLoadPickingList();
            }
        });

        onLoadPickingList();
    }

    void onLoadPickingList()
    {
        if(!api.pickingList.isEmpty())
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
    }

    void refreshAdapter(Integer pickingListToVisualize)
    {
        ArrayList<Section> pickingListsGroupedBySection = api.getPickingListsGroupedBySection(pickingListToVisualize);

        if(pickingListsGroupedBySection.isEmpty())
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
            builderInput.setRawInputType(Configuration.KEYBOARD_QWERTY);
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
