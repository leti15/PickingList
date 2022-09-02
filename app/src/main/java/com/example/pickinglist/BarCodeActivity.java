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
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

public class BarCodeActivity extends AppCompatActivity {
    public static int CAMERA_PERMISSION_CODE = 1;
    public static int CAMERA = 2;

    ActivityResultLauncher<Intent> activityResultLauncher;
    Context context;
    Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar_code);

        context = this.getBaseContext();
        activity = this;
        ImageView ivTestCamera = findViewById(R.id.ivCameraResult);

        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if(result.getResultCode() == Activity.RESULT_OK)
                        {
                            Bitmap thumbnail = (Bitmap) result.getData().getExtras().get("data");
                            ivTestCamera.setImageBitmap(thumbnail);
                        }
                    }
                });

        Button camera = findViewById(R.id.btnCamera);
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)
                {
                    Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    setResult(CAMERA, i);
                    activityResultLauncher.launch(i);
                }
                else { ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE); }
            }
        });

        Button scanBarCode = findViewById(R.id.btnScanBarCode);
        scanBarCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanCode();
            }
        });
    }

    private void scanCode(){
        ScanOptions options = new ScanOptions();
        options.setPrompt("Volume up to flash on");
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);
        options.setCaptureActivity(CaptureAct.class);
        barLuncher.launch(options);
    }

    ActivityResultLauncher<ScanOptions> barLuncher = registerForActivityResult(new ScanContract(), result -> {

        Toast.makeText(context, "Risultato" + result.toString(), Toast.LENGTH_LONG).show();
        if(result.getContents() != null)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(BarCodeActivity.this);
            builder.setTitle("Result");
            builder.setMessage(result.getContents());
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).show();
        }
        else
        {
            Toast.makeText(context, "Nessun risultato", Toast.LENGTH_LONG).show();
        }
    });

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == CAMERA_PERMISSION_CODE)
        {
            if(grantResults.length == 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                setResult(CAMERA, i);
                activityResultLauncher.launch(i);
            }
            else
            {
                Toast.makeText(context, "Non hai il permesso di usare la fotocamera", Toast.LENGTH_LONG).show();
            }
        }
    }
}