package com.cy.pj.sys.service;

import java.util.List;

import com.cy.pj.common.vo.CheckBox;
import com.cy.pj.common.vo.PageObject;
import com.cy.pj.sys.entity.SysRole;
import com.cy.pj.sys.vo.SysRoleMenuVo;

public interface SysRoleService {
	
	List<CheckBox> findObjects();
	
	//用于更新角色对象的方法
	int updateObject(SysRole entity,Integer[] menuIds);
	
	//基于id查询对应角色及相关信息的方法
	SysRoleMenuVo findObjectById(Integer id);
	
	/**
	   *  保存角色以及角色和菜单的关系数据
	  * @param entity
	  * @param menuIds
	  * @return
	  */
	int saveObject(SysRole entity,Integer[] menuIds);
	/**
	    * 基于角色id删除角色以及角色对应的关系数据
	  * @param id
	  * @return
	  */
	int deleteObject(Integer id);	
	/**
	 * 基于条件查询当前页角色信息
	 * @param name
	 * @param pageCurrent
	 * @return
	 */
	PageObject<SysRole> findPageObjects(
			String name,Integer pageCurrent);

}
