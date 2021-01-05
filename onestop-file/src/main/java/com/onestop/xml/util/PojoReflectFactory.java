package com.onestop.xml.util;

import java.util.HashMap;
import java.util.Map;

/**
 * @类名: PojoReflectFactory
 * @描述: pojoReflect工厂，为避免重复多次反射同一个类型
 * @版本: 
 * @创建日期: 2015-11-20下午03:13:40
 * @作者: liangjw
 * @JDK: 1.6
 */
public class PojoReflectFactory {
	private Map<Class<?>,PojoReflect> readerMap = new HashMap<Class<?>,PojoReflect>();
	private static PojoReflectFactory factory;
	
	private PojoReflectFactory(){
		
	}
	
	public synchronized PojoReflect getPojoReflect(Class<?> clazz){
		PojoReflect reflect = readerMap.get(clazz);
		if(reflect == null) {
			reflect = new PojoReflect(clazz);
			readerMap.put(clazz, reflect);
		}
		//pojoReflect创建新的内部实例
		reflect.getInstance(true);
		return reflect;
	}
	
	public static PojoReflectFactory getInstance(){
		if (factory == null) {
			factory = new PojoReflectFactory();
		}
		return factory;
	}
}
