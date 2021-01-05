/**
 * 
 */
package com.onestop.fsf.spi;

import java.io.*;

/**
 * @author root
 *
 */
public class FileUtils {
	private  static String SPRIT = File.separator;
	public static final String	LOCAL						= "LOCAL";
	public static final String	REMOTE						= "REMOTE";
	
	
	
	/**
	 * 文件拷贝
	 * 
	 * @param sfrom
	 *            源文件
	 * @param sto
	 *            拷贝目的文件
	 * @return 拷贝结果
	 */
	public static boolean copyToFile(String sfrom, String sto) {
		String from = sfrom.trim();
		String to = sto.trim();
		try {
			File f = new File(from);
			if (!f.exists()) {
				return false;
			} else {
				String toPath = to.substring(0, to.lastIndexOf(SPRIT));
				f = new File(toPath);
				if (!f.exists()) {
					f.mkdirs();
				}
				BufferedInputStream bin = new BufferedInputStream(new FileInputStream(from));
				BufferedOutputStream bout = new BufferedOutputStream(new FileOutputStream(to));
				int c;
				while ((c = bin.read()) != -1) {
					bout.write(c);
				}
				bin.close();
				bout.close();
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * 将源文件拷贝到目的目录
	 * 
	 * @param fileName
	 *            源文件
	 * @param destDir
	 *            目的目录
	 * @return
	 */
	public static boolean copyTo(String fileName, String destDir) {
		String from = fileName.trim();
		String to = destDir.trim();
		if (!to.endsWith(SPRIT)) {
			to += SPRIT;
		}
		try {
			File f = new File(from);
			to += f.getName();
			if (!f.exists()) {
				return false;
			} else {
				String toPath = to.substring(0, to.lastIndexOf(SPRIT));
				f = new File(toPath);
				if (!f.exists()) {
					f.mkdirs();
				}
				BufferedInputStream bin = new BufferedInputStream(new FileInputStream(from));
				BufferedOutputStream bout = new BufferedOutputStream(new FileOutputStream(to));
				int c;
				while ((c = bin.read()) != -1) {
					bout.write(c);
				}
				bin.close();
				bout.close();
				return true;
			}
		} catch (Exception e) {
			return false;
		}
	}
	
	
	/**
	 * 提供源文件到目标路径的拷贝 源文件可以是文件或目录
	 * 
	 * @param absFileName
	 *            源文件或目录的绝对路径名
	 * @param tarDir
	 *            目标路径的绝对路径名
	 * @return 复制状态，复制成功则返回true，否则返回false
	 * @exception 如果尝试复制一个不存在的文件或目录
	 *                ，者目标路径不存在将抛出异常
	 */
	public static synchronized boolean copy(String absFileName, String tarDir) throws Exception {
		File srcFile = new File(absFileName);
		if (!srcFile.exists()) {
			throw new Exception("can't copy file or folder:source file or folder[" + srcFile.getAbsolutePath() + "] not exist!");
		}
		if (srcFile.isFile()) {
			copyTo(absFileName, tarDir);
			return true;
		}
		File tarFileDir = new File(tarDir);
		if (!tarFileDir.exists()) {
			throw new Exception("can't copy file or folder,destiny path[" + tarFileDir.getAbsolutePath() + "] not exist!");
		}
		File tarFile = new File(tarDir + SPRIT + srcFile.getName());
		tarFile.mkdir();
		if (!tarFile.exists()) {
			throw new Exception("can't copy file or folder,create folder[" + srcFile.getAbsolutePath() + "] failed!");
		}
		File[] files = srcFile.listFiles();
		for (File file : files) {
			if (file.isDirectory()) {
				copy(file.getAbsolutePath(), tarFile.getAbsolutePath());
			} else {
				copyTo(file.getAbsolutePath(), tarFile.getAbsolutePath());
			}
		}
		return true;
	}
	
	/**
	 * 删除指定的文件或目录
	 * 
	 * @param dir
	 *            要删除的文件或目录
	 * @return 如果成功删除则返回true，否则返回false
	 * @exception 如果尝试删除一个不存在的文件或目录
	 *                ，将抛出异常
	 */
	public synchronized static boolean delete(String absFilePath) throws Exception {
		File tarFile = new File(absFilePath);
		if (!tarFile.exists()) {
			throw new Exception("can't delete file or folder:source file or folder[" + tarFile.getAbsolutePath() + "] not exist!");
		}
		if (tarFile.isFile()) {
			if (!tarFile.delete()) {
				throw new Exception("can't delete file or folder:delete source file[" + tarFile.getAbsolutePath() + "] failed!");
			}
			return true;
		}
		File[] files = tarFile.listFiles();
		for (File file : files) {
			if (file.isDirectory()) {
				delete(file.getAbsolutePath());
			} else {
				if (!file.delete()) {
					throw new Exception("can't delete file or folder:delete source file[" + file.getAbsolutePath() + "]failed!");
				}
			}
		}
		if (!tarFile.delete()) {
			throw new Exception("can't delete file or folder:delete source file[" + tarFile.getAbsolutePath() + "]failed!");
		}
		return true;
	}
	
	/**
	 * 移动一个目录或文件到另一个目录
	 * 
	 * @param srcDir
	 *            源目录或文件的绝对路径名
	 * @param tarDir
	 *            目标路径的绝对路径名
	 * @param bufferSize
	 *            缓存大小
	 * @exception 如果尝试复制一个不存在的文件或目录
	 *                ，或者目标路径不存在，将抛出异常
	 * @return 如果移动成功则返回true，否则返回false
	 */
	public static synchronized boolean move(String srcFileName, String tarDir) throws Exception {
		File srcFile = new File(srcFileName);
		if (!srcFile.exists()) {
			throw new Exception("can't remove file or folder:delete source file[" + srcFile.getAbsolutePath() + "]not exist!");
		}
		if (srcFile.isFile()) {
			copyTo(srcFileName, tarDir);
			if (!srcFile.delete()) {
				throw new Exception("can't remove file or folder:delete source file[" + srcFile.getAbsolutePath() + "] failed!");
			}
			return true;
		}
		File tarFile = new File(tarDir + SPRIT + srcFile.getName());
		if (!tarFile.mkdir()) {
			throw new Exception("can't remove file or folder:create destiny folder[" + tarFile.getAbsolutePath() + "] failed!");
		}
		File[] files = srcFile.listFiles();
		for (File file : files) {
			if (file.isDirectory()) {
				move(file.getAbsolutePath(), tarFile.getAbsolutePath());
			} else {
				copyTo(file.getAbsolutePath(), tarFile.getAbsolutePath());
				if (!file.delete()) {
					throw new Exception("can't remove file or folder:delete source file[" + file.getAbsolutePath() + "]failed!");
				}
			}
		}
		if (!srcFile.delete()) {
			throw new Exception("can't remove file or folder:delete source file[" + srcFile.getAbsolutePath() + "] failed!");
		}
		return true;
	}
	
	/**
	 * 移动一个目录或文件到另一个目录
	 * 
	 * @param srcDir
	 *            源目录或文件的绝对路径名
	 * @param tarDir
	 *            目标路径的绝对路径名
	 * @param bufferSize
	 *            缓存大小
	 * @exception 如果尝试复制一个不存在的文件或目录
	 *                ，或者目标路径不存在，将抛出异常
	 * @return 如果移动成功则返回true，否则返回false
	 */
	public static synchronized boolean moveFile(String srcFileName, String tarDir) throws Exception {
		File srcFile = new File(srcFileName);
		if (!srcFile.exists()) {
			throw new Exception("can't remove file or folder:delete source file[" + srcFile.getAbsolutePath() + "]not exist!");
		}
		if (srcFile.isFile()) {
			copyTo(srcFileName, tarDir);
			if (!srcFile.delete()) {
				throw new Exception("can't remove file or folder:delete source file[" + srcFile.getAbsolutePath() + "] failed!");
			}
			return true;
		}
		File tarFile = new File(tarDir + SPRIT + srcFile.getName());
		if (!tarFile.mkdir()) {
			throw new Exception("can't remove file or folder:create destiny folder[" + tarFile.getAbsolutePath() + "] failed!");
		}
		File[] files = srcFile.listFiles();
		for (File file : files) {
			if (file.isDirectory()) {
				move(file.getAbsolutePath(), tarFile.getAbsolutePath());
			} else {
				copyTo(file.getAbsolutePath(), tarFile.getAbsolutePath());
				if (!file.delete()) {
					throw new Exception("can't remove file or folder:delete source file[" + file.getAbsolutePath() + "]failed!");
				}
			}
		}
		if (!srcFile.delete()) {
			throw new Exception("can't remove file or folder:delete source file[" + srcFile.getAbsolutePath() + "] failed!");
		}
		return true;
	}
	
	/**
	 * 根据制定的绝对路径名或相对路径创建一个文件夹
	 * 
	 * @param absFolderPath
	 *            文件夹得绝对路径名或相对路径名
	 * @return 如果创建成功则返回true ，否则返回false
	 */
	public static boolean createFolder(String folderPath) throws Exception {
		File file = new File(folderPath);
		file.mkdirs();
		if (!file.exists()) {
			throw new Exception("can't create folder:create folder[" + folderPath + "] failed!");
		}
		return true;
	}
	
	/**
	 * 检测目标文件或目录是否存在
	 * 
	 * @param tarPath
	 *            目标文件或目录的绝对文件名
	 * @return 如果存在则返回true ，否则返回false
	 */
	public static boolean checkFileExists(String tarPath) {
		File file = new File(tarPath);
		return file.exists();
	}
	
	
	public static String cleanSrcFile(String srcFilePath) {
		try {
			File srcFile = new File(srcFilePath);
			File parentFolder = srcFile.getParentFile();
			 //删除源文件
			if (srcFile.exists()) {
				srcFile.delete();
			}
			// 删除源文件上级文件夹
			if (parentFolder != null && parentFolder.exists()) {
				if (parentFolder.listFiles().length == 0) {
					//上级文件夹为空再删除
					parentFolder.delete();
				}
			}
			return null;
		} catch (Exception e) {
			return e.getMessage();
		}
	}

	
	
	/*
	 * public static String getSequenceId(String messageType, String type) {  固定长14位，包括8位年月日信息和六位流水线号（例如： String sequenceId = "";
	 * if (type.equalsIgnoreCase(LOCAL)) { sequenceId = FileIdParam.getInstance().getLocalSequenceId(messageType);
	 * FileIdParam.getInstance().addLocalSequenceId(messageType); } else { sequenceId =
	 * FileIdParam.getInstance().getSequenceId(messageType); FileIdParam.getInstance().addSequenceId(messageType); } return
	 * sequenceId;
	 */
	/**
	 * 获取文件扩展名称
	 * 
	 * @param file
	 *            目标文件
	 * @return 扩展名称
	 */
	public static String suffix(File file) {
		String fileName = file.getName();
		return suffix(fileName);
	}
	
	/**
	 * 获取文件扩展名称
	 * 
	 * @param fileName
	 *            目标文件名称
	 * @return 扩展名称
	 */
	public static String suffix(String fileName) {
		int len, pos;
		String suffix = null;
		if (fileName == null) {
			return suffix;
		}
		len = fileName.length();
		pos = fileName.lastIndexOf(".");
		if (pos > 0 && pos < len - 1) {
			suffix = fileName.substring(pos + 1);
		}
		return suffix;
	}
}
