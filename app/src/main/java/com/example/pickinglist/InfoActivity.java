package com.example.pickinglist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

public class InfoActivity extends AppCompatActivity {

    Context context;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        intent = getIntent();

        this.context = InfoActivity.this;

        Button btnBack = findViewById(R.id.btnBack2);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                setResult(200, intent);
                finish();
            }
        });

        CheckBox cbName = findViewById(R.id.cbInfoName);
        cbName.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                if(cbName.isChecked())
                {
                    Toast t = new Toast(context);
                    t.setText("Trovati tutti!");
                    t.setDuration(Toast.LENGTH_LONG);
                    t.show();
                }

            }
        });
    }
}