package com.elbehiry.header;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.res.ResourcesCompat;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

import java.io.File;
import java.util.ArrayList;


public class SlideImageLayout {
	private static final String SDCARD_ROOT = Environment.getExternalStorageDirectory().getAbsolutePath() + "/";
	public static final String FILE_ROOT = SDCARD_ROOT + "adTemp/";

	private ArrayList<ImageView> imageList = null;
	private Activity activity = null;
	private ImageView[] imageViews = null;
	private ImageView imageView = null;
	private int pageIndex = 0;
	
	private int imageWidth = 0;
	private int imageHeight = 0;
	public SlideImageLayout(Activity activity) {
		// TODO Auto-generated constructor stub
		this.activity = activity;
		imageWidth = activity.getResources().getDisplayMetrics().widthPixels;
		imageHeight = imageWidth / 2;
		imageList = new ArrayList<ImageView>();
		
	}
	 	public View getSlideImageLayout(String imgUrl,int position,int defId){
		LinearLayout imageLinerLayout = new LinearLayout(activity);
		LinearLayout.LayoutParams imageLinerLayoutParames = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		ImageView iv = new ImageView(activity);
		iv.setScaleType(ScaleType.FIT_XY);
		iv.setTag(position);
			mkdir();





			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
				Drawable drawable = ResourcesCompat.getDrawable(activity.getResources(), defId, null);
				ImageLoaderUtil.loadImageAsync("SlideImageLayout", iv, imgUrl, FILE_ROOT,drawable, imageWidth);

			} else {
				ImageLoaderUtil.loadImageAsync("SlideImageLayout", iv, imgUrl, FILE_ROOT, activity.getResources().getDrawable(defId), imageWidth);
			}
		iv.setOnClickListener(new ImageOnClickListener());
		imageLinerLayout.addView(iv,imageLinerLayoutParames);
		imageList.add(iv);
		return imageLinerLayout;
	}

	public View getLinearLayout(View view,int width,int height){
		LinearLayout linerLayout = new LinearLayout(activity);
		LinearLayout.LayoutParams linerLayoutParames = new LinearLayout.LayoutParams(width, height,1);
		linerLayout.setPadding(10, 0, 10, 0);
		linerLayout.addView(view, linerLayoutParames);
		
		return linerLayout;
	}

	public void setCircleImageLayout(int size){
		imageViews = new ImageView[size];
	}
		public ImageView getCircleImageLayout(int index){
		imageView = new ImageView(activity);
		imageView.setLayoutParams(new LayoutParams(10,10));
        imageView.setScaleType(ScaleType.FIT_XY);
        
        imageViews[index] = imageView;
         
        if (index == 0) {  
            imageViews[index].setBackgroundResource(R.drawable.page_indicator_focused);
        } else {  
            imageViews[index].setBackgroundResource(R.drawable.page_indicator_unfocused);
        }  
         
        return imageViews[index];
	}
	public void setPageIndex(int index){
		pageIndex = index;
	}
    private class ImageOnClickListener implements OnClickListener {
    	@Override
    	public void onClick(View v) {
    		
    	}
    }
	public static void mkdir(){

		File file = new File(FILE_ROOT);
		if (!file.exists() || !file.isDirectory())
			file.mkdir();
	}

}
