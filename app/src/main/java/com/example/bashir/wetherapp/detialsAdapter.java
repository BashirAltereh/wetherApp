package com.example.bashir.wetherapp;

import android.content.Context;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class detialsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private ArrayList<detailsWeather> list;
    private int resource;

    private String urlImage;

    public detialsAdapter(Context context, int resource,String urlImage ,ArrayList<detailsWeather> list) {
        this.context = context;
        this.list = list;
        this.resource = resource;
        this.urlImage = urlImage;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(resource, parent, false);
        return new detailsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((detailsHolder) holder).textName.setText(list.get(position).getName());
        ((detailsHolder) holder).textValue.setText(list.get(position).getValue());
        String name = ((detailsHolder) holder).textName.getText().toString().toLowerCase();
        if (name.contains("temperature"))
            ((detailsHolder) holder).textValue.setText(((detailsHolder) holder).textValue.getText().toString() + '°');
        Picasso.get().load(urlImage).into(((detailsHolder)holder).imageView);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class detailsHolder extends RecyclerView.ViewHolder {
        private TextView textName, textValue;
        private ImageView imageView;

        public detailsHolder(View itemView) {
            super(itemView);
            textName = itemView.findViewById(R.id.mtName);
            textValue = itemView.findViewById(R.id.mtValue);
            imageView = itemView.findViewById(R.id.imageView);

        }
    }
}
