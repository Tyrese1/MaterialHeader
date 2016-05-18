package com.elbehiry.header;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.SoftReference;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;


public class ImageLoaderUtil {
	private static final String TAG = "ImageLoaderUtil";
	private static HashMap<String, SoftReference<Bitmap>> mImageCache = new HashMap<String, SoftReference<Bitmap>>();
	private static int i = 0;
	public static void loadImageAsync(String mClass,ImageView imageview, String url,
			String cacheLocalDir, Drawable defImg,int commpressWidth) {
		if(imageview !=null && url !=null && url.trim().length() > 0 && !"null".equals(url)){
			new ImageAsyncTask().execute(imageview, url, cacheLocalDir, defImg,commpressWidth);
		}else if(imageview != null && defImg != null){
			imageview.setImageDrawable(defImg);
		}
	}

	public static void loadLocalImageAsync(ImageView imageview, String path,
			int defatultImg,int commpressWidth) {
		if(imageview !=null && path !=null && path.trim().length() > 0 && !"null".equals(path)){
			new ImageLocalAsyncTask().execute(imageview, path, defatultImg,commpressWidth);
		}else if(imageview != null && defatultImg != 0){
			imageview.setImageResource(defatultImg);
		}
	}
	
	

	public static class ImageAsyncTask extends AsyncTask<Object, Object, Bitmap> {
		private ImageView imageView = null;
		private String url = null;
		private String cacheLocalPathDir = null;
		private Drawable defaultImage = null;
		private int minCompressWidth = 50;
		private int commpressWidth = 50;
		@Override
		protected Bitmap doInBackground(Object... params) {
			imageView = (ImageView) params[0];
			if (params.length > 1)
				url = (String) params[1];
			if (params.length > 2)
				cacheLocalPathDir = (String) params[2];
			if (params.length > 3)
				defaultImage = (Drawable) params[3];
			if(params.length > 4)
				commpressWidth =(Integer) params[4];
			Bitmap bmp = null;
			
			if (cacheLocalPathDir != null ) {
				File file = new File(cacheLocalPathDir + MD5.md5(url));
				if (file.exists()) {
					try {
						FileInputStream fs = new FileInputStream(file);
						bmp = BitmapFactory.decodeStream(fs);
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}
				}
				
			}else {
				SoftReference<Bitmap> reference = mImageCache.get(url);
				if(reference !=null)
				   bmp = reference.get(); 
			}
			if (bmp == null) {
				try {
					bmp = BitmapFactory.decodeStream(new URL(url).openStream());
					
				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				if(bmp != null ){
					if (cacheLocalPathDir != null) {
						if(commpressWidth < minCompressWidth)
							commpressWidth = minCompressWidth;
						float commpressHeight =0;
						if(commpressWidth > bmp.getWidth())
							commpressWidth =  bmp.getWidth();
						commpressHeight =(float) bmp.getHeight() / ((float)bmp.getWidth() / (float)commpressWidth);
						bmp = Bitmap.createScaledBitmap(bmp, (int) commpressWidth, (int) commpressHeight, true);
						//saveBitmap(cacheLocalPathDir + MD5.md5(url),bmp);
						bmp = HImage.compressImage(bmp, 100);
						String imageType = url.substring(url.lastIndexOf(".")+ 1,url.length());
						HImage.saveBitmap(bmp, cacheLocalPathDir + MD5.md5(url), imageType);
					}else {
						mImageCache.put(url, new SoftReference<Bitmap>(bmp));
					}
				}
				
			}
			return bmp;
		}
		
		protected void onPostExecute(Bitmap result) {
			if (result != null){
				
				imageView.setImageBitmap(result);
			} else if (defaultImage != null){
				imageView.setImageDrawable(defaultImage);
			}
			result = null;
		}
	}
	

