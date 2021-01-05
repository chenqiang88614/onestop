package com.onestop.fsf.matcher;

import com.onestop.fsf.api.AbstractFileMatcher;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.regex.Pattern;


/**
 * @类名: FileMatcherByPattern
 * @描述: 通过正则表达是来匹配的工具
 * @版本: 
 * @创建日期: 2015-10-13下午03:50:37
 * @作者: huangr
 * @JDK: 1.6
 */
@Slf4j
public class FileMatcherByPattern extends AbstractFileMatcher {
	
	private String	regex;

	/**
	 * @param s
	 *            匹配的正则表达式，参考java.util.regex.Pattern
	 */
	@Override
	public void setParmString(String s) {
		this.regex = s;
	}
	
	@Override
	public boolean matchFile(String filePath, String fileName) {
		if (regex == null) {
			log.error("Please use setParmString()to set regex!");
			return false;
		}
		File file = new File(filePath, fileName);
		// System.out.println(file.getName());
		// Pattern p = Pattern.compile(regex);
        // 文件可控,且匹配成功
        return Pattern.matches(regex, file.getName());
	}
}
