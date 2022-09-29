package com.example.pickinglist;

import android.content.Context;
import android.content.res.Configuration;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ArticleHolder> {

    private Context context;
    private ArrayList<Article> articles;

    public ArticleAdapter(Context context, ArrayList<Article> articles){
        this.context = context;
        this.articles = articles;
    }

    @NonNull
    @Override
    public ArticleHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.recycler_object, parent, false);
        return new ArticleHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ArticleHolder holder, int position) {
        Article a = articles.get(position);
        holder.setArticle(a);
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    protected static class ArticleHolder extends RecyclerView.ViewHolder {

        private Context context;
        private Article article;
        private int qtaNeed;

        private CheckBox cbFinished;
        private Button btnAdd, btnMinus;
        private EditText edQta;
        private TextView tvLocation;
        private TextView tvQtaNeed;

        public ArticleHolder(@NonNull View itemView) {
            super(itemView);

            context = itemView.getContext();
            cbFinished = itemView.findViewById(R.id.cbArticle);
            btnAdd = itemView.findViewById(R.id.btnAdd);
            btnMinus = itemView.findViewById(R.id.btnMinus);
            edQta = itemView.findViewById(R.id.edQta);
            tvLocation = itemView.findViewById(R.id.tvArticleLocation);
            tvQtaNeed = itemView.findViewById(R.id.tvQtaNeed);

            edQta.setRawInputType(Configuration.KEYBOARD_QWERTY);

            cbFinished.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v)
                {
                    if(cbFinished.isChecked())
                    {
                        Toast t = new Toast(context);
                        t.setText("Trovati tutti e " + qtaNeed + " !");
                        t.setDuration(Toast.LENGTH_LONG);
                        t.show();

                        edQta.setText( String.valueOf(qtaNeed) );
                        article.setQta(qtaNeed);
                    }
                }
            });

            btnMinus.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v) {
                    Integer x = Integer.parseInt(String.valueOf(edQta.getText()));

                    if(x != 0)
                    {
                        edQta.setText(String.valueOf(x-1));
                        article.setQta(x-1);
                    }
                }
            });
            btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Integer x = Integer.parseInt(String.valueOf(edQta.getText()));

                    edQta.setText(String.valueOf(x+1));
                    article.setQta(x+1);
                }
            });
        }

        public void setArticle(Article article){
            this.article = article;
            this.cbFinished.setText(article.getName());
            this.edQta.setText( String.valueOf(article.getQta()) );

            String s = article.getLocation().getLocationToStringCustomized();
            this.tvLocation.setText(s);
            this.tvQtaNeed.setText( "Servono " + String.valueOf( article.getNeedingQta() ) + " " +  article.getMeasureUnit() );
            this.qtaNeed = article.getNeedingQta();
        }


    }
}

