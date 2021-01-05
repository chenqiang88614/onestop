package com.onestop.xml.valid;

import lombok.extern.slf4j.Slf4j;
import org.dom4j.util.XMLErrorHandler;
import org.xml.sax.SAXException;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.IOException;

@Slf4j
public class XsdValid {

	public XsdValid() {
	}
	
	/**
	 * valid
	 * @描述: 使用XSD文件对文件进行验证（文件路径有中文时将不能正常验证）
	 * @作者: huangr
	 * @创建时间: 2015-10-20下午06:30:03
	 * @param messageType  xsd名称
	 * @param xmlFilePath  待验证文件路径
	 * @return
	 *  验证结果，为null则成功 
	 */
	public static String valid(String messageType, String xmlFilePath) {
 		//String xmlSchemaFilePath = XmlConfig.validatePath + messageType + ".xsd";
		String xmlSchemaFilePath = XmlConfig.validatePath + "/" + messageType + ".xsd";

		XMLErrorHandler errorHandler = new XMLErrorHandler();
		// 建立schema工厂
		SchemaFactory schemaFactory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
		// 建立验证文档文件对象，利用此文件对象所封装的文件进行schema验证
		File schemaFile = new File(xmlSchemaFilePath);
		
		if(!schemaFile.exists()){
			try {
				schemaFile.createNewFile();
			} catch (IOException e) {
				log.error("create xsd file faild，reason:"+e.getMessage());
				e.printStackTrace();
				
			}
				
		}
		// 利用schema工厂，接收验证文档文件对象生成Schema对象
		Schema schema = null;
		try {
			schema = schemaFactory.newSchema(schemaFile);
		} catch (SAXException e) {
			log.error("Schema file created faild,reason： " + e.getMessage());
			return e.getMessage();
		}
		// 通过Schema产生针对于此Schema的验证器，利用schenaFile进行验证
		Validator validator = schema.newValidator();
		// 得到验证的数据源
		Source source = new StreamSource(xmlFilePath);
		// 开始验证，成功输出success!!!，失败输出fail
		try {
			validator.setErrorHandler(errorHandler);
			validator.validate(source);
			// XMLWriter writer = new XMLWriter(OutputFormat.createPrettyPrint());
			// 用于控制台输出！
			// 如果错误信息不为空，说明校验失败，打印错误信息
			if (errorHandler.getErrors().hasContent()) {
				String errorMsgArray = errorHandler.getErrors().elementText("error");
				log.error("valid file("+xmlFilePath+") error,reason:"+errorMsgArray);
				errorMsgArray = errorMsgArray.replace("element", "Element");
				// 不能确保是"Element"还是"element",因此加了个判断
				if (errorMsgArray.contains("Element")) {
					String[] errorMsg = errorMsgArray.split("Element '");
					String errormsg = errorMsg[1];
					String[] error = errormsg.split("'");
					errorMsgArray = error[0];
				}
				errorMsgArray = "Element < " + errorMsgArray + " > is not vaild !";
				return errorMsgArray;
			} else {
				log.info(xmlFilePath+" file validate success!");
				return null;
			}
		} catch (Exception e) {
//			log.error("valid file failed,reason:" + e.getMessage());
			return e.getMessage();
		}
	}
}
