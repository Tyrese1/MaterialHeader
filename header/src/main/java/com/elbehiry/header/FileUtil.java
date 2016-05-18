package com.elbehiry.header;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


public class FileUtil {

	public static void createDipPath(String file) {
		String parentFile = file.substring(0, file.lastIndexOf("/"));
		File file1 = new File(file);
		File parent = new File(parentFile);
		if (!file1.exists()) {
			parent.mkdirs();
			try {
				file1.createNewFile();
			} catch (IOException e) {
			}
		}
	}

		public static void copyFile(String sourcePath, String toPath) {
			File sourceFile = new File(sourcePath);
			File targetFile = new File(toPath);
			createDipPath(toPath);
			try {
				BufferedInputStream inBuff = null;
				BufferedOutputStream outBuff = null;
				try {
					inBuff = new BufferedInputStream(
							new FileInputStream(sourceFile));

					outBuff = new BufferedOutputStream(new FileOutputStream(
							targetFile));

					byte[] b = new byte[1024 * 5];
					int len;
					while ((len = inBuff.read(b)) != -1) {
						outBuff.write(b, 0, len);
					}
					outBuff.flush();
				} finally {
					if (inBuff != null)
						inBuff.close();
					if (outBuff != null)
						outBuff.close();
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}



}
