package com.cy.pj.sys.dao;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.cy.pj.sys.entity.SysLog;
/*
 * DAO(数据访问对象)
 * 1)此对象的实现类由mybatis框架
 * 2)此对象的实现类中会自动注入SqlSessionFactory对象
 */
@Mapper
public interface SysLogDao  {  //com.cy.pj.sys.dao.SysLogDao
	//用于实现日志信息持久化的方法
	int insertObject(SysLog entity);
	
	/**
	 * 基于日志记录id执行删除业务
	 * @param ids (多个记录id)
	 * @return 删除行数
	 */
	int deleteObjects(@Param("ids")Integer... ids);
	
	/**
	 * 基于条件查询总记录数
	 * @param username 查询条件(例如查询哪个用户的日志信息)
	 * @return 总记录数(基于这个结果可以计算总页数)
	 * 说明：假如如下方法没有使用注解修饰，在基于名字进行查询
	 * 时候会出现There is no getter for property named
	 * 'username' in 'class java.lang.String'
	 */
	int getRowCount(@Param("username")String username);
			
	/**
	 * 基于条件分页查询日志信息
	 * @param username  用户名,查询条件(例如查询哪个用户的日志信息)
	 * @param startIndex 当前页的起始位置
	 * @param pageSize 当前页的页面大小
	 * @return 当前页的日志记录信息
	 * 数据库中每条日志信息封装到一个SysLog对象中
	 */
	List<SysLog> findPageObjects(
			@Param("username")String username,
			@Param("startIndex")Integer startIndex,
			@Param("pageSize")Integer pageSize);
	
	

}
