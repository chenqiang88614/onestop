package com.onestop.xml.impl;

import com.onestop.xml.api.IXmlFileAdapter;
import com.onestop.xml.model.Bean;
import com.onestop.xml.model.IBean;
import com.onestop.xml.model.Leaf;
import com.onestop.xml.model.Node;
import lombok.extern.slf4j.Slf4j;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Map;
@Slf4j
public class XmlFileAdapterImpl implements IXmlFileAdapter {
	private static String	defaultEncoding	= "utf-8";
	@Override
	public Node fileToIBean(File dataFile, Map<String, Node> dataTemplate) {
		SAXBuilder sb = new SAXBuilder();
		Document doc = null;
		Element root = null;
		try {
			doc = sb.build(dataFile);
			root = doc.getRootElement();
		} catch (Exception e) {
			log.error("文件【" + dataFile.getName() + "】格式错误，原因：" + e.getLocalizedMessage() + "请用notepad软件核对" +
					"该文件首行编码和实际编码是否一致，首行和右下角是否均为UTF-8或GB2312！！！");
			return null;
		}
		Node result = dataTemplate.get(root.getName());
		iterateData(dataTemplate, result, root, 1);
		return result;
	}

	@Override
	public Node fileToBean(String path, Map<String, Node> dataTemplate) {
		return fileToIBean(new File(path), dataTemplate);
	}

	@Override
	public boolean beanToFile(IBean data, File f) {
		if(f.getName().endsWith("USERREQLIST")) {
			return beanToFile(data, f, "gb2312");
		} else {
			return beanToFile(data, f, defaultEncoding);
		}
		
	}


	@Override
	public boolean beanToFile(IBean data, String path) {
		return beanToFile(data, new File(path), defaultEncoding);
	}


	public boolean beanToFile(IBean data, String path, String encoding) {
		return beanToFile(data, new File(path), defaultEncoding);
	}
	
	public boolean beanToFile(IBean data, File f, String encoding) {
		if (encoding == null || "".equals(encoding)) {
			encoding = defaultEncoding;
		}
		try {
			String sPath = f.getParent();
			File path = new File(sPath);
			if (!path.exists()) {
				path.mkdirs();
			}
			Element root = new Element(data.getName());
			Document doc = new Document(root);
			iterateData2((Node) data, 1, root, null);
			Format format = Format.getCompactFormat();
			format.setEncoding(encoding); // 设置xml文件的字符
			format.setIndent("    "); // 设置xml文件的缩进为4个空格
			XMLOutputter XMLOut = new XMLOutputter(format);// 元素后换行一层元素缩四格
			FileOutputStream fs = new FileOutputStream(f.getAbsolutePath());
			XMLOut.output(doc, fs);
			fs.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}
	/**
	 * 将文件转化成IBean数据结构，包括将XML文件中的数据给IBean赋值。
	 * 
	 * @param dataTemplate
	 *            IBean数据模板
	 * @param data
	 *            通过引用返回的IMA数据
	 * @param parent
	 *            XML解析出的Element对象
	 * @param level
	 *            当前所处的级别（用于递归调用）
	 */
	public void iterateData(Map<String, Node> dataTemplate, Node data, Element parent, int level) {
		if (data == null) {
			return;
		}
		for (Bean t : data.getData()) {
			if (t.getType().equals(Node.class)) {// 是复杂类型
				Node complex = (Node) t;
				// 可重复复杂类型,maxOccurs = unbounded
				if (complex.getData().size() == 0) {
					List elements = parent.getChildren(complex.getName());
					if (elements == null) {
						continue;
					}
					int i = 0;
					for (Object tObj : elements) {// 遍历可重复列表中的元素
						Element tElm = (Element) tObj;
						if (tElm.getChildren().size() == 0) { // 若是简单元素
							// todo: 简单元素类型暂时全部用String代替
							Leaf tLeaf = new Leaf(tElm.getValue(), String.class, String.valueOf(i), String.valueOf(i));
							complex.addProperty(tLeaf);
						} else {
							Node tNode = dataTemplate.get(complex.getName());
							if (tNode == null) {
								continue;
							}
							Node ttNode = tNode.clone();
							ttNode.setName(String.valueOf(i));
							ttNode.setChName(String.valueOf(i));
							iterateData(dataTemplate, ttNode, tElm, level + 1);
							complex.addProperty(ttNode);
						}
						i++;
					}
				} else {// 不可重复复杂类型
					iterateData(dataTemplate, complex, parent.getChild(complex.getName()), level + 1);
				}
			} else {// 是简单类型
				Element child = parent.getChild(t.getName());
				if (child != null) {
					t.setData(child.getValue());
				} else {
					t.setData("");
				}
			}
		}
	}
	
	/**
	 * 将赋值的IMA对象写入到XML文件中
	 * 
	 * @param imaData
	 *            IMA对象
	 * @param level
	 *            当前级别
	 * @param parent
	 *            JDom解析出的xml节点
	 * @param name
	 *            节点名
	 */
	public void iterateData2(Node imaData, int level, Element parent, String name) {
		for (Bean t : imaData.getData()) {
			if (t.isHide() == true) {
				continue;
			}
			// 复杂类型
			if (t.getType().equals(Node.class)) {
				if (((Node) t).getData() == null || ((Node) t).getData().size() == 0) {
					continue;
				}
				Element newElement = null;
				if (name != null) {
					newElement = new Element(name);
				} else {
					newElement = new Element(t.getName());
				}
				String nodeName = ((Node) t).getData().get(0).getName();
				if ("0".equals(nodeName)) {
					iterateData2((Node) t, level + 1, parent, t.getName());
				} else {
					iterateData2((Node) t, level + 1, newElement, null);
					parent.addContent(newElement);
				}
			} else { // 简单类型，直接加到父节点中
				Element newElement = null;
				if (name != null) {
					newElement = new Element(name);
				} else {
					newElement = new Element(t.getName());
				}
				newElement.setText(t.getData(String.class));
				parent.addContent(newElement);
			}
		}
	}

}
