package com.onestop.xml.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.OneToMany;

import com.onestop.xml.iterator.IIterateBean;

public class Node extends Bean implements Serializable{

	/**
	 * long serialVersionUID
	 */
	private static final long serialVersionUID = -975122682362535198L;

	
	@OneToMany(cascade = CascadeType.ALL)
	private List<Bean>			data;
	private Map<String, Bean>	mapData;
	
	
	public Node(){
		this.type = Node.class;
		this.data = new ArrayList<Bean>();
		this.mapData = new HashMap<String, Bean>();
	}
	/**
	 * 构造函数，需要提供分支列表数据和属性名
	 * 
	 * @param data
	 *            分支列表数据
	 * @param name
	 *            属性名
	 */
	public Node(List<Bean> data, String name) {
		this(data, name, "");
	}
	
	/**
	 * 构造函数，需要提供分支列表数据和属性名
	 * 
	 * @param data
	 *            分支列表数据
	 * @param name
	 *            属性名
	 * @param chName
	 *            属性中文名
	 */
	public Node(List<Bean> data, String name, String chName) {
		this.mapData = new HashMap<String, Bean>();
		if (data == null) {
			this.data = new ArrayList<Bean>();
		} else {
			this.data = data;
			for (Bean t : data) {
				mapData.put(t.getName(), t);
			}
		}
		this.type = Node.class;
		this.name = name;
		this.chName = chName;
	}
	
	public List<Bean> getData() {
		return data;
	}
	
	public void setData(List<Bean> data) {
		this.data = null;
		this.data = data;
		this.mapData = null;
		this.mapData = new HashMap<String, Bean >();
		for (Bean t : data) {
			mapData.put(t.getName(), t);
		}
	}
	@Override
	public <T> T getData(Class<T> type) {
		if (type.isAssignableFrom(this.type)) {
			try {
				return type.cast(data);
			} catch (Exception e) {
				return null;
			}
		} else {
			return null;
		}
	}

	@Override
	public <T> void setData(T data) {
		if (data == null) {
			this.data = null;
			return;
		}
		if (data instanceof List) {
			try {
				setData((List) data);
			} catch (Exception e) {
			}
		}
		
	}
	/**
	 * 生成一个和本IMA类相同但占用的不同地址空间的，Node数据，本克隆方法不克隆数据本身
	 */
	@Override
	public Node clone() {
		Node result = new Node(null, this.getName(), this.getChName());
		Bean[] ls = data.toArray(new Bean[0]);
		for (int i = 0; i < ls.length; i++) {
			Bean t = ls[i];
			if (t.getType().equals(Node.class)) {
				Node node = ((Node) t).clone();
				result.addProperty(node);
			} else {
				Leaf leaf = new Leaf(t.getData(String.class), t.getType(), t.getName(), t.getChName());
				result.addProperty(leaf);
			}
		}
		return result;
	}
	/**
	 * 隐藏指定的属性
	 * 
	 * @param propertyName
	 *            属性名
	 */
	public void hideProperty(String propertyName) {
		Bean data = mapData.get(propertyName);
		if (data != null) {
			data.setHide(true);
		}
		// Bean[] ls = data.toArray(new Bean[0]);
		//
		// for (int i = 0; i < ls.length; i++) {
		// Bean t = ls[i];
		// if (t.getName().equals(propertyName)) {
		// t.setHide(true);
		// }
		// }
	}
	
	/**
	 * 增加属性
	 * 
	 * @param propertyName
	 *            属性名
	 * @param chName
	 *            属性中文名
	 * @param type
	 *            属性类型
	 * @param _data
	 *            数据
	 * @return 返回生成的子节点
	 */
	public Bean addProperty(String propertyName, String chName, Class type, Object _data) {
		Bean res = addProperty(propertyName, chName, type);
		if (res != null) {
			this.setPropertyValue(propertyName, _data);
		}
		return res;
	}
	
