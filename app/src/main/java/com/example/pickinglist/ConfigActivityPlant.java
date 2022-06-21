package com.example.pickinglist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class ConfigActivityPlant extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    //#region Variables
    String namePlant;
    Spinner spPlant;

    Context context;
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    Intent intent;

    String[] courses = { "C", "Data structures",
            "Interview prep", "Algorithms",
            "DSA with java", "OS" };
    //#endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config_plant);

        //#region Inizializzazioni
        intent = getIntent();
        context = getBaseContext();

        Button btnIndietro = findViewById(R.id.btnIndietroPlant);
        Button btnAvanti = findViewById(R.id.btnAvantiPlant);
        spPlant = findViewById(R.id.spnPlant);

        pref = getSharedPreferences(MainActivity.PREFERENCES_FILE, context.MODE_PRIVATE);
        editor = pref.edit();

        String plant = pref.getString(MainActivity.PLANT, MainActivity.NONE).toString();
        if(plant != MainActivity.NONE)
        {
            for (int i = 0; i < courses.length; i ++)
            {
                 if(courses[i].compareTo(plant) == 0)
                     spPlant.setSelection(i);
            }
        }

        //#region Spinner
        spPlant.setOnItemSelectedListener(this);

        ArrayAdapter ad = new ArrayAdapter( this, android.R.layout.simple_spinner_item, courses);
        ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spPlant.setAdapter(ad);
        //#endregion

        //#endregion

        btnAvanti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //siccome è l'ultima activity di configurazione chiama il finish
                namePlant = courses[spPlant.getSelectedItemPosition()];

                //Salvo preferences
                editor.putString(MainActivity.PLANT, namePlant);
                editor.putString(MainActivity.CONFIGURED, "true");
                editor.commit();

                //Setto risultato per l'intent
                intent = getIntent();
                setResult(MainActivity.OK_PLANT, intent);

                finish();
            }
        });

        btnIndietro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Setto risultato per l'intent
                intent = getIntent();
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
}


