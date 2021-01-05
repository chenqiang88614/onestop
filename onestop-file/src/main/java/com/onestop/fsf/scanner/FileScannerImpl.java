/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package com.onestop.fsf.scanner;

import com.onestop.fsf.spi.FileUtils;
import com.onestop.fsf.api.IFileMatcher;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * @author fuchl
 */
@Slf4j
public abstract class FileScannerImpl extends IFileScanner {
	private static int					timeInterval;
	private static List<PathPair>		pathList;
	private static List<IFileMatcher>	fileMatcherList;
	private List<IFileMatcher>	ignoreFileMatcherList;
	private static String				ignoreFilePath;
	private boolean				scan;
	private Thread				scanThread; 
	private static String SPLIT = File.separator;

	protected abstract String setBackupSubPath();


	public FileScannerImpl() {
		fileMatcherList = new ArrayList<IFileMatcher>();
		ignoreFileMatcherList = new ArrayList<IFileMatcher>();
		pathList = new ArrayList<PathPair>();
		scan = false;
		timeInterval = 5;// 给个默认值
	}
	public static boolean updateConfig(Integer time,List<PathPair> pathLis,String ignorePath){
		timeInterval=time;
		pathList=pathLis;
		ignoreFilePath=ignorePath;	
		return false;
	} 
	
	@Override
    public  void setTimeInterval(int timeInterva) {
		timeInterval = timeInterva;
	}
	
	public static void setFileMatcherList(List<IFileMatcher> fileMatchers) {
		fileMatcherList = fileMatchers;
	}

	public void setIgnoreFilePath(String ignoreFilePat) {
		ignoreFilePath = ignoreFilePat;
	}
	
	@Override
	protected void addFileMatcher(IFileMatcher fm) {
		if (fm == null) {
			return;
		}
		fileMatcherList.add(fm);
	}
	@Override
	public void resetScanDirectory(){
		pathList=new ArrayList<PathPair>();
	}
	protected void addIgnoreFileMatcher(IFileMatcher fm) {
		if (fm == null) {
			return;
		}
		ignoreFileMatcherList.add(fm);
	}
	
	/*
	 * 设置扫描的源路径，和处理后的目标路径,处理后的路径实际上是把传来的文件备份下
	 */
	@Override
	public void addScanDirectory(String scanPath, String destPath) {
		//说明是从当前路径开始算的路径，当前路径指的是项目根路径
		if(scanPath.startsWith("./")){
			scanPath = scanPath.substring(2) ;
		}
		if(destPath.startsWith("./")){
			destPath = destPath.substring(2) ;
		}
		
		if (!scanPath.endsWith(SPLIT)) {
			scanPath +=  SPLIT;
		}
		if (!destPath.endsWith(SPLIT)) {
			destPath += SPLIT;
		}
		// 如果scanPath 和 destPath不存在,要创建这两个目录
		File scanPathFile = new File(scanPath);
		File desPathFile = new File(destPath);
		if (scanPathFile.exists() == false) {
			scanPathFile.mkdirs();
		}
		if (desPathFile.exists() == false) {
			desPathFile.mkdirs();
		}
		PathPair pair = new PathPair(scanPath, destPath);
		pathList.add(pair);
	}
	
	@Override
	public void startScan() {
		scanThread = new Thread(new ScanThread());
		scan = true;
		scanThread.start();
	}
	
	@Override
	public void stopScan() {

		scan = false;
	}
	
	/**
	 * @param scanPath
	 *            扫描路径
	 * @return 返回scanPath路径下的所有可读可写可操作的文件
	 */
	private List<File> listScanPathAllFile(String scanPath) {
		List<File> result = new ArrayList<File>();
		File f = new File(scanPath);
		File[] fl = f.listFiles();
		if(fl==null){
			return result;
		}
		for (int i = 0; i < fl.length; i++) {
			String fileName = fl[i].getName();
			if (!fileName.startsWith(".")) {// is not hide file
				//xiayj20180519修改去掉只读
				if (fl[i].isFile() && fl[i].canWrite() && fl[i].canRead()) {
//				if (fl[i].isFile()) {
					result.add(fl[i]);
				}
			}
		}
		return result;
	}
	
	private class ScanThread implements Runnable {
		@Override
		public void run() {
			System.out.println("start scan the path.......");
			for(int a=0;a<pathList.size();a++){
				System.out.println("path"+(a+1)+":>>>>> srcPath: "+pathList.get(a).getSrcPath()+", desPath:"+pathList.get(a).getDesPath());
			}
			System.out.println("timeInterval:"+timeInterval+"\n");
			while (scan) {
				// 对每一个路径扫描
				for (int i = 0; i < pathList.size(); i++) {
					String tscanPath = pathList.get(i).getSrcPath();
					String tdestPath = pathList.get(i).getDesPath();
					// 扫描文件，并将文件移动到指定的目标路径
					List<File> fileList = listScanPathAllFile(tscanPath);
					if (fileList != null) {
						Collections.sort(fileList,new TimeComparator());
						// 对目标路径下的每一个文件分别进行处理
						for (File tfile : fileList) {
							boolean isContinue = false;
							for (IFileMatcher tfm : ignoreFileMatcherList) {
								if (tfm.matchFile(tfile.getPath() + SPLIT, tfile.getName())) {
									isContinue = true;
									break;
								}
							}
							if (isContinue) {
								continue;
							}
							// 把文件移动到备份文件夹，该文件夹下建立以日期命名的子文件夹
							String desSubPath = setBackupSubPath();
							String desFileName = tdestPath + desSubPath + SPLIT + tfile.getName();
							File destfolder = new File(tdestPath + desSubPath);
							// 重试三次
							if (!destfolder.exists()) {
								destfolder.mkdir();
							}
							if (!destfolder.exists()) {
								destfolder.mkdir();
							}
							if (!destfolder.exists()) {
								destfolder.mkdir();
							}
							if (!destfolder.exists()) {
								System.out.println("Folder " + destfolder.getAbsolutePath() + " create failed");
							}
							File df = new File(desFileName);
							String srcFileName = tfile.getAbsolutePath();
							try {
								FileUtils.move(srcFileName, destfolder.getAbsolutePath());
							} catch (Exception e) {
								System.out.println("File " + srcFileName + " delete failed");
								continue;
							}
							//tfile.renameTo(df);
							if (!df.exists()) {
								System.out.println("File " + df.getAbsolutePath() + " rename failed");
							}
							// 用文件匹配器匹配文件，若匹配成功，对文件进行处理
							boolean matched = false;
							for (IFileMatcher tfm : fileMatcherList) {
								if (tfm.matchFile(tfile.getPath() + SPLIT, tfile.getName())) {
									matched = true;
									NotifyObserver(new File(desFileName), tfm.getName());
								}
							}
							// 如果文件匹配不成功，将文件移动到专门的文件夹
							if (!matched) {
								if (ignoreFilePath != null && ignoreFilePath.length() > 0) {
									File ignoreFolder = new File(ignoreFilePath);
									if (!ignoreFolder.exists()) {
										ignoreFolder.mkdir();
									}
									try {
										FileUtils.move(df.getAbsolutePath(), ignoreFilePath);
									} catch (Exception e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									log.warn("file:"+df.getName()+"is not matched any,it has been cut to "+ignoreFilePath);
								}
							}
						}
					}
				}
				try {
					Thread.sleep(1000 * timeInterval);
				} catch (InterruptedException ex) {
					log.error("terminal has throw a exception, scan Threed  will not running,exit!");
				}
			}
		}
	}
}