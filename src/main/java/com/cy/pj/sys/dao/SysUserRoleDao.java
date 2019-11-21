package com.cy.pj.sys.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
/**
 * 基于此DAO操作用户和角色关系数据
 * @author Administrator
 *
 */
@Mapper
public interface SysUserRoleDao {
	/**
	    * 查找用户对应的角色id
	 * @param userId
	 * @return
	 */
	List<Integer> findRoleIdsByUserId(Integer id);
	/**
	    * 保存用户和角色关系数据
	 * @param userId
	 * @param roleIds
	 * @return
	 */
	int insertObjects(
			@Param("userId")Integer userId,
			@Param("roleIds")Integer[] roleIds);
	
	int deleteObjectsByUserId(Integer userId);
	
	int deleteObjectsByRoleId(Integer roleId);

}
