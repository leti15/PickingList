package com.example.pickinglist;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import java.security.Permission;
import java.util.jar.Pack200;

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
    ActivityResultLauncher<Intent> activityResultLauncher;

    Button btnConfig;
    Button btnPickingLists;
    Switch swtDebug;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this.getBaseContext();
        pref = getSharedPreferences("config_variables", context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        btnConfig = findViewById(R.id.btnConfig);
        btnPickingLists = findViewById(R.id.btnList2);
        swtDebug = findViewById(R.id.swtDebug);

        btnConfig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.putString(CONFIGURED, "false");
                Intent i = new Intent(context, ConfigActivity.class);
                i.putExtra("debugMode", swtDebug.isChecked());
                startActivityForResult(i, 2);
            }
        });

        btnPickingLists.setOnClickListener(new View.OnClickListener() {
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
            Toast t = new Toast(context);
            t.setText("Configurazione riuscita!");
            t.show();

            switchActivities(DrawerLayoutActivity.class);
        }
        else
        {
            Toast t = new Toast(context);
            t.setText("Configurazione fallita.");
            t.show();
        }
    }

    private void switchActivities(Class c) {
        Intent switchActivityIntent = new Intent(this, c);
        switchActivityIntent.putExtra("debugMode", swtDebug.isChecked());
        startActivity(switchActivityIntent);
    }
}