	/**
	 * @param property
	 * @return
	 */
	public Bean addProperty(Bean property) {
		if (property == null) {
			return null;
		}
		if (property.getName() == null || "".equals(property.getName())) {
			return null;
		}
		Bean t = this.getPropertyValue(property.getName());
		if (t == null) {
			this.data.add(property);
			this.mapData.put(property.getName(), property);
		} else {
			if (property.getType().equals(t.getType())) {
				t.setData(property.getData(property.getType()));
				t.setChName(property.getChName());
			} else { // 类型不匹配
				return null;
			}
		}
		return t;
	}
	
	/**
	 * 删除Ima中的一个属性
	 * 
	 * @param propertyName
	 *            属性名称
	 * @return 被删除的属性对象
	 */
	public Bean removeProperty(String propertyName) {
		Bean result = mapData.get(propertyName);
		if (result == null) {
			return null;
		}
		mapData.remove(result);
		data.remove(result);
		return result;
	}
	
	/**
	 * 在Node中增加属性，如果该属性已存在，则修改现有的属性
	 * 
	 * @param propertyName
	 *            属性英文名
	 * @param chName
	 *            属性中文名
	 * @param type
	 *            属性类型
	 * @return
	 */
	public Bean addProperty(String propertyName, String chName, Class type) {
		if (propertyName == null || type == null) {
			return null;
		}
		// 判断是否有重复的数据，若有，则修改
		if (type == Node.class) {
			Leaf pValue = getPropertyValue(propertyName);
			if (pValue != null) {
				return null;// 类型不匹配
			}
			Node pNode = getNodeValue(propertyName);
			if (pNode != null) {
				if (pNode.getType().equals(type)) {
					pNode.setChName(chName);
					return pNode;
				} else {
					return null;// 类型不匹配
				}
			}
		} else {
			Leaf pValue = getPropertyValue(propertyName);
			if (pValue != null) {
				pValue.setType(type);
				pValue.setChName(chName);
				return pValue;
			}
		}
		Bean ii;
		if (type.equals(Node.class)) {
			ii = new Node();
		} else {
			ii = new Leaf();
			((Leaf) ii).setNode(this);
		}
		ii.setChName(chName);
		ii.setName(propertyName);
		ii.setType(type);
		if (this.data == null) {
			this.data = new ArrayList<Bean>();
			this.mapData = null;
			this.mapData = new HashMap<String, Bean>();
		}
		this.data.add(ii);
		this.mapData.put(ii.getName(), ii);
		return ii;
	}
	
	/**
	 * 获取Leaf属性。本方法根据属性名查找对应的Leaf对象， 若对应的是Node对象，则输入有误，返回空
	 * 
	 * @param propertyName
	 *            属性英文名
	 * @return 属性值
	 */
	public Leaf getPropertyValue(String propertyName) {
		Bean tdata = mapData.get(propertyName);
		if (tdata instanceof Leaf) {
			return (Leaf) tdata;
		} else {
			return null;
		}
		// Bean[] ls = data.toArray(new Bean[0]);
		//
		// for (int i = 0; i < ls.length; i++) {
		// Bean t = ls[i];
		// if (t.getName().equals(propertyName)) {
		// if (t.getType().equals(Node.class)) {
		// return null;
		// } else {
		// return Leaf.class.cast(t);
		// }
		// }
		// }
		// return null;
	}
	
	/**
	 * 获取属性值
	 * 
	 * @param <T>
	 *            属性值的类型
	 * @param propertyName
	 *            属性名称
	 * @param type
	 *            属性类型
	 * @return 属性值
	 */
	public <T> T getPropertyData(String propertyName, Class<T> type) {
		Leaf l = null;
		Bean tdata = mapData.get(propertyName);
		if (tdata == null) {
			return null;
		}
		if (tdata instanceof Node) {
			return null;
		} else {
			Leaf tleaf = (Leaf) tdata;
			return tleaf.getData(type);
		}
		//
		// Bean[] ls = data.toArray(new Bean[0]);
		//
		// for (int i = 0; i < ls.length; i++) {
		// Bean t = ls[i];
		// if (t.getName().equals(propertyName)) {
		// if (t.getType().equals(Node.class)) {
		// return null;
		// } else {
		// l = Leaf.class.cast(t);
		// }
		// }
		// }
		// if (l == null) {
		// return null;
		// } else {
		// T t = l.getData(type);
		// return t;
		// }
	}
	
