/**
 * 
 */
package com.onestop.fsf.fileobserver;

import com.onestop.fsf.api.AbstractFileObserver;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;


/**
 * 文件复制处理类观察者
 * @author fuchl
 */
public class FileObserverCopy extends AbstractFileObserver {
	
	/**
	 * String desFolderPath
	 * 目标文件路径
	 */
	private String	desFolderPath	= null;
	
	/* (non-Javadoc)
	 * @see com.phy.oms.fsf.api.IFileObserver#setParmString(java.lang.String)
	 */
	@Override
	public void setParmString(String s) {
		this.desFolderPath = s;
	}
	
	/**
	 * @return 目标路径建立子目录
	 */
	public String getDesSubFile() {
		return desFolderPath;
	}

	/* (non-Javadoc)
	 * @see com.phy.oms.fsf.api.IFileObserver#update(java.io.File)
	 */
	@Override
	public void update(File scrFile) {
		System.out.println("FileObserverCopy updates,Des folder is" + desFolderPath);
		if (!desFolderPath.endsWith("/")) {
			desFolderPath = desFolderPath + "/";
		}
		File desFolder = new File(desFolderPath);
		if (!desFolder.exists()) {
			desFolder.mkdirs();
		}
		String desFileName = desFolderPath + scrFile.getName();
		try {
			if (scrFile.canWrite() == true && scrFile.canExecute() == true && scrFile.canWrite() == true) {
				copyFile(scrFile, new File(desFileName));
			} else {
				System.out.println("File can't write or execute or write");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		// scrFile.renameTo(new File(desFolderPath + scrFile.getName()));
	}

	@Override
	public void doHandler(File srcFile) {

	}

	/**
	 * @param src
	 *            源文件
	 * @param des
	 *            目标文件
	 * @throws IOException
	 *             文件不存在
	 */
	public void copyFile(File src, File des) throws IOException {
		if (!src.exists()) {
			return;
		}
		if (!des.exists()) {
			des.createNewFile();
		}
		FileChannel sf = new FileInputStream(src).getChannel();
		FileChannel df = new FileOutputStream(des).getChannel();
		long totalSize = src.length();
		long leftSize = totalSize;
		long blockSize = 200L * 1024L * 1024L;
		long position = 0;
		long readBytes = 0;
		while (leftSize > 0) {
			readBytes = sf.transferTo(position, blockSize, df);
			leftSize = leftSize - readBytes;
			position = position + readBytes;
			sf.position(position);
		}
		sf.close();
		df.close();
		sf = null;
		df = null;
	}
}
