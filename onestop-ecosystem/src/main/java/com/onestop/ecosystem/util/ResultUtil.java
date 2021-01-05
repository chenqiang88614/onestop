package com.onestop.ecosystem.util;

import com.onestop.ecosystem.constant.Status;
import org.springframework.ui.ModelMap;

public class ResultUtil {
	
	public ResultUtil() {
		
	}
	
	public static ModelMap opSuccess() {
		ModelMap modelMap = new ModelMap();
		modelMap.addAttribute("code", Status.SUCCESS.getValue());
		modelMap.addAttribute("msg", Status.SUCCESS.getStr());
		return modelMap;
	}
	
	public static ModelMap opFailed() {
		ModelMap modelMap = new ModelMap();
		modelMap.addAttribute("code", Status.FAILURE.getValue());
		modelMap.addAttribute("msg", Status.FAILURE.getStr());
		return modelMap;
	}
	
	public static ModelMap success(Object o) {
		ModelMap modelMap = new ModelMap();
		modelMap.addAttribute("code", Status.SUCCESS.getValue());
		modelMap.addAttribute("msg", Status.SUCCESS.getStr());
		modelMap.addAttribute("data", o);
		return modelMap;
	}
	
	public static ModelMap success() {
		ModelMap modelMap = new ModelMap();
		modelMap.addAttribute("code", Status.SUCCESS.getValue());
		modelMap.addAttribute("msg", Status.SUCCESS.getStr());
		return modelMap;
	}
	
	public static ModelMap error(Object o) {
		ModelMap modelMap = new ModelMap();
		modelMap.addAttribute("code", Status.REQUIRE_FIELD_NULL.getValue());
		modelMap.addAttribute("msg", Status.REQUIRE_FIELD_NULL.getStr());
		modelMap.addAttribute("data", o);
		return modelMap;
	}
	
	public static ModelMap error(String code,String msg) {
		ModelMap modelMap = new ModelMap();
		modelMap.addAttribute("code", code);
		modelMap.addAttribute("msg", msg);
		return modelMap;
	}
	
	public static ModelMap error(Status s) {
		ModelMap modelMap = new ModelMap();
		modelMap.addAttribute("code", s.getValue());
		modelMap.addAttribute("msg", s.getStr());
		return modelMap;
	}
	
	public static ModelMap server_error() {
		ModelMap modelMap = new ModelMap();
		modelMap.addAttribute("code", Status.SERVER_ERROR.getValue());
		modelMap.addAttribute("msg", Status.SERVER_ERROR.getStr());
		return modelMap;
	}
	
	public static ModelMap error(String code,Object msg,Object o) {
		ModelMap modelMap = new ModelMap();
		modelMap.addAttribute("code", code);
		modelMap.addAttribute("msg", msg);
		modelMap.addAttribute("data", o);
		return modelMap;
	}
	
}
