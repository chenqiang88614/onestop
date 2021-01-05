package com.onestop.xml.iterator;

/**
 * @类名: IIterateBean
 * @描述: 定义Bean遍历方法需要注入的实现类应实现的接口
 * @版本: 
 * @创建日期: 2015-10-15下午03:17:11
 * @作者: huangr
 * @JDK: 1.6
 */
/*
* 类的横向关系：TODO 说明与其它类的关联、调用或依赖等关系。
*/
public interface IIterateBean {
	/**
	 * 用于注入Node的iterateData方法，
	 * iterateData方法会将遍历到的数据输入到本方法中，
	 * 由本方法进行处理。
	 * @param <T> Ima数据的类型
	 * @param dataType 数据类型指示类
	 * @param data Bean数据，通常为String，对于Node，固定值为Node
	 * @param name 字段名称
	 * @param chName 字段的中文名
	 * @param level 顶层Node的属性层数为1
	 */
    <T> void processData(Class<T> dataType, T data, String name, String chName, int level);

}
