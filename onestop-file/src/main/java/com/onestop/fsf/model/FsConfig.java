package com.onestop.fsf.model;


/**
 * @类名: FsConfig
 * @描述: 扫描配置项，对于扫描配置进行处理
 * @版本: 
 * @创建日期: 2016-1-22下午04:59:58
 * @作者: huangr
 * @JDK: 1.6
/*
* 类的横向关系：TODO 说明与其它类的关联、调用或依赖等关系。
*/
public class FsConfig{ 
	/**
	 * String scanPath  扫描路径
	 */
	private String scanPath;
	/**
	 * String backupPath 接收文件备份路径
	 */
	private String backupPath;
	/**
	 * String ignoreFilePath 不合法的忽略的文件暂存路径
	 */
	private String ignoreFilePath;
	/**
	 * Integer timeInterval  扫描时间间隔，单位为秒(s)
	 */
	private Integer timeInterval;
	public String getScanPath() {
		return scanPath;
	}
	public void setScanPath(String scanPath) {
		this.scanPath = scanPath;
	}
	public String getBackupPath() {
		return backupPath;
	}
	public void setBackupPath(String backupPath) {
		this.backupPath = backupPath;
	}
	public String getIgnoreFilePath() {
		return ignoreFilePath;
	}
	public void setIgnoreFilePath(String ignoreFilePath) {
		this.ignoreFilePath = ignoreFilePath;
	}
	public Integer getTimeInterval() {
		return timeInterval;
	}
	public void setTimeInterval(Integer timeInterval) {
		this.timeInterval = timeInterval;
	}
	@Override
	public String toString() {

		return "fsScanPath:"+this.scanPath+",timeInterval:"+this.timeInterval;
	}
}
