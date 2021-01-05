package com.onestop.xml.impl;

import com.onestop.xml.api.IBeanAdapter;
import com.onestop.xml.api.IXmlFileAdapter;


/**
 * @类名: BeanComponentInterfaceFactory
 * @描述: 获取IBean组件各接口的实现对象工厂类。
 * @版本: 
 * @创建日期: 2015-10-15下午04:18:36
 * @作者: huangr
 * @JDK: 1.6
 */
/*
* 类的横向关系：TODO 说明与其它类的关联、调用或依赖等关系。
*/
public class BeanComponentInterfaceFactory {
	/**
	 * JavaBean与IMA间转换接口工厂方法
	 * 
	 * @return IBeanAdapter的接口实现对象
	 */
	public static IBeanAdapter getBeanAdapter() {
		return new BeanAdapterImpl();
	//	throw new RuntimeException("unsupported method");
	}
	
/*	*//**
	 * IMA入外部库接口工厂方法
	 * 
	 * @return IDb的接口实现对象
	 *//*
	public static IImaDb getIImaDb() {
		return new DbImpl2();
	}
	
	*//**
	 * IMA入外部库接口工厂方法
	 * 
	 * @return IDb的接口实现对象
	 *//*
	public static IDb getIDb() {
		return new DbImpl();
	}
	*/
	/**
	 * IMA入本地库接口工厂方法
	 * 
	 * @return ILocalDb的接口实现对象
	 */
/*	public static ILocalDb getILocalDb() {
		return new LocalDbImpl();
	}*/
	
	/**
	 * 读写XML文件接口工厂方法
	 * 
	 * @return IXmlFile的接口实现对象
	 */
	public static IXmlFileAdapter getXmlFileAdapter() {
		return new XmlFileAdapterImpl();
	}
}
