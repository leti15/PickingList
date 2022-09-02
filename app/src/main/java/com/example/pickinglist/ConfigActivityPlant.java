package com.example.pickinglist;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class ConfigActivityPlant extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    //#region Variables
    String company;
    String namePlant;
    Spinner spPlant;

    DataApi api;
    Context context;
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    Intent intent;

    ArrayList<String> plants;
    //#endregion

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config_plant);

        //#region Inizializzazioni
        api = new DataApi();
        intent = getIntent();

        context = getBaseContext();
        ActivityResultLauncher<Intent> activityResultLauncher;
        String tmp;

        Button btnIndietro = findViewById(R.id.btnIndietroPlant);
        Button btnAvanti = findViewById(R.id.btnAvantiPlant);
        spPlant = findViewById(R.id.spnPlant);
        //#endregion

        pref = getSharedPreferences(MainActivity.PREFERENCES_FILE, context.MODE_PRIVATE);
        editor = pref.edit();
        namePlant = pref.getString(MainActivity.PLANT, MainActivity.NONE).toString();
        company = intent.getExtras().getString("company");

        //#region Serializzazione da JsonArray
        ArrayList<DataApi.Plant> plants = new ArrayList<>(); //array di Plant <String,Long>
        ArrayList<String> plantsDisplayNames = new ArrayList<>(); //array di nomi dei Plants <String>
        JSONArray array = null;
        try {
            array = new JSONArray(intent.getExtras().getString("plants"));
            JSONObject obj;
            for (int i = 0; i < array.length(); i++) {
                obj = new JSONObject(array.getJSONObject(i).toString());
                plants.add(new DataApi.Plant(obj.getString("name"), obj.getLong("id")));
                plantsDisplayNames.add(obj.getString("name"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
/*
        JSONArray plantsFromJson = null;
        ArrayList<DataApi.Plant> plants = new ArrayList<>(); //array di Plant <String,Long>
        ArrayList<String> plantsDisplayNames = new ArrayList<>(); //array di nomi dei Plants <String>
        try {
            plantsFromJson = new JSONArray(intent.getExtras().getString("plants"));
            JSONObject obj;

            for (int i = 0; i < plantsFromJson.length(); i++) {
                obj = (JSONObject) plantsFromJson.get(i);
                String displayName = obj.getString("displayName");
                Long id = obj.getLong("id");
                plants.add(new DataApi.Plant(displayName, id));
                plantsDisplayNames.add(displayName);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }*/
        //#endregion

        spPlant.setOnItemSelectedListener(this);
        ArrayAdapter ad = new ArrayAdapter( this, android.R.layout.simple_spinner_item, plantsDisplayNames);
        ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spPlant.setAdapter(ad);

        if(namePlant != MainActivity.NONE)
        {
            for (int i = 0; i < plantsDisplayNames.size(); i ++)
            {
                if(plantsDisplayNames.get(i).compareTo(namePlant) == 0)
                    spPlant.setSelection(i);
            }
        }

        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {

                        if (result.getResultCode() == MainActivity.OK_LOGIN && pref.getString(MainActivity.CONFIGURED, MainActivity.NONE).compareTo("true") == 0)
                        {
                            //Salvo preferences
                            editor.putString(MainActivity.PLANT, namePlant);
                            editor.commit();

                            //Setto risultato per l'intent
                            setResult(MainActivity.OK_PLANT, intent);
                            finish();
                        }
                        else
                        {
                            Log.wtf("2", "onActivityResult: BAD_LOGIN");
                        }
                    }
                });

        btnAvanti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(spPlant.getSelectedItem().toString().trim().compareTo("") != 0)
                {
                    namePlant = spPlant.getSelectedItem().toString();
                    Intent intent = new Intent(context, ConfigActivityLogin.class);
                    intent.putExtra("company", company);
                    intent.putExtra("plant", namePlant);
                    activityResultLauncher.launch(intent);
                }
            }
        });

        btnIndietro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Setto risultato per l'intent
                setResult(MainActivity.BAD_PLANT, intent);
                finish();
            }
        });
    }

    @Override
    public void onResume()
    {
        super.onResume();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == MainActivity.OK_LOGIN) {
            Log.wtf("2", "onActivityResult: OK_LOGIN");

            pref = getSharedPreferences(MainActivity.PREFERENCES_FILE, context.MODE_PRIVATE);

            if( pref.getString(MainActivity.CONFIGURED, MainActivity.NONE).compareTo("true") == 0)
            {
                //Salvo preferences
                editor.putString(MainActivity.PLANT, namePlant);
                editor.commit();

                //Setto risultato per l'intent
                setResult(MainActivity.OK_PLANT, intent);

                finish();
            }
        }
        else
        {
            Log.wtf("2", "onActivityResult: BAD_LOGIN");
        }
    }
}


