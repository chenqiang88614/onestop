package com.onestop.fsf.fileobserver;

import com.onestop.fsf.api.AbstractFileObserver;

import java.io.File;


/**
 * 移动文件处理类观察者
 * @author fuchl
 * 
 */
public class FileObserverMove extends AbstractFileObserver {
	/**
	 * String desFolderPath
	 * 目标文件夹
	 */
	private String	desFolderPath	= null;
	
	public FileObserverMove() {
	}
	
	/**
	 * @return 目标路径建立子目录
	 */
	public String getDesSubFile() {
		return desFolderPath;
	}
	
	/**
	 * @param s
	 *            移动的目标文件夹路径
	 */
	@Override
	public void setParmString(String s) {
		this.desFolderPath = s;
	}
	
	@Override
	public void update(File scrFile) {
		System.out.println("FileObserverMove updates, Des folder is " + desFolderPath);
		if (!desFolderPath.endsWith("/")) {
			desFolderPath = desFolderPath + "/";
		}
		File desFolder = new File(desFolderPath);
		if (!desFolder.exists()) {
			desFolder.mkdirs();
		}
		if (scrFile.canWrite() == true && scrFile.canExecute() == true && scrFile.canWrite() == true) {
			scrFile.renameTo(new File(desFolderPath + scrFile.getName()));
		} else {
			System.out.println("File can't write or execute or write");
		}
	}

	@Override
	public void doHandler(File srcFile) {

	}
}
