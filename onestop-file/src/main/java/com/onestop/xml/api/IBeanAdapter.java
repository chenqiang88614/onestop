package com.onestop.xml.api;

import com.onestop.xml.model.IBean;


/**
 * @类名: IBeanAdapter
 * @描述: 将XML的IBean转换成JavaBean对象
 * @版本: 
 * @创建日期: 2015-10-15下午03:40:01
 * @作者: huangr
 * @JDK: 1.6
 */
/*
* 类的横向关系：由于JavaBean对象千变万化，故使用Node.getNodeValue("FileHeader");
* 
* 										     Node.getPropertyData("Property", String.class)
* 反而更加方便，效率更高，故放弃了该接口
*/
public interface IBeanAdapter {
	/**
	 * XML——IBean对象转换成JavaBeans
	 * 
	 * @param data
	 *            XML——IBean数据
	 * @return JavaBean对象
	 */
	/**
	 * beanToObject
	 * @描述: XML——IBean对象转换成JavaBeans
	 * @作者: huangr
	 * @创建时间: 2015-10-15下午03:42:43
	 * @param data    XML——IBean数据
	 * @return
	 *  JavaBean对象
	 */


    Object beanToObject(IBean data);
	
	
	/**
	 * objectToBean
	 * @描述: JavaBeans转换成XML——IBean对象 需要是符合bean规范的对象。 自定义对象与其他对象等同处理 List类型的属性做单独处理。 不处理父类中的属性
	 * 	
	 * @作者: huangr
	 * @创建时间: 2015-10-15下午03:42:13
	 * @param data   JavaBean对象
	 * @param name   待生成Bean的名称
	 * @return
	 *  XML——IBean数据
	 */


    IBean objectToBean(Object data, String name);
}
