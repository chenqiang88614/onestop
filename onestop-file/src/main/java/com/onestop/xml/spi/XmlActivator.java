package com.onestop.xml.spi;

import com.onestop.xml.valid.XmlConfig;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * @类名: XmlActivator
 * @描述: XML处理组件启动器，主要是检测配置路径是否存在，若不存在将抛出异常，无法调用其他方法，或调用会出异常
 * @版本: 
 * @创建日期: 2015-10-16上午09:16:45
 * @作者: huangr
 * @JDK: 1.6
 */
/**
* XML处理器主要功能包括：
*    1.xml文件验证（需传入xsd文件路径），XsdValid.valid()返回检测结果，返回null表示验证成功
*    
*    2.IBean模板Map 生成（通过在config/xml路径下的xsd文件名传入）BeanTemplateFactory.createIBean() 返回Map<String,Node>模板
*   
*    3.BeanComponentInterfaceFactory.getXmlFileAdapter()，返回XmlFileAdapter对象:
*    3.1    xml文件转Node对象（通过xml文件路径、BeanTemplate传入）  XmlFileAdapter.fileToBean()   返回node对象
*    3.2   把node对象保存到xml文件中（通过需要保存的路径、IBean对象传入 ）   XmlFileAdapter.beanToFile()  保存成功返回true
*    
*    
*    4. BeanComponentInterfaceFactory.getBeanAdapter(),返回BeanAdapter对象：
*    4.1   JavaBean对象转IBean对象（需要待生成的Bean名称，Object）BeanAdapter.objectToBean()  返回Node对象
*    4.2   IBean对象转JavaBean暂不支持，使用Node.getNodeValue("FileHeader");
* 										   Node.getPropertyData("Property", String.class)
*          可依次赋值。
*    
*    5.提供IXmlSolver 接口，为xml文件读取处理的整个流程明确化，适用于需要将之转JavaBean并入库 和 生成html文档的处理逻辑
*    6.提供
*    
*   
**/
@Slf4j
public class XmlActivator {

		
		/**
		 * 初始化方法，指定配置文件的默认路径为"../config/tsf/"
		 * 
		 * @return
		 */
		public static boolean start() {
			return start("../config/tsf/");
		}
		
		/**
		 * 初始化方法，指定TSF配置文件路径 状态矩阵配置文件路径在TSF配置文件路径下的matrix目录里
		 * 
		 * @param configPath
		 *            路径
		 * @return boolean
		 */
		public static boolean start(String configPath) {
			XmlConfig.configPath = configPath+"ima/";
			XmlConfig.validatePath=configPath+"valid/";
			if(!validate(XmlConfig.configPath, XmlConfig.validatePath)){
				log.error("ima module init faild!,please check your config path:\n imaPath:"+XmlConfig.configPath+"; validPath:"+ XmlConfig.validatePath);
				return false;
			}
			log.info("ima module init success!, your config path:\n imaPath:"+XmlConfig.configPath+"; validPath:"+ XmlConfig.validatePath);
			return true;
		}
		
		/**
		 * @param configPath
		 * @param validatePath
		 * @return boolean
		 */
		public static boolean start(String configPath, String validatePath) {
			XmlConfig.configPath = configPath;
			XmlConfig.validatePath = validatePath;
			if(!validate(XmlConfig.configPath, XmlConfig.validatePath)){
				log.error("ima module init faild!,please check your config path:\n imaPath:"+XmlConfig.configPath+"; validPath:"+ XmlConfig.validatePath);
				return false;
			}
			log.info("ima module init success!, your config path:\n imaPath:"+XmlConfig.configPath+"; validPath:"+ XmlConfig.validatePath);
			return true;
		}
		
		public static boolean stop() {
			return true;
		}
		public static boolean validate(String configPath,String validatePath){
			File f1 =new File(XmlConfig.configPath);
			File f2 =new File(XmlConfig.validatePath);
			if(!f1.exists()||!f2.exists()){
				try {
					throw new FileNotFoundException("path not found!\n"+XmlConfig.configPath+" or "+XmlConfig.validatePath);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return false;
			}
			return true;
			
		}
}
