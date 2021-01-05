package com.onestop.xml.impl;

import com.onestop.xml.api.IXmlFileAdapter;
import com.onestop.xml.model.Node;
import com.onestop.xml.spi.IXmlSolver;
import com.onestop.xml.template.BeanTemplateFactory;
import com.onestop.xml.valid.XsdValid;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.Map;

/**
 * @param <T>
 * @类名: AbstractXmlSolver
 * @描述: 自定义xml文件处理类，包括xml文件的验证，xml文件的解析，
 * @版本: 
 * @创建日期: 2015-10-20下午01:58:55
 * @作者: huangr
 * @JDK: 1.6
 */
@Slf4j
public abstract class AbstractXmlSolver<T> implements IXmlSolver {
	public AbstractXmlSolver(){
		this.setValidName();
	}
	
	/**
	 * @描述: 获取由验证文件生成的文件模板
	 * @return the fileTemplate
	 */
	public Map<String, Node> getFileTemplate() {
		return fileTemplate;
	}


	/**
	 * String validName   以该文件名的验证文件验证文件
	 */
	private String validName=null;
	/**
	 * Map<String,Node> fileTemplate  验证文件输出的Node模板
	 */
	private Map<String,Node> fileTemplate=null;
	/**
	 * Node node   解析文件后生成的Node对象
	 */
	protected Node node=null;
	
	/**
	 * Object object  解析文件生成的JavaBean 仅测试使用，
	 */
	private T object=null;
	/**
	 * setValidName
	 * @描述: 设置 验证名称
	 * @作者: huangr
	 * @创建时间: 2015-10-20下午01:55:51
	 *  
	 */
	abstract void setValidName();

	
	
	/**
	 * paserXSD
	 * @描述: TODO 描述这个方法的功能、适用条件及注意事项
	 * @作者: huangr
	 * @创建时间: 2015-10-20下午04:18:39
	 * @return
	 *  TODO 描述每个输入输出参数的作用、量化单位、值域、精度
	 */
	private Map<String, Node> paserXSD() {
		this.fileTemplate = BeanTemplateFactory.createIBean(this.validName);
		return this.fileTemplate;
		
	}

	/**
	 * execute
	 * @描述: 执行任务，一般完成锁机制的文件操作,包括验证、解析、入库、生成HTML结合
	 *   已完成验证、解析、生成Node。
	 * 
	 * @作者: huangr
	 * @创建时间: 2015-10-15下午08:08:29
	 *  
	 */
	public   void execute(File file) {
		String msg =this.validFile(file);
		if(msg!=null){
			log.warn("valid file("+file.getName()+")failed,reason:"+msg);
			return ;
		}
		this.paserXSD();
		this.paserXmlFile(file);
		this.object=this.beanToObject();
	}
	/**
	 * validFile
	 * @描述: 通过xsd文件验证是否符合要求
	 * @作者: huangr
	 * @创建时间: 2015-10-20下午04:16:25
	 * @param file
	 * @return
	 *   返回验证结果
	 */
	private String validFile(File file){
		String result = XsdValid.valid(validName, file.getAbsolutePath());
		return result;
	}
	
	private Node paserXmlFile(File file) {
		IXmlFileAdapter fileAdapter= BeanComponentInterfaceFactory.getXmlFileAdapter();
		  this.node= fileAdapter.fileToIBean(file, this.fileTemplate);
		  return this.node;
	}
	
	/**
	 * beanToObject
	 * @param <T>
	 * @描述: 将生成的解析出来的Node转换成JavaBean类型
	 * @作者: huangr
	 * @创建时间: 2015-10-20下午07:29:50
	 * @return
	 *  返回生成的Object
	 */
	abstract <T> T beanToObject();
	
	public  <T> Object getResult(){
		if(this.object==null){
			this.object=this.beanToObject();
		}
		return this.object;
	}

	

}
