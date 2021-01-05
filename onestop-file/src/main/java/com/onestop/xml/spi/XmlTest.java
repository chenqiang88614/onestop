package com.onestop.xml.spi;

import com.onestop.xml.api.IXmlFileAdapter;
import com.onestop.xml.iterator.IterateBeanImpl;
import com.onestop.xml.model.Node;
import com.onestop.xml.template.BeanTemplateCreator;
import com.onestop.xml.valid.XsdValid;
import com.onestop.xml.impl.BeanComponentInterfaceFactory;

import java.io.File;
import java.util.Map;

/**
 * @类名: XmlTest
 * @描述: xml插件的测试
 * @版本: 
 * @创建日期: 2015-10-15下午07:10:33
 * @作者: huangr
 * @JDK: 1.6
 */
/*
* 类的横向关系：TODO 说明与其它类的关联、调用或依赖等关系。
*/
public class XmlTest {
	
//	public static File srcFile =new File("V:\\xml","e.TDRSQUERYACK");
	public static File srcFile =new File("V:/xml/我是谁/","e.TDRSQUERYACK");
	
	public static void start(){
		System.out.println("......开始启动XML工具!");
		
		 boolean isStarted=XmlActivator.start("V:/xml/config/tsf/");
		 if(!isStarted){
			 System.out.println("启动失败！");
			 return ;
		 }
	//	AbstractXmlReader reader =new AbstractXmlReader();
	   //创建XML读写适配器
		IXmlFileAdapter xmlAdapter =BeanComponentInterfaceFactory.getXmlFileAdapter();
		
	
		
		//reader.initialize();
		String validResult = XsdValid.valid("TDRSQUERYACK",srcFile.getAbsolutePath());
		if(validResult==null){
			System.out.println(srcFile.getName()+"文件验证成功！！");
			Map<String, Node> dataTemplate  =	BeanTemplateCreator.createIBean("TDRSQUERYACK");
			Node d= xmlAdapter.fileToIBean(srcFile, dataTemplate);
			d.iterateData(new IterateBeanImpl(), 0);
			System.out.println("文件转Node对象成功！。。。。");
		}
		else{
			System.out.println(srcFile.getName()+"文件验证失败！！\n失败原因:"+validResult);
		}
	}
	
	public static void main(String[] args){
		start();
	}
}
