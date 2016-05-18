package com.elbehiry.header;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by elbehiry on 15/05/16.
 */
public class MaterialHeader {
    protected final Builder mBuilder;

    protected MaterialHeader(Builder builder) {
        mBuilder = builder;

    }
    public static class Builder {
        private  static final int H_UPDATE_AUTO_SELECTED = 1;
        private Handler handler;
        private boolean adScrolling = false;
        private ArrayList<View> imagePageViews = null;
        private int pageIndex = 0;
        private SlideImageLayout slideLayout = null;
        private ImageView[] imageCircleViews = null;
        private ViewPager viewPager = null;
        private ViewGroup imageCircleView = null;
        private boolean adAuto = false;
        private final Activity activity ;
        private int count;
        private  int [] image = null ;
        private  String [] imgsUrl = null;



        public Builder(@NonNull Activity activity) {
            this.activity = activity;
        }


        public Builder TimeDuration(@NonNull int duration){
            this.count = duration;
            return this;
        }
        public Builder DefaultImages(@NonNull int[] images){
            this.image = images;
            return this;
        }
         public Builder ImagesLinks(@NonNull String[] links){
             if(checkIntetrnetPermission()) {
                 this.imgsUrl = links;
             }
            return this;
        }
        public boolean checkIntetrnetPermission()
        {

            String permission = "android.permission.INTERNET";
            int res = activity.checkCallingOrSelfPermission(permission);
            return (res == PackageManager.PERMISSION_GRANTED);
        }

        public Builder PagerView(@NonNull ViewPager pager){
            this.viewPager = pager;
            return this;

        }
        public Builder CircleContainer(@NonNull ViewGroup viewGroup){
            this.imageCircleView = viewGroup;
            return this;
        }
        public Builder autoAd(@NonNull Boolean auto){
            this.adAuto = auto;
            return this;
        }
        private void initHandler() {
            handler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    switch (msg.what) {

                        case H_UPDATE_AUTO_SELECTED:
                            updateAddSelecedStatus();
                            if(viewPager != null) {
                                viewPager.setCurrentItem(pageIndex);
                            }

                    }
                }
            };
        }
        private void InitHeadAd() {
            imagePageViews = new ArrayList<View>();
            int length;
            if(imgsUrl != null && image != null) {
                 length = imgsUrl.length;
            }
            else if(imgsUrl == null && image != null){
                 length = image.length;

            }
            else {
                length = 0;
            }
            if(length != 0 ) {
                imageCircleViews = new ImageView[length];
                slideLayout = new SlideImageLayout(activity);
                slideLayout.setCircleImageLayout(length);
                for (int i = 0; i < length; i++) {
                    //int defId =image[0];
                    int defId = image[i];

                   View ImageView ;
                    if(imgsUrl == null){
                        ImageView = slideLayout.getSlideImageLayout(null, i, defId);

                    }
                    else {
                    ImageView = slideLayout.getSlideImageLayout(imgsUrl[i], i, defId);
                    }
                    imagePageViews.add(ImageView);
                    imageCircleViews[i] = slideLayout.getCircleImageLayout(i);
                    if (imageCircleView != null) {
                        imageCircleView.addView(slideLayout.getLinearLayout(imageCircleViews[i], 10, 10));
                    }
                }
                if (viewPager != null) {
                    viewPager.setAdapter(new SlideImageAdapter());
                    // viewPager.setOnPageChangeListener(new ImagePageChangeListener());
                    viewPager.addOnPageChangeListener(new ImagePageChangeListener());
                }
            }
        }

        private void updateAddSelecedStatus() {
            try {
                slideLayout.setPageIndex(pageIndex);
                for (int i = 0; i < imageCircleViews.length; i++) {
                    if (i == pageIndex) {
                        imageCircleViews[pageIndex].setBackgroundResource(R.drawable.page_indicator_focused);
                    } else {
                        imageCircleViews[i].setBackgroundResource(R.drawable.page_indicator_unfocused);
                    }
                }
            }
            catch (Exception c){
                c.printStackTrace();
            }
        }
        Thread adThread = new Thread() {
            public void run() {
                while (adAuto) {
                    try {
                        sleep(count);
                        if (adScrolling == false) {
                            pageIndex++;
                            if (pageIndex > imagePageViews.size() - 1)
                                pageIndex = 0;
                            handler.sendEmptyMessage(H_UPDATE_AUTO_SELECTED);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            };
        };
        private class SlideImageAdapter extends PagerAdapter {
            @Override
            public int getCount() {
                // adScrolling = true;
                return imagePageViews.size();
            }

            @Override
            public boolean isViewFromObject(View arg0, Object arg1) {
                return arg0 == arg1;

            }

            @Override
            public int getItemPosition(Object object) {
                return super.getItemPosition(object);
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                ( container).removeView(imagePageViews.get(position));
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                ( container).addView(imagePageViews.get(position));

                return imagePageViews.get(position);

            }

            @Override
            public void restoreState(Parcelable arg0, ClassLoader arg1) {
            }

            @Override
            public Parcelable saveState() {
                return null;
            }


        }

        private class ImagePageChangeListener implements ViewPager.OnPageChangeListener {
            @Override
            public void onPageScrollStateChanged(int arg0) {
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageSelected(int index) {
                pageIndex = index;
                updateAddSelecedStatus();
            }
        }

        public Builder init() {
            if(image == null && imgsUrl == null) {
            }
            else {
                initHandler();
                InitHeadAd();
                adThread.start();
            }

            return this;
        }


        }

}
