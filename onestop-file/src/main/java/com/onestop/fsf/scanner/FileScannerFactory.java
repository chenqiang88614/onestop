/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package com.onestop.fsf.scanner;

import com.onestop.fsf.model.FsConfig;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * @author fuchl 根据配置文件,生成具体的文件扫描类
 */
public class FileScannerFactory extends FileScannerImpl {
	boolean	subPathFlag	= false;
	private static ConfigFileParser parser;
	/**
	 * @param config
	 */
	public FileScannerFactory(ConfigFileParser config) {
		parser=config;
		this.init();
	}
	private void init(){
		super.setTimeInterval(parser.getTimeInterval());
		int i;
		for (i = 0; i < parser.getFilematcherList().size(); i++) {
			super.addFileMatcher(parser.getFilematcherList().get(i));
		}
		for (i = 0; i < parser.getIgnoreFilematcherList().size(); i++) {
			super.addIgnoreFileMatcher(parser.getIgnoreFilematcherList().get(i));
		}
		for (i = 0; i < parser.getObserverList().size(); i++) {
			super.addObserver(parser.getObserverList().get(i));
		}
		for (i = 0; i < parser.getScanPathList().size(); i++) {
			super.addScanDirectory(parser.getScanPathList().get(i).getSrcPath(), parser.getScanPathList().get(i).getDesPath());
		}
		subPathFlag = parser.getSubPathFlag();
		setIgnoreFilePath(parser.getIgnoreFilePath());
	}
	
	/**
	 * getConfig
	 * @描述: 获取文件扫描的配置信息
	 * @作者: huangr
	 * @创建时间: 2016-1-23下午06:53:49
	 * @return 扫描时间间隔、扫描路径（:符号隔开多个路径）、分别的备份路径(:符号隔开多个路径、忽略的文件存放路径)
	 *  
	 */
	public static FsConfig getConfig(){
		FsConfig fs = new FsConfig();
		if(parser!=null){
			fs.setTimeInterval(parser.getTimeInterval());
			List<PathPair> scanPathList =parser.getScanPathList();
			String scan ="";
			String bak ="";
			for(PathPair pa : scanPathList){
				String scanP=pa.getSrcPath();
				String bakP =pa.getDesPath();
				scan = scan+scanP+";";
				bak = bak +bakP +";";	
			}
			if(scanPathList.size()>0){
				fs.setScanPath(scan.substring(0, scan.length()-1));
				fs.setBackupPath(bak.substring(0,bak.length()-1));
			}
			fs.setIgnoreFilePath(parser.getIgnoreFilePath());
			
		}
		return fs;
		
	}
	/**
	 * setConfig
	 * @描述: 设置扫描配置项（扫描间隔、扫描文件夹列表、备份列表、忽略文件存放路径）扫描文件夹列表
	 * @作者: huangr
	 * @创建时间: 2016-1-23下午07:26:34
	 * @param fs 配置项模型
	 * @return
	 *  扫描配置结果
	 */
	public static boolean setConfig(FsConfig fs){
		
		Integer timeT =fs.getTimeInterval();
		String scP =fs.getScanPath();
		String baP =fs.getBackupPath();
		String igP =fs.getIgnoreFilePath();
		String[] scPList=scP.split(";");
		String[] baPList =baP.split(";");
		List<PathPair> scanList =new ArrayList<PathPair>(); 
		int i =0;
		for(String ll :scPList){
			PathPair pa =new PathPair(ll, baPList[i]);
			scanList.add(pa);
			i++;
		}
		//对文件进行操作
		parser.setTimeInteger(timeT);
		parser.setScanPathList(scanList);
		parser.setIgnoreFilePath(igP);
		boolean flag=parser.updateFile();
		if(flag){
			updateConfig(timeT,scanList,igP);// 同步更新配置
		}
		return flag;
		
	}
	
	@Override
	protected String setBackupSubPath() {
		String subPath;
		// 建立以时间命名的子文件夹
		if (subPathFlag)
		{
			SimpleDateFormat exFormatter = new SimpleDateFormat("yyyyMMddHHmmssSSS"); // yyyyMMddHHmmssSSS
			subPath = exFormatter.format(Calendar.getInstance().getTime());
		} else {
			subPath = "";
		}
		return subPath;
	}
}
