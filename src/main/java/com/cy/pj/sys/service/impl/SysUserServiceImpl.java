package com.cy.pj.sys.service.impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import com.cy.pj.common.annotation.RequiredLog;
import com.cy.pj.common.config.PageProperties;
import com.cy.pj.common.exception.ServiceException;
import com.cy.pj.common.util.ShiroUtil;
import com.cy.pj.common.vo.PageObject;
import com.cy.pj.sys.dao.SysMenuDao;
import com.cy.pj.sys.dao.SysRoleMenuDao;
import com.cy.pj.sys.dao.SysUserDao;
import com.cy.pj.sys.dao.SysUserRoleDao;
import com.cy.pj.sys.entity.SysUser;
import com.cy.pj.sys.service.SysUserService;
import com.cy.pj.sys.vo.SysUserDeptVo;
import com.cy.pj.sys.vo.SysUserMenuVo;

@Service
@Transactional(readOnly = false,
               rollbackFor = Throwable.class,
               timeout = 30,
               isolation = Isolation.READ_COMMITTED,
               propagation = Propagation.REQUIRED)
public class SysUserServiceImpl implements SysUserService{
	@Autowired
	private SysUserDao sysUserDao;
	@Autowired
	private SysUserRoleDao sysUserRoleDao;
	@Autowired
	private PageProperties pageProperties;
	@Autowired
	private SysRoleMenuDao sysRoleMenuDao;
	@Autowired
	private SysMenuDao sysMenuDao;
	
	@Transactional(readOnly = true)
	@RequiredLog("select")
	@Override
	public PageObject<SysUserDeptVo> findPageObjects(
			String username, Integer pageCurrent) {
		//1.对参数进行校验
		if(pageCurrent==null||pageCurrent<1)
			throw new IllegalArgumentException("当前页码值无效");
		//2.查询总记录数并进行校验
		int rowCount = sysUserDao.getRowCount(username);
		if(rowCount==0)
			throw new ServiceException("没有找到对应记录");		
		//3.查询当前页记录
		int pageSize=pageProperties.getPageSize();
		int startIndex=(pageCurrent-1)*pageSize;
		List<SysUserDeptVo> records = 
		sysUserDao.findPageObjects(username, startIndex, pageSize);
		//4.对查询结果进行封装并返回
		return new PageObject<>(rowCount, records, pageCurrent, pageSize);
	}
	@RequiresPermissions("sys:user:valid")
    @RequiredLog("valid")
	@Override
	public int validById(Integer id, Integer valid, 
			String modifiedUser) {
		//1.合法性验证
		if(id==null||id<=1)
			throw new ServiceException("参数不合法,id="+id);
		if(valid!=1&&valid!=0)
			throw new ServiceException("参数不合法,valid="+valid);
		if(StringUtils.isEmpty(modifiedUser))
			throw new ServiceException("修改用户不能为空");
		//2.执行禁用或启用操作
		int rows = sysUserDao.validById(id, valid, modifiedUser);
		//3.判定结果,并返回
		if(rows==0)
			throw new ServiceException("此记录可能已经不存在");
		return rows;
	}

	@Override
	public int saveObject(SysUser entity, Integer[] roleIds) {
		//1.参数校验
		if(entity==null)
			throw new ServiceException("保存对象不能为空");
		if(StringUtils.isEmpty(entity.getUsername()))
			throw new ServiceException("用户名不能为空");
		if(StringUtils.isEmpty(entity.getPassword()))
			throw new ServiceException("密码不能为空");
		if(roleIds==null||roleIds.length==0)
			throw new ServiceException("至少要为用户分配角色");
		//2.保存用户自身信息
		//2.1创建一个盐值(对密码进行加密)
		String source=entity.getPassword();
		String salt=UUID.randomUUID().toString();
		//借助spring中的工具类对密码进行盐值加密
		//String pwd=DigestUtils.md5DigestAsHex((salt+entity.getPassword()).getBytes());
		//借助shiro中的API完成加密操作
		SimpleHash sh = new SimpleHash( //Shiro框架
				"MD5", //algorithmName 算法
				source, //原密码
				salt, //盐值
				1); //hashIterations表示加密次数
		entity.setSalt(salt);
		entity.setPassword(sh.toHex());
		int rows = sysUserDao.insertObject(entity);
		//3.保存用户角色关系数据
		sysUserRoleDao.insertObjects(entity.getId(), roleIds);
		//4.返回结果
		return rows;
	}
	
