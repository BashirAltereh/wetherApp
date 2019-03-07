package com.example.bashir.wetherapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

public class CityAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {
    private static int TAG = 0;
    private Context context;
    private int resource;
    private ArrayList<City> list;
    private onHomeClick onHomeClick;
    private CardView cardView;
    private View.OnClickListener onClickListener;
    private boolean isLoaded[];
    private View view;
    private boolean isFirst,isLast;

    public CityAdapter(Context context, int resource, final ArrayList<City> list, onHomeClick mOnHomeClick) {
        this.context = context;
        this.list = list;
        this.resource = resource;
        this.onHomeClick = mOnHomeClick;
        isFirst = false;
        isLast = false;

        isLoaded = new boolean[list.size()+1];
        Arrays.fill(isLoaded, false);



        /*onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("CityAdapter", "" + cardView.getTag(R.string.tag));
                onHomeClick.onHomeClickListener(v, list);
            }
        };*/
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(resource, parent, false);
        view.setOnClickListener(onClickListener);

        return new cityHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        //Log.d("onBindViewHolder", "" + position + " : " + isLoaded[position]);
        ((cityHolder) holder).cardView.setVisibility(View.VISIBLE);

        if(position == 0 ){
            ((cityHolder) holder).cardView.setVisibility(View.GONE);
           // isFirst = true;
            return;
        }
        if((position == list.size()-1  ) ){
            ((cityHolder) holder).cardView.setVisibility(View.GONE);
           //
            // isLast = true;
            return;
        }
        Date currentTime = Calendar.getInstance().getTime();
        String time = currentTime.getHours()+" : "+currentTime.getMinutes();

        ((cityHolder) holder).nameCity.setText(context.getString(R.string.today)+", "+time);
        ((cityHolder) holder).statue.setText(list.get(position).getStatue());
        ((cityHolder) holder).degree.setText(list.get(position).getTempreture() + '°' + 'C');
        ((cityHolder) holder).humidity.setText("Humidity "+list.get(position).getHumidity() + '%');
        ((cityHolder) holder).speedWind.setText("Wind " + list.get(position).getWindSpeed() + " kmh");
        Picasso.get().load(list.get(position).getUrlImage()).into(((cityHolder) holder).imageView);
        if (position < list.size() - 1) {
            Animation animation = AnimationUtils.loadAnimation(context,
                    R.anim.down_from_top);
         //   holder.itemView.startAnimation(animation);
        }
        //  onHomeClick.onHomeClickListener(((cityHolder)holder).itemView,list);

    }

    @Override
    public void onViewDetachedFromWindow(RecyclerView.ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.itemView.clearAnimation();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class cityHolder extends RecyclerView.ViewHolder  {
        private TextView nameCity, statue, degree, humidity, speedWind;
        private ImageView imageView;
        private CardView cardView;


        public cityHolder(View itemView) {
            super(itemView);
            nameCity = itemView.findViewById(R.id.mtCity);
            statue = itemView.findViewById(R.id.tStatue);
            degree = itemView.findViewById(R.id.tTextDegree);
            imageView = itemView.findViewById(R.id.tImageStatue);
            cardView = itemView.findViewById(R.id.tCardView);
            humidity = itemView.findViewById(R.id.tTextHumidity);
            speedWind = itemView.findViewById(R.id.tTextWind);
            cardView = itemView.findViewById(R.id.tCardView);

            view = itemView;

        }
    }
}
