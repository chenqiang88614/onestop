package com.onestop.ftpclient.impl;

// import java.io.*;

import com.onestop.ftpclient.spi.IFtpOperation;
import com.onestop.ftpclient.util.FtpParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

/**
 * FTP传输服务
 * 
 * @author zhangli 说明：包括文件、文件夹的上传和下载。以及实现由配置文件实现文件和文件件的上传下载功能
 */
@Slf4j
public class FtpOperationImpl implements IFtpOperation {
	public static final String ERROR = "ERROR";
	public static final String ERROR_CONNECT_FTP = "ERROR_CONNECT_FTP";
	public static final String ERROR_FILE_NOTFOUND = "ERROR_FILE_NOTFOUND";
	public static final String ERROR_PATH_NOTFOUND = "ERROR_PATH_NOTFOUND";
	public static final String ERROR_CONFIG_NOTFOUND = "ERROR_CONFIG_NOTFOUND";
	public static final int RETRYTIMES = 3;

	private FTPClient ftpClient = new FTPClient();
	private FtpParam param;

	private String configRemotePath;
	private String configLocalPath;
	private String configLocalRoot;

	public FtpOperationImpl(){
		
	}
	
	public FtpOperationImpl(FtpParam param){
		this.param = param;
	}
	
	@Override
	public void setParam(FtpParam param) {
		this.param = param;
	}

	@Override
	public FtpParam getParam() {
		return this.param;
	}

	@Override
	synchronized
	public String uploadFile(String remotePath, String localFile) {
		String connect = issureConnect();
		if (connect != null) {
            return connect;
        }
		try {
			// 设置以二进制流的方式传输
			ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
			ftpClient.enterLocalPassiveMode();
			ftpClient.setFileTransferMode(FTP.STREAM_TRANSFER_MODE);
			ftpClient.setControlEncoding("UTF-8");
			// 获取文件上传的路径和名称
			InputStream input = new FileInputStream(localFile);
			File filePath = new File(localFile);
			if(!"".equals(remotePath)&&!remotePath.endsWith("/")){
				remotePath=remotePath+"/";
			}
			if(remotePath.startsWith("/")){
				remotePath=remotePath.replaceFirst("/", "");
			}
				
			String filename = remotePath + filePath.getName();
			if (!filePath.exists()) {
				System.out.println("file[  " + localFile + " ]not exist!");
				log.warn("upload failed!,file[ " + localFile + " ]not exist!");
				return "ERROR_FIND_FILE";
			}
			boolean flag = ftpClient.storeFile(new String(filename.getBytes("GBK"), StandardCharsets.ISO_8859_1),input);
			remotePath=param.getServerName()+"("+param.getIp()+":"+param.getPort()+" "+remotePath+")";
			if (flag) {
				System.out.println("upload file[  " + filePath.getName() + " ] to  " + remotePath + " success!");
				//log.info("upload file[  " + filePath.getName() + "] to " + remotePath + " success!");
			} else {
				System.out.println("upload file[" + filePath.getName() + " ] to " + remotePath + " failed!,remote path not found!");
				log.warn("upload file[" + filePath.getName() + "] to " + remotePath + " failed!,remote path not found!");
				input.close();
				return ERROR;
			}
			input.close();
		} catch (IOException e) {
			System.out.println(e.getMessage());
			log.error("upload file[" + localFile + "] failed!,reason:" + e.getMessage());
			return ERROR;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			log.error("upload file[" + localFile + "] failed!,reason:" + e.getMessage());
			return ERROR;
		}
		return null;
	}

