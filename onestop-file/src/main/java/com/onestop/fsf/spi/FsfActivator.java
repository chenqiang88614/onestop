package com.onestop.fsf.spi;

import com.onestop.fsf.scanner.ConfigFileParser;
import com.onestop.fsf.scanner.FileScannerFactory;


/**
 * 组件使用方法<br>
 * 1、编写配置文件<br>
 * 2、调用start方法启动扫描<br>
 * 3、调用stop方法停止扫描<br>
 * 配置文件编写方法<br>
 * 1、扫描文件路径：<Path>..//file//ftpdir//rmr//,..//file//ftpbak//rmr//;</Path><br>
 * 2、扫描时间间隔<TimeInterval>1</TimeInterval><br>
 * 3、文件匹配器列表，本组件使用列表中的所有匹配器匹配扫描到的文件。<FileMatcher><br>
 * 4、标记匹配器是否有效<flag><br>
 * 5、匹配器参数，如匹配器对应的扩展名等<parm><br>
 * 6、匹配器名称<name><br>
 * 7、文件处理器列表，当有文件匹配成功后，通知所有的文件处理器，<br>
 * 由文件处理器根据匹配器名判断是否需要处理该文件。<FileObserver><br>
 * 8、文件处理器对应的文件匹配器名称，可对应多个，  <Matchers></Matchers> <br>
 * 注意同一个匹配器实现类可出现多次，但是需要对应不同的参数和名称。<br>
 * 注：本组件提供四种匹配器:<br>
 * 1、fsf.matcher.FileMatcherBySuffix <br>
 * 2、
 * 
 * @author 贺然
 */
public class FsfActivator {
	private static FileScannerFactory fs	= null;
//	public static String webClassPath=ServerRealPathTools.getRootPath()+File.separator;
	/**
	 * @param configFilePath
	 *            配置文件路径
	 * @return
	 */
	public static boolean start(String configFilePath) {
		ConfigFileParser parser = new ConfigFileParser(configFilePath);
//		webClassPath=configFilePath.split("WEB-INF")[0];
		fs = new FileScannerFactory(parser);
		if (fs == null) {
			return false;
		} else {
			fs.startScan();
			return true;
		}
	}
	
	public static boolean stop() {
		if (fs == null) {
			return false;
		} else {
			fs.stopScan();
			return true;
		}
	}
}
