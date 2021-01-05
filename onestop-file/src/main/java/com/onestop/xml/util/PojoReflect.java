package com.onestop.xml.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @类名: BeanReflect
 * @描述: bean类反射工具。用于基本pojo字段的读写
 * @版本: 
 * @创建日期: 2015-11-20下午02:03:29
 * @作者: liangjw
 * @JDK: 1.6
 */
public class PojoReflect{
	//被反射的pojo类类型
	private Class<?> clazz;
	//pojo的字段
	private Map<String,Field> fields = new HashMap<String,Field>();
	//pojo的get方法
	private Map<String,Method> readMethods = new HashMap<String,Method>();
	//pojo的set方法
	private Map<String,Method> writeMethods = new HashMap<String,Method>();
	//被反射类的实例
	private Object instance;
	
	/**
	 * PojoReflect
	 * @描述 构造方法，传入指定的类型，装配get/set方法及字段映射，并默认创建一个类实例
	 * @param t
	 */
	public PojoReflect(Class<?> t){
		this.clazz = t;
		//获取循环获取该类属性及超类的属性
		Class<?> superClass = clazz;
		do{
			for(Field f : superClass.getDeclaredFields()){
				fields.put(f.getName().toLowerCase(),f);
			}
		}while((superClass = superClass.getSuperclass()) != Object.class);
		
		try {
			Method[] methods = clazz.getMethods();
			clazz.getMethods();
			for(Method method : methods){
				String methodName = method.getName();
				String fieldName = method2Field(methodName).toLowerCase();
				if(methodName.startsWith("set") && fields.keySet().contains(fieldName)){
					writeMethods.put(fieldName, method);
				}else if(methodName.startsWith("get") && fields.keySet().contains(fieldName)){
					readMethods.put(fieldName, method);
				}
			}
		} catch (SecurityException e) {
			e.printStackTrace();
		}
		getInstance(true);
	}
	
	/**
	 * setValue
	 * @描述: 为被反射的类的指定实例赋值
	 * @作者: liangjw
	 * @创建时间: 2015-11-20下午02:07:33
	 * @param fieldName
	 * @param value
	 * @param instance
	 */
	 
	public void setValue(String fieldName,Object value,Object instance){
		try {
			writeMethods.get(fieldName.toLowerCase()).invoke(instance, value);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NullPointerException e){
			System.out.println("has no such fileld or setting method: set" + fieldName + " in " + clazz.getName());
		}
	}
	
	/**
	 * setValue
	 * @描述: 为被反射的类实例赋值
	 * @作者: liangjw
	 * @创建时间: 2015-11-20下午02:07:59
	 * @param fieldName
	 * @param value
	 */
	 
	public void setValue(String fieldName,Object value){
		setValue(fieldName.toLowerCase(),value,instance);
	}
	
	/**
	 * getValue
	 * @描述: 获取被反射类的指定实例的某一字段值
	 * @作者: liangjw
	 * @创建时间: 2015-11-20下午02:08:46
	 * @param fieldName 指定的字段
	 * @param instance 指定的对象实例
	 * @return
	 */
	 
	public Object getValue(String fieldName,Object instance){
		try {
			return readMethods.get(fieldName.toLowerCase()).invoke(instance);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * getValue
	 * @描述: 获取该对象实例的字段值
	 * @作者: liangjw
	 * @创建时间: 2015-11-20下午02:09:29
	 * @param fieldName
	 * @return
	 */
	 
	public Object getValue(String fieldName){
		return getValue(fieldName,instance);
	}
	
	/**
	 * getFields
	 * @描述: 按字段名获取字段对象
	 * @作者: liangjw
	 * @创建时间: 2015-11-20下午02:10:00
	 * @param fieldName
	 * @return field
	 */
	 
	public Field getField(String fieldName){
		return fields.get(fieldName);
	}

	/**
	 * getFields
	 * @描述: 获取字段集
	 * @作者: liangjw
	 * @创建时间: 2016-5-30下午04:37:06
	 * @return
	 */
	 
	public Set<String> getFields() {
		return fields.keySet();
	}
	
	/**
	 * getBean
	 * @描述: 取得该类的实例。如果该参数为true，则每调用一次，上一个instance会被覆盖为新建的空白实例
	 * @作者: liangjw
	 * @创建时间: 2015-11-20下午02:11:24
	 * @param boolean isNew 
	 * @return
	 */
	 
	public Object getInstance(boolean isNew){
		if(!isNew) {
            return instance;
        }
		try {
			instance = clazz.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return instance;
	}
	
	/**
	 * getInstance
	 * @描述: 取得对象实例
	 * @作者: liangjw
	 * @创建时间: 2015-11-20下午02:25:46
	 * @return
	 */
	 
	public Object getInstance(){
		return getInstance(false);
	}

	/**
	 * method2Field
	 * @描述: 根据set/get方法名，得到该set/get方法的field
	 * @作者: liangjw
	 * @创建时间: 2015-11-20下午02:16:24
	 * @param word
	 * @return
	 */
	 
	private String method2Field(String word) {
		word = word.substring(3);
		if(fields.keySet().contains(word)) {
            return word;
        }
		return word.substring(0, 1).toLowerCase() + word.substring(1);
	}

	public void setInstance(Object obj) {
		if(clazz.isInstance(obj)){
			instance = clazz.cast(obj);
		}
	}
}