	public static class ImageLocalAsyncTask extends AsyncTask<Object, Object, Bitmap> {
		private ImageView imageView = null;
		private String path = null;
		private int defaultImage = 0;
		private int minCompressWidth = 50;
		private int commpressWidth = 50;
		@Override
		protected Bitmap doInBackground(Object... params) {
			imageView = (ImageView) params[0];
			if (params.length > 1)
				path = (String) params[1];
			if (params.length > 2)
				defaultImage =  (Integer) params[2];
			if(params.length > 3)
				commpressWidth =(Integer) params[3];

			Bitmap bmp = null;
			File file = new File(path);
			if (file.exists()) {
				try {
					FileInputStream fs = new FileInputStream(file);
					bmp = BitmapFactory.decodeStream(fs);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			}
			if (bmp != null ) {
				if(commpressWidth < minCompressWidth)
					commpressWidth = minCompressWidth;
				float commpressHeight =0;
				if(commpressWidth > bmp.getWidth())
					commpressWidth =  bmp.getWidth();
				commpressHeight =(float) bmp.getHeight() / ((float)bmp.getWidth() / (float)commpressWidth);
				bmp = Bitmap.createScaledBitmap(bmp, (int) commpressWidth, (int) commpressHeight, true);
			}else if (bmp !=null && path != null){
				mImageCache.put(path, new SoftReference<Bitmap>(bmp));
			}
			return bmp;
		}

		protected void onPostExecute(Bitmap result) {
			if (result != null){
				imageView.setImageBitmap(result);
			} else if (defaultImage != 0){
				imageView.setImageResource(defaultImage);
			}
		}
	}


	public static void saveBitmap(String path, Bitmap mBitmap) {
		File f = new File(path);
		if(!f.exists())
			createDipPath(path);
		FileOutputStream fOut = null;
		try {
			fOut = new FileOutputStream(f);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
		try {
			fOut.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			fOut.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
		public static void createDipPath(String file) {
		String parentFile = file.substring(0, file.lastIndexOf("/"));
		File file1 = new File(file);
		File parent = new File(parentFile);

		if (!file1.exists()) {
			parent.mkdirs();
			try {
				file1.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void loadRoundedImageAsync(Context context,ImageView imageview, String url,
			String cacheLocalDir, int defaultImage,int commpressWidth) {
		Bitmap defaultBitmap = null;
		if(defaultImage > 0)
			BitmapFactory.decodeResource(context.getResources(), defaultImage);
		if(imageview !=null && url !=null && url.trim().length() > 0 && !"null".equals(url)){
			new ImageRoundedAsyncTask().execute(imageview, url, cacheLocalDir, defaultBitmap,commpressWidth);
		}else if(imageview != null && defaultBitmap != null){
			defaultBitmap = HImage.getRoundedCornerBitmap(defaultBitmap);
			imageview.setImageBitmap(defaultBitmap);
		}
	}
	

	public static class ImageRoundedAsyncTask extends
			AsyncTask<Object, Object, Bitmap> {
		private ImageView imageView = null;
		private String url = null;
		private String cacheLocalPathDir = null;
		private Bitmap defaultImage = null;
		private int minCompressWidth = 50;
		private int commpressWidth = 50;
		@Override
		protected Bitmap doInBackground(Object... params) {
			imageView = (ImageView) params[0];
			if (params.length > 1)
				url = (String) params[1];
			if (params.length > 2)
				cacheLocalPathDir = (String) params[2];
			if (params.length > 3)
				defaultImage = (Bitmap) params[3];
			if(params.length > 4)
				commpressWidth =(Integer) params[4];
			Bitmap bmp = null;
			
			if (cacheLocalPathDir != null && url !=null) {
				File file = new File(cacheLocalPathDir + MD5.md5(url));
				if (file.exists()) {
					try {
						FileInputStream fs = new FileInputStream(file);
						bmp = BitmapFactory.decodeStream(fs);
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}
				}
			}else if (url !=null ){
				SoftReference<Bitmap> reference = mImageCache.get(url);
				if(reference !=null)
				   bmp = reference.get(); 
			}
			if (bmp == null && url !=null) {
				try {
					System.out.println("URL:" + url);
					bmp = BitmapFactory.decodeStream(new URL(url).openStream());
				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				if (bmp != null && cacheLocalPathDir != null) {
					if(commpressWidth < minCompressWidth)
						commpressWidth = minCompressWidth;
					int commpressHeight =0;
					if(commpressWidth > bmp.getWidth())
						commpressWidth =  bmp.getWidth();
					commpressHeight = bmp.getHeight() / (bmp.getWidth() / commpressWidth);
					bmp = Bitmap.createScaledBitmap(bmp, commpressWidth, commpressHeight, true);
					saveBitmap(cacheLocalPathDir + MD5.md5(url),bmp);
				}else if (bmp !=null && url != null){
					mImageCache.put(url, new SoftReference<Bitmap>(bmp));
				}
			}
			return bmp;
		}
		
		protected void onPostExecute(Bitmap result) {
			if (result != null){
				result = HImage.getRoundedCornerBitmap(result,10);
				imageView.setImageBitmap(result);
			} else if (defaultImage != null){
				imageView.setImageBitmap(HImage.getRoundedCornerBitmap(defaultImage,20));
			}
			result = null;
		}
	}
}