	/**
	 * 函数名称：uploadFolder 函数功能：FTP上传文件夹
	 */
	@Override
	public String uploadFolder(String remotePath, String localPath,
			String localRoot) {
		// 如果不提供上传目录或为空，则作为不用上传处理
		if (localPath == null || "".equals(localPath)) {
			System.err.println("上传目录为空!");
			return ERROR;
		}
		String connect = issureConnect();
		if (connect != null) {
            return connect;
        }

		// 判断本地上传目录是否存在
		if (!new File(localPath).exists()) {
			System.out.println("找不到指定路径" + localPath);
			return ERROR_PATH_NOTFOUND;
		}
		// 在此目录中找文件
		File upFiles = new File(localPath);
		// 待上传的文件列表
		File[] file = upFiles.listFiles();
		if (file != null) {
			for (int i = 0; i < (file.length); i++) {
				File inFile = file[i].getAbsoluteFile();
				// 如果是目录，再递归迭代
				if (inFile.isDirectory()) {
					File newFileFolder = inFile.getAbsoluteFile();
					localPath = newFileFolder.toString();
					File rootfilePath = new File(localRoot);
					String furi1 = newFileFolder.getParentFile()
							.getAbsoluteFile().toURI().toString();
					String furi2 = rootfilePath.getParentFile()
							.getAbsoluteFile().toURI().toString();
					// 获取本文件上传至FTP时应在的路径
					String dir = remotePath + furi1.substring(furi2.length());
					// 在服务器上建立文件夹
					try {
						if (!ftpClient.changeWorkingDirectory(new String(dir
								.getBytes("GBK"), StandardCharsets.ISO_8859_1))) {
							// 如果不能进入dir下，说明此目录不存在！，创建目录
							if (!ftpClient.makeDirectory(new String(dir
									.getBytes("GBK"), StandardCharsets.ISO_8859_1))) {
								System.out.println("create files directory["
										+ dir + "]failed");
							}
						}
					} catch (IOException e) {
						System.out.println(e.getMessage());
						return "ERROR";
					}
					uploadFolder(remotePath, localPath, localRoot);
				} else {// 如果是文件，则直接上传
					uploadEachFile(remotePath, inFile.getPath(), localRoot);
				}
			}
		}
		return null;
	}
	
	@Override
	public String uploadFile(File file){
		return uploadFile(param.getFolder(),file.getAbsolutePath());
	}

	/**
	 * 函数名称：downloadFile 函数功能：FTP下载文件
	 */
	@Override
	public String downloadFile(String remoteFile, String localPath) {
		String connect = issureConnect();
		if (connect != null) {
            return connect;
        }
		try {
			// 转到指定下载目录
			File rf = new File(remoteFile);
			String filetoLoad = rf.getName();
			// // 列出该目录下的所有文件
			// FTPFile[] fs = ftpClient.listFiles();
			File localFile = new File(localPath + "/" + filetoLoad);
			// 输出流
			OutputStream localFileStream = new FileOutputStream(localFile);
			// 下载文件
			if (ftpClient.retrieveFile(remoteFile, localFileStream)) {
				System.out.println("download file[" + filetoLoad + "]success!");
			} else {
				System.out.println("download file[" + filetoLoad + "]failed!");
			}
			localFileStream.close();
		} catch (IOException e) {
			System.out.println(e.getMessage());
			return ERROR;
		}
		return null;
	}

