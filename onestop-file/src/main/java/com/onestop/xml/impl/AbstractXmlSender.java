package com.onestop.xml.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

import com.onestop.xml.model.Node;
import com.onestop.xml.spi.IXmlSender;
import com.onestop.xml.template.BeanTemplateFactory;

/**
 * @类名: AbstractXmlSender
 * @描述: 由 xsd模板文件传入相关参数值（查询条件）生成文件，并发送到FTP服务器。
 * @版本: 
 * @创建日期: 2015-10-20下午01:15:02
 * @作者: huangr
 * @JDK: 1.6
 */
/*
* 类的横向关系：TODO 说明与其它类的关联、调用或依赖等关系。
*/
public abstract class AbstractXmlSender implements IXmlSender {
	
	protected String xsdName=null;
	/**
	 * Node node   待转换的node对象
	 */
	protected Node node=null;
	/**
	 * File file  待发送的 xml文件
	 */
	protected File file=null;
	
	/**
	 * Map<String,Node> beanMap   bean的模板
	 */
	private Map<String,Node> beanMap= null;

	/**
	 * AbstractXmlSender  构造函数，初始化时将xsd值传入，并设置需要生成的文件路径
	 *  
	 */
	public AbstractXmlSender(){
		this.setXsdName();
	}
	/**
	 * setXsdName
	 * @描述: 设置待生成的文件所需的xsd文件名
	 * @作者: huangr
	 * @创建时间: 2015-10-19下午05:19:07
	 *  
	 */
	protected abstract String  setXsdName();
	/**
	 * setFile
	 * @描述: 设置需要生成的文件的路径
	 * @作者: huangr
	 * @创建时间: 2015-10-19下午05:05:00
	 */
	protected abstract File setFile();
	/**
	 * validFile
	 * @描述: 检测需要生成的文件是否存在
	 * @作者: huangr
	 * @创建时间: 2015-11-28下午06:38:40
	 * @return
	 *  存在返回true
	 */
	private boolean validFile() {
		if(this.setFile()!=null){
			if(!file.exists()){
				try {
					file.createNewFile();
				} catch (IOException e) {
					
					e.printStackTrace();
				}
			}
		}else{
			
			try {
				throw new FileNotFoundException("expect creating file not found,make sure you had set the file");
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} 
		}
	
		return true;
	}
	/**
	 * getBeanMap
	 * @描述: 获取由xsd文件模板生成的Node模板
	 * @作者: huangr
	 * @创建时间: 2015-10-20下午01:17:06
	 * @return
	 *  Node模板
	 */
	protected Map<String,Node> getBeanMap(){
		return this.beanMap;
	}
	
	/* (non-Javadoc)
	 * @see com.phy.oms.xml.spi.IXmlSender#createXmlFile(com.phy.oms.xml.model.Node)
	 */
	@Override
	public File createXmlFile(Node node) {
		if(validFile()){
			XmlFileAdapterImpl adapter =new XmlFileAdapterImpl();
			boolean flag =adapter.beanToFile(node, file);
			if(flag){
				System.out.println("********create("+this.xsdName+")acturally File success! the path: "+this.file.getAbsolutePath() );
			}
			else{
				System.out.println("********create("+this.xsdName+")acturally File faild! the path: "+this.file.getAbsolutePath() );
			}
			
		}
		return file;
	}


	/* (non-Javadoc)
	 * @see com.phy.oms.xml.spi.IXmlSender#paserXSD(java.lang.String)
	 */
	@Override
	public Node paserXSD(String xsdName) {
		Map<String,Node> map = BeanTemplateFactory.createIBean(xsdName);
		if(map!=null){
			this.beanMap=map;
			Node node = map.get(xsdName);
			this.node=node;
			return node;
		}
		return null;
		
	}
	/**
	 * setBeanValue
	 * @描述: 为Node设置值，将参数值设置到Node对象中
	 * @作者: huangr
	 * @创建时间: 2015-10-19下午04:54:08
	 * @return
	 *  返回设置好的node对象
	 */
	protected abstract Node setBeanValue();
	

}
