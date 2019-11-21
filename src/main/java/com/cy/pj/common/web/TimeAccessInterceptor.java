package com.cy.pj.common.web;

import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;

import com.cy.pj.common.exception.ServiceException;
/**
 * Spring MVC中的拦截器:
  *  控制层方法执行之前进行拦截处理
 */
public class TimeAccessInterceptor 
          implements HandlerInterceptor{
	/**
	 * preHandler方法会在目标controller方法执行之前执行
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, 
			HttpServletResponse response, Object handler) 
					throws Exception{
		System.out.println("=preHandle==");
		//1.获取当前时间的日历对象
		Calendar c = Calendar.getInstance();
		//2.基于业务设定时间边界
		c.set(Calendar.HOUR_OF_DAY, 9);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		long start = c.getTimeInMillis();
		c.set(Calendar.HOUR_OF_DAY, 18);
		long end = c.getTimeInMillis();		
		//3.基于当前时间与业务设定时间进行放行和终止
		long cTime = System.currentTimeMillis();
		if(cTime<start||cTime>end)
			throw new ServiceException("您不在访问时间范围");
		return true; //true表示放行(可以去执行controller)
		
	}
	
	

}
