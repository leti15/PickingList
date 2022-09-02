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
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ConfigActivityLogin extends AppCompatActivity {

    //#region Variables
    String username;
    String password;
    String company;
    String plant;
    EditText edUsername;
    EditText edPassword;

    SharedPreferences pref;
    SharedPreferences.Editor editor;


    DataApi api;
    Context context;
    Intent intent;
    //#endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config_login);

        //#region Inizializzazioni
        intent = getIntent();
        company = intent.getExtras().getString("company");
        plant = intent.getExtras().getString("plant");
        context = getBaseContext();
        api = new DataApi();

        Button btnIndietro = findViewById(R.id.btnIndietroLogin);
        Button btnAvanti = findViewById(R.id.btnAvantiLogin);
        edUsername = findViewById(R.id.edUsername);
        edPassword = findViewById(R.id.edPassword);

        pref = getSharedPreferences(MainActivity.PREFERENCES_FILE, MODE_PRIVATE);
        editor = pref.edit();

        ActivityResultLauncher<Intent> activityResultLauncher;
        String tmp;
        //#endregion

        tmp = pref.getString(MainActivity.USERNAME, MainActivity.NONE).toString();
        if(!tmp.equals(MainActivity.NONE))
            edUsername.setText(tmp);
        tmp = pref.getString(MainActivity.PASSWORD, MainActivity.NONE).toString();
        if(!tmp.equals(MainActivity.NONE))
            edPassword.setText(tmp);

        btnAvanti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //siccome è l'ultima activity di configurazione chiama il finish
                username = edUsername.getText().toString().trim();
                password = edPassword.getText().toString().trim();

                if(username.compareTo("") != 0 && password.compareTo("") != 0)
                {
                    api.verifyCredential(username, password, context, new VolleyCallback() {
                        @Override
                        public void onSuccessResponse(String result) {
                            Toast t = new Toast(context);
                            if(result.compareTo("t") == 0)
                            {
                                t.setText("Autenticazione effettutata con successo.");
                                t.show();

                                //Salvo preferences
                                editor.putString(MainActivity.USERNAME, username);
                                editor.putString(MainActivity.PASSWORD, password);
                                editor.putString(MainActivity.CONFIGURED, "true");
                                editor.commit();

                                //Setto risultato per l'intent
                                setResult(MainActivity.OK_LOGIN, intent);
                                finish();
                            }
                            else
                            {
                                t.setText("Autenticazione fallita, credenziali errate.");
                                t.show();
                            }
                        }
                    });
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
                setResult(MainActivity.BAD_LOGIN, intent);
                finish();
            }
        });
    }
}