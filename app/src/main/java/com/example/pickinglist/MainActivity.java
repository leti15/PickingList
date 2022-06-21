package com.example.pickinglist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    //#region Preferences names
    public static String COMPANY = "company_name";
    public static String PLANT = "plant_name";
    public static String USERNAME = "customer_username";
    public static String PASSWORD = "customer_password";
    public static String CONFIGURED = "is_configured";
    public static String NONE = "not_specified";
    public static String PREFERENCES_FILE = "config_variables";
    //#endregion

    //#region Result codes
    public static int OK_PLANT = 20;
    public static int BAD_PLANT = 21;
    public static int OK_LOGIN = 22;
    public static int BAD_LOGIN = 23;
    public static int OK_COMPANY = 24;
    public static int BAD_COMPANY = 25;
    //#endregion

    Context context;
    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this.getBaseContext();
        pref = getSharedPreferences("config_variables", context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();


        Button btnConfig = findViewById(R.id.btnConfig);
        btnConfig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.putString(CONFIGURED, "false");
                Intent i = new Intent(context, ConfigActivity.class);
                startActivityForResult(i, 2);
            }
        });


        Button btnList1 = findViewById(R.id.btnList1);
        btnList1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pref.getString(CONFIGURED, "false") != "false")
                    switchActivities(VisualizeSingleList.class);
            }
        });

        Button btnList2 = findViewById(R.id.btnList2);
        btnList2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchActivities(DrawerLayoutActivity.class);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == MainActivity.OK_COMPANY) 
        {
            Log.wtf("2", "onActivityResult: OK_COMPANY");

            Toast t = new Toast(context);
            t.setText("Configurazione riuscita!");
            t.show();

            switchActivities(VisualizeSingleList.class);
        }
        else
        {
            Log.wtf("2", "onActivityResult: BAD_COMPANY");

            Toast t = new Toast(context);
            t.setText("Configurazione fallita... :(");
            t.show();

        }
    }
    
    @Override
    public void onResume(){
        super.onResume();
    }

    private void switchActivities(Class c) {
        Intent switchActivityIntent = new Intent(this, c);
        startActivity(switchActivityIntent);
        int code = switchActivityIntent.getFlags();
        Log.wtf("2", "switchActivities: return from intent with code " + code );
    }

    private void DoRequest()
    {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            // fetch data
        } else {
            Toast netError = new Toast(context);
            netError.setText("Errore di connessione");
            netError.show();
        }

    }
}