package com.onestop.common.util;

import java.util.HashMap;
import java.util.Map;

public class RedisLocalData {

	private static ThreadLocal<Boolean> isInTran = new ThreadLocal<>();
	private static ThreadLocal<Map<String,Object>> invalidCache = new ThreadLocal<>();


	public static void clean(){
		clear();
	}

	public static void init(){
		clear();
		invalidCache.set(new HashMap<>());
		setIsInTran(true);
	}

	private static void clear() {
		isInTran.set(null);
		isInTran.remove();
		invalidCache.set(null);
		invalidCache.remove();
	}

	public static boolean isInTran(){
		Boolean result = isInTran.get();
		return result != null && result;
	}

	public static void setIsInTran(boolean isInTransaction){
		isInTran.set(isInTransaction);
	}

	public static void setIsInvalidCache(String key){
		setCache(key,null);
	}

	public static void setCache(String key, byte[] value){
		if (!isInTran()) {
            return;
        }
		Map<String, Object> map = invalidCache.get();
		if (map == null){
			map = new HashMap<>();
			invalidCache.set(map);
		}
		map.put(key,value != null ? value : true);
		map.put(key.split("\\:")[0] + ":",true); //  同一事务中,更新了一个对象,再调用该对象的维度分页查询方法时,就会出现查不到该对象问题,这种情况需要直接查MySQL
	}

	public static boolean isDeleted(String key){
		Map<String, Object> map = invalidCache.get();
		return map != null && map.get(key) != null && !(map.get(key) instanceof byte[]);
	}

	public static boolean isModified(String key){
		Map<String, Object> map = invalidCache.get();
		Object obj = map.get(key);
		return obj != null;
	}

	public static byte[] getObject4Cache(String key){
		Map<String, Object> map = invalidCache.get();
		if (map == null){
			return null;
		}
		Object obj = map.get(key);
		return obj instanceof byte[] ? ((byte[]) obj) : null;
	}


}
