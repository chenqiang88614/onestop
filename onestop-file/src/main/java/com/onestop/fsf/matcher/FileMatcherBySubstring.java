package com.onestop.fsf.matcher;

import com.onestop.fsf.api.AbstractFileMatcher;
import lombok.extern.slf4j.Slf4j;

import java.io.File;


/**
 * @类名: FileMatcherBySubstring
 * @描述: 通过文件名包含的字符串来匹配文件
 * @版本: 
 * @创建日期: 2015-10-13下午07:46:02
 * @作者: huangr
 * @JDK: 1.6
 */
@Slf4j
public class FileMatcherBySubstring extends AbstractFileMatcher {
	/**
	 * String subString
	 *    包含的文件名字符串
	 */
	private String	subString;
	
	/**
	 * 
	 */
	public FileMatcherBySubstring() {
	}
	
	@Override
	public void setParmString(String s) {
		this.subString = s;
	}
	
	/* (non-Javadoc)
	 * @see com.phy.oms.fsf.api.IFileMatcher#matchFile(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean matchFile(String filePath, String fileName) {
		if (subString == null) {
			log.error("Please use setParmString()to set subString!");
			return false;
		}
		File file = new File(filePath, fileName);
        // 文件可控
        return file.getName().contains(subString);
	}
}
