package com.example.bashir.wetherapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ItemDecoration;
import android.support.v7.widget.SnapHelper;
import android.transition.Slide;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.bashir.wetherapp.PagerRecycler.RecyclerViewPager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements onHomeClick, SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {
    public static boolean isLandScape;
    private static int currentPosition;
    private static JSONArray jsonArrayO;

    private static final String URL = "https://samples.openweathermap.org/data/2.5/box/city?bbox=12,32,15,37,10&appid=b6907d289e10d714a6e88b30761fae22.json";
    private static final String urlImage = "http://openweathermap.org/img/w/";

    private RecyclerViewPager mRecyclerView;
    private RecyclerView recyclerView;
    private ConstraintLayout constraintLayout;
    private SwipeRefreshLayout swipeRefreshLayout;

    private ProgressBar progressBar;

    private CityAdapter adapter;
    private ArrayList<City> list;
    private RequestQueue requestQueue = null;
    private Context context;
    private ConstraintLayout noConnectionLayout;
    private Button buRety;
    private TextView mtvCityName;

    private boolean isResponse;

    @SuppressLint("ResourceAsColor")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("method_", "onCreate Method");
        //setupWindowAnimations();
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            window.setNavigationBarColor(Color.TRANSPARENT);
        }
        this.context = this;
        progressBar = findViewById(R.id.tProgress);
        mRecyclerView = findViewById(R.id.mRecyclerList);
        recyclerView = findViewById(R.id.mRecyclerDetails);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.VISIBLE);
        constraintLayout = findViewById(R.id.mConstraintLayout);
        initViewPager();
        if (!isLandScape) {
            Log.d("isLandScape", "" + isLandScape);
            swipeRefreshLayout = findViewById(R.id.mSwipeRefresh);
            mtvCityName = findViewById(R.id.mtv_city_name);
            swipeRefreshLayout.setOnRefreshListener(this);
        }
        noConnectionLayout = findViewById(R.id.mNoConnectionLayout);
        buRety = findViewById(R.id.mBuRetry);
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(this);
        }
        progressBar.setVisibility(View.VISIBLE);
        getList();
        adapter = new CityAdapter(this,R.layout.item_list,list, (onHomeClick) context);
        mRecyclerView.setAdapter(adapter);


        buRety.setOnClickListener(this);




    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("method_", "onStart Method");

    }

    @SuppressLint({"NewApi"})
    public void setupWindowAnimations() {
        // Re-enter transition is executed when returning to this activity
        Slide slideTransition = new Slide();
        slideTransition.setSlideEdge(Gravity.LEFT);
        slideTransition.setDuration(getResources().getInteger(R.integer.anim_duration_long));
        getWindow().setReenterTransition(slideTransition);
        getWindow().setExitTransition(slideTransition);
    }

    public void setDetail(final int pos) {
        if(!isLandScape){
            if(pos+1 < list.size())
              mtvCityName.setText(list.get(pos+1).getName());
        }

        Log.d("setDetaill", "pos: " + pos);
        JSONObject object = null;
        try {
            object = (JSONObject) jsonArrayO.get(pos);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ArrayList<detailsWeather> arrayList = new ArrayList<>();
        try {
            arrayList.add(new detailsWeather("City Name ", object.getString("name")));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            arrayList.add(new detailsWeather("Temperature", String.valueOf(((JSONObject) object.get("main")).getInt("temp"))));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            arrayList.add(new detailsWeather("Minimum Temperature", String.valueOf(((JSONObject) object.get("main")).getInt("temp_min"))));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            arrayList.add(new detailsWeather("Maximum Temperature", String.valueOf(((JSONObject) object.get("main")).getInt("temp_max"))));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            arrayList.add(new detailsWeather("Pressure", String.valueOf(((JSONObject) object.get("main")).getInt("pressure"))));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            arrayList.add(new detailsWeather("Sea Level", String.valueOf(((JSONObject) object.get("main")).getInt("sea_level"))));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            arrayList.add(new detailsWeather("Ground Level", String.valueOf(((JSONObject) object.get("main")).getInt("grnd_level"))));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            arrayList.add(new detailsWeather("Humidity", String.valueOf(((JSONObject) object.get("main")).getInt("humidity"))));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        recyclerView.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
        Log.d("bashiralterh", "size: "+ list.size());

        detialsAdapter adapter = new detialsAdapter(getApplicationContext(), R.layout.item_list_details, list.get(pos+1).getUrlImage(), arrayList);
        recyclerView.setAdapter(adapter);
        Log.d("visibility_","re: "+recyclerView.getVisibility()+" , Re: "+mRecyclerView.getVisibility()+", progress: "+progressBar.getVisibility());

    }

    @Override
    public void onHomeClickListener(View view, final ArrayList<City> list) {

        final int pos = mRecyclerView.getChildLayoutPosition(view);
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d("Handlerp", "Handler");
                setDetail(pos);
            }
        }, 0);
    }

    public boolean getList() {
        isResponse = false;
        list = new ArrayList<>();
        list.add(new City());

        JsonObjectRequest objectRequest = new JsonObjectRequest(URL, null, new Response.Listener<JSONObject>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(JSONObject response) {
                mRecyclerView.setVisibility(View.VISIBLE);
                noConnectionLayout.setVisibility(View.GONE);
                JSONArray jsonArray = null;
                try {
                    jsonArray = response.getJSONArray("list");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                jsonArrayO = jsonArray;
                Log.d("jsonArrayRequestResponseBashir", "the length = " + jsonArray.length());
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = null;
                    try {
                        object = (JSONObject) jsonArray.get(i);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    String nameCity = null;
                    try {
                        nameCity = object.getString("name");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    String statu = null;
                    try {
                        statu = (((JSONObject) object.getJSONArray("weather").get(0)).getString("main"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    String tempreture = null;
                     try {
                        tempreture = String.valueOf(((JSONObject) object.get("main")).getInt("temp"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    String image = null;
                    try {
                        image = ((JSONObject) object.getJSONArray("weather").get(0)).getString("icon");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    String urlIcon = urlImage + image + ".png";
                    int hum = 0;
                    try {
                        hum = (int) Math.ceil(((JSONObject) object.get("main")).getInt("humidity"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    String humidity = String.valueOf(hum);
                    String windSpeed = null;
                    try {
                        windSpeed = String.valueOf(((JSONObject) object.get("wind")).getInt("speed"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    ArrayList<detailsWeather> arrayList = new ArrayList<>();

                    list.add(new City(nameCity, tempreture, statu, urlIcon, humidity, windSpeed, arrayList));

                }
                mRecyclerView.setVisibility(View.VISIBLE);
                adapter = new CityAdapter(context, R.layout.item_list, list, (onHomeClick) context);
                mRecyclerView.setAdapter(adapter);
                Log.d("setVisibility", "yesoo");
                progressBar.setVisibility(View.GONE);
                currentPosition = 0;
                mRecyclerView.smoothScrollToPosition(1);
                recyclerView.setVisibility(View.GONE);
                setDetail(0);
                list.add(new City());
                isResponse = true;
                Log.d("whatis",""+list.get(1).getName());
                mRecyclerView.setVisibility(View.VISIBLE);
            }
        }, new Response.ErrorListener() {
            @SuppressLint("LongLogTag")
            @Override
            public void onErrorResponse(VolleyError error) {
               // mRecyclerView.setVisibility(View.GONE);
                recyclerView.setVisibility(View.GONE);
                Log.d("whatis","آخخخ");
                noConnectionLayout.setVisibility(View.VISIBLE);
                Log.d("jsonArrayRequestResponseBashir", "error response: " + error.getMessage());
                progressBar.setVisibility(View.GONE);
                Snackbar.make(constraintLayout, getResources().getString(R.string.noConnection), Snackbar.LENGTH_LONG).setAction("Retry",
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                retry();
                            }
                        }).show();
                isResponse = false;

            }
        });

        requestQueue.add(objectRequest);
        return isResponse;
    }


    protected void initViewPager() {
        LinearLayoutManager layout;
        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) { //check if landscape or portrait
            // In landscape
            isLandScape = true;
            layout = new LinearLayoutManager(this);
            Log.d("orientation_", "isLandScape");
        } else {
            // In portrait
            Log.d("orientation_", "portrait");
            isLandScape = false;
            layout = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,
                    false);
        }

        mRecyclerView.setLayoutManager(layout);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLongClickable(true);
        try {
            SnapHelper snapHelper = new LinearSnapHelper();
            snapHelper.attachToRecyclerView(mRecyclerView);
        }
        catch (Exception e){}
        mRecyclerView.addOnScrollListener(new CustomScrollListener());

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int scrollState) {
                //              updateState(scrollState);
                //  Log.d("onScrollStateChanged",": "+scrollState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int i, int i2) {
//                mPositionText.setText("First: " + mRecyclerViewPager.getFirstVisiblePosition());
                int childCount = mRecyclerView.getChildCount();
                int width = mRecyclerView.getChildAt(0).getWidth();
                int padding = (mRecyclerView.getWidth() - width) / 2;
//                mCountText.setText("Count: " + childCount);

                for (int j = 0; j < childCount; j++) {
                    View v = recyclerView.getChildAt(j);
                    float rate = 0;

                    if (v.getLeft() <= padding) {
                        if (v.getLeft() >= padding - v.getWidth()) {
                            rate = (padding - v.getLeft()) * 1f / v.getWidth();
                        } else {
                            rate = 1;
                        }
                        v.setScaleY(1 - rate * 0.1f);
                        v.setScaleX(1 - rate * 0.1f);

                    } else {
                        if (v.getLeft() <= recyclerView.getWidth() - padding) {
                            rate = (recyclerView.getWidth() - padding - v.getLeft()) * 1f / v.getWidth();
                        }
                        v.setScaleY(0.9f + rate * 0.1f);
                        v.setScaleX(0.9f + rate * 0.1f);
                    }
                }
            }
        });


        mRecyclerView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if (mRecyclerView.getChildCount() < 3) {
                    if (mRecyclerView.getChildAt(1) != null) {
                        if (mRecyclerView.getCurrentPosition() == 0) {
                            View v1 = mRecyclerView.getChildAt(1);
                            v1.setScaleY(0.9f);
                            v1.setScaleX(0.9f);
                        } else {
                            View v1 = mRecyclerView.getChildAt(0);
                            v1.setScaleY(0.9f);
                            v1.setScaleX(0.9f);
                        }
                    }
                } else {
                    if (mRecyclerView.getChildAt(0) != null) {
                        View v0 = mRecyclerView.getChildAt(0);
                        v0.setScaleY(0.9f);
                        v0.setScaleX(0.9f);
                    }
                    if (mRecyclerView.getChildAt(2) != null) {
                        View v2 = mRecyclerView.getChildAt(2);
                        v2.setScaleY(0.9f);
                        v2.setScaleX(0.9f);
                    }
                }
            }
        });
       /* mRecyclerView.addOnPageChangedListener(new RecyclerViewPager.OnPageChangedListener() { //
            @SuppressLint("LongLogTag")
            @Override
            public void OnPageChanged(int oldPosition, final int newPosition) {
                if (newPosition == oldPosition)
                    return;
                Log.d("addOnPageChangedListener", "old: " + oldPosition + " new: " + newPosition);
                progressBar.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        setDetail(newPosition);
                        Log.d("wowowo", "wowowo");


                    }
                }, 0);

            }
        });*/

    }

    @Override
    public void onRefresh() {
        recyclerView.setVisibility(View.GONE);
        retry();
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onClick(View v) {
        retry();
    }

    public void retry() {
        Handler handler = new Handler();
        mRecyclerView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        noConnectionLayout.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        //mRecyclerView.setVisibility(View.GONE);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getList();
            }
        }, 0);

    }

    public class CustomScrollListener extends RecyclerView.OnScrollListener {
        public CustomScrollListener() {
        }

        public void onScrollStateChanged(RecyclerView recyclerViewW, int newState) {
            final int pos = mRecyclerView.getCurrentPosition();
            if (pos == 0) {
                mRecyclerView.smoothScrollToPosition(1);
                return;
            }
            if (pos == list.size() || pos == list.size() - 1) {
                mRecyclerView.smoothScrollToPosition(list.size() - 2);
                return;
            }
            switch (newState) {
                case RecyclerView.SCROLL_STATE_IDLE:

                    if (currentPosition == pos)
                        return;
                    currentPosition = pos;
                    progressBar.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                    setDetail(pos - 1);
                    Log.d("onScrollStateChanged", "The RecyclerView is not scrolling -> " + pos + "\n");
                    break;
                case RecyclerView.SCROLL_STATE_DRAGGING:
                    Log.d("onScrollStateChanged", "Scrolling now\n");
                    break;
                case RecyclerView.SCROLL_STATE_SETTLING:
                    Log.d("onScrollStateChanged", "Scroll Settling\n");
                    break;
            }
        }
    }
}
