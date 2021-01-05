package com.onestop.xml.model;

/**
 * @类名: IBean
 * @描述: 自定义的自描述数据类型需要实现的基本接口
 * @版本: 
 * @创建日期: 2015-10-15上午11:23:01
 * @作者: huangr
 * @JDK: 1.6
 */
/*
* 类的横向关系：TODO 说明与其它类的关联、调用或依赖等关系。
*/
public interface IBean {

	/**
	 * 获取属性的中文名
	 * @return 属性中文名
	 */
    String getChName();

	/**
	 * 设置属性的中文名
	 * @param chName 中文名
	 */
    void setChName(String chName);

	/**
	 * 获取属性名
	 * @return 属性名
	 */
    String getName();

	/**
	 * 设置属性名
	 * @param name 属性名
	 */
    void setName(String name);
	
	/**
	 * 获取属性数据
	 * @param <T> 泛型类型
	 * @param type 指定数据的类型
	 * @return 如果指定数据的类型和getType()获取的类型不匹配，则会返回null
	 */
    <T> T getData(Class<T> type);

	/**
	 * 设置属性数据
	 * @param <T> 泛型类型
	 * @param data 数据
	 */
    <T> void setData(T data);

	/**
	 * 设置属性数据
	 * @param <T> 泛型类型
	 * @param data 数据
	 */
    <T> Class<T> getType();

	/**
	 * 设置IMA的数据类型
	 * @param <T> 泛型类型
	 * @param type 类型
	 */
    <T> void setType(Class<T> type);

	
	/**
	 * 返回该字段是否隐藏，用于生成xml文件和显示
	 * @return boolean型的隐藏情况
	 */
    boolean isHide();

	/**
	 * 设置该字段是否隐藏，用于生成xml文件和显示
	 * @param boolean型的隐藏情况
	 */
    void setHide(boolean isHide);

}
