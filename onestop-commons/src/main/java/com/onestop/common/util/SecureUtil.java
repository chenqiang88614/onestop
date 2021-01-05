
package com.onestop.common.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;

public class SecureUtil {
	/**
	 * md5计算后进行16进制转换得到的字符串
	 */
	public static String md5X16Str(String datas, String encoding) {
		byte[] bytes = md5(datas, encoding);
		StringBuilder md5StrBuff = new StringBuilder();
		String hexString = null;
		for (int i = 0; i < bytes.length; i++) {
			hexString = Integer.toHexString(0xFF & bytes[i]);
			if (hexString.length() == 1) {
				md5StrBuff.append("0").append(hexString);
			} else {
				md5StrBuff.append(hexString);
			}
		}
		return md5StrBuff.toString();
	}

	public static byte[] md5(String datas, String encoding) {
		try {
			return md5(datas.getBytes(encoding));
		} catch (UnsupportedEncodingException e) {
			return null;
		}
	}

	public static byte[] md5(byte[] datas) {
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("MD5");
			md.reset();
			md.update(datas);
			return md.digest();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
