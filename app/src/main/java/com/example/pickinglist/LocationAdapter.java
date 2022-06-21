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

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.LocationHolder> {
    private Context context;
    private ArrayList<Location> locations;

    public LocationAdapter(Context context, ArrayList<Location> locations)
    {
        this.context = context;
        this.locations = locations;
    }

    @NonNull
    @Override
    public LocationHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(context).inflate(R.layout.fragment_list_by_location, parent, false);
        return new LocationHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LocationHolder holder, int position) {
        Location l = locations.get(position);
        holder.setLocation(l);
    }

    @Override
    public int getItemCount() { return locations.size(); }

    protected class LocationHolder extends RecyclerView.ViewHolder
    {
        private Button btnExpand;
        private TextView tvNameLocation;
        private RecyclerView rvItems;
        private  boolean isCollapsed;

        public LocationHolder(@NonNull View itemView) {
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

        public void setLocation(Location location)
        {
            tvNameLocation.setText(location.getLocationName());
            rvItems.setLayoutManager(new LinearLayoutManager(context));
            ArticleAdapter articleAdapter = new ArticleAdapter(context, location.getItems());
            rvItems.setAdapter(articleAdapter);
        }
    }
}
