package com.example.comicapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.comicapp.Adapter.MyComicAdapter;
import com.example.comicapp.Adapter.MySliderAdapter;
import com.example.comicapp.Common.Common;
import com.example.comicapp.Interface.IBannerLoadDone;
import com.example.comicapp.Interface.ICommicLoadDone;
import com.example.comicapp.Model.Comic;
import com.example.comicapp.Service.PicassoLoadingService;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;
import ss.com.bannerslider.Slider;

public class MainActivity extends AppCompatActivity implements IBannerLoadDone, ICommicLoadDone {
    Slider slider;
    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView recycler_comic;
    TextView tv_comic;
    ImageView btn_filter_search;


    //Database
    DatabaseReference banners, comics;

    //Listener
    IBannerLoadDone bannerListener;
    ICommicLoadDone comicListener;
    Dialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //init database
        banners = FirebaseDatabase.getInstance().getReference("Banners");
        comics = FirebaseDatabase.getInstance().getReference("Comic");

        //inti listener
        bannerListener = this;
        comicListener = this;

        btn_filter_search = findViewById(R.id.btn_showfilter_search);
        btn_filter_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, FilterSearchActivity.class));
            }
        });

        slider = findViewById(R.id.slider);
        Slider.init(new PicassoLoadingService());

        swipeRefreshLayout = findViewById(R.id.swipe_to_refresh);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                R.color.colorPrimaryDark);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadBanner();
                loadComic();
            }
        });
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                loadBanner();
                loadComic();
            }
        });

        recycler_comic = findViewById(R.id.recycler_comic);
        recycler_comic.setHasFixedSize(true);
        recycler_comic.setLayoutManager(new GridLayoutManager(this, 2));

        tv_comic = findViewById(R.id.txt_comic);

    }

    private void loadComic() {
        //show dialog
        dialog = new SpotsDialog.Builder().setContext(this)
                .setCancelable(false)
                .setMessage("Please wait...")
                .build();

        if (!swipeRefreshLayout.isRefreshing())
            dialog.show();


        comics.addListenerForSingleValueEvent(new ValueEventListener() {

            List<Comic> comic_load = new ArrayList<>();

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot comicSnapShot : dataSnapshot.getChildren()) {
                    Comic comic = comicSnapShot.getValue(Comic.class);
                    comic_load.add(comic);
                }
                comicListener.onComicLoadDoneListener(comic_load);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(MainActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void loadBanner() {
        banners.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String> bannerList = new ArrayList<>();

                for (DataSnapshot bannerSnapShot : dataSnapshot.getChildren()) {
                    String image = bannerSnapShot.getValue(String.class);
                    bannerList.add(image);
                }
                //Call listener
                bannerListener.onBannerLoadDoneListener(bannerList);
                Log.e("SIZEBANNER", bannerList.size() + "");

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBannerLoadDoneListener(List<String> banners) {
        slider.setAdapter(new MySliderAdapter(banners));
    }

    @Override
    public void onComicLoadDoneListener(List<Comic> comicList) {
        Common.comicList = comicList;
        recycler_comic.setAdapter(new MyComicAdapter(getBaseContext(), comicList));
        tv_comic.setText(new StringBuilder("NEW COMIC (")
                .append(comicList.size())
                .append(")"));

        if (!swipeRefreshLayout.isRefreshing()) {
            dialog.dismiss();
        }

    }
}