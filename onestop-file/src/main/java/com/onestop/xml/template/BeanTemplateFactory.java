package com.onestop.xml.template;

import java.util.Map;

import com.onestop.xml.model.Node;


/**
 * @类名: BeanTemplateFactory
 * @描述: Ibean 对象模板生成器
 * @版本: 
 * @创建日期: 2015-10-16上午09:24:13
 * @作者: huangr
 * @JDK: 1.6
 */
/*
* 类的横向关系：TODO 说明与其它类的关联、调用或依赖等关系。
*/
public class BeanTemplateFactory {
	/**
	 * 输入IBean名，返回IBean对象数据结构 ,需要读取IBean对应的xsd文件,
	 * 
	 * @param ibeanName
	 *           IBean 名的xsd文件名
	 * @return IBean模板
	 */
	public synchronized static Map<String, Node> createIBean(String ibeanName) {
		return BeanTemplateCreator.createIBean(ibeanName);
	}
}
