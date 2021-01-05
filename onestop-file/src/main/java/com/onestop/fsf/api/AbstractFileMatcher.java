/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.onestop.fsf.api;


/**
 *  AbstractFileMatcher   
 *  文件匹配策略抽象类
 * @author 贺然
 * @version 1.0
 */
public abstract class AbstractFileMatcher implements IFileMatcher {
    private String name = "";

    @Override
	public void setName (String matcherName){
        this.name = matcherName;
    }

    @Override
	public String getName() {
        return name;
    }
    
    
}
