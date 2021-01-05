package com.onestop.ftpclient.spi;

import com.onestop.ftpclient.util.FtpParam;

import java.io.File;

/**
 * Title:FTP传输操作
 * Description：实现文件和文件夹的上传下载功能
 * @author:zhangli
 * version:1.0
 *
 */
public interface IFtpOperation {
		
	/**
	 * 上传文件到指定路径
	 * @param remotePath：FTP路径
	 * @param localFile：本地待传输文件(包括路径)
	 * @return：成功返回null
	 */
    String uploadFile(String remotePath, String localFile);
	
	/**
	 * 上传文件夹到指定路径
	 * @param remotePath：FTP路径
	 * @param localpath：本地待传输文件夹及其所在路径
	 * @param localRoot：本地待传输文件夹及其所在路径
	 * @return：成功返回null
	 */
    String uploadFolder(String remotePath, String localpath, String localRoot);
	
	/**
	 * 从FTP下载指定文件
	 * @param remoteFile：远程文件（含路径）
	 * @param localpath:本地路径
	 * @return：成功返回null
	 */
    String downloadFile(String remoteFile, String localpath);
	
	/**
	 * 从FTP下载指定文件夹，按照远程目录一一对应建立文件夹
	 * @param remoteFolder：远程文件夹
	 * @param localpath：本地路径
	 * @return：成功返回null
	 */
    String downloadFolder(String remoteFolder, String localpath);
	
	/**
	 * 将远程路径中的所有文件包括文件夹下载到本地路径中，不再按照远程目录重新建立文件夹
	 * @param remoteFolder：远程文件夹
	 * @param localPath：本地路径
	 * @return：成功返回null
	 */
    String downloadFilesToOneFolder(String remoteFolder, String localPath);
	
	/**
	 * 上传文件，参数信息在配置文件中
	 * @param configFile：配置文件
	 * @return：成功返回null
	 */
    String uploadFile(String configFile);
	
	/**
	 * 上传文件夹，参数信息在配置文件中
	 * @param configFile：配置文件
	 * @return：成功返回null
	 */
    String uploadFolder(String configFile);
	
	/**
	 * 下载文件，参数信息在配置文件中
	 * @param configFile：配置文件
	 * @return：成功返回null
	 */
    String downloadFile(String configFile);
	
	/**
	 * 下载文件夹，参数信息在配置文件中
	 * @param configFile：配置文件
	 * @return：成功返回null
	 */
    String downloadFolder(String configFile);
	
	/**
	 * 断开与FTp的连接
	 */
    void closeServer();
	
	/**
	 * 重命名文件，要求ftp已经连接
	 * @param srcFileName 源文件名
	 * @param finalFileName 目标文件名
	 * @return boolean
	 */
    boolean renameFile(String srcFileName, String finalFileName);

	/**
	 * openServer
	 * @描述: 开启ftp服务
	 * @作者: huangr
	 * @创建时间: 2016-1-4上午10:05:30
	 * @return
	 *  开启服务的返回值，表示状态
	 */
    String openServer();
	
	/**
	 * checkDirExist
	 * @描述: 判断远程是否存在该目录
	 * @作者: huangr
	 * @创建时间: 2016-1-25上午10:28:22
	 * @param path
	 * @return
	 *  存在返回true，不存在返回false
	 */
    boolean checkDirExist(String path);

	void setParam(FtpParam param);
	
	FtpParam getParam();

	String uploadFile(File file);
}
