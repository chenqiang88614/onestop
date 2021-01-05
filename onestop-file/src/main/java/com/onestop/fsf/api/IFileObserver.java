package com.onestop.fsf.api;

import java.io.File;


/**
 * @类名: IFileObserver
 * @描述: 文件处理器接口，作为一个观察者
 * @版本: 
 * @创建日期: 2015-10-13下午07:50:22
 * @作者: huangr
 * @JDK: 1.6
 */

public interface IFileObserver {
	
	

	/**
	 * @param s 设置观察者的参数
	 */
	void setParmString(String s);
	/**
	 *  update(File srcFile)
	 *   对收到的满足匹配要求的文件进行处理的方法
	 * @param srcFile 文件的完整文件名，包含绝对路径的
	 *  
	 */
	void update(File srcFile);
        
        
        /**
         * 设置需要观察的Matcher
         * @param matchers
         */
		void setMatchers(String matchers);
        
        /**
         * isMatch
         * @描述: 检测是否匹配
         * @作者: huangr
         * @创建时间: 2015-10-13下午07:53:08
         * @param matcher
         * @return
         *  TODO 描述每个输入输出参数的作用、量化单位、值域、精度
         */


		boolean isMatch(String matcher);

}
