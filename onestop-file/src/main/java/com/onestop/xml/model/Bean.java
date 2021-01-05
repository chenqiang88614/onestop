package com.onestop.xml.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.TableGenerator;



public abstract class Bean implements IBean, Serializable{
	/**
	 * 序列化使用字段
	 */
	private static final long serialVersionUID = -6450017724752605964L;

	/**
	 * JPA入库的ID
	 */
	@Id
	@TableGenerator(name = "bean_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "bean_seq")
	@Column(name = "id")
	private long				id;
	/**
	 * 属性的中文名，通常用于界面显示
	 */
	@Column(name = "chName")
	protected String			chName;
	/**
	 * 属性的英文名，对应JavaBean的属性
	 */
	@Column(name = "name")
	protected String			name;
	/**
	 * 属性的类型
	 */
	@Column(name = "type")
	protected Class<?>			type;
	/**
	 * 是否隐藏的属性
	 */
	@Column(name = "ishide")
	protected boolean			isHide				= false;
	
	/**
	 * @return the isHide
	 */
	@Override
	public boolean isHide() {
		return isHide;
	}
	
	/**
	 * @param isHide
	 *            the isHide to set
	 */
	@Override
	public void setHide(boolean isHide) {
		this.isHide = isHide;
	}
	
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	@Override
	public String getName() {
		return name;
	}
	
	@Override
	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public String getChName() {
		return chName;
	}
	
	@Override
	public void setChName(String chName) {
		this.chName = chName;
	}
	
	@Override
	public Class getType() {
		return type;
	}
	
	@Override
	public void setType(Class type) {
		this.type = type;
	}
}
