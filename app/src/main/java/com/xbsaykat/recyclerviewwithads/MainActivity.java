package com.xbsaykat.recyclerviewwithads;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public static final int ITEMS_PER_AD = 9;
    private ArrayList<Object> mListItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initAdMobAdsSDK();
        addCountriesData();
        addAdMobBannerAds();
        setUIRef();
        loadBannerAds();
    }

    private void initAdMobAdsSDK()
    {
        MobileAds.initialize(this, new OnInitializationCompleteListener()
        {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus)
            {
            }
        });
    }

    private void setUIRef()
    {
        //Reference of RecyclerView
        RecyclerView mRecyclerView = findViewById(R.id.myRecyclerView);

        //Linear Layout Manager
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this, RecyclerView.VERTICAL, false);

        //Set Layout Manager to RecyclerView
        mRecyclerView.setLayoutManager(linearLayoutManager);

        //Create adapter
        MyRecyclerViewAdapter myRecyclerViewAdapter = new MyRecyclerViewAdapter(mListItems, new MyRecyclerViewAdapter.MyRecyclerViewItemClickListener()
        {
            //Handling clicks
            @Override
            public void onItemClicked(Country country)
            {
                Toast.makeText(MainActivity.this, country.getName(), Toast.LENGTH_SHORT).show();
            }
        });

        //Set adapter to RecyclerView
        mRecyclerView.setAdapter(myRecyclerViewAdapter);
    }

    private void addCountriesData()
    {
        mListItems.add(new Country("Canada", "Ottawa", "Canadian Dollar", "North America"));
        mListItems.add(new Country("Norway", "Oslo", "Norwegian Krone", "Europe"));
        mListItems.add(new Country("Malaysia", "Kuala Lumpur", "Malaysian Ringgit", "Asia"));
        mListItems.add(new Country("Singapore", "Singapore", "Singapore Dollar", "Asia"));
        mListItems.add(new Country("United States of America", "Washington, D.C.", "United States Dollar", "North America"));
        mListItems.add(new Country("India", "New Delhi", "Indian Rupee", "Asia"));
        mListItems.add(new Country("Brazil", "Brasilia", " Brazilian Real", "South America"));
        mListItems.add(new Country("Australia", "Canberra", "Australian Dollar", "Oceania"));
        mListItems.add(new Country("Russia", "Moscow", "Russian Ruble", "Europe, Asia"));
        mListItems.add(new Country("Japan", "Tokyo", "Japanese Yen", "Asia"));

        //adding extra dummy data
        for (int i = 0; i < 80; i++)
        {
            mListItems.add(new Country("Country" + i, "Capital" + i, "dummy", "dummy"));
        }
    }

    private void addAdMobBannerAds()
    {
        for (int i = ITEMS_PER_AD; i <= mListItems.size(); i += ITEMS_PER_AD)
        {
            final AdView adView = new AdView(MainActivity.this);
            adView.setAdSize(AdSize.MEDIUM_RECTANGLE);
            adView.setAdUnitId(getResources().getString(R.string.admob_banner_ad_id));
            mListItems.add(i, adView);
        }

        loadBannerAds();
    }

    private void loadBannerAds()
    {
        //Load the first banner ad in the items list (subsequent ads will be loaded automatically in sequence).
        loadBannerAd(ITEMS_PER_AD);
    }

    private void loadBannerAd(final int index)
    {
        if (index >= mListItems.size())
        {
            return;
        }

        Object item = mListItems.get(index);
        if (!(item instanceof AdView))
        {
            throw new ClassCastException("Expected item at index " + index + " to be a banner ad" + " ad.");
        }

        final AdView adView = (AdView) item;

        // Set an AdListener on the AdView to wait for the previous banner ad
        // to finish loading before loading the next ad in the items list.
        adView.setAdListener(new AdListener()
        {
            @Override
            public void onAdLoaded()
            {
                super.onAdLoaded();
                // The previous banner ad loaded successfully, call this method again to
                // load the next ad in the items list.
                loadBannerAd(index + ITEMS_PER_AD);
            }

            @Override
            public void onAdFailedToLoad(int errorCode)
            {
                // The previous banner ad failed to load. Call this method again to load
                // the next ad in the items list.
                Log.e("MainActivity", "The previous banner ad failed to load. Attempting to"
                        + " load the next banner ad in the items list.");
                loadBannerAd(index + ITEMS_PER_AD);
            }
        });

        // Load the banner ad.
        adView.loadAd(new AdRequest.Builder().build());
    }

    @Override
    protected void onResume()
    {
        for (Object item : mListItems)
        {
            if (item instanceof AdView)
            {
                AdView adView = (AdView) item;
                adView.resume();
            }
        }
        super.onResume();
    }

    @Override
    protected void onPause()
    {
        for (Object item : mListItems)
        {
            if (item instanceof AdView)
            {
                AdView adView = (AdView) item;
                adView.pause();
            }
        }
        super.onPause();
    }

    @Override
    protected void onDestroy()
    {
        for (Object item : mListItems)
        {
            if (item instanceof AdView)
            {
                AdView adView = (AdView) item;
                adView.destroy();
            }
        }
        super.onDestroy();
    }
}
