package com.elbehiry.header;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@SuppressLint("NewApi")
public class HImage {
	private static final String TAG = "AdHeader";
	public static final int LEFT = 0;   
	public static final int RIGHT = 1;   
	public static final int TOP = 3;   
	public static final int BOTTOM = 4;
	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap) {
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);
		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);
		final float roundPx = 90;
		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);
		return output;
	}

	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap,float angle) {
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);
		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);
		//final float roundPx = 90;
		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, angle, angle, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);
		return output;
	}

	public static Bitmap getSmallBitmap(String filePath) {
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		//BitmapFactory.decodeFile(filePath, options);
		//options.inSampleSize = calculateInSampleSize(options, 480, 800);
		options.inSampleSize = 2;
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile(filePath, options);
	}
	
	public static Bitmap getSmallBitmap(String filePath,int width,int height) {
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, options);
		options.inSampleSize = calculateInSampleSize(options, width, height);
		options.inJustDecodeBounds = false;
		
		return BitmapFactory.decodeFile(filePath, options);
	}

	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;
		if (height > reqHeight || width > reqWidth) {
			// Calculate ratios of height and width to requested height and
			// width
			final int heightRatio = Math.round((float) height
					/ (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);
			// Choose the smallest ratio as inSampleSize value, this will
			// guarantee
			// a final image with both dimensions larger than or equal to the
			// requested height and width.
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}
		return inSampleSize;
	}

	public static void saveBitmap(Bitmap bitmap, String imagePath,String imageType) {
		try {
			File file = new File(imagePath);
			FileUtil.createDipPath(imagePath);
			FileOutputStream fOut = new FileOutputStream(file);
			if("PNG".equalsIgnoreCase(imageType)){
				bitmap.compress(CompressFormat.PNG, 100, fOut);
			}else if("JPG".equalsIgnoreCase(imageType) || "JPEG".equalsIgnoreCase(imageType)){
				bitmap.compress(CompressFormat.JPEG, 100, fOut);
			}else  if("WEBP".equalsIgnoreCase(imageType) ){
				bitmap.compress(CompressFormat.WEBP, 100, fOut);
			}else{
			}
			fOut.flush();
			fOut.close();
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
		}

	}


	public static void saveBitmap(Bitmap bitmap, String imagePath,int s) {
		File file = new File(imagePath);
		FileUtil.createDipPath(imagePath);
		FileOutputStream fOut = null;
		try {
			fOut = new FileOutputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		bitmap.compress(CompressFormat.WEBP, s, fOut);
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


	public static String compressImage(String sourceImagePath, String outDirectory) {
		int maxWidth = 480;
		int maxHeight = 800;
		String compressPath = null;
		BitmapFactory.Options ops = new BitmapFactory.Options();
		ops.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(sourceImagePath, ops);
		double ratio = 1.0;
		if (ops.outWidth > ops.outHeight && ops.outWidth > maxWidth) {
			ratio = ops.outWidth / maxWidth;
		} else if (ops.outHeight > ops.outWidth && ops.outHeight > maxHeight) {
			ratio = ops.outHeight / maxHeight;
		} else {
			return sourceImagePath;
		}
		BitmapFactory.Options newOps = new BitmapFactory.Options();
		newOps.inSampleSize = (int) (ratio + 1);
		newOps.outWidth = (int) (ops.outWidth / ratio);
		newOps.outHeight = (int) (ops.outHeight / ratio);
		Bitmap bitmap = BitmapFactory.decodeFile(sourceImagePath, newOps);
		compressPath = outDirectory+ UUID.randomUUID()+ sourceImagePath.substring(sourceImagePath.lastIndexOf("."),sourceImagePath.length());
		File outFile = new File(compressPath);
		try {
			File parent = outFile.getParentFile();
			if (!parent.exists()) {
				parent.mkdirs();
			}
			outFile.createNewFile();
			OutputStream os = new FileOutputStream(outFile);
			bitmap.compress(CompressFormat.JPEG, 100, os);
			os.close();
			bitmap.recycle();

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return compressPath;
	}


	public static Bitmap getBitmap(String url, String localPath) {
		Bitmap bitmap = null;
		File file = new File(localPath);
		if (file.exists()) {
			try {
				FileInputStream fs = new FileInputStream(file);
				bitmap = BitmapFactory.decodeStream(fs);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		} else {
			bitmap = getHttpBitmap(url);
		}
		return bitmap;

	}


	public static Bitmap getHttpBitmap(String url) {
		URL myFileURL;
		Bitmap bitmap = null;
		try {
			myFileURL = new URL(url);

			HttpURLConnection conn = (HttpURLConnection) myFileURL
					.openConnection();
			conn.setConnectTimeout(6000);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			// conn.connect();
			InputStream is = conn.getInputStream();
			bitmap = BitmapFactory.decodeStream(is);
			is.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bitmap;

	}


	public static Bitmap getLocalBitMap(String localPath) {
		System.out.println("localPath:" + localPath);
		File file = new File(localPath);
		Bitmap bitmap = null;
		if (file.exists()) {
			try {
				FileInputStream fs = new FileInputStream(file);
				bitmap = BitmapFactory.decodeStream(fs);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}

		}
		return bitmap;
	}

	public static boolean copyAppFileToSDCard(Context context, int imgForm,
			String toPath) {
		boolean copyResult = true;
		try {
			// File dir = new File(toPath);

			FileUtil.createDipPath(toPath);
			InputStream is = context.getResources().openRawResource(imgForm);
			FileOutputStream fos = new FileOutputStream(new File(toPath));
			byte[] buffer = new byte[8192];
			int count = 0;
			while ((count = is.read(buffer)) > 0) {
				fos.write(buffer, 0, count);
			}
			fos.close();
			is.close();
			// setTitle("copy DB");

		} catch (Exception e) {
			copyResult = false;
		}
		return copyResult;
	}


	public static ArrayList<String> compressImage(List<String> images,String compressDir){
		if(images == null)
			return null;
		int maxSize = 1000*1024;
		ArrayList<String> resultImage = new ArrayList<String>();
		for (int i = 0; i < images.size(); i++) {
			String imagePath = images.get(i);
			File file = new File(imagePath);
			if(file.exists()){
				long length = file.length();
				//int compressPercent = 100;
				if(length > maxSize){
					BitmapFactory.Options newOpts = new BitmapFactory.Options();
			        newOpts.inJustDecodeBounds = true;
			        Bitmap bitmap = BitmapFactory.decodeFile(imagePath, newOpts);
			        newOpts.inJustDecodeBounds = false;
			        int w = newOpts.outWidth;
			        int h = newOpts.outHeight;
			        float hh = 800f;
			        float ww = 480f;
			        int be = 1;
			        if (w > h && w > ww) {
			            be = (int) (newOpts.outWidth / ww);
			        } else if (w < h && h > hh) {
			            be = (int) (newOpts.outHeight / hh);
			        }
			        if (be <= 0)
			            be = 1;
			        newOpts.inSampleSize = be;
			        bitmap = BitmapFactory.decodeFile(imagePath, newOpts);

					String fileName  = file.getName();
					String newFileName = null;
					if(fileName.indexOf(".") > 0)
						newFileName = compressDir + UUID.randomUUID().toString() + fileName.substring(fileName.lastIndexOf("."),fileName.length());
					else
						newFileName = compressDir + UUID.randomUUID().toString() +".jpg";

					//String saveImagePath =compressDir +file.getName();
					File saveImagefile = new File(newFileName);
					FileUtil.createDipPath(newFileName);
					FileOutputStream fOut = null;
					try {
						fOut = new FileOutputStream(saveImagefile);
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}
					file = new File(imagePath);
					length = file.length();
					//compressPercent =(int)(length /maxSize);
					bitmap.compress(CompressFormat.JPEG, 100, fOut);
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
					resultImage.add(newFileName);
				}else{
					String strNewName = compressDir + UUID.randomUUID().toString() + imagePath.substring(imagePath.lastIndexOf("."), imagePath.length());
					FileUtil.copyFile(imagePath, strNewName);
					resultImage.add(strNewName);
				}
			}
		}
		return resultImage;
	}

	public static Bitmap comp(Bitmap image) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(CompressFormat.JPEG, 100, baos);
		if( baos.toByteArray().length / 1024> 512) {
			baos.reset();
			image.compress(CompressFormat.JPEG, 50, baos);
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		newOpts.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
		newOpts.inJustDecodeBounds = false;
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		float hh = 800f;
		float ww = 480f;
		int be = 1;
		if (w > h && w > ww) {
			be = (int) (newOpts.outWidth / ww);
		} else if (w < h && h > hh) {
			be = (int) (newOpts.outHeight / hh);
		}
		if (be <= 0)
			be = 1;
		newOpts.inSampleSize = be;
		isBm = new ByteArrayInputStream(baos.toByteArray());
		bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
		return compressImage(bitmap,100);
	}

	public static Bitmap getimage(String srcPath) {
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		newOpts.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
		newOpts.inJustDecodeBounds = false;
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		float hh = 800f;
		float ww = 480f;

		int be = 1;
		if (w > h && w > ww) {
			be = (int) (newOpts.outWidth / ww);
		} else if (w < h && h > hh) {
			be = (int) (newOpts.outHeight / hh);
		}
		if (be <= 0)
			be = 1;
		newOpts.inSampleSize = be;
		bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
		return compressImage(bitmap,100);
	}

	public static Bitmap compressImage(Bitmap image, int size) {
		try{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(CompressFormat.JPEG, 100, baos);
		int options = 100;
		while ( baos.toByteArray().length / 1024 > size) {
			baos.reset();
			image.compress(CompressFormat.JPEG, options, baos);
			options -= 10;
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);
			return bitmap;
		}catch(Exception e){
			return null;
		}
	}

	public static Bitmap rotaingImageView(int angle, Bitmap bitmap) {
		if(bitmap == null)
			return null;
		Matrix matrix = new Matrix();
		matrix.postRotate(angle);
		Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
				bitmap.getWidth(), bitmap.getHeight(), matrix, true);
		return resizedBitmap;
	}


	public static int readPictureDegree(String path) {
		int degree = 0;
		try {
			ExifInterface exifInterface = new ExifInterface(path);
			int orientation = exifInterface.getAttributeInt(
					ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);
			switch (orientation) {
			case ExifInterface.ORIENTATION_ROTATE_90:
				degree = 90;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				degree = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_270:
				degree = 270;
				break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return degree;
	}


	public static Bitmap getLocalThumbImg(String path,float width,float height){
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		newOpts.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeFile(path, newOpts);
		newOpts.inJustDecodeBounds = false;
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		int be = 1;
		if (w > h && w > width) {
			be = (int) (newOpts.outWidth / width);
		} else if (w < h && h > height) {
			be = (int) (newOpts.outHeight / height);
		}
		if (be <= 0)
			be = 1;
		newOpts.inSampleSize = be;
		bitmap = BitmapFactory.decodeFile(path, newOpts);
		bitmap = compressImage(bitmap,100);
		int degree = readPictureDegree(path);
		bitmap = rotaingImageView(degree, bitmap);
		return bitmap;
	}


	public static Bitmap toGrayscale(Bitmap bmpOriginal) {
		int width, height;
		height = bmpOriginal.getHeight();
		width = bmpOriginal.getWidth();
		Bitmap bmpGrayscale = Bitmap.createBitmap(width, height,
				Config.RGB_565);
		Canvas c = new Canvas(bmpGrayscale);
		Paint paint = new Paint();
		ColorMatrix cm = new ColorMatrix();
		cm.setSaturation(0);
		ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
		paint.setColorFilter(f);
		c.drawBitmap(bmpOriginal, 0, 0, paint);
		return bmpGrayscale;
	}

	public static Bitmap toGrayscale(Bitmap bmpOriginal, int pixels) {
		return toRoundCorner(toGrayscale(bmpOriginal), pixels);
	}

	public static Bitmap toRoundCorner(Bitmap bitmap, int pixels) {
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);
		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);
		final float roundPx = pixels;
		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);
		return output;
	}

	public static BitmapDrawable toRoundCorner(BitmapDrawable bitmapDrawable, int pixels) {
	Bitmap bitmap = bitmapDrawable.getBitmap();
	bitmapDrawable = new BitmapDrawable(toRoundCorner(bitmap, pixels));
	return bitmapDrawable;
	}

	public static Bitmap createBitmapForWatermark(Bitmap src, Bitmap watermark) {
		if (src == null) {
			return null;
		}
		int w = src.getWidth();
		int h = src.getHeight();
		int ww = watermark.getWidth();
		int wh = watermark.getHeight();
		// create the new blank bitmap
		Bitmap newb = Bitmap.createBitmap(w, h, Config.ARGB_8888);
		Canvas cv = new Canvas(newb);
		// draw src into
		cv.drawBitmap(src, 0, 0, null);
		// draw watermark into
		cv.drawBitmap(watermark, w - ww + 5, h - wh + 5, null);
		// save all clip
		cv.save(Canvas.ALL_SAVE_FLAG);
		// store
		cv.restore();
		return newb;
	}


	public static Bitmap potoMix(int direction, Bitmap... bitmaps) {
		if (bitmaps.length <= 0) {
			return null;
		}
		if (bitmaps.length == 1) {
			return bitmaps[0];
		}
		Bitmap newBitmap = bitmaps[0];
		// newBitmap = createBitmapForFotoMix(bitmaps[0],bitmaps[1],direction);
		for (int i = 1; i < bitmaps.length; i++) {
			newBitmap = createBitmapForFotoMix(newBitmap, bitmaps[i], direction);
		}
		return newBitmap;
	}

	private static Bitmap createBitmapForFotoMix(Bitmap first, Bitmap second,
			int direction) {
		if (first == null) {
			return null;
		}
		if (second == null) {
			return first;
		}
		int fw = first.getWidth();
		int fh = first.getHeight();
		int sw = second.getWidth();
		int sh = second.getHeight();
		Bitmap newBitmap = null;
		if (direction == LEFT) {
			newBitmap = Bitmap.createBitmap(fw + sw, fh > sh ? fh : sh,
					Config.ARGB_8888);
			Canvas canvas = new Canvas(newBitmap);
			canvas.drawBitmap(first, sw, 0, null);
			canvas.drawBitmap(second, 0, 0, null);
		} else if (direction == RIGHT) {
			newBitmap = Bitmap.createBitmap(fw + sw, fh > sh ? fh : sh,
					Config.ARGB_8888);
			Canvas canvas = new Canvas(newBitmap);
			canvas.drawBitmap(first, 0, 0, null);
			canvas.drawBitmap(second, fw, 0, null);
		} else if (direction == TOP) {
			newBitmap = Bitmap.createBitmap(sw > fw ? sw : fw, fh + sh,
					Config.ARGB_8888);
			Canvas canvas = new Canvas(newBitmap);
			canvas.drawBitmap(first, 0, sh, null);
			canvas.drawBitmap(second, 0, 0, null);
		} else if (direction == BOTTOM) {
			newBitmap = Bitmap.createBitmap(sw > fw ? sw : fw, fh + sh,
					Config.ARGB_8888);
			Canvas canvas = new Canvas(newBitmap);
			canvas.drawBitmap(first, 0, 0, null);
			canvas.drawBitmap(second, 0, fh, null);
		}
		return newBitmap;
	}


	public static void saveBefore(String path) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeFile(path, options);
		options.inJustDecodeBounds = false;
		int be = (int) (options.outHeight / (float) 200);
		if (be <= 0)
			be = 1;
		options.inSampleSize = 2;
		bitmap = BitmapFactory.decodeFile(path, options);
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();
		System.out.println(w + " " + h);
		// savePNG_After(bitmap,path);
		saveJPGE(bitmap, path);
	}


	public static Bitmap potoMix(Bitmap bigBitmap, Bitmap smallBitmap, int width, int height, int pixels){

		bigBitmap = bigBitmap.copy(Config.ARGB_8888, true);
		smallBitmap = smallBitmap.copy(Config.ARGB_8888, true);
		Canvas canvas = new Canvas(bigBitmap);
		Paint paint = new Paint();
		if(width != 0 && height != 0){
			smallBitmap = HImage.createBitmapBySize(smallBitmap, width, height);
		}
		if(pixels != 0){
			smallBitmap = HImage.toRoundCorner(smallBitmap, pixels);
		}
		int bW = bigBitmap.getWidth();
		int bH = bigBitmap.getHeight();
		int sW = smallBitmap.getWidth();
		int sH = smallBitmap.getHeight();
		canvas.drawBitmap(smallBitmap, (bW - sW)/2, (bH - sH)/2, paint);
		canvas.save(Canvas.ALL_SAVE_FLAG);
		canvas.restore();
		return bigBitmap;
	}


	public static void savePNG(Bitmap bitmap, String name) {
		File file = new File(name);
		try {
			FileOutputStream out = new FileOutputStream(file);
			if (bitmap.compress(CompressFormat.PNG, 100, out)) {
				out.flush();
				out.close();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	public static void saveJPGE(Bitmap bitmap, String path) {
		File file = new File(path);
		try {
			FileOutputStream out = new FileOutputStream(file);
			if (bitmap.compress(CompressFormat.JPEG, 100, out)) {
				out.flush();
				out.close();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static Bitmap createBitmapBySize(Bitmap bitmap, int width, int height) {
		return Bitmap.createScaledBitmap(bitmap, width, height, true);
	}


//	public static Bitmap drawableToBitmapByBD(Drawable drawable) {
//		BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
//		return bitmapDrawable.getBitmap();
//	}


//	public static Drawable bitmapToDrawableByBD(Bitmap bitmap) {
//		Drawable drawable = new BitmapDrawable(bitmap);
//		return drawable;
//	}


	public static Bitmap bytesToBimap(byte[] b) {
		if (b.length != 0) {
			return BitmapFactory.decodeByteArray(b, 0, b.length);
		} else {
			return null;
		}
	}

	public static byte[] bitmapToBytes(Bitmap bm) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(CompressFormat.PNG, 100, baos);
		return baos.toByteArray();
	}
}
