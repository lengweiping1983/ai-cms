package com.ai.common.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Hex;

public class MD5Util {

	public static String getMd5ByFile(File file)
			throws NoSuchAlgorithmException, IOException {
		FileInputStream in = new FileInputStream(file);
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			byte[] buffer = new byte[8192];
			int length;
			while ((length = in.read(buffer)) != -1) {
				md5.update(buffer, 0, length);
			}
			return new String(Hex.encodeHex(md5.digest()));
		} finally {
			if (null != in) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static void main(String[] args) throws IOException,
			NoSuchAlgorithmException {
		String path = "/usr/local/nginx/html/video/transcode/8/201608/2016XJXW0824D1LGLND11BLYLYEJJ/8m_CBR_Jiangsu/2016XJXW0824D1LGLND11BLYLYEJJ_4_276249_1_8m_CBR_Jiangsu.TS";
		String v = getMd5ByFile(new File(path));
		System.out.println("MD5:" + v.toUpperCase());
	}

}
