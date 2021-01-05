package com.onestop.fsf.matcher;

import com.onestop.fsf.api.AbstractFileMatcher;
import lombok.extern.slf4j.Slf4j;

import java.io.File;


/**
 * @类名: FileMatcherByPrefix
 * @描述: 通过文件前缀来匹配文件
 * @版本: 
 * @创建日期: 2015-10-13下午07:43:58
 * @作者: huangr
 * @JDK: 1.6
 */
@Slf4j
public class FileMatcherByPrefix extends AbstractFileMatcher {
	
	/**
	 * String prefix
	 *   文件前缀
	 */
	String			prefix;
	
	/**
	 * 
	 */
	public FileMatcherByPrefix() {
	}
	
	/* (non-Javadoc)
	 * @see com.phy.oms.fsf.api.IFileMatcher#setParmString(java.lang.String)
	 */
	@Override
	public void setParmString(String s) {
		this.prefix = s;
	}
	
	/*
	 * (non-Javadoc)
	 * @see fsf.api.IFileMatcher#matchFile(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean matchFile(String filePath, String fileName) {
		// System.out.println("File mather1:" + filePath + fileName);
		if (prefix == null) {
			log.error("Please use setParmString()to set prefix!");
			return false;
		}
		File file = new File(filePath, fileName);
        // 文件可控
        return file.getName().startsWith(prefix);
	}
}
