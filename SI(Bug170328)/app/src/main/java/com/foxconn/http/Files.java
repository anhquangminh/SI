package com.foxconn.http;

import android.content.Context;


import org.apache.http.util.ByteArrayBuffer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class Files {
	private static String sd_card = "/sdcard/";
	private static String path = "product/";

	/**

	 * 
	 * @param context
	 */
	public static void mkdir(Context context) {
		File file;
		file = new File(sd_card + path);
		if (!file.exists()) {
			file.mkdir();
		}
	}

	/**

	 * 
	 * @param URL
	 * @param data
	 * @throws IOException
	 */
	public static void saveImage(String URL, byte[] data) throws IOException {
		String name = MyHash.mixHashStr(URL);
		saveData(sd_card + path, name, data);
	}

	/**

	 * 
	 * @param filename
	 * @return
	 * @throws IOException
	 */
	public static byte[] readImage(String filename) throws IOException {
		String name = MyHash.mixHashStr(filename);
		byte[] tmp = readData(sd_card + path, name);
		return tmp;
	}

	/**
	 *
	 * @param path
	 * @param name
	 * @return
	 * @throws IOException
	 */
	private static byte[] readData(String path, String name) throws IOException {
		// String name = MyHash.mixHashStr(url);
		ByteArrayBuffer buffer = null;
		String paths = path + name;
		File file = new File(paths);
		if (!file.exists()) {
			return null;
		}
		InputStream inputstream = new FileInputStream(file);
		buffer = new ByteArrayBuffer(1024);
		byte[] tmp = new byte[1024];
		int len;
		while (((len = inputstream.read(tmp)) != -1)) {
			buffer.append(tmp, 0, len);
		}
		inputstream.close();
		return buffer.toByteArray();
	}

	/**
	 *
	 * @param path
	 * @param fileName
	 * @param data
	 * @throws IOException
	 */
	private static void saveData(String path, String fileName, byte[] data)
			throws IOException {
		// String name = MyHash.mixHashStr(AdName);
		File file = new File(path + fileName);
		if (!file.exists()) {
			file.createNewFile();
		}
		FileOutputStream outStream = new FileOutputStream(file);
		outStream.write(data);
		outStream.close();
	}

	/**
	 *
	 * @param url
	 * @return
	 */
	public static boolean compare(String url) {
		String name = MyHash.mixHashStr(url);
		String paths = sd_card + path + name;
		File file = new File(paths);
		if (!file.exists()) {
			return false;
		}
		return true;
	}
}
