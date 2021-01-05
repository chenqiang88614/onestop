package com.onestop.xml.api;

import java.io.File;
import java.util.Map;

import com.onestop.xml.model.IBean;
import com.onestop.xml.model.Node;


/**
 * @类名: IXmlFileAdapter
 * @描述: 读取文件，装换成IBean对象，或者由IBean对象，写入到文件中
 * @版本: 
 * @创建日期: 2015-10-15下午03:38:42
 * @作者: huangr
 * @JDK: 1.6
 */
/*
* 类的横向关系：TODO 说明与其它类的关联、调用或依赖等关系。
*/
public interface IXmlFileAdapter {
	/**
	 * 从文件中读取数据，赋值给IBean对象， 要求IBean对象已经建立好了数据结构。
	 * 
	 * @param dataFile
	 *            数据文件
	 * @param dataTemplate
	 *            数据格式模板
	 * @return IBean对象
	 */
	/**
	 * fileToIBean
	 * @描述: 从文件中读取数据，赋值给IBean对象， 要求IBean对象已经建立好了数据结构。
	 * @作者: huangr
	 * @创建时间: 2015-10-15下午03:36:03
	 * @param dataFile 数据文件
	 * @param dataTemplate  数据格式模板
	 * @return
	 *  IBean对象
	 */


    Node fileToIBean(File dataFile, Map<String, Node> dataTemplate);
	
	/**
	 * fileToBean
	 * @描述: 从文件中读取数据，赋值给IBean对象，要求IBean对象已经建立好数据结构。
	 * @作者: huangr
	 * @创建时间: 2015-10-15下午03:35:06
	 * @param path   数据文件路径
	 * @param dataTemplate   数据格式模板
	 * @return
	 *  IBean对象
	 */


    Node fileToBean(String path, Map<String, Node> dataTemplate);
	
	/**
	 * IBeanToFile
	 * @描述:  将IBean数据写入文件，如果该文件已存在，将被覆盖。
	 * @作者: huangr
	 * @创建时间: 2015-10-15下午03:34:21
	 * @param data  IBean数据对象
	 * @param f  文件对象
	 * @return
	 *    写入的结果。
	 */


    boolean beanToFile(IBean data, File f);
	
	/**
	 * 将IBean数据写入文件，如果该文件已存在，将被覆盖。
	 * 
	 * @param data
	 *            IBean数据对象
	 * @param f
	 *            文件路径
	 * @return 写入的结果。
	 */
	/**
	 * IBeanToFile
	 * @描述: 将IBean数据写入文件，如果该文件已存在，将被覆盖。
	 * @作者: huangr
	 * @创建时间: 2015-10-15下午03:37:10
	 * @param data    IBean数据对象
	 * @param path  文件路径
	 * @return
	 *  写入的结果。
	 */


    boolean beanToFile(IBean data, String path);
}
