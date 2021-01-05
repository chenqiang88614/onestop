/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package com.onestop.fsf.matcher;

import com.onestop.fsf.api.AbstractFileMatcher;
import lombok.extern.slf4j.Slf4j;

import java.io.File;


/**
 * @类名: FileMatcherBySuffix
 * @描述: 通过文件名后缀来匹配文件
 * @版本: 
 * @创建日期: 2015-10-13下午07:48:00
 * @作者: huangr
 * @JDK: 1.6
 */
@Slf4j
public class FileMatcherBySuffix extends AbstractFileMatcher {
	/**
	 * String suffix
	 * 文件名后缀
	 */
	private String			suffix;
	
	/**
	 * 
	 */
	public FileMatcherBySuffix() {
	}
	
	@Override
	public void setParmString(String s) {
		this.suffix = s;
	}
	
	/*
	 * (non-Javadoc)
	 * @see fsf.api.IFileMatcher#matchFile(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean matchFile(String filePath, String fileName) {
		// System.out.println("File mather1:" + filePath + fileName);
		if (suffix == null) {
			log.error("Please use setParmString()to set suffix!");
			return false;
		}
		File file = new File(filePath, fileName);
        // 文件可控
        return file.getName().endsWith(suffix);
	}
}
