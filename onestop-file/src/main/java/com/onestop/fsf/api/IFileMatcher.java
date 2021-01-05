package com.onestop.fsf.api;

/**
 * @author 付春岭
 * @author Refactor by 贺然 200906 <br>
 * 文件匹配策略的抽象接口<br>
 * 定义用于匹配文件是否为指定文件类型的匹配器<br>
 */
public interface IFileMatcher {

	/**
	 * 
	 * @param s 设置匹配的参数
	 */
    void setParmString(String s);

	/**
	 * @param filePath 匹配的文件路径
	 * @param fileName 匹配的文件名
	 * @return 匹配成功返回true
	 */
    boolean matchFile(String filePath, String fileName);

	/**
	 * 
	 * @param matcherName 匹配器名
	 */
    void setName(String matcherName);

	/**
	 * 
	 * @return 匹配器名
	 */
    String getName();

}
