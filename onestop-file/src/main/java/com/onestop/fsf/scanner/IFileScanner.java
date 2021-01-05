package com.onestop.fsf.scanner;

import com.onestop.fsf.api.IFileObserver;
import com.onestop.fsf.api.IFileMatcher;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


/**
 * @author fuchl
 */
public abstract class IFileScanner {
	private List<IFileObserver>	observers	= new ArrayList<IFileObserver>();
	
	/**
     * 
     */
	abstract public void startScan();
	
	/**
     * 
     */
	abstract public void stopScan();
	
	/**
	 * @param scanPath
	 *            扫描的源路径
	 * @param destPath
	 *            扫描后的目的地路径
	 */
	abstract public void addScanDirectory(String scanPath, String destPath);
	
	/**
	 * @param timeInterval
	 *            每次扫描的时间间隔
	 */
	abstract public  void setTimeInterval(int timeInterval);
	
	abstract public void resetScanDirectory();
	/**
	 * @param fm
	 *            扫描匹配器
	 */
	abstract protected void addFileMatcher(IFileMatcher fm);
	
	protected void addObserver(IFileObserver observer) {
		observers.add(observer);
	}
	
	/**
	 * @param observer
	 *            文件扫描的观察者
	 */
	protected void removeObserver(IFileObserver observer) {
		observers.remove(observer);
	}
	
	/**
	 * @param srcFile
	 *            扫描到的符合匹配原则的文件
	 */
	protected void NotifyObserver(File srcFile, String name) {
		int i;
		for (i = 0; i < observers.size(); i++) {
			if (observers.get(i).isMatch(name)) {
				IFileObserver observer = observers.get(i);
				Thread t = new NotifyThread(observer, srcFile);
				t.start();
				try {
					t.join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
