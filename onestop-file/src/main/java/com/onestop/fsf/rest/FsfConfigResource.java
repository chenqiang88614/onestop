package com.onestop.fsf.rest;

import com.onestop.fsf.model.FsConfig;
import com.onestop.fsf.scanner.FileScannerFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @类名: FsfConfigResource
 * @描述: 文件扫描配置前台访问和配置接口
 * @版本:
 * @创建日期: 2016-1-22下午05:17:21
 * @作者: huangr
 * @JDK: 1.6 /* 类的横向关系：TODO 说明与其它类的关联、调用或依赖等关系。
 */
@Controller
@RequestMapping(value = "com/onestop/fsf")
@Slf4j
public class FsfConfigResource {

	@RequestMapping(method = RequestMethod.GET)
	public @ResponseBody FsConfig get() {

		return FileScannerFactory.getConfig();

	}

	@RequestMapping(method = RequestMethod.PUT)
	public @ResponseBody boolean set(@RequestBody FsConfig fs, HttpServletRequest request) {

		boolean flag = FileScannerFactory.setConfig(fs);
		if (flag) {
			log.info("update fsfConfig(" + fs.toString() + ") success!");
		} else {
			log.info("update fsfConfig failed!");
		}
		return flag;
	}

	@RequestMapping(value = "/isRight", method = RequestMethod.POST)
	public @ResponseBody String isRight(@RequestBody FsConfig config) {

		if (config == null) {
			return null;
		}
		// 首先看一下什么系统，看一下符不符合相应系统的路径
		Pattern pattern = null;
		Matcher matcher = null;
		String scanpath = config.getScanPath();
		String bakpath = config.getBackupPath();
		String ignorepath = config.getIgnoreFilePath();
		if ("\\".equals(File.separator)) {
			// windows 系统
			// String req
			// ="[a-zA-Z]:((\\\\|/)([0-9a-zA-Z\u0391-\uFFE5@_.;-]+)+\\\\{0,1})+";
			String req = "\\w:((\\\\|/).*)*";
			pattern = Pattern.compile(req);
			matcher = pattern.matcher(scanpath);
			Boolean a = matcher.matches();
			if (!a) {
				return "scanPath,not legal path";
			}
			matcher = pattern.matcher(bakpath);
			a = matcher.matches();
			if (!a) {
				return "backupPath,not legal path";
			}
			matcher = pattern.matcher(ignorepath);
			a = matcher.matches();
			if (!a) {
				return "ignoreFilePath,not legal path";
			}
		} else {
			// linux 系统
			String req = "(\\/([0-9a-zA-Z\u0391-\uFFE5@_.;-]+)+\\/{0,1})+";
			pattern = Pattern.compile(req);
			matcher = pattern.matcher(scanpath);
			Boolean a = matcher.matches();
			if (!a) {
				return "scanPath,not legal path";
			}
			matcher = pattern.matcher(bakpath);
			a = matcher.matches();
			if (!a) {
				return "backupPath,not legal path";
			}
			matcher = pattern.matcher(ignorepath);
			a = matcher.matches();
			if (!a) {
				return "ignoreFilePath,not legal path";
			}

		}
		// 看一下该路径是否存在，不存在则创建
		File scP = new File(config.getScanPath());
		File backP = new File(config.getBackupPath());
		File igP = new File(config.getIgnoreFilePath());

		if (!scP.exists()) {
			scP.mkdirs();
		}
		if (!backP.exists()) {
			backP.mkdirs();
		}
		if (!igP.exists()) {
			igP.mkdirs();
		}

		return "";
	}

	public static void main(String[] args) {
		// String req
		// ="[a-zA-Z]:((\\\\|/)([0-9a-zA-Z\u0391-\uFFE5@_.;]+)+\\\\{0,1})+|(\\/([0-9a-zA-Z]+)+\\/{0,1})+";
		// String req ="(\\/([0-9a-zA-Z\u0391-\uFFE5@_.;-]+)+\\/{0,1})+";
		String req = "[a-zA-Z]:((\\\\|/)([0-9a-zA-Z\u0391-\uFFE5@_.;-]+)+\\\\{0,1})+";
		Boolean a = Pattern.matches(req, "V:\\sd汉字s_d/-ddsd");
		Boolean b = Pattern.matches(req, "/sdwe/gh");
		System.out.println("V:\\sdsd ?  :" + a);
		System.out.println("\\sdwe/gh ?  :" + b);
	}
}
