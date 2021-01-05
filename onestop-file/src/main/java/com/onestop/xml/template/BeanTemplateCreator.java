package com.onestop.xml.template;

import com.onestop.xml.model.Node;
import com.onestop.xml.valid.XmlConfig;
import com.onestop.xml.model.Leaf;
import lombok.extern.slf4j.Slf4j;
import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class BeanTemplateCreator extends BeanTemplateFactory {
	/**
	 * 创建IBean对象模板
	 * 
	 * @param ibeanName
	 *            IBean类型的名称
	 * @return IBean对象map，map中包括IBean中可重复出现的子节点
	 */
	public static Map<String, Node> createIBean(String ibeanName) {
		//String path = XmlConfig.configPath + ibeanName + ".xsd";
		String path = XmlConfig.configPath + "/" +  ibeanName + ".xsd";
		if(!new File(path).exists()){
			try {
				throw new FileNotFoundException("path："+path+"not found,please check your path config!");
			} catch (FileNotFoundException e) {
				
				e.printStackTrace();
			}
		}
		return accessFile(new File(path), "");
	}
	
	/**
	 * 访问XSD文件，创建IBean模板
	 * 
	 * @param file
	 *            IBean的XSD描述文件
	 * @param beanClassName
	 *            IBean的名称
	 * @return IBeanName IBean类型的名称
	 */
	private static Map<String, Node> accessFile(File file, String beanClassName) {
		SAXBuilder sb = new SAXBuilder();
		Document doc = null;
		Element root = null;
		try {
			doc = sb.build(file);
			root = (Element) doc.getRootElement().getChildren().get(0);
		} catch (Exception e) {
			log.error("文件【" + file.getName() + "】格式错误，原因：" + e.getLocalizedMessage() + "请用notepad软件核对" +
					"该文件首行编码和实际编码是否一致，首行和右下角是否均为UTF-8或GB2312！！！");
			System.out.println("IBeanCreate Error : load xsd file fail,not a xml file");
			return null;
		}
		// 遍历节点！
		if ("complexType".equals(((Element) root.getChildren().get(0)).getName())) {
			Map<String, Node> map = new HashMap<String, Node>();
			String nodeName = root.getAttributeValue("name");
			String chName = root.getAttributeValue("chname");
			Node node = new Node(null, nodeName, chName);
			map.put(nodeName, node);
			accessElement(map, root, node, 1);
			return map;
		} else {
			return null;
		}
	}
	
	/**
	 * 遍历XSD中的结点
	 * 
	 * @param map
	 *            IBean格式描述映射
	 * @param parent
	 *            XSD文件解析出的总节点
	 * @param node
	 *            通过引用返回生成的IBean
	 * @param level
	 *            当前所处的级别
	 * @return 返回-1为失败
	 */
	private static int accessElement(Map<String, Node> map, Element parent, Node node, int level) {
		Element son = null;
		son = (Element) ((Element) parent.getChildren().get(0)).getChildren().get(0);
		int flag = -1;
		List listChild = son.getChildren();
		int iChild = listChild.size();
		for (int i = 0; i < iChild; i++) {
			Element e = (Element) listChild.get(i);
			if ("element".equals(e.getName())) {
				String subOccurs = e.getAttributeValue("maxOccurs");
				String subNodeName = e.getAttributeValue("name");
				String subChName = e.getAttributeValue("chname");
				// String subType = e.getAttributeValue("type");
				if ("unbounded".equals(subOccurs)) {// 可重复
					// todo: 可重复类型最好能设法带上type，如增加属性，说明其子结点的type
					Node subNode = new Node(null, subNodeName, subChName);
					node.addProperty(subNode);
					if (e.getChildren().size() != 0) { // 可重复的大家伙
						Node subNodeTemplate = new Node(null, subNodeName, subChName);
						map.put(subNodeName, subNodeTemplate);
						accessElement(map, e, subNodeTemplate, level + 1);
					}
				} else {// 不可重复
					if (e.getChildren().size() != 0) { // 不可重复大家伙
						Node subNode = new Node(null, subNodeName, subChName);
						node.addProperty(subNode);
						accessElement(map, e, subNode, level + 1);
					} else {
						node.addProperty(accessAttribute(e, level));
					}
				}
			}
		}
		return flag;
	}
	
	/**
	 * 访问结点的属性
	 * 
	 * @param e
	 *            节点对应的JDom元素
	 * @param level
	 *            节点所处层次
	 */
	private static Leaf accessAttribute(Element e, int level) {
		String name = null;
		String type = null;
		String chname = null;
		boolean isHide = false;
		boolean isKey = false;
		List listAttributes = e.getAttributes();
		int iAttributes = listAttributes.size();
		for (int j = 0; j < iAttributes; j++) {
			Attribute attribute = (Attribute) listAttributes.get(j);
			if ("name".equalsIgnoreCase(attribute.getName())) {
				name = attribute.getValue();
			} else if ("type".equalsIgnoreCase(attribute.getName())) {
				type = attribute.getValue();
			} else if ("chname".equalsIgnoreCase(attribute.getName())) {
				chname = attribute.getValue();
			} else if ("ishide".equalsIgnoreCase(attribute.getName())) {
				if ("true".equalsIgnoreCase(attribute.getValue())) {
					isHide = true;
				}
			} else if ("isKey".equalsIgnoreCase(attribute.getName())) {
				if ("true".equalsIgnoreCase(attribute.getValue())) {
					isKey = true;
				}
			}
		}
		Leaf result = null;
		if (name == null || type == null) {
			return result;
		} else {
			try {
				if ("xs:string".equalsIgnoreCase(type)) {
					result = new Leaf(null, String.class, name);
				} else if (type.indexOf("xs:boolean") != -1) {
					result = new Leaf(null, boolean.class, name);
				} else if ("xs:int".equalsIgnoreCase(type)) {
					result = new Leaf(null, int.class, name);
				} else if ("xs:long".equalsIgnoreCase(type)) {
					result = new Leaf(null, long.class, name);
				} else if ("xs:float".equalsIgnoreCase(type)) {
					result = new Leaf(null, float.class, name);
				} else if ("xs:double".equalsIgnoreCase(type)) {
					result = new Leaf(null, double.class, name);
				} else {
					result = new Leaf(null, String.class, name);
				}
			} catch (Exception ex) {
				return result;
			}
		}
		result.setChName(chname);
		result.setHide(isHide);
		result.setKey(isKey);
		return result;
	}
	// /**
	// * 访问附加的命名空间
	// * @param e
	// * @param level 节点所处层次
	// */
	// private static void accessAdditionalNamespaces(Element e,int level){
	// List listAdditionalNamespaces = e.getAdditionalNamespaces();
	// int iAttributes = listAdditionalNamespaces.size();
	// for(int j = 0;j < iAttributes;j++){
	// Namespace namespace = (Namespace)listAdditionalNamespaces.get(j);
	// System.out.print(namespace.getPrefix() + "=\"" + namespace.getURI() +
	// "\"  ");
	// }
	// }
}
