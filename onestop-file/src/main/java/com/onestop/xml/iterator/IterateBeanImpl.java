package com.onestop.xml.iterator;

/**
 * @类名: IterateBeanImpl
 * @描述: 将Node对象遍历输出到控制台的实现
 * @版本: 
 * @创建日期: 2015-10-16上午10:10:21
 * @作者: huangr
 * @JDK: 1.6
 */
/*
* 类的横向关系：TODO 说明与其它类的关联、调用或依赖等关系。
*/
public class IterateBeanImpl implements IIterateBean {

	@Override
	public <T> void processData(Class<T> dataType, T data, String name,
			String chName, int level) {
		for (int i = 1; i <= level; i++) {
			System.out.print("**");
		}
		System.out.println(name + "   " + data);

	}

}