	/**
	 * 函数名称：downloadFolder 函数功能：FTP下载文件夹,带文件夹
	 */
	@Override
	public String downloadFolder(String remoteFolder, String localPath) {
		if (ftpClient != null && !ftpClient.isConnected()) {
			connectFTPServer();
		}
		try {
			FTPFile[] files = null;
			boolean changedir = ftpClient.changeWorkingDirectory(remoteFolder);
			if (changedir) {
				// 列出文件夹下所有文件
				files = ftpClient.listFiles();
				for (int i = 0; i < files.length; i++) {
					// 下载文件
					downloadFile(files[i], localPath, remoteFolder);
				}
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return "ERROR";
		}
		return null;
	}

	/**
	 * 函数名称：downloadFilesToOneFolder 函数功能：FTP下载文件夹
	 */
	@Override
	public String downloadFilesToOneFolder(String remotePath, String localPath) {
		if (ftpClient != null && !ftpClient.isConnected()) {
			connectFTPServer();
		}
		try {
			// 转到指定下载目录
			ftpClient.changeWorkingDirectory(remotePath);
			// 列出该目录下的所有文件
			FTPFile[] fs = ftpClient.listFiles();
			// 遍历所有文件，找到指定文件
			for (FTPFile ff : fs) {
				// if(ff.getName().equalsIgnoreCase(File)){
				// 根据绝对路径初始化文件
				File localFile = new File(localPath + "/" + ff.getName());
				// 输出流
				OutputStream localFileStream = new FileOutputStream(localFile);
				// 下载文件
				ftpClient.retrieveFile(ff.getName(), localFileStream);
				localFileStream.close();
				System.out.println("文件【" + ff.getName() + "】下载成功！");
				// }
			}
		} catch (IOException e) {
			System.out.println(e.getMessage());
			return "ERROR";
		}
		return null;
	}

	/**
	 * 利用配置文件上传文件
	 */
	@Override
	public String uploadFile(String uploadFile) {
		setArg(uploadFile);
		uploadFile(configRemotePath, configLocalPath);
		return null;
	}

	/**
	 * 利用配置文件上传文件夹
	 */
	@Override
	public String uploadFolder(String configFile) {
		setArg(configFile);
		uploadFolder(configRemotePath, configLocalPath, configLocalRoot);
		return null;
	}

	/**
	 * 利用配置文件下载文件
	 */
	@Override
	public String downloadFile(String configFile) {
		setArg(configFile);
		downloadFile(configRemotePath, configLocalPath);
		return null;
	}

	/**
	 * 利用配置文件下载文件夹
	 */
	@Override
	public String downloadFolder(String configFile) {
		setArg(configFile);
		downloadFolder(configRemotePath, configLocalPath);
		return null;
	}

	@Override
	public boolean renameFile(String srcFileName, String finalFileName) {
		if (issureConnect() != null) {
            return false;
        }
		try {
			return ftpClient.rename(srcFileName, finalFileName);
		} catch (IOException e) {
			return false;
		}
	}

	@Override
	public String openServer() {
		return issureConnect();
	}

	/**
	 * closeServer 断开与ftp服务器的链接
	 */
	@Override
	public void closeServer() {
		try {
			if (ftpClient != null) {
				ftpClient.disconnect();
			}
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

	/**
	 * 函数名称：uploaEachFile 函数功能：FTP上传文件,由uploadFolder调用
	 */
	private String uploadEachFile(String remotePath, String localFile,
			String localRoot) {
		String connect = issureConnect();
		if (connect != null) {
            return connect;
        }

		try {
			// 待传输文件类型
			ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
			ftpClient.enterLocalPassiveMode();
			ftpClient.setFileTransferMode(FTP.STREAM_TRANSFER_MODE);
			InputStream input = new FileInputStream(localFile);
			File filePath = new File(localFile);
			File rootfilePath = new File(localRoot);
			String filename = filePath.getName();
			String furi1 = filePath.getParentFile().getAbsoluteFile().toURI()
					.toString();
			String furi2 = rootfilePath.getParentFile().getAbsoluteFile()
					.toURI().toString();
			// 获取此级路径名
			String objFolder = remotePath + furi1.substring(furi2.length());
			ftpClient.changeWorkingDirectory("/");
			// 如果不能进入dir下，说明此目录不存在！建立目录
			if (!ftpClient.changeWorkingDirectory(new String(objFolder
					.getBytes("GBK"), StandardCharsets.ISO_8859_1))) {
				if (!ftpClient.makeDirectory(new String(objFolder
						.getBytes("GBK"), StandardCharsets.ISO_8859_1))) {
					System.out.println("create files directory[" + objFolder
							+ "]failed!");
				}
				ftpClient.changeWorkingDirectory(new String(objFolder
						.getBytes("GBK"), StandardCharsets.ISO_8859_1));
			}
			boolean flag = ftpClient.storeFile(filename, input);
			if (flag) {
				System.out.println("upload file[  " + filePath.getName()
						+ "]to[ " + objFolder + "]success!");
			} else {
				System.out.println("upload file[ " + filePath.getName()
						+ "]to[" + objFolder + "]failed!");
				return ERROR;
			}
			input.close();
		} catch (IOException e) {
			System.out.println(e.getMessage());
			return ERROR;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return ERROR;
		}
		return null;
	}

	/**
	 * 下载文件，用于FTP下载文件夹时调用
	 * 
	 * @param ftpFile
	 * @param relativeLocalPath
	 * @param relativeRemotePath
	 */
	private void downloadFile(FTPFile ftpFile, String relativeLocalPath,
			String relativeRemotePath) {
		if (ftpFile.isFile()) {// down file
			if (ftpFile.getName().indexOf("?") == -1) {
				OutputStream outputStream = null;
				try {
					outputStream = new FileOutputStream(relativeLocalPath
							+ ftpFile.getName());
					ftpClient.retrieveFile(ftpFile.getName(), outputStream);
					outputStream.flush();
					outputStream.close();
				} catch (Exception e) {
					System.out.println(e.getMessage());
				} finally {
					try {
						if (outputStream != null) {
                            outputStream.close();
                        }
					} catch (IOException e) {
						System.out.println(e.getMessage());
					}
				}
			}
		} else { // deal dirctory
			String newLocalRelatePath = relativeLocalPath + ftpFile.getName();
			String newRemote = relativeRemotePath
                    + ftpFile.getName();
			File fl = new File(newLocalRelatePath);
			if (!fl.exists()) {
				fl.mkdirs();
			}
			try {
				newLocalRelatePath = newLocalRelatePath + '/';
				newRemote = newRemote + "/";
				String currentWorkDir = ftpFile.getName();
				// 进入相应路径
				boolean changedir = ftpClient
						.changeWorkingDirectory(currentWorkDir);
				if (changedir) {
					FTPFile[] files = null;
					files = ftpClient.listFiles();
					for (int i = 0; i < files.length; i++) {
						downloadFile(files[i], newLocalRelatePath, newRemote);
					}
				}
				// return parent directory
				if (changedir) {
                    ftpClient.changeToParentDirectory();
                }
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
	}

	/**
	 * 由参数配置文件设置参数
	 * 
	 * @param configFile
	 *            ：参数文件
	 */
	private void setArg(String configFile) {
		Properties property = new Properties();
		BufferedInputStream inBuff = null;
		try {
			File file = new File(configFile);
			inBuff = new BufferedInputStream(new FileInputStream(file));
			property.load(inBuff);
			param.setUserName(property.getProperty("username"));
			param.setPassword(property.getProperty("password"));
			param.setIp(property.getProperty("ip"));
			param.setPort(Integer.parseInt(property.getProperty("port")));
			configRemotePath = property.getProperty("remotePath");
			configLocalPath = property.getProperty("localpath");
			configLocalRoot = property.getProperty("localRoot");
		} catch (FileNotFoundException e) {
			System.out.println("config file [" + configFile + "] not exist!");
			System.out.println(e.getMessage());
		} catch (IOException e) {
			System.out.println("config file [" + configFile + "]can't read!");
			System.out.println(e.getMessage());
		}
	}

	private String issureConnect() {
		String result = null;
		// 判断是否处于连接状态，若不处于连接状态，则登录FTP
		if (!ftpClient.isConnected()) {
			// 连接重试次数
			for (int i = 0; i < RETRYTIMES; i++) {
				result = connectFTPServer();
				if (result == null) {
                    return null;
                }
			}
		}
		return result;
	}

	private String connectFTPServer() {
		try {
			ftpClient.connect(param.getIp(), param.getPort());
			ftpClient.setControlEncoding("GBK");
			ftpClient.login(param.getUserName(), param.getPassword());
			int reply = ftpClient.getReplyCode();
			// 等待2分钟，超时退出登录
			ftpClient.setDataTimeout(2000);// 2分钟的时间需要确认
			if (!FTPReply.isPositiveCompletion(reply)) {
				ftpClient.disconnect();
				System.err.println("FTP server refuse to connect!");
				return ERROR_CONNECT_FTP;
			}
			//System.out.println("login ftp server " + param.getIp() + " success!");
		} catch (SocketException e) {
			System.err.println("login ftp server " + param.getIp()
					+ " failed,timeout!");
			return ERROR_CONNECT_FTP;
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("login ftp server " + param.getIp()
					+ " failed,can't open server!");
			return ERROR_CONNECT_FTP;
		}
		return null;
	}

	@Override
	public boolean checkDirExist(String path) {
		Boolean flag = false;
		try {
			flag = ftpClient.makeDirectory(path);
			if (flag) {
				ftpClient.removeDirectory(path);
			}
		} catch (IOException e) {
			log.error("remote fileSystem has an Exception!");
			e.printStackTrace();
		}

		return !flag;
	}

	public String connectFTPServer(int timeout) {
		try {
			ftpClient.setDataTimeout(timeout);// 2分钟的时间需要确认
			ftpClient.connect(param.getIp(), param.getPort());
			ftpClient.setControlEncoding("GBK");
			ftpClient.login(param.getUserName(), param.getPassword());
			int reply = ftpClient.getReplyCode();
			// 等待2分钟，超时退出登录
			
			if (!FTPReply.isPositiveCompletion(reply)) {
				ftpClient.disconnect();
				System.err.println("FTP server refuse to connect!");
				return ERROR_CONNECT_FTP;
			}
			//System.out.println("login ftp server " + param.getIp() + " success!");
		} catch (SocketException e) {
			System.err.println("login ftp server " + param.getIp()
					+ " failed,timeout!");
			return ERROR_CONNECT_FTP;
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("login ftp server " + param.getIp()
					+ " failed,can't open server!");
			return ERROR_CONNECT_FTP;
		}
		return null;
	}

}
