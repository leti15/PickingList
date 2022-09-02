package com.example.pickinglist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

public class VisualizeSingleList extends AppCompatActivity {

    RecyclerView rvObject;
    private SectionAdapter adapter;
    private ArrayList<Article> articles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualize_single_list);

        Button btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                finish();
            }
        });

        rvObject = findViewById(R.id.rvObject);

        rvObject.setLayoutManager(new LinearLayoutManager(this));
        articles = new ArrayList<Article>();
        //adapter = new SectionAdapter(VisualizeSingleList.this , getDataSource());
        rvObject.setAdapter(adapter);
    }


    /*private ArrayList<Location> getDataSource(){
        Article article = new Article(3, "sca20", "Tachipirina", 823516931, 12);
        articles.add(article);
        article = new Article(5, "sca10", "Aspirina", 821513531, 20);
        articles.add(article);
        article = new Article(2, "bustina", "Oki", 823006931, 5);
        articles.add(article);
        article = new Article(3, "sca50", "Siringhe", 822316931, 30);
        articles.add(article);
        article = new Article(3, "sca", "Garza", 820116931, 10);
        articles.add(article);
        article = new Article(5, "sca10", "Aspirina", 821513531, 20);
        articles.add(article);
        article = new Article(2, "bustina", "Oki", 823006931, 5);
        articles.add(article);
        article = new Article(3, "sca50", "Siringhe", 822316931, 30);
        articles.add(article);
        article = new Article(3, "sca", "Garza", 820116931, 10);
        articles.add(article);
        article = new Article(5, "sca10", "Aspirina", 821513531, 20);
        articles.add(article);
        article = new Article(2, "bustina", "Oki", 823006931, 5);
        articles.add(article);
        article = new Article(3, "sca50", "Siringhe", 822316931, 30);
        articles.add(article);
        article = new Article(3, "sca", "Garza", 820116931, 10);
        articles.add(article);

        Location l = new Location("Reparto1", 1, articles);
        ArrayList<Location> locations = new ArrayList<Location>();
        locations.add(l);
        locations.add(l);
        locations.add(l);
        locations.add(l);
        locations.add(l);

        return locations;
    }*/
}

