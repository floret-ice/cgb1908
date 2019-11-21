package com.cy.pj.sys.dao;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.cy.pj.common.vo.CheckBox;
import com.cy.pj.sys.entity.SysRole;
import com.cy.pj.sys.vo.SysRoleMenuVo;

@Mapper
public interface SysRoleDao {
	
	List<CheckBox> findObjects();
	/**
	    * 更新角色自身信息
	 * @param entity
	 * @return
	 */
	int updateObject(SysRole entity);
	/**
	    *   基于角色id查询角色以及角色对应的菜单id
	    *   并将结果封装到SysRoleMenuVo对象
	 * @param id
	 * @return
	 */
	SysRoleMenuVo findObjectById(Integer id);	
	/**
	 * 负责将角色自身信息写入到数据库
	 * @param entity
	 * @return
	 */
	int insertObject(SysRole entity);
	/**
	 * 基于id删除角色自身信息
	 * @param id
	 * @return
	 */
	int deleteObject(Integer id);
	
	/**
	 * 基于条件查询总记录数
	 * @param name 查询条件(角色名称)
	 * @return 总记录数(基于这个结果可以计算总页数)
	 * 说明：假如如下方法没有使用注解修饰，在基于名字进行查询
	 * 时候会出现There is no getter for property named
	 * 'username' in 'class java.lang.String'
	 */
	int getRowCount(@Param("name")String name);
	
	/**
	 * 基于条件分页查询角色信息
	 * @param name  查询条件(角色名称)
	 * @param startIndex 当前页的起始位置
	 * @param pageSize 当前页的页面大小
	 * @return 当前页的角色记录信息
	 * 数据库中每条角色信息封装到一个SysLog对象中
	 */
	List<SysRole> findPageObjects(
			      @Param("name")String name,
	              @Param("startIndex")Integer startIndex,
	              @Param("pageSize")Integer pageSize);

}
