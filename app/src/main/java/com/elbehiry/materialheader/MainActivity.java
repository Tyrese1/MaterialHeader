package com.elbehiry.materialheader;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;

import com.elbehiry.header.MaterialHeader;

public class MainActivity extends AppCompatActivity {
    boolean adAuto = true;

    int [] images = {
            R.drawable.image1,
            R.drawable.image2,
            R.drawable.image3,
            R.drawable.image4
    };
    String [] image_links = {
            "http://imgur.com/download/55IkcVP",
            "http://imgur.com/download/5rtoa9S",
            "http://imgur.com/download/BilVc1r",
            "http://imgur.com/download/vRrmZO1"
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();


        ViewPager viewPager = (ViewPager) findViewById(R.id.image_slide_page);
        ViewGroup  imageCircleView = (ViewGroup) findViewById(R.id.layout_circle_images);

        new MaterialHeader.Builder(this)
                .PagerView(viewPager)
                .CircleContainer(imageCircleView)
                .TimeDuration(3000)
                .ImagesLinks(image_links)
                .DefaultImages(images)
                .autoAd(adAuto)
                .init();



    }
}