	@Cacheable(value = "userCache")
	@Transactional(readOnly = true)
	@Override
	public Map<String, Object> findObjectById(Integer userId) {
		//1.合法性验证
		if(userId==null||userId<=0)
			throw new ServiceException(
					"参数数据不合法,userId="+userId);
		//2.业务查询
		SysUserDeptVo user=
				sysUserDao.findObjectById(userId);
		if(user==null)
			throw new ServiceException("此用户已经不存在");
		List<Integer> roleIds=
				sysUserRoleDao.findRoleIdsByUserId(userId);
		//3.数据封装
		Map<String,Object> map=new HashMap<>();
		map.put("user", user);
		map.put("roleIds", roleIds);
		return map;
	}
	@RequiresPermissions("sys:user:update")
	@CacheEvict(value = "userCache",key = "#entity.id")
	@Override
	public int updateObject(SysUser entity, Integer[] roleIds) {
		//1.参数有效性验证
		if(entity==null)
			throw new IllegalArgumentException("保存对象不能为空");
		if(StringUtils.isEmpty(entity.getUsername()))
			throw new IllegalArgumentException("用户名不能为空");
		if(roleIds==null||roleIds.length==0)
			throw new IllegalArgumentException("必须为其指定角色");
		//其它验证自己实现，例如用户名已经存在，密码长度，...
		//2.更新用户自身信息
		int rows=sysUserDao.updateObject(entity);
		if(rows==0)
			throw new ServiceException("用户可能不存在");
		//3.保存用户与角色关系数据
		sysUserRoleDao.deleteObjectsByUserId(entity.getId());
		sysUserRoleDao.insertObjects(entity.getId(),
				roleIds);
		//4.返回结果
		return rows;
	}
    @Transactional(readOnly = true)
	@Override
	public boolean isExists(String columnName,String columnValue) {
		int rows = sysUserDao.isExists(columnName,columnValue);
		return rows>0;
	}
	@Override
	public List<SysUserMenuVo> findUserMenus() {
		//1.获取登录用户id
		Integer id=ShiroUtil.getUser().getId();
		//2.基于登录用户id获取角色id
		List<Integer> roleIds = 
				sysUserRoleDao.findRoleIdsByUserId(id);
		//3.基于角色id获取菜单id
		List<Integer> menuIds = 
				sysRoleMenuDao.findMenuIdsByRoleIds(
				roleIds.toArray(new Integer[] {}));
		//4.基于菜单id找到对应用户菜单信息
		List<SysUserMenuVo> userMenus = 
				sysMenuDao.findUserMenus();
		//5.删除当前用户不具备访问权限的菜单
		Iterator<SysUserMenuVo> it = 
				userMenus.iterator();
		while (it.hasNext()) {
			SysUserMenuVo um = it.next();
			if(!menuIds.contains(um.getId())) {
				it.remove(); //借助迭代器进行删除，否则并发问题
			}
			List<SysUserMenuVo> childs = 
					um.getChildMenus();
			Iterator<SysUserMenuVo> cit = 
					childs.iterator();
			while(cit.hasNext()) {
				SysUserMenuVo ucm = cit.next();
				if(!menuIds.contains(ucm.getId())) {
					cit.remove();
				}
			}
		}
		return userMenus;
	}
	@Override
	public int updatePassword(String oldPassword, 
			          String newPassword, 
			          String cfgPassword) {
		//1.判定新密码与密码确认是否相同
		if(StringUtils.isEmpty(newPassword))
			throw new IllegalArgumentException("新密码不能为空");
		if(StringUtils.isEmpty(cfgPassword))
			throw new IllegalArgumentException("确认密码不能为空");
		if(!newPassword.equals(cfgPassword))
			throw new IllegalArgumentException("两次输入的密码不相等");
		//2.判定原密码是否正确
		if(StringUtils.isEmpty(oldPassword))
			throw new IllegalArgumentException("原密码不能为空");
		//获取登陆用户
		SysUser user=(SysUser)SecurityUtils.getSubject().getPrincipal();
		SimpleHash sh=new SimpleHash("MD5",
				oldPassword, user.getSalt(), 1);
		if(!user.getPassword().equals(sh.toHex()))
			throw new IllegalArgumentException("原密码不正确");
		//3.对新密码进行加密
		String salt=UUID.randomUUID().toString();
		sh=new SimpleHash("MD5",newPassword,salt, 1);
		//4.将新密码加密以后的结果更新到数据库
		int rows=sysUserDao.updatePassword(sh.toHex(), salt,user.getId());
		if(rows==0)
			throw new ServiceException("修改失败");
		return rows;
	}

}
