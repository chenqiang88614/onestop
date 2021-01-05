package com.onestop.xml.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.ManyToOne;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlTransient;



public class Leaf  extends Bean implements Serializable{
	/**
	 * 序列化ID
	 */
	private static final long	serialVersionUID	= 1819632452261808286L;
	/**
	 * 数据的属性值
	 */
	@Column(name = "rdata")
	private String				data;
	/**
	 * 上一层的Node类型节点
	 */
	@ManyToOne
	@XmlTransient
	private Node				Node;
	private boolean				key					= false;
	
	public boolean isKey() {
		return key;
	}
	
	public void setKey(boolean key) {
		this.key = key;
	}
	
	public Leaf() {
	}
	
	/**
	 * 构造函数，需要指定Bean的数据、数据类型、属性名和属性的中文名
	 * 
	 * @param data
	 *            数据
	 * @param type
	 *            数据类型
	 * @param name
	 *            名称
	 * @param chName
	 *            中文名
	 */
	public Leaf(Object data, Class<?> type, String name, String chName) {
		if (data == null) {
			this.data = "";
		} else {
			this.data = data.toString();
		}
		this.type = type;
		this.setName(name);
		this.setChName(chName);
	}
	
	/**
	 * 构造函数，需要指定IMA的数据、数据类型、属性名
	 * 
	 * @param data
	 *            数据
	 * @param type
	 *            数据类型
	 * @param name
	 *            名称
	 */
	public Leaf(Object data, Class<?> type, String name) {
		this(data, type, name, "");
	}
	
	/**
	 * 获取数据
	 * 
	 * @return 数据
	 */
	public String getData() {
		return data;
	}
	
	/**
	 * 设置数据
	 * 
	 * @param data
	 *            数据
	 */
	public void setData(String data) {
		this.data = data;
	}
	
	@Override
	public <T> T getData(Class<T> type) {
		if (type.equals(String.class)) {
			return (T) this.data;
		}
		if (type.isAssignableFrom(this.type)) {
			if (type.equals(Integer.class)) {
				return type.cast(Integer.valueOf(data));
			}
			return type.cast(data);
		}
		return null;
	}
	
	@Override
	public <T> void setData(T data) {
		if (data == null) {
			this.data = "";
		} else {
			this.data = data.toString();
		}
	}
	
	public Node getNode() {
		return Node;
	}
	
	public void setNode(Node Node) {
		this.Node = Node;
	}
	
	/**
	 * 用于解包时恢复双向链接关系，该方法在序列化和反序列化时使用
	 * 
	 * @param u
	 *            解包器
	 * @param parent
	 *            上层的Node节点
	 */
	public void afterUnmarshal(Unmarshaller u, Object parent) {
		this.Node = (Node) parent;
	}
}
