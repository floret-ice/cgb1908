package com.cy.pj.sys.dao;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.CacheNamespaceRef;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import com.cy.pj.common.vo.Node;
import com.cy.pj.sys.entity.SysMenu;
import com.cy.pj.sys.vo.SysUserMenuVo;
/**
 * @CacheNamespaceRef 注解表示此接口中的查询操作
 * 要使用二级缓存,其二级缓存的配置参考name属性对应
 * 的命名空间配置.
 */
@Mapper
@CacheNamespaceRef(name="com.cy.pj.sys.dao.SysMenuDao")
public interface SysMenuDao {
	/**
	   *  查询用户菜单(默认假设是最高权限用户的所有菜单)
	 * @return
	 */
	public List<SysUserMenuVo> findUserMenus();
	/**
	    * 基于对个菜单id,查找权限标识
	 * @param menuIds
	 * @return
	 */
	List<String> findPermissions(
			@Param("menuIds")
			Integer[] menuIds);
	
	//用于更新菜单信息
	int updateObject(SysMenu entity);
	//用于写入菜单信息
	int insertObject(SysMenu entity);
	//查询上级菜单相关信息
	List<Node> findZtreeMenuNodes();
	//基于菜单id统计子菜单记录的个数
	//@Select("select count(*) from sys_menus where id=#{id}")
	int getChildCount(Integer id);
	//基于菜单id删除菜单记录
	//@Delete("delete from sys_menus where id=#{id}")
	int deleteObject(Integer id);
	
	@Select("select * from sys_menus where id=#{id}")
	Map<String, Object> findById(Integer id);
	/**
	 * 查询所有菜单以及上级菜单的名称
	 * 一行记录映射为一个map对象
	 * @return
	 */
	List<Map<String,Object>>findObjects();

}
