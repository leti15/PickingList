package com.example.pickinglist;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

public class ConfigActivity extends AppCompatActivity {

    //#region Variables
    String nameCompany;
    EditText edCompany;

    DataApi api;
    Context context;
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    Intent intent;
    Boolean debugMode;
    //#endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);

        //#region Inizializzazioni
        intent = getIntent();
        debugMode = Boolean.valueOf(intent.getExtras().get("debugMode").toString());
        context = this.getBaseContext();

        api = new DataApi();

        Button btnIndietro = findViewById(R.id.btnIndietroCompany);
        Button btnAvanti = findViewById(R.id.btnAvantiCompany);
        edCompany = findViewById(R.id.edCompany);

        pref = getSharedPreferences(MainActivity.PREFERENCES_FILE, context.MODE_PRIVATE);
        editor = pref.edit();

        ActivityResultLauncher<Intent> activityResultLauncher;

        String company = pref.getString(MainActivity.COMPANY, MainActivity.NONE).toString();
        if(company != MainActivity.NONE)
            edCompany.setText(company);
        //#endregion

        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {

                        if (result.getResultCode() == MainActivity.OK_LOGIN) {
                            Log.wtf("2", "onActivityResult: OK_LOGIN");

                            pref = getSharedPreferences(MainActivity.PREFERENCES_FILE, context.MODE_PRIVATE);
                            if(pref.getString(MainActivity.CONFIGURED, MainActivity.NONE).compareTo("true") == 0)
                            {
                                //Salvo preferencess
                                editor.putString(MainActivity.COMPANY, nameCompany);
                                editor.commit();

                                //Setto risultato per l'intent
                                setResult(MainActivity.OK_COMPANY, intent);

                                Log.wtf("2", "onResume: finish company" );

                                finish();
                            }
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
                nameCompany = edCompany.getText().toString().trim();

                if(nameCompany.compareTo("") != 0)
                {

                    if(!debugMode)
                        api.setDomainName(nameCompany.toLowerCase(Locale.ROOT).trim());

                    api.getPlants(company, context, new VolleyCallback(){
                        @Override
                        public void onSuccessResponse(String result) {

                            Intent i = new Intent(context, ConfigActivityPlant.class);
                            i.putExtra("company", nameCompany);
                            i.putExtra("plants", result);
                            activityResultLauncher.launch(i);
                        }
                    });
                }
                else
                {
                    Toast t = new Toast(context);
                    t.setText("Inserisci una compagnia");
                    t.show();
                }
            }
        });

        btnIndietro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Setto risultato per l'intent
                intent = getIntent();
                setResult(MainActivity.BAD_COMPANY, intent);

                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == MainActivity.OK_PLANT) {
            pref = getSharedPreferences(MainActivity.PREFERENCES_FILE, context.MODE_PRIVATE);
            if(pref.getString(MainActivity.CONFIGURED, MainActivity.NONE).compareTo("true") == 0)
            {
                //Salvo preferencess
                editor.putString(MainActivity.COMPANY, nameCompany);
                editor.commit();

                //Setto risultato per l'intent
                setResult(MainActivity.OK_COMPANY, intent);

                Log.wtf("2", "onResume: finish company" );

                finish();
            }
        }
        else
        {
            Log.wtf("2", "onActivityResult: BAD_LOGIN");
        }
    }

    @Override
    public void onResume(){
        super.onResume();
    }
}