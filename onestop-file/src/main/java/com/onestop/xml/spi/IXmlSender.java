package com.onestop.xml.spi;

import java.io.File;

import com.onestop.xml.model.Node;

public interface IXmlSender {
	
	/**
	 * createXmlFile
	 * @描述: 由node生成文件
	 * @作者: huangr
	 * @创建时间: 2015-10-20上午09:32:30
	 * @param node
	 * @return
	 *  生成的文件，失败将返回null。
	 */
    File createXmlFile(Node node);
	/**
	 * sendFile
	 * @描述: 发送生成的文件
	 * @作者: huangr
	 * @创建时间: 2015-10-20上午09:38:51
	 * @param file
	 * @return  发送返回结果，未null表示发送成功，否则不成功。
	 *  
	 */
    String sendFile(File file);
	/**
	 * paserXSD
	 * @描述: 根据xsd配置文件生成Node对象（各参数为空）
	 * @作者: huangr
	 * @创建时间: 2015-10-19下午04:43:07
	 * @param xsdName   xsd文件名
	 * @return
	 *  Node对象，若执行失败则抛出异常或返回null。
	 */
    Node paserXSD(String xsdName);

}
