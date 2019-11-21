package com.cy.pj.sys.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cy.pj.common.util.ShiroUtil;
import com.cy.pj.sys.entity.SysUser;
import com.cy.pj.sys.service.SysUserService;
import com.cy.pj.sys.vo.SysUserMenuVo;
/**
 * 基于此Controller对象处理项目中所有页面请求
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/")
public class PageController {
	@Autowired
	private SysUserService sysUserService;
	
	//呈现登录页面
	@RequestMapping("doLoginUI")
	public String doLoginUI(){
			return "login";
	}	
	//返回首页页面
	@RequestMapping("doIndexUI")
	public String doIndexUI(Model model) {
		SysUser user=ShiroUtil.getUser();
		model.addAttribute("user",user);
		List<SysUserMenuVo> userMenus = 
				sysUserService.findUserMenus();
		System.out.println("userMenus="+userMenus);
		model.addAttribute("userMenus",userMenus);
		return "starter";
	}
	//返回分页页面
	@RequestMapping("doPageUI")
	public String doPageUI() {
		return "common/page";
	}
	//返回日志列表页面
//	@RequestMapping("sys/log_list")
//	public String doLogUI() {
//		return "sys/log_list";
//	}
	//返回菜单列表页面
//	@RequestMapping("menu/menu_list")
//	public String doMenuUI() {
//		return "sys/menu_list";
//	}
	//rest风格(软件架构风格)的url映射
	//其中:
	//1) {}表示rest表达式,在这个表达式中用变量的形式表示url
	//2) @PathVariable 注解描述方法参数时表示这个参数的值可以取自
	@RequestMapping("{module}/{moduleUI}")
	public String doMenuUI(@PathVariable String moduleUI) {
		return "sys/"+moduleUI;
	}

}
