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

public class ConfigActivityLogin extends AppCompatActivity {

    //#region Variables
    String username;
    String password;
    EditText edUsername;
    EditText edPassword;

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context context;

    Intent intent;
    //#endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config_login);

        //#region Inizializzazioni
        intent = getIntent();
        context = getBaseContext();

        Button btnIndietro = findViewById(R.id.btnIndietroLogin);
        Button btnAvanti = findViewById(R.id.btnAvantiLogin);
        edUsername = findViewById(R.id.edUsername);
        edPassword = findViewById(R.id.edPassword);

        pref = getSharedPreferences(MainActivity.PREFERENCES_FILE, MODE_PRIVATE);
        editor = pref.edit();

        String tmp = pref.getString(MainActivity.USERNAME, MainActivity.NONE).toString();
        if(tmp != MainActivity.NONE)
            edUsername.setText(tmp);
        tmp = pref.getString(MainActivity.PASSWORD, MainActivity.NONE).toString();
        if(tmp != MainActivity.NONE)
            edPassword.setText(tmp);
        //#endregion

        btnAvanti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username = edUsername.getText().toString().trim();
                password = edPassword.getText().toString().trim();

                if(username.compareTo("") != 0 && password.compareTo("") != 0)
                {
                    Intent i = new Intent(context, ConfigActivityPlant.class);
                    startActivityForResult(i, 2);
                }
                else
                {
                    Toast t = new Toast(context);
                    t.setText("Inserisci username e password");
                    t.show();
                }
            }
        });

        btnIndietro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Setto risultato per l'intent
                intent = getIntent();
                setResult(MainActivity.BAD_LOGIN, intent);

                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == MainActivity.OK_PLANT) {
            Log.wtf("2", "onActivityResult: OK_PLANT");

            pref = getSharedPreferences(MainActivity.PREFERENCES_FILE, context.MODE_PRIVATE);

            if( pref.getString(MainActivity.CONFIGURED, MainActivity.NONE).compareTo("true") == 0)
            {
                //Salvo preferences
                editor.putString(MainActivity.USERNAME, username);
                editor.putString(MainActivity.PASSWORD, password);
                editor.commit();

                //Setto risultato per l'intent
                setResult(MainActivity.OK_LOGIN, intent);

                finish();
            }
        }
        else
        {
            Log.wtf("2", "onActivityResult: BAD_PLANT");
        }
    }

    @Override
    public void onResume()
    {
        super.onResume();
    }
}