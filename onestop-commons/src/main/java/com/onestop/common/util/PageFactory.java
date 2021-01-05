package com.onestop.common.util;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.onestop.common.enums.SystemConst;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * 默认分页
 * 
 * @author chenq
 * @date 2019年6月20日11:27:29
 */
public enum PageFactory {

	INSTANCE;

	public Page defaultPage() {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		int size = SystemConst.PAGE_SIZE;
		int current = 0;
		if (!StringUtils.isEmpty(request.getParameter("currentPageNum"))) {
			if (!StringUtils.isEmpty(request.getParameter("pageSize"))) {
				size = Integer.valueOf(request.getParameter("pageSize"));
			}
			current = Integer.valueOf(request.getParameter("currentPageNum"));
		} else {
			size = SystemConst.PAGE_SIZE;
			current = 0;
		}
		String sortIndex = request.getParameter("sortIndex");
		String sortUp = request.getParameter("sortUp");
		if (StringUtils.isEmpty(sortIndex)) {
			Page<T> page = new Page<>(current, size);
//			page.setOptimizeCountSql(false);
			return page;
		} else {
			Page<T> page = new Page<>(current, size);
			if (StringUtils.isEmpty(sortUp) || StringUtils.equals(sortUp, "false")) {
				page.setAsc(sortIndex);
			} else {
				page.setDesc(sortIndex);
			}
			return page;
		}
	}
}
