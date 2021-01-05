package com.onestop.xml.impl;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.onestop.xml.api.IBeanAdapter;
import com.onestop.xml.model.Bean;
import com.onestop.xml.model.IBean;
import com.onestop.xml.model.Leaf;
import com.onestop.xml.model.Node;

/**
 * @类名: BeanAdapterImpl
 * @描述: JavaBean转IBean对象，对于大多数JavaBean支持，极少数不支持
 *       IBean对象转JavaBean 暂不支持
 * @版本: 
 * @创建日期: 2015-10-16上午10:24:49
 * @作者: huangr
 * @JDK: 1.6
 */
/*
* 类的横向关系：TODO 说明与其它类的关联、调用或依赖等关系。
*/
public class BeanAdapterImpl implements IBeanAdapter {

	@Override
	public Object beanToObject(IBean data) {
		throw new RuntimeException("unsupported method!" +
				"");
	}

	/* 
	 * @描述: JavaBeans转换成XML——IBean对象
	 * 	需要是符合bean规范的对象。 自定义对象与其他对象等同处理 List类型的属性做单独处理。 不处理父类中的属性
	 * 
	 */
	@Override
	public Bean objectToBean(Object obj, String name) {
		// TODO: 做嵌套层数限制，或者检查该类是否无穷循环类（参考jaxb）
				// TODO：支持父类
				// 错误输入
				if (obj == null || name == null) {
					return null;
				}
				if (name.length() == 0) {
					return null;
				}
				Bean result = null;
				List<Bean> imaList = null;
				if (Bean.class.isAssignableFrom(obj.getClass())) {
					// 不处理类型为Bean的数据
					return (Bean) obj;
				} else if (isBasicType(obj.getClass())) {
					// isBasicType的参数，通过之前的赋值后
					// （基本类型赋值到Object中,property = m.invoke(obj, new Object[0]);）
					// 已经不可能是基本类型了，而是对应的包装类，即isPrimitive不可能为真
					// 所以imaToBean方法中，必须做Bean的属性的类型判断后，若为基本类型，需要将包装类转换成基本类型赋值
					// 如果是基本类型，包括枚举类型
					result = new Leaf(obj, obj.getClass(), name);
				} else if (obj.getClass().isArray()) {
					// 如果是数组
					imaList = new ArrayList<Bean>();
					result = new Node(imaList, name);
					for (int index = 0; index < ((Object[]) obj).length; index++) {
						Bean tBean = objectToBean(((Object[]) obj)[index], "#" + index);
						if (Leaf.class.isAssignableFrom(tBean.getClass())) {
							((Leaf) tBean).setNode((Node) result);
						}
						imaList.add(tBean);
						//
						// imaList.add(beanToIma(((Object[]) obj)[index], "#"
						// + String.valueOf(index)));
					}
				} else if (Iterable.class.isAssignableFrom(obj.getClass())) {
					// 如果是可遍历类型
					imaList = new ArrayList<Bean>();
					result = new Node(imaList, name);
					int index = 0;
					for (Object t : (Iterable<Object>) obj) {
						Bean tBean = objectToBean(t, "#" + index);
						if (Leaf.class.isAssignableFrom(tBean.getClass())) {
							((Leaf) tBean).setNode((Node) result);
						}
						imaList.add(tBean);
						index++;
					}
					// 如果是其他复杂对象（对于无法按bean标准获取的属性，将被忽略）
				} else {
					imaList = new ArrayList<Bean>();
					result = new Node(imaList, name);
					// 获取对象自定义的所有属性（不包括被继承的）
					Field[] f = obj.getClass().getDeclaredFields();
					// obj.getClass().isAssignableFrom(cls);
					// obj.getClass().getSuperclass();
					// 循环处理每一个属性
					Method m = null;
					Class[] c = new Class[0];
					String mName = null; // 生成的方法名
					Object property = null; // 调用bean中属性的获取方法返回的值
					for (int i = 0; i < f.length; i++) {
						try {
							if ("boolean".equals(f[i].getType().toString())) {
								mName = "is" + f[i].getName().substring(0, 1).toUpperCase() + f[i].getName().substring(1);
							} else {
								mName = "get" + f[i].getName().substring(0, 1).toUpperCase() + f[i].getName().substring(1);
							}
							// System.out.println(mName);
							m = obj.getClass().getMethod(mName, c);
							// 获取属性
							property = m.invoke(obj);
							if (property == null) {
								// 注，提示：如果调用get方法得到的是null，那么该对象的类型不可能是基本类型
								Leaf tBean = new Leaf(null, f[i].getType(), f[i].getName());
								tBean.setNode((Node) result);
								imaList.add(tBean);
							} else {
								Bean tBean = objectToBean(property, f[i].getName());
								if (Leaf.class.isAssignableFrom(tBean.getClass())) {
									((Leaf) tBean).setNode((Node) result);
								}
								imaList.add(tBean);
							}
						} catch (Exception ex) {
							ex.printStackTrace();
							System.out.println("类结构可能不完全符合bean标准，属性：" + f[i].getName() + "未能读出");
						}
						// 输入属性的类型，可能会是list
						// System.out.println(f[i].getType());
					}
				}
				return result;
	}
	
	/**
	 * isBasicType
	 * @描述: 测试是否为基本类型
	 * @作者: huangr
	 * @创建时间: 2015-10-16上午10:16:43
	 * @param c
	 * @return
	 *  TODO 描述每个输入输出参数的作用、量化单位、值域、精度
	 */
	private static boolean isBasicType(Class<?> c) {
		if (c.isEnum()) {
			return true;
		} else if (c.isPrimitive()) {
			// boolean, byte, char, short, int, long, float, double
			return true;
		} else // Short, Integer, Long,
// Float, Double, Byte
// 基本类型
		{
			return c.equals(String.class) || c.equals(Date.class) || c.equals(Calendar.class) || c.equals(Boolean.class)
					|| c.equals(Character.class) || Number.class.isAssignableFrom(c);
		}
	}

}