	/**
	 * 根据中文名获取属性值
	 * 
	 * @param <T>
	 *            属性类型
	 * @param propertyName
	 *            属性名
	 * @param type
	 *            属性类型
	 * @return 属性值
	 */
	public <T> T getChPropertyData(String propertyName, Class<T> type) {
		Leaf l = null;
		Bean[] ls = data.toArray(new Bean[0]);
		for (int i = 0; i < ls.length; i++) {
			Bean t = ls[i];
			if (t.getChName().equals(propertyName)) {
				if (t.getType().equals(Node.class)) {
					return null;
				} else {
					l = (Leaf) t;
				}
			}
		}
		if (l == null) {
			return null;
		} else {
			T t = l.getData(type);
			return t;
		}
	}
	
	/**
	 * 获取Node属性。本方法根据属性名查找对应的Node对象， 若对应的是Leaf对象，则输入有误，返回空
	 * 
	 * @param propertyName
	 *            属性英文名
	 * @return Node属性
	 */
	public Node getNodeValue(String propertyName) {
		Bean tData = mapData.get(propertyName);
		if (tData instanceof Node) {
			return (Node) tData;
		} else {
			return null;
		}
		//
		// Bean[] ls = data.toArray(new Bean[0]);
		//
		// for (int i = 0; i < ls.length; i++) {
		// Bean t = ls[i];
		// if (t.getName().equals(propertyName)) {
		// if (t.getType().equals(Node.class)) {
		// return Node.class.cast(t);
		// } else {
		// return null;
		// }
		// }
		// }
		// return null;
	}
	
	/**
	 * 遍历Bean中的数据，将每个遍历到的数据交给注入的iterator对象处理
	 * 
	 * @param iterator
	 *            符合IIterateIma接口的对象
	 * @param level
	 *            目前的层次，初始时可以设置为0
	 */
	public void iterateData(IIterateBean iterator, int level) {
		Bean[] ls = data.toArray(new Bean[0]);
		for (int i = 0; i < ls.length; i++) {
			Bean t = ls[i];
			if (t.getType().equals(Node.class)) {
				iterator.processData(String.class, "Node", t.getName(), t.getChName(), level);
				((Node) t).iterateData(iterator, level + 1);
			} else {
				iterator.processData(String.class, t.getData(String.class), t.getName(), t.getChName(), level);
			}
		}
	}
	
	/**
	 * 设置属性值，如果属性值和属性类型不匹配，则会抛出异常
	 * 
	 * @param propertyName
	 *            属性英文名
	 * @param value
	 *            属性值
	 */
	public void setPropertyValue(String propertyName, Object value) {
		Bean tdata = mapData.get(propertyName);
		if (tdata != null) {
			tdata.setData(value);
			return;
		}
		// Bean[] ls = data.toArray(new Bean[0]);
		//
		// for (int i = 0; i < ls.length; i++) {
		// Bean t = ls[i];
		// if (t.getName().equals(propertyName)) {
		// // if (t.getType().isAssignableFrom(value.getClass())) {
		// t.setData(value);
		// // }
		// return;
		// }
		// }
		throw new RuntimeException();
	}
	
	/**
	 * 设置属性值，如果属性值和属性类型不匹配，则会抛出异常
	 * 
	 * @param propertyName
	 *            属性英文名
	 * @param value
	 *            属性值
	 */
	public void setChPropertyValue(String propertyName, Object value) {
		Bean[] ls = data.toArray(new Bean[0]);
		for (int i = 0; i < ls.length; i++) {
			Bean t = ls[i];
			if (t.getChName().equals(propertyName)) {
				// if (t.getType().isAssignableFrom(value.getClass())) {
				t.setData(value);
				// }
				return;
			}
		}
		throw new RuntimeException();
	}
}
