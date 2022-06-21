package com.example.pickinglist;

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

public class ConfigActivity extends AppCompatActivity {

    //#region Variables
    String nameCompany;
    EditText edCompany;

    Context context;
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    Intent intent;
    //#endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);

        //#region Inizializzazioni
        intent = getIntent();
        context = this.getBaseContext();

        Button btnIndietro = findViewById(R.id.btnIndietroCompany);
        Button btnAvanti = findViewById(R.id.btnAvantiCompany);
        edCompany = findViewById(R.id.edCompany);

        pref = getSharedPreferences(MainActivity.PREFERENCES_FILE, context.MODE_PRIVATE);
        editor = pref.edit();

        String company = pref.getString(MainActivity.COMPANY, MainActivity.NONE).toString();
        if(company != MainActivity.NONE)
            edCompany.setText(company);
        //#endregion

        btnAvanti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nameCompany = edCompany.getText().toString().trim();

                if(nameCompany.compareTo("") != 0)
                {
                    Intent switchActivityIntent = new Intent(context, ConfigActivityLogin.class);
                    startActivityForResult(switchActivityIntent, 2);
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

        if (resultCode == MainActivity.OK_LOGIN) {
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

    @Override
    public void onResume(){
        super.onResume();
    }
}