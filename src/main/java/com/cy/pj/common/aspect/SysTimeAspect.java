package com.cy.pj.common.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Order(2)
@Aspect
@Component
public class SysTimeAspect {
	
	@Pointcut("bean(sysUserServiceImpl)")
	public void doTimePointCut(){}
	
	@Before("doTimePointCut()")
	public void doBefore(JoinPoint jp){
		//获取连接对象对应的方法签名(
		//封装了方法名,参数信息等的一个对象)
		MethodSignature ms = 
				(MethodSignature) jp.getSignature();
		System.out.println(ms.getName()+"->@Before");
	}
	@After("doTimePointCut()")
	public void doAfter(JoinPoint jp){
		System.out.println("@After");
	}
	/**核心业务正常结束时执行
	 * 说明：假如有after，先执行after,再执行returning*/
	@AfterReturning("doTimePointCut()")
	public void doAfterReturning(){
		System.out.println("@AfterReturning");
	}
	/**核心业务出现异常时执行
                 说明：假如有after，先执行after,再执行Throwing*/
	@AfterThrowing("doTimePointCut()")
	public void doAfterThrowing(JoinPoint jp){
		System.out.println("@AfterThrowing");
	}
	@Around("doTimePointCut()")
	public Object doAround(ProceedingJoinPoint jp)
			throws Throwable{
		System.out.println("@Around.before");
		Object result=jp.proceed();
		System.out.println("@Around.After");
		return result;
	}
	

}
