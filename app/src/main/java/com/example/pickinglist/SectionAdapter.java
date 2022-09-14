package com.example.pickinglist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class SectionAdapter extends RecyclerView.Adapter<SectionAdapter.SectionHolder> {
    private Context context;
    private ArrayList<Section> sections;
    private ArrayList<ArticleAdapter> articleAdapterList;

    public SectionAdapter(Context context, ArrayList<Section> locations)
    {
        this.context = context;
        this.sections = locations;
        this.articleAdapterList = new ArrayList<ArticleAdapter>();
    }

    @NonNull
    @Override
    public SectionHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(context).inflate(R.layout.fragment_list_by_location, parent, false);
        return new SectionHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SectionHolder holder, int position) {
        Section s = sections.get(position);
        holder.setSection(s);
    }

    @Override
    public int getItemCount() { return sections.size(); }

    public ArrayList<Section> getSections() { return sections; }

    public void setSections(ArrayList<Section> sections)
    {
        this.sections = sections;
        notifyDataSetChanged();
        updateArticleAdapterList();
    }

    public void updateArticleAdapterList ()
    {
        for (ArticleAdapter a : this.articleAdapterList)
            a.notifyDataSetChanged();
    }


    protected class SectionHolder extends RecyclerView.ViewHolder
    {
        private Button btnExpand;
        private TextView tvNameLocation;
        private RecyclerView rvItems;
        private  boolean isCollapsed;

        public SectionHolder(@NonNull View itemView) {
            super(itemView);

            context = itemView.getContext();
            btnExpand = itemView.findViewById(R.id.btnExpand);
            tvNameLocation = itemView.findViewById(R.id.tvLocation);
            rvItems = itemView.findViewById(R.id.rvCompartmentList);

            isCollapsed = true;
            rvItems.setVisibility(itemView.GONE);

            btnExpand.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(isCollapsed)
                    {
                        isCollapsed = false;
                        btnExpand.setText("-");
                        rvItems.setVisibility(itemView.VISIBLE);
                    }
                    else
                    {
                        isCollapsed = true;
                        btnExpand.setText("+");
                        rvItems.setVisibility(itemView.GONE);
                    }
                }
            });
        }

        public void setSection(Section section)
        {
            tvNameLocation.setText(section.getSectionName());
            rvItems.setLayoutManager(new LinearLayoutManager(context));
            ArticleAdapter articleAdapter = new ArticleAdapter(context, section.getArticles());
            articleAdapterList.add(articleAdapter);
            rvItems.setAdapter(articleAdapter);
        }
    }
}
