/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.onestop.fsf.api;


import com.onestop.common.util.SpringContextHolder;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.io.File;

/**
 * 文件观察者抽象类，含有终态方法，匹配在实例化时已经决定
 * @author 贺然
 * @version 1.0
 */
public abstract class AbstractFileObserver implements IFileObserver {
    private String[] matchers;
    
    @Override
	final public void setMatchers(String matchers){
        this.matchers = matchers.split(";");
    }
    
    @Override
	final public boolean isMatch(String matcher){
        if (matchers == null){
            return false;
        }
        for (int i = 0; i < matchers.length; i ++){
            if (matcher.equals(matchers[i])){
                return true;
            }
        }
        return false;
    }

    @Override
    public void update(File srcFile) {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = SpringContextHolder.getBean("threadPoolTaskExecutor");
        threadPoolTaskExecutor.execute(() -> {
            doHandler(srcFile);
        });
    }

    public abstract void doHandler(File srcFile);


}
