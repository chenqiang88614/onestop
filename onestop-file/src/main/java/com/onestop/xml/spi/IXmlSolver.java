package com.onestop.xml.spi;


/**
 * @类名: IXmlSolver
 * @描述: XML文件处理逻辑的一个参考
 * @版本: 
 * @创建日期: 2015-10-16上午08:20:40
 * @作者: huangr
 * @JDK: 1.6
 */
/*
*  1.首先使用XmlActivator.start() 启动组件，检测组件配置目录是否存在
*  2.新建类继承该类，在目标类中实现相应方法（包括自定义xsd文件转IBean模板，
*  自定义XML文件转Node，Node转JavaBean，Bean的保存入库，生成Html文件）
*/
public interface IXmlSolver {

	
	/**
	 * createHtml
	 * @描述: 生成前台Html文件，根据模板创建
	 * @作者: huangr
	 * @创建时间: 2015-10-15下午08:16:54
	 * @param  templatePath   html 页面模板
	 * @return
	 *  创建成功返回true，否则返回false
	 */
    boolean createHtml(String templatePath);
	
	/**
	 * saveBean
	 * @描述: 保存 转换成功的对象到数据库
	 * @作者: huangr
	 * @创建时间: 2015-11-29下午03:17:40
	 * @return
	 *  保存成功返回true
	 */
    boolean saveBean();
	
}